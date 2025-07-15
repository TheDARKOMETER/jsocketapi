/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.jsocket.models.User;
import javax.persistence.Persistence;

/**
 *
 * @author thebe
 */
public class UserRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jsocketapiPU");
    private static UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User getUser(long id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.find(User.class, id);
        } finally {
            entityManager.close();
        }
    }

}
