/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.models;

import java.util.ArrayList;

/**
 *
 * @author thebe
 */
public class UsersOnline {
    private int onlineCount;
    private ArrayList<String> users;
    
    public UsersOnline() {}
    
    
    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
    
    public void setUserList(ArrayList<String> users) {
        this.users = users;
    }
    
    public int getOnlineCount() {
        return onlineCount;
    }
    
    public ArrayList<String> getUserList() {
        return users;
    }
   
    
}
