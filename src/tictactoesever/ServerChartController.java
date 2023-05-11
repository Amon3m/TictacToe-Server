/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Online",Integer.parseInt("5") ),
                        new PieChart.Data("Offline", 3),
                        new PieChart.Data("In Game", 4));
         for (PieChart.Data data : pieChartData) {
    data.nameProperty().bind(
            Bindings.concat(
                    data.getName(), " : ", data.pieValueProperty()
            )
    );
}
        
                pieChart.getData().addAll(pieChartData);

    }    
      private void navigate(ActionEvent event, String url) throws IOException{
    
                // Load the FXML file for the first screen
        Parent root;
        Stage stage;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        root = loader.load();
        stage =  (Stage)((Node)event.getSource()).getScene().getWindow();

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
        goToMainsever( event);
    }
    
}
