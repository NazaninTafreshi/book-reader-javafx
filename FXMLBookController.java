/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

/**
 *
 * @author Nazanin
 */
public class FXMLBookController implements Initializable {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    @FXML
    private TextField searchTextField;
    @FXML
    private ProgressIndicator progressbar;
                              

    @FXML
    private void searchButtonOnAction(ActionEvent event) {
        System.out.println(searchTextField.getText());
        Search search = new Search(searchTextField.getText());
        progressbar.progressProperty().bind(search.progressProperty());
        executor.submit(search);
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }

}
