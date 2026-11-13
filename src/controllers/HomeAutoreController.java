package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class HomeAutoreController {

    @FXML
    void goToMyArticles(ActionEvent event) {
        SceneManager.switchScene("/views/mieiArticoli.fxml");
    }

    @FXML
    void handleSubmitArticle(ActionEvent event) {
        SceneManager.switchScene("/views/sottomettiArticolo.fxml");
    }

    @FXML
    void handleNewRole(ActionEvent event) {
        SceneManager.switchScene("/views/inserisciRuolo.fxml");
    }



    @FXML
    void handleDati(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }
}