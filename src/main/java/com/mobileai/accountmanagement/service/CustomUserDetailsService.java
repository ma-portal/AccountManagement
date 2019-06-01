package com.mobileai.accountmanagement.service;

import com.mobileai.accountmanagement.model.mysql.User;
import com.mobileai.accountmanagement.repos.mysql.UserRepos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * TODO: implement roles
 * 用于用户身份认证
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        User user = userRepos.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("invalid username");
        }
        return org.springframework.security.core.userdetails.User
            .withUsername(username).password(user.getPassword())
            .roles("").build();
    }

}