/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;

public class Player implements Serializable {
    private String username ;
    private String password;
    private String ImagePath;
    private int score ;
    private int status;
    private boolean inGame=false;

    public Player() {
    }

    
    public Player(String username) {
        this.username = username;
        this.score = 0;
    }



    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Player(String username, String password,String ImagePath) {
        this.username = username;
        this.password = password;
        this.ImagePath=ImagePath;
    }
    
    public Player(String username, String password, String ImagePath, int score, int status) 
    {

        this.username = username;
        this.password = password;
        this.ImagePath = ImagePath;
        this.score = score;
        this.status = status;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    
    
    
    
}
