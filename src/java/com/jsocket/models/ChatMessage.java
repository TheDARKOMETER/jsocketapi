/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.models;


import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name="chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private UUID uuid;
    
    private String content;
    
    private long timestamp;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private User author;
    
    private int likes;
    
    private int dislikes;

    protected ChatMessage() {
    }

    public ChatMessage(String content, long timestamp, User author, int likes, int dislikes, UUID uuid) {
        this.content = content;
        this.timestamp = timestamp;
        this.author = author;
        this.likes = likes;
        this.dislikes = dislikes;
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public User getAuthor() {
        return author;
    }
    
    public Long getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public UUID getUuid() {
        return uuid;
    }

}
