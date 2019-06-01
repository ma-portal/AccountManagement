package com.mobileai.accountmanagement.config;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级别的权限认证
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 注册一个TokenStore以保存token信息
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 注册一个AuthorizationCodeServices以保存authorization_code的授权码code
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(
    RedisConnectionFactory redisConnectionFactory) {
    
        RedisTemplate<String, OAuth2Authentication> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return new RandomValueAuthorizationCodeServices() {
            protected void store(String code, OAuth2Authentication authentication) {
                redisTemplate.boundValueOps(code)
                    .set(authentication, 10, TimeUnit.MINUTES); // 存储authentication，设置有效时间10mins
            }

            protected OAuth2Authentication remove(String code) {
                OAuth2Authentication authentication =
                    redisTemplate.boundValueOps(code).get();
                if (authentication != null) {
                    redisTemplate.delete(code);
                }
                return authentication;
            }
        };

    }

    @Bean
    public AuthenticationManager authenticationManager(
    UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(Collections.singletonList(provider));
    }

    @Bean
    public AuthorizationServerConfigurer authorizationServerConfigurer(
    UserDetailsService userDetailsService, ClientDetailsService clientDetailsService,
    TokenStore tokenStore, AuthorizationCodeServices authorizationCodeServices,
    AuthenticationManager authenticationManager) {

        return new AuthorizationServerConfigurer() {
            public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {

            }

            public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
                clients.withClientDetails(clientDetailsService);
            }

            public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
                endpoints.userDetailsService(userDetailsService);
                endpoints.tokenStore(tokenStore);
                endpoints.authorizationCodeServices(authorizationCodeServices);
                endpoints.authenticationManager(authenticationManager);
            }
        };

    }

}