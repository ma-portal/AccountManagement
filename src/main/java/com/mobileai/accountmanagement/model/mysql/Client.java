package com.mobileai.accountmanagement.model.mysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="client",
    uniqueConstraints=@UniqueConstraint(columnNames = {"clientId"})
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String clientSecret;

    private String[] redirectUrl;

    /**
     * 
     * authorization_code
     * client_credentials
     * password
     * implicit
     */
    private String[] grantType;

    private String[] scope;

}