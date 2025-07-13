/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.repository;

import javax.persistence.EntityManager;
import com.jsocket.models.ChatMessage;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thebe
 */
public class ChatMessageRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jsocketapiPU");
    private static ChatMessageRepository instance;
    private ChatMessageRepository() {
    }

    public static ChatMessageRepository getInstance() {
        if (instance == null) {
            instance = new ChatMessageRepository();
        }
        return instance;
    }

    public ChatMessage getChatMessage(long id) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.find(ChatMessage.class, id);
    }

    public void addChatMessage(ChatMessage msg) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(msg);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<ChatMessage> getAllChatMessages() {
        EntityManager entityManager = emf.createEntityManager();
        List<ChatMessage> chatMessages = entityManager.createQuery("SELECT msg from ChatMessage msg", ChatMessage.class).getResultList();
        entityManager.close();
        return chatMessages;
    }

    public void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
