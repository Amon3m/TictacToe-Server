/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.DataAccessLayer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import models.Player;

/**
 *
 * @author mohamed
 */
public class ClientHandler implements Runnable {

    public static Vector<ClientHandler> clientHandlers = new Vector<>();
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    DataAccessLayer dataAccessLayer;


    public ClientHandler(Socket socket, DataAccessLayer dataAccessLayer) {
        try {
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.dataAccessLayer = dataAccessLayer;
            clientHandlers.add(this);

        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {

        try {
            while (!socket.isClosed()) {
                String requestType = inputStream.readUTF();
                
                switch (requestType) {
                    case "login":
                        String username = inputStream.readUTF();
                        String password = inputStream.readUTF();
                        boolean success = logInPlayer(username, password);
                        outputStream.writeBoolean(success);
                        break;
                    case "signup":
                        String newUser = inputStream.readUTF();
                        String newPassword = inputStream.readUTF();
                        boolean added = signUpPlayer(newUser, newPassword);
                        outputStream.writeBoolean(added);
                        break;
                    case "whatever":
                        // handle closing of connection
                        break;
                    default:
                        // handle unknown request type
                        break;
                }                
                
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //method to remove the clientHandler
    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    public void closeEverything() {
        removeClientHandler();
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
private boolean signUpPlayer(String username, String password) {
        try {
            Player player = new Player(username,password);
            int res = dataAccessLayer.insert(player);
            return res >0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
       
    }


    
    //Edit this
    private boolean logInPlayer(String username, String password) {
        System.out.println("USER try to login");
        return true;
    }
    
    
    
    
}


