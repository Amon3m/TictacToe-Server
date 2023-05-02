/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.DataAccessLayer;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mohamed 
 */
public class Server {

    private ServerSocket serverSocket;
    private DataAccessLayer dataAccessLayerr;
    private static Server instance = null;
    private static int port = 3333;

    
    public static Server getInstance() throws IOException {
        if (instance == null) {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            DataAccessLayer dataAccessLayer = new DataAccessLayer();

            instance = new Server(new ServerSocket(port), dataAccessLayer);
        }
        return instance;
    }

    public Server(ServerSocket serverSocket, DataAccessLayer dataAccessLayer) {

        this.serverSocket = serverSocket;
        this.dataAccessLayerr = dataAccessLayer;
    }

    //close the server socket if it is open
    public void stopServer() {

            try {
                    
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    System.out.println("Server stopped.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

    }

    public void startServer() {
        try {
            // Check if serverSocket is null or closed
            if (serverSocket == null || serverSocket.isClosed()) {
                // Create new ServerSocket object
                serverSocket = new ServerSocket(port);
            }
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection : " + socket);
                ClientHandler clientHandler = new ClientHandler(socket, dataAccessLayerr);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    
}



