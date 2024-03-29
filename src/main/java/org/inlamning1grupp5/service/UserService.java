package org.inlamning1grupp5.service;

import java.util.UUID;

import org.inlamning1grupp5.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class UserService{

    @Inject
    EntityManager em;

    public Response findUserByUsername(String username) {
        try {
            return Response.ok(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult()).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user found.").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response createNewUser(User user) {
        
        try {
            try {
                em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", user.getEmail()).getSingleResult();
                return Response.status(Response.Status.CONFLICT).entity("This email address is already connected to an account! Both the email and the username must be unique.").build();
            } catch (Exception e) {
            }
            em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", user.getUsername()).getSingleResult();
            return Response.status(Response.Status.CONFLICT).entity("This username is already in use! Both the email and the username must be unique.").build();
        } catch (NoResultException e) {
            user.setUserId(UUID.randomUUID());
            user.setSubscribed("Not subscribed");
            String encrypted = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(encrypted);
            em.persist(user);
            return Response.ok(user).build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response deleteUserAccount(String username, String password) {

            Boolean userAuthenticated = verifyUser(username, password);
            if (userAuthenticated == true) {
                Response customResponse = findUserByUsername(username);
                User user = (User) customResponse.getEntity();
                String encryptedPassword = user.getPassword();
                int deleteSuccessful = em.createQuery("DELETE FROM User u WHERE u.username = :username AND u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", encryptedPassword)
                    .executeUpdate();
                if (deleteSuccessful > 0) {
                    return Response.ok().entity(username + " successfully deleted.").build(); 
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("User account doesnt exist.").build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Incorrect password.").build();
            }

    }

    public boolean verifyUser(String username, String password) {
        try {
            Response customResponse = findUserByUsername(username);
            User user = (User) customResponse.getEntity();
            String encryptedPassword = user.getPassword();
            return BCrypt.checkpw(password, encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    public Response getUserAccount(String username, String password) {
        Boolean authenticateUser = verifyUser(username, password);
        if (authenticateUser == true) {
            Response response = findUserByUsername(username);
            User user = (User) response.getEntity();
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Incorrect username or password").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response editUserAccount(String username, String password, User user) {
        
        Boolean authenticateUser = verifyUser(username, password);
        if (authenticateUser == true) {
            
            int editSuccessful = em.createQuery("UPDATE User u SET email = :email, password = :password WHERE u.username = :username")
                .setParameter("email", user.getEmail())
                .setParameter("username", username)
                .setParameter("password", BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .executeUpdate();
            if (editSuccessful > 0) {
                Response response = findUserByUsername(username);
                User updatedUser = (User) response.getEntity();
                return Response.ok(updatedUser).build(); 
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Your account has not been updated.").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Incorrect username or password").build();
        }
    }

    public Response getUserByUserId(UUID userId) {
        try {
            return Response.ok(em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId)
                .getSingleResult()).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user found.").build();
        }
    }

}
