/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import database.DataAccessLayer;
import database.DatabaseHandler;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Player;

/**
 *
 * @author ahmed
 */
public class TictacToeSever extends Application {
    private DatabaseHandler databaseHandler;
    private DataAccessLayer dataAccessLayer;

    
    @Override
    public void start(Stage stage) throws Exception {

        databaseHandler = new DatabaseHandler();
        dataAccessLayer = new DataAccessLayer();
        Player player = new Player("asdsdad","Haaaamza");
        dataAccessLayer.insert(player);
        
        List<Player> players = dataAccessLayer.getAll();
        for (Player p : players){
            System.out.println(p.getUsername());
            System.out.println(p.getPassword());
            System.out.println(p.getImagePath());
            

        }
       
                        Parent root = FXMLLoader.load(getClass().getResource("MainServer.fxml"));
        
         Scene scene = new Scene(root, 800, 600);

        
        

        
        stage.setScene(scene);
        stage.show();

        // Fix the size of the stage
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaxWidth(800);
        stage.setMaxHeight(600);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
