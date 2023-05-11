/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import jdk.nashorn.internal.parser.JSONParser;
import model.CustomException;
import model.Player;

/**
 *
 * @author mohamed
 */
public class ClientHandler implements Runnable {

    public static Vector<ClientHandler> clientHandlers = new Vector<>();
    public static Vector<Player> onlinePlayers = new Vector<>();
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    DataAccessLayer dataAccessLayer;
    Player player;

    public ClientHandler(Socket socket, DataAccessLayer dataAccessLayer) {
        try {
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.dataAccessLayer = dataAccessLayer;
            clientHandlers.add(this);

            List<Player> players = dataAccessLayer.getAll();
            System.out.println("ClientHandler constractor");
            for (int i = 0; i < players.size(); i++) {
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
                System.out.println(requestType);

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode rootNode = objectMapper.readTree(requestType);
//                System.out.println(rootNode.get("username").asText());
//                System.out.println(rootNode.get("password").asText());
//                System.out.println(rootNode.get("func").asText());

                switch (rootNode.get("func").asText()) {
                    case "signin":
                        String username = rootNode.get("username").asText();
                        String password = rootNode.get("password").asText();
                        player = logInPlayer(username, password);
                        System.out.println("playerobject Username before send : " + player.getStatus());
                        System.out.println("playerobject Password before send : " + player.getStatus());

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("head", "loginResponse");
                        jsonObject.addProperty("username", player.getUsername());
                        jsonObject.addProperty("password", player.getPassword());
                        jsonObject.addProperty("status", player.getStatus());
                        jsonObject.addProperty("ImagePath", player.getImagePath());
                        Gson gson = new Gson();
                        outputStream.writeUTF(gson.toJson(jsonObject));

                        onlinePlayers.add(player);
                        broadcastOnLinePlayers();

//                        outputObjectStream.writeObject(player);
                        break;
                    case "signup":

                        boolean added = signUpPlayer(rootNode.get("username").asText(),
                                rootNode.get("password").asText(),rootNode.get("ImagePath").asText());
                        JsonObject signupResponseJsonObject = new JsonObject();
                        signupResponseJsonObject.addProperty("head", "signupResponse");
                        signupResponseJsonObject.addProperty("result", added);
                        String signupResponseJsonString = new Gson().toJson(signupResponseJsonObject);
                        outputStream.writeUTF(signupResponseJsonString);

                        // outputStream.writeBoolean(added);
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
        }

    }

    //method to remove the clientHandler
    public void removeClientHandler() {
        clientHandlers.remove(this);
        onlinePlayers.remove(player);

        broadcastOnLinePlayers();
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

    private boolean signUpPlayer(String username, String password,String ImagePath) {
        try {
            Player player = new Player(username, password,ImagePath);
            int res = dataAccessLayer.insert(player);
            return res > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private Player logInPlayer(String username, String password) throws SQLException {
        try {
            System.out.println("USER try to login");

            player = dataAccessLayer.checkPlayerExists(username, password);
            player.setStatus(1);
            if (player.equals(null)) {

                System.out.println("logInPlayer not success");

            } else {
                System.out.println("logInPlayer success");
            }
            return player;
        } catch (CustomException.IncorrectPasswordException ex) {
            player = new Player();
            player.setStatus(0);

            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException.PlayerNotFoundException ex) {

            player = new Player();
            player.setStatus(-1);
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return player;

        }

    }

    public void broadcastOnLinePlayers() {

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                JsonObject jsonObject = new JsonObject();
                JsonArray jsonArray = new Gson().toJsonTree(onlinePlayers).getAsJsonArray();
                jsonObject.addProperty("head", "onlineplayersResponse");
                jsonObject.add("playerslist", jsonArray);
                String jsonString = new Gson().toJson(jsonObject);

                clientHandler.outputStream.writeUTF(jsonString);

            } catch (IOException e) {
                System.out.println("ok great!");
            }
        }

    }

}
