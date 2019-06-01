package com.mobileai.accountmanagement.repos;

import com.mobileai.accountmanagement.model.mysql.Client;
import com.mobileai.accountmanagement.repos.mysql.ClientRepos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClientReposTests {

    @Autowired
    private ClientRepos repos;

    @Test
    public void testSaveAndFind() {
        Client client = repos.findByClientId("test");
        Assert.assertTrue("Database is not clear", client == null);

        client = Client.builder()
            .clientId("test")
            .clientSecret("password")
            .redirectUrl(new String[]{"https://www.baidu.com"})
            .grantType(new String[]{"authorization_code"})
            .scope(new String[]{"admin"})
            .build();

        client = repos.save(client);
        Assert.assertNotNull("save failed", client);

        Client tmp = repos.findByClientId("test");
        Assert.assertNotNull("find failed", tmp);
        Assert.assertTrue("query result doesn't match to initial data",
            client.equals(tmp));

    }

}