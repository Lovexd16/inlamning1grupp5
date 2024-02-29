package org.inlamning1grupp5.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "t_customer", uniqueConstraints = @UniqueConstraint(columnNames = {"customerId", "username", "email"}))
public class Customer {
    
    @Id
    @Column(unique = true)
    private Long customerId;

    @NotEmpty(message = "You must give a value for first name.")
    @Size(min = 1, max = 30)
    private String firstName;

    @NotEmpty(message = "You must give a value for last name.")
    @Size(min = 1, max = 30)
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "You must give a value for username.")
    @Column(unique = true)
    @Size(min = 5, max = 15)
    private String username;

    @NotEmpty(message = "You must give a value for password.")
    @Size(min = 5, max = 15)
    private String password;

    @NotNull
    private Integer subscribed;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Integer subscribed) {
        this.subscribed = subscribed;
    }

    
}