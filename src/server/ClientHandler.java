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
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;


/**
 *
 * @author mohamed
 */
public class ClientHandler implements Runnable {

    public static Vector<ClientHandler> clientHandlers = new Vector<>();
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectOutputStream outputObjectStream;
    DataAccessLayer dataAccessLayer;


    public ClientHandler(Socket socket, DataAccessLayer dataAccessLayer) {
        try {
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.outputObjectStream=new ObjectOutputStream(socket.getOutputStream());
            this.dataAccessLayer = dataAccessLayer;
            clientHandlers.add(this);
            
            List<Player>players=dataAccessLayer.getAll();
            System.out.println("ClientHandler constractor");
                        for(int i=0;i<players.size();i++){
                            System.out.println(players.get(i).getUsername());
                            System.out.println(players.get(i).getPassword());

                      }
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
                    case "signin":
                        String username = inputStream.readUTF();
                        String password = inputStream.readUTF();
                        Player player = logInPlayer(username, password);
                        System.out.println("playerobject Username before send : "+player.getUsername());
                        System.out.println("playerobject Password before send : "+player.getPassword());
                        outputObjectStream.writeObject(player);
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
        } catch (Exception e) {
            closeEverything();
            System.out.println("Client Disconnected");
            
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
    private Player logInPlayer(String username, String password) throws SQLException {
        System.out.println("USER try to login");
        
        Player rePlayer = dataAccessLayer.checkPlayerExists(username,password);
        if(rePlayer.equals(null)){
        
            System.out.println("logInPlayer not success");
        
        }else{System.out.println("logInPlayer success");
}
    return rePlayer;}
    
    
    
    
}


