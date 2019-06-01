package com.mobileai.accountmanagement.repos;

import com.mobileai.accountmanagement.model.mysql.User;
import com.mobileai.accountmanagement.repos.mysql.UserRepos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserReposTests {

    @Autowired
    private UserRepos repos;

    @Test
    public void testSaveAndFind() {
        User user = repos.findByUsername("test");
        Assert.assertTrue("Database is not clear", user == null);
        
        user = User.builder()
            .username("test")
            .password("dacasrwa")
            .build();

        user = repos.save(user);
        Assert.assertNotNull("save failed", user);

        User tmp = repos.findByUsername("test");
        Assert.assertNotNull("find failed", tmp);

        Assert.assertTrue("query result doesn't match to initial data",
            user.equals(tmp));
    }

}