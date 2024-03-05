package org.inlamning1grupp5.service;

import org.inlamning1grupp5.model.User;

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

    // public List<Admin> findAllAdmins() {
    //     return em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    // }
    
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
            em.createQuery("SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password")
            .setParameter("email", email)
            .setParameter("password", password)
            .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public Response findAllCustomers(String email, String password) {
        Boolean authenticateAdmin = verifyAdmin(email, password);
        if (authenticateAdmin == true) {
            return Response.ok(em.createQuery("SELECT u FROM User u", User.class).getResultList()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to do this.").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response deleteCustomerAccount(String email, String password, String username) {
        Boolean authenticateAdmin = verifyAdmin(email, password);
        if (authenticateAdmin == true) {
            int deleteSuccessful = em.createQuery("DELETE FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .executeUpdate();
                if (deleteSuccessful > 0) {
                    return Response.ok().entity(username + " successfully deleted.").build(); 
                } else {
                    return Response.ok().entity("Customer account doesnt exist.").build();
                }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to do this.").build();
        }
    }

    public Response countAllCustomers(String email, String password) {
        Boolean authenticateAdmin = verifyAdmin(email, password);
        if (authenticateAdmin == true) {
            return Response.ok(em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult()).build();
            
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to do this.").build();
        }
    }

    public Response countAllSubscribers(String email, String password) {
        Boolean authenticateAdmin = verifyAdmin(email, password);
        if (authenticateAdmin == true) {
            return Response.ok(em.createQuery("SELECT COUNT(u) FROM User u WHERE u.subscribed = 1", Long.class).getSingleResult()).build();
            
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to do this.").build();
        }
    }

}

