package org.inlamning1grupp5.service;

import java.util.List;
import java.util.Random;

import org.inlamning1grupp5.model.Customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
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
            em.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class).setParameter("username", customer.getUsername()).getSingleResult();
            return Response.status(Response.Status.CONFLICT).entity("This username already exists!").build();
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
    
}
