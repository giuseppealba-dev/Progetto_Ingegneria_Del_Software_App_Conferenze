package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class HomeRevisoreController {

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }

    @FXML
    void handleDati(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }

    @FXML
    void handlePapersReview(ActionEvent event) {
        SceneManager.switchScene("/views/paperDaRevisionare.fxml");
    }

    @FXML
    void handleBroadCast(ActionEvent event) {
        SceneManager.switchScene("/views/richiesteRevisione.fxml");
    }
}

