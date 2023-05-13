/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import model.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CustomException.IncorrectPasswordException;
import model.CustomException.PlayerNotFoundException;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Nouran
 */
public class DataAccessLayer {

    private static PreparedStatement preparedStatement = null;
    private static final String jdbcUrl = "jdbc:derby:tictactoedb;create=true";
    DatabaseHandler databaseHandler = new DatabaseHandler();
    Connection connection = databaseHandler.createConnection();

    public int insert(Player player) {

        String tableName = "PLAYER";
        int rst = 0;
        try {
            connection = databaseHandler.createConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO PLAYER (Username,Password,ImagePath) VALUES (?,?,?)");
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            preparedStatement.setString(3, player.getImagePath());

            rst = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            rst = 0;

            System.out.println("Username " + player.getUsername() + "  already exists!!");

//            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rst;
    }

    public Player checkPlayerExists(String username, String password) throws SQLException, IncorrectPasswordException, PlayerNotFoundException {
        String tableName = "PLAYER";
        Player player = null;
        try {
            List<Player> players = getAll();
            System.out.println("checkPlayerExists getAll");
            for (int i = 0; i < players.size(); i++) {
                System.out.println(players.get(i).getUsername());
                System.out.println(players.get(i).getPassword());

            }
            System.out.println("user from checkPlayerExists " + username);
            System.out.println("user from checkPlayerExists " + password);

            connection = databaseHandler.createConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM PLAYER WHERE Username=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String dbPassword = resultSet.getString("Password");
                String dbImage = resultSet.getString("ImagePath");
                int score=resultSet.getInt("Score");

                if (dbPassword.equals(password)) {
                    player = new Player(username, password, dbImage,score);
                } else {
                    throw new IncorrectPasswordException();
                }
            } else {
                throw new PlayerNotFoundException();
            }
        } catch (SQLException ex) {
            System.out.println("Error selecting record from " + tableName + ": " + ex.getMessage());
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error closing database connection: " + ex.getMessage());
                }
            }
        }
        return player;
    }

    public List<Player> getAll() {
        List<Player> players = new ArrayList<Player>();
        Connection connection = null;
        try {
            connection = databaseHandler.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PLAYER");
            ResultSet rst = preparedStatement.executeQuery();
            while (rst.next()) {
                String firstName = rst.getString(1);
                String password = rst.getString(2);
                String imagePath = rst.getString(3);
                int score = rst.getInt(4);
                int status = rst.getInt(5);
                Player cont = new Player(firstName, password, imagePath, score, status);
                players.add(cont);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error closing database connection: " + ex.getMessage());
                }
            }
        }
        return players;
    }

    public void addScore(String playerID) {
        String sql = "UPDATE PLAYER SET Score = Score + ? WHERE Username = ?";
        int scoreToAdd = 100;

        Connection connection = null;
        try {
            connection = databaseHandler.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, scoreToAdd);
            preparedStatement.setString(2, playerID);
            int rowsAffected = preparedStatement.executeUpdate();
            
           
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Error closing database connection: " + ex.getMessage());
                }
            }
        }
}
}
