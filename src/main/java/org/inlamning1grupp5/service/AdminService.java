package org.inlamning1grupp5.service;

import java.util.List;

import org.inlamning1grupp5.model.Admin;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class AdminService {
    
    @Inject
    EntityManager em;

    public List<Admin> findAll() {
        return em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }
}
