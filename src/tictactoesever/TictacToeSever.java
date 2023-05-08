/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import database.DataAccessLayer;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;
import server.Server;

/**
 *
 * @author ahmed
 */
public class TictacToeSever extends Application {

    Server server;
    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("MainServer.fxml"));

        Scene scene = new Scene(root, 800, 600);

        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.show();

        // Fix the size of the stage
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaxWidth(800);
        stage.setMaxHeight(600);

        Task<Void> serverTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Server server = Server.getInstance();
                server.startServer();

                return null;
            }

        };
        new Thread(serverTask).start();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);

    }

}
