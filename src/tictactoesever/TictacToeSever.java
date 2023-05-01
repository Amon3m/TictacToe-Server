/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ahmed
 */
public class TictacToeSever extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
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
