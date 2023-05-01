/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.scene.control.Alert;

/**
 *
 * @author Nouran
 */
public class DatabaseHandler 
{    
    private static DatabaseHandler databaseHandler;
    private static final String jdbcUrl="jdbc:derby:tictactoedb;create=true";
    private static Connection connection=null;
    private static Statement statement=null;
    private static PreparedStatement preparedStatement=null;

    public DatabaseHandler() {
        createConnection();
        createPlayerTable();
    }
    
    void createPlayerTable()
    {
        String tableName="PLAYER";
        int rst=0;
        try {
            statement=connection.createStatement();
            
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet tables=databaseMetaData.getTables(null, null, tableName.toUpperCase(), null);
            
            if(tables.next())
            {
                System.out.println("Table "+tableName+" is already exist!!");
            }
            else
            {
                statement.executeUpdate("CREATE TABLE "
                        + tableName
                        + "(Username VARCHAR(255)NOT NULL,"
                        + "Password VARCHAR(255) NOT NULL,"
                        + "ImagePath VARCHAR(255) default 'single.png',"
                        + "Score INT default 0,"
                        + "Status INT default 0,"
                        + "PRIMARY KEY(Username))");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
             
    }
   
    
    Connection createConnection()
    {
        
        try {
            //Register the driver
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Driver Loaded.");
            
            //Getting connection object
            connection=DriverManager.getConnection(jdbcUrl);
            System.out.println("Got Connection.");
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return connection;
    }
    
}
