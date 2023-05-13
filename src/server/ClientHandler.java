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
import java.net.SocketException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jdk.nashorn.internal.parser.JSONParser;
import model.CustomException;
import model.Player;

/**
 *
 * @author mohamed
 */
public class ClientHandler implements Runnable {

    public static Vector<ClientHandler> clientHandlers = new Vector<>();
    public Player player;
    public static Vector<Player> onlinePlayers = new Vector<>();

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    DataAccessLayer dataAccessLayer;

    String ImagePath;

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
                System.out.println("request type+arguments" + requestType);

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode rootNode = objectMapper.readTree(requestType);

                switch (rootNode.get("func").asText()) {
                    case "signin":
                        String username = rootNode.get("username").asText();
                        String password = rootNode.get("password").asText();
                        player = logInPlayer(username, password);
                        player.setInGame(false);

                        System.out.println("playerobject Username before send : " + player.getStatus());
                        System.out.println("playerobject Password before send : " + player.getStatus());
                        System.out.println("playerobject score before send : " + player.getScore());

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("head", "loginResponse");
                        jsonObject.addProperty("username", player.getUsername());
                        jsonObject.addProperty("password", player.getPassword());
                        jsonObject.addProperty("status", player.getStatus());
                        jsonObject.addProperty("score", player.getScore());
                        
                        
                        if (player.getImagePath() == null) {
                            System.out.println("innnn");
                            System.out.println(player.getImagePath());

                            ImagePath = "/assets/avatar.png";
                            player.setImagePath(ImagePath);

                        }
                        System.out.println("ouut");
                        System.out.println(player.getImagePath());

                        jsonObject.addProperty("ImagePath", player.getImagePath());

                        Gson gson = new Gson();
                        outputStream.writeUTF(gson.toJson(jsonObject));

//                        outputObjectStream.writeObject(player);
                        break;
                    case "signup":

                        boolean added = signUpPlayer(rootNode.get("username").asText(),
                                rootNode.get("password").asText(), rootNode.get("ImagePath").asText());
                        JsonObject signupResponseJsonObject = new JsonObject();
                        signupResponseJsonObject.addProperty("head", "signupResponse");
                        signupResponseJsonObject.addProperty("result", added);
                        String signupResponseJsonString = new Gson().toJson(signupResponseJsonObject);
                        outputStream.writeUTF(signupResponseJsonString);

                        // outputStream.writeBoolean(added);
                        break;

                    case "invite":
                        // handle whatever request
                        //JsonObject inviteResponse=new JsonObject();
                        String player1 = rootNode.get("player1").asText();
                        String player2 = rootNode.get("player2").asText();
                        System.out.println("from server player1 " + player1 + " invite player2 " + player2);
                        invitePlayer(player1, player2);

                        break;
                    case "replyToInvite":
                        // handle whatever request
                        //JsonObject inviteResponse=new JsonObject();
                        String sender = rootNode.get("senderUsername").asText();
                        String reply = rootNode.get("reply").asText();
                        String reciever = rootNode.get("recievererUsername").asText();
                        checkAcceptence(sender, reciever, reply);

                        System.out.println("from server player2 " + reciever + " reciver from  " + sender);

                        break;
                    case "playMove":
                        String p1 = rootNode.get("player1").asText();
                        String p2 = rootNode.get("player2").asText();
                        String move = rootNode.get("move").asText();
                        String owner = rootNode.get("owner").asText();
                        String counter = rootNode.get("counter").asText();
                        play(p1, p2, move, owner, counter);
                        System.out.println("from server player2 " + p2 + " player from  " + p1 + "owner " + owner + "counter" + counter);
                        break;
                    case "inGame":
                        player.setInGame(true);
                        System.out.println("player from in game "+player.getUsername()+player.isInGame());
                        notifyPlayersListWithPlayerInOrOutOfGame(true);

                        break;
                    case "outGame":
                        player.setInGame(false);
                        System.out.println("player from outgame "+player.getUsername()+player.isInGame());
                        notifyPlayersListWithPlayerInOrOutOfGame(false);

                        break;
                    case "closeclient":
                        closeEverything();
                        break;
                    case "addScore":
                        dataAccessLayer.addScore(rootNode.get("player").asText());
                        break;    

                    default:
                        // handle unknown request type
                        break;
                }

            }

        } catch (Exception e) {
            closeEverything();
        }
        /* 

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Sql Exc000");
            closeEverything();
        }
        catch (Exception e) {
            closeEverything();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("ioe Exc");
                e.printStackTrace();
            }
         */

    }

    void notifyPlayersListWithPlayerInOrOutOfGame(boolean isInGame) {
        for (Player p : onlinePlayers) {
            if (p.getUsername().equals(player.getUsername())) {
                p.setInGame(isInGame);
                break;
            }
        }
        broadcastOnLinePlayers();
    }
    

    //method to remove the clientHandler
    public void removeClientHandler() {
        clientHandlers.remove(this);
        onlinePlayers.remove(player);

        broadcastOnLinePlayers();
    }

    public void closeEverything() {
        System.out.println("Closing a client handler");

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

    private boolean signUpPlayer(String username, String password, String ImagePath) {
        try {
            Player player = new Player(username, password, ImagePath,0);
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

                onlinePlayers.add(player);
                broadcastOnLinePlayers();

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
    public static int getInGamePlayers()
    {
        int inGamePlayers=0;
        for (Player p : onlinePlayers) {
            if (p.isInGame()) {
                inGamePlayers++;
            }
        }
        
        return inGamePlayers;
    }

    public void broadcastOnLinePlayers() {

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                JsonObject jsonObject = new JsonObject();
                List<Player> filteredPlayers = onlinePlayers.stream()
                        .filter(player -> !player.isInGame())
                        .collect(Collectors.toList());

                JsonArray jsonArray = new Gson().toJsonTree(filteredPlayers).getAsJsonArray();
                jsonObject.addProperty("head", "onlineplayersResponse");
                jsonObject.add("playerslist", jsonArray);
                String jsonString = new Gson().toJson(jsonObject);

                clientHandler.outputStream.writeUTF(jsonString);

            } catch (IOException e) {
                System.out.println("ok great!");
            }
        }

    }

    private void invitePlayer(String player1, String player2) {

        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.player.getUsername().equals(player2)) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("head", "inviteRequest");
                    jsonObject.addProperty("player1", player1);
                    jsonObject.addProperty("player2", player2);
                    String jsonString = new Gson().toJson(jsonObject);

                    clientHandler.outputStream.writeUTF(jsonString);
                    //return true;
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
        }
        //return false;
    }

    private void checkAcceptence(String sender, String reciever, String reply) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.player.getUsername().equals(reciever) || clientHandler.player.getUsername().equals(sender)) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("head", "checkAcceptance");
                    jsonObject.addProperty("sender", sender);
                    jsonObject.addProperty("reciever", reciever);
                    jsonObject.addProperty("reply", reply);
                    String jsonString = new Gson().toJson(jsonObject);

                    clientHandler.outputStream.writeUTF(jsonString);
                    //return true;
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
        }
    }

    private void play(String p1, String p2, String move, String owner, String counter) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.player.getUsername().equals(p1) || clientHandler.player.getUsername().equals(p2)) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("head", "play");
                    jsonObject.addProperty("player1", p1);
                    jsonObject.addProperty("player2", p2);
                    jsonObject.addProperty("move", move);
                    jsonObject.addProperty("owner", owner);
                    jsonObject.addProperty("counter", counter);

                    String jsonString = new Gson().toJson(jsonObject);

                    clientHandler.outputStream.writeUTF(jsonString);
                    //return true;
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
        }

    }

}
