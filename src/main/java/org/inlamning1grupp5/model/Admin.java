package org.inlamning1grupp5.model;

import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "t_admin", uniqueConstraints = @UniqueConstraint(columnNames = {"adminId", "email"}))
public class Admin {
    
    @Id
    private Long adminId;

    @Email
    private String email;

    @NotEmpty(message = "You must give a value for password.")
    @Size(min = 5, max = 15)
    private String password;

    public Admin() {
        Random random = new Random();
        this.adminId = random.nextLong(100000000, 999999999);
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
