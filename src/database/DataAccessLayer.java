/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import models.Player;
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
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Nouran
 */
public class DataAccessLayer {
    
    private static PreparedStatement preparedStatement=null;
    private static final String jdbcUrl="jdbc:derby:tictactoedb;create=true";
    DatabaseHandler databaseHandler=new DatabaseHandler();
    Connection connection=databaseHandler.createConnection();
    
    public int insert(Player player)
    {
        
        String tableName="PLAYER";
        int rst=0;
        try {
            preparedStatement=connection.prepareStatement("INSERT INTO PLAYER (Username,Password) VALUES (?,?)");
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            rst=preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return rst;
    }
    
}
