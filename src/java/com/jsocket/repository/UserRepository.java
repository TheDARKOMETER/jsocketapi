/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.repository;

import com.jsocket.exceptions.UserNotFoundException;
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

    public User findById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.find(User.class, id);
        } catch (Exception e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createQuery("Select user from User user WHERE user.name = :username", User.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createQuery("Select user from User user where user.email = :email", User.class).setParameter("email", email).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void deleteById(Long id) throws UserNotFoundException {
        EntityManager entityManager = emf.createEntityManager();
        User user = findById(id);
        if (user == null) {
            throw new UserNotFoundException("User by id: " + id.toString() + " not found.");
        } else {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    public User update(User user) {
        EntityManager entityManager = emf.createEntityManager();
        User managedUser = null;
        try {
            entityManager.getTransaction().begin();
            managedUser = entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return managedUser;
    }

    public User save(User user) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }

        return user;
    }

    
    public void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
