package com.mobileai.accountmanagement.repos.mysql;

import com.mobileai.accountmanagement.model.mysql.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepos extends JpaRepository<Client, Long> {

    Client findByClientId(String clientId);

}