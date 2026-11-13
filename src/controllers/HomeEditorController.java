package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class HomeEditorController {
 
    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }
    

    @FXML
    void handlePapersDaControllare(ActionEvent event) {
        SceneManager.switchScene("/views/papersDaControllare.fxml");
    }


    @FXML
    void handleDati(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }
}
