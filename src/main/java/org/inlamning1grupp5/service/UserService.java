package org.inlamning1grupp5.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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

    @Inject
    @ConfigProperty(name = "mp3.file.path")
    String mp3FilePath;

    public Response findUserByUsername(String username) {
        try {
            return Response.ok(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult()).build();
        } catch (NoResultException e) {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).entity("No user found.").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response createNewUser(User user) {
        
        try {
            try {
                em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", user.getEmail()).getSingleResult();
                return Response.status(Response.Status.CONFLICT).entity("This email adress is already connected to an account! Both the email and the username must be unique.").build();
            } catch (Exception e) {
                System.out.println(e);
            }
            em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", user.getUsername()).getSingleResult();
            return Response.status(Response.Status.CONFLICT).entity("This username is already in use! Both the email and the username must be unique.").build();
        } catch (NoResultException e) {
            Random random = new Random();
            user.setUserId(random.nextLong(100000000, 999999999));
            user.setSubscribed(0);
            String encrypted = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(encrypted);
            System.out.println(user.getLastName());
            System.out.println(user.getUserId());
            em.persist(user);
            return Response.ok(user).entity("You have successfully created an account!").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response deleteUserAccount(String username, String password) {

            Boolean userAuthenticated = verifyUser(username, password);
            System.out.println(userAuthenticated);
            if (userAuthenticated == true) {
                Response customResponse = findUserByUsername(username);
                User user = (User) customResponse.getEntity();
                String encryptedPassword = user.getPassword();
                int deleteSuccessful = em.createQuery("DELETE FROM User u WHERE u.username = :username AND u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", encryptedPassword)
                    .executeUpdate();
                System.out.println("success: " + deleteSuccessful);
                if (deleteSuccessful > 0) {
                    return Response.ok().entity(username + " successfully deleted.").build(); 
                } else {
                    return Response.ok().entity("User account doesnt exist.").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password. Try again.").build();
            }

    }

    public boolean verifyUser(String username, String password) {
        try {
            Response customResponse = findUserByUsername(username);
            User user = (User) customResponse.getEntity();
            String encryptedPassword = user.getPassword();
            return BCrypt.checkpw(password, encryptedPassword);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public Response getUserAccount(String username, String password) {
        Boolean authenticateUser = verifyUser(username, password);
        if (authenticateUser == true) {
            Response response = findUserByUsername(username);
            User user = (User) response.getEntity();
            user.setPassword("");
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password").build();
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
                return Response.ok().entity(username + " successfully updated.").build(); 
            } else {
                return Response.ok().entity("Your account has not been updated.").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password").build();
        }
    }

    public Response getPodcastFromServer(String productId) {

        try {
            String newPath = "C:\\Users\\david\\repos\\github\\Pod grupp5\\inlamning1grupp5\\src\\main\\resources\\META-INF\\resources\\" + productId + ".mp3";
            java.nio.file.Path filePath = java.nio.file.Path.of(newPath);
            InputStream audioFile = Files.newInputStream(filePath, StandardOpenOption.READ);
            return Response.ok(audioFile).build();
        } catch (NoSuchFileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
