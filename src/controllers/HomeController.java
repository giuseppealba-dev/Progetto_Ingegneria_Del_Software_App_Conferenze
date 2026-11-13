package controllers;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import utils.SceneManager;

public class HomeController {

    
    
    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText(null);
        alert.setContentText("Vuoi fare il logout?");

        ButtonType yesButton = new ButtonType("Si", ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // L'utente ha confermato il logout
            utils.SessionManager.clearSession(); 
            SceneManager.switchScene("/views/autenticazione.fxml");
        }
        // Se clicca "No" o chiude il popup con la X, non fa nulla e rimane sulla schermata corrente
    }
    

    @FXML
    void handleModificaPassword(ActionEvent event) {
        SceneManager.switchScene("/views/modificaPassword.fxml");
    }

    @FXML
    void handleCreateConference(ActionEvent event) {
        SceneManager.switchScene("/views/creaConferenza.fxml"); 
    }

    @FXML
    void handleConferenceList(ActionEvent event) {
        SceneManager.switchScene("/views/listaConferenze.fxml");
    }
}
