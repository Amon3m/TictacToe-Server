/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoesever;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class MainServerController implements Initializable {

    @FXML
    private Button playerStateBtn;
    private Font myCustomFont;
    @FXML
    private ToggleButton onServerBtn;
    @FXML
    private AnchorPane parent;
    private boolean flag=true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myCustomFont = Font.loadFont(getClass().getResourceAsStream("/fonts/gumbo.otf"), 18);
        Set<Node> allNodes = parent.lookupAll("*");
        for (Node node : allNodes) {
            if (node instanceof Text) {
                ((Text) node).setFont(myCustomFont);

            } else if (node instanceof Button) {
                ((Button) node).setFont(myCustomFont);
            } else if (node instanceof ToggleButton) {
                ((ToggleButton) node).setFont(myCustomFont);
            } else if (node instanceof TextField) {
                ((TextField) node).setFont(myCustomFont);
            }
        }
    }

    @FXML
    private void StatusOnClick(ActionEvent event) throws IOException {
            goToServerChart(event);

    }

    @FXML
    private void toggleOnClick(ActionEvent event) {
         ToggleButton onClick = (ToggleButton) event.getSource();
         if (flag){
            onClick.setText("Server is on");
            flag=false;
         }
         
         else{onClick.setText("Server is off");
                     flag=true;
}
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
    private void goToServerChart(ActionEvent event) throws IOException {
        navigate(event, "serverChart.fxml");
    }

}
