/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.com.softchat.quickchat;

/**
 *
 * @author Angel
 */
public class Login {
     public boolean checkUserName(String username) {
       return username.contains("_") && username.length() <= 5; 
    }   
}
