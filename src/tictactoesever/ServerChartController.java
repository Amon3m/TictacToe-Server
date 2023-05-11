/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import database.DataAccessLayer;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Player;
import server.ClientHandler;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class ServerChartController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private Button BackBtn;
    private int allUsers;
    private int offlinePlayers;
    private int onlinePlayers;
    private int inGamePlayers;
    private DataAccessLayer dataAccessLayer = new DataAccessLayer();
    ;

    /**
     * Initializes the controller class.
     */
     ObservableList<PieChart.Data> pieChartData= FXCollections.observableArrayList(
                        new PieChart.Data("Online", onlinePlayers),
                        new PieChart.Data("Offline", offlinePlayers),
                        new PieChart.Data("In Game",inGamePlayers ));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Player> players = dataAccessLayer.getAll();
        //set the first player to be inGame
        players.get(0).setInGame(true);
        //Count the number of inGame players
        for(int i=0;i<players.size();i++)
        {
            if(players.get(i).isInGame())
            {   inGamePlayers++;
            }
        }
        allUsers = dataAccessLayer.getAll().size();
        pieChart.setData(pieChartData);
        
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            // Update the data in the list based on the values of online, offline, and inGame players
            int onlinePlayers = getOnlinePlayers();
            int offlinePlayers = getOfflinePlayers();
            int unknownPlayers = getInGamePlayers();

            pieChartData.get(0).setPieValue(onlinePlayers);
            pieChartData.get(1).setPieValue(offlinePlayers);
            pieChartData.get(2).setPieValue(unknownPlayers);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        for (PieChart.Data data : pieChartData) {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " : ", data.pieValueProperty()
                    )
            );
            
        }
    }

    private void navigate(ActionEvent event, String url) throws IOException {

        // Load the FXML file for the first screen
        Parent root;
        Stage stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void goToMainsever(ActionEvent event) throws IOException {
        navigate(event, "MainServer.fxml");
    }

    @FXML
    private void onBackBtn(ActionEvent event) throws IOException {
        goToMainsever(event);
    }
    private int getOnlinePlayers() {
        onlinePlayers = ClientHandler.onlinePlayers.size();
        return onlinePlayers ;
    }

    private int getOfflinePlayers() {
        offlinePlayers=allUsers - getOnlinePlayers();
        return offlinePlayers;
    }

    private int getInGamePlayers() {
        return inGamePlayers;
    }

}
