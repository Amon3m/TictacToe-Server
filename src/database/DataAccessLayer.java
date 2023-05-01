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
            rst = 0;
            
            System.out.println("Username " +player.getUsername()+"  already exists!!");

//            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rst;
    }
    
  
    
    public List<Player> getAll()
    {
        List<Player> players=new ArrayList<Player>();
        
        try {
            //Statement stmt = con.createStatement();
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM PLAYER");
           
           
             ResultSet rst= preparedStatement.executeQuery();
             
             while(rst.next())
             {
                 String firstName=rst.getString(1);
                 String password=rst.getString(2);
                 String imagePath=rst.getString(3);
                 int score=rst.getInt(4);
                 int status=rst.getInt(5);
                 Player cont=new Player(firstName, password,imagePath,score,status);
                // ContactsDTO cont1=new ContactsDTO(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6));
                 
                 players.add(cont);
             }
            
            connection.close();


        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;

}
    
    
    
    
}
