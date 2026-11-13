package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class HomeChairController {

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }
    

    @FXML
    void handlePartecipanti(ActionEvent event) {
        SceneManager.switchScene("/views/listaPartecipanti.fxml");
    }

    @FXML
    void handleModifiche(ActionEvent event) {
        SceneManager.switchScene("/views/modificaDatiConferenza.fxml");
    }

    @FXML
    void handleInvitaChair(ActionEvent event) {
        SceneManager.switchScene("/views/invitaChair.fxml");
    }

    @FXML
    void handleInvitaRevisore(ActionEvent event) {
        SceneManager.switchScene("/views/invitaRevisore.fxml");
    }

    @FXML
    void handleInvitaEditor(ActionEvent event) {
        SceneManager.switchScene("/views/invitaEditor.fxml");
    }


    @FXML
    void handleElencoArticoli(ActionEvent event) {
        SceneManager.switchScene("/views/elencoPapersChair.fxml");
    }

    @FXML
    void handleDati(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }


    
}
