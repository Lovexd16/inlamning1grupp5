package org.inlamning1grupp5.service;

import java.util.List;

import org.inlamning1grupp5.model.Customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class CustomerService{

    @Inject
    EntityManager em;

    public List<Customer> findAll() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }
    
}
