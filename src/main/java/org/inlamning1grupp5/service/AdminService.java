package org.inlamning1grupp5.service;

import java.util.List;

import org.inlamning1grupp5.model.Admin;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class AdminService {
    
    @Inject
    EntityManager em;

    public List<Admin> findAll() {
        return em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }
    
    public Response getAdminAccount(String email, String password) {
        Boolean authenticateAdmin = verifyAdmin(email, password);
        if (authenticateAdmin == true) {
            return Response.ok().entity("Login successful " + email).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect email or password").build();
        }
    }

    public boolean verifyAdmin(String email, String password) {
        try {
            Admin findAdmin = (Admin) em.createQuery("SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password")
            .setParameter("email", email)
            .setParameter("password", password)
            .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}

