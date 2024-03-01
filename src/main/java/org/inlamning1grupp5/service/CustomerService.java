package org.inlamning1grupp5.service;

import java.util.List;
import java.util.Random;

import org.inlamning1grupp5.model.Customer;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class CustomerService{

    @Inject
    EntityManager em;

    public List<Customer> findAll() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    public Response findCustomerByUsername(String username) {
        try {
            return Response.ok(em.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class)
                .setParameter("username", username)
                .getSingleResult()).build();
        } catch (NoResultException e) {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).entity("No user found.").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response createNewCustomer(Customer customer) {
        
        try {
            try {
                em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class).setParameter("email", customer.getEmail()).getSingleResult();
                return Response.status(Response.Status.CONFLICT).entity("This email adress is already connected to an account! Both the email and the username must be unique.").build();
            } catch (Exception e) {
                System.out.println(e);
            }
            em.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class).setParameter("username", customer.getUsername()).getSingleResult();
            return Response.status(Response.Status.CONFLICT).entity("This username is already in use! Both the email and the username must be unique.").build();
        } catch (NoResultException e) {
            Random random = new Random();
            customer.setCustomerId(random.nextLong(100000000, 999999999));
            customer.setSubscribed(0);
            String encrypted = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
            customer.setPassword(encrypted);
            System.out.println(customer.getLastName());
            System.out.println(customer.getCustomerId());
            em.persist(customer);
            return Response.ok(customer).entity("You have successfully created an account!").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response deleteCustomerAccount(String username, String password) {

            Boolean userAuthenticated = verifyUser(username, password);
            System.out.println(userAuthenticated);
            if (userAuthenticated == true) {
                Response customResponse = findCustomerByUsername(username);
                Customer customer = (Customer) customResponse.getEntity();
                String encryptedPassword = customer.getPassword();
                int deleteSuccessful = em.createQuery("DELETE FROM Customer c WHERE c.username = :username AND c.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", encryptedPassword)
                    .executeUpdate();
                System.out.println("success: " + deleteSuccessful);
                if (deleteSuccessful > 0) {
                    return Response.ok().entity(username + " successfully deleted.").build(); 
                } else {
                    return Response.ok().entity("Customer account doesnt exist.").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password. Try again.").build();
            }

    }

    public boolean verifyUser(String username, String password) {
        try {
            Response customResponse = findCustomerByUsername(username);
            Customer customer = (Customer) customResponse.getEntity();
            String encryptedPassword = customer.getPassword();
            return BCrypt.checkpw(password, encryptedPassword);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public Response getCustomerAccount(String username, String password) {
        Boolean authenticateCustomer = verifyUser(username, password);
        if (authenticateCustomer == true) {
            Response response = findCustomerByUsername(username);
            Customer customer = (Customer) response.getEntity();
            customer.setPassword("");
            return Response.ok(customer).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response editCustomerAccount(String username, String password, Customer customer) {
        
        Boolean authenticateCustomer = verifyUser(username, password);
        if (authenticateCustomer == true) {
            
            int editSuccessful = em.createQuery("UPDATE Customer c SET email = :email, password = :password WHERE c.username = :username")
                .setParameter("email", customer.getEmail())
                .setParameter("username", username)
                .setParameter("password", BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt()))
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
}
