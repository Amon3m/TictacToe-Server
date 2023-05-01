/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.DataAccessLayer;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mohamed
 */
public class Server {

    private ServerSocket serverSocket ;
    DataAccessLayer dataAccessLayer;

    public Server(ServerSocket serverSocket, DataAccessLayer dataAccessLayer) {
        
        this.serverSocket = serverSocket;
        this.dataAccessLayer = dataAccessLayer;
    }


    //close the server socket if it is open
    public void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void startServer() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection : " + socket);
                ClientHandler clientHandler = new ClientHandler(socket, dataAccessLayer );
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    
    
    
}
