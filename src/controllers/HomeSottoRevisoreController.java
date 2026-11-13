package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class HomeSottoRevisoreController {
    

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }

    @FXML
    void handlePapersReview2(ActionEvent event) {
        SceneManager.switchScene("/views/paperDaSottoRevisionare.fxml");
    }

    @FXML
    void handleDati(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }

}
