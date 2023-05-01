/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class ServerChartController implements Initializable {

    @FXML
    private PieChart pieChart;

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
        // TODO
                pieChart.getData().addAll(pieChartData);

    }    
    
}
