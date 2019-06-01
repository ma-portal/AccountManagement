package com.mobileai.accountmanagement.service;

import java.util.Arrays;
import java.util.HashSet;

import com.mobileai.accountmanagement.model.mysql.Client;
import com.mobileai.accountmanagement.repos.mysql.ClientRepos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * 进行client credential验证
 */
@Service
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRepos clientRepos;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clientRepos.findByClientId(clientId);
        if (client == null) {
            throw new ClientRegistrationException("invalid clientId");
        }

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(client.getClientId());
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.setRegisteredRedirectUri(
            new HashSet<>(Arrays.asList(client.getRedirectUrl())));

        clientDetails.setAuthorizedGrantTypes(Arrays.asList(client.getGrantType()));

        clientDetails.setScope(Arrays.asList(client.getScope()));
        return clientDetails;
    }

}