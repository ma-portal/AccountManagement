package com.mobileai.accountmanagement.repos.mysql;

import com.mobileai.accountmanagement.model.mysql.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {

    User findByUsername(String username);

}