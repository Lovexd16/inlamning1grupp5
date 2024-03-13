package org.inlamning1grupp5.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_user", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "username", "email"}))
public class User {
    
    @Id
    @Column(unique = true)
    private UUID userId;

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

    // @NotEmpty(message = "You must give a value for password.")
    private String password;

    @NotNull
    private String subscribed;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "t_user_purchase_history", joinColumns = @JoinColumn(name = "usedId"))
    @Column(name = "userPurchaseHistory")
    private List<String> userPurchaseHistory = new ArrayList<>();

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    public List<String> getUserPurchaseHistory() {
        return userPurchaseHistory;
    }

    public void setUserPurchaseHistory(List<String> userPurchaseHistory) {
        this.userPurchaseHistory = userPurchaseHistory;
    }

    
}
