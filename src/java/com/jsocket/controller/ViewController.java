/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author thebe
 */

// Java based url mapping instead of xml configuration for less verbosity.


@Controller
public class ViewController {
    
    @GetMapping("/index.htm")
    public String index() {
        return "index";
    }
}
