package org.inlamning1grupp5.service;

import java.util.List;
import java.util.Random;

import org.inlamning1grupp5.model.Customer;

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
            System.out.println(customer.getLastName());
            System.out.println(customer.getCustomerId());
            em.persist(customer);
            return Response.ok(customer).entity("You have successfully created an account!").build();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Response deleteCustomerAccount(String username, String password) {

            int deleteSuccessful = em.createQuery("DELETE FROM Customer c WHERE c.username = :username AND c.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .executeUpdate();

            if (deleteSuccessful > 0) {
                return Response.ok().entity(username + " successfully deleted.").build(); 
            } else {
                return Response.ok().entity("Customer account doesnt exist.").build();
            }
    }
    
}
