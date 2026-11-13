package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.SceneManager;

public class AutenticazioneController {

    @FXML
    void handleLogin(ActionEvent event) {
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    void handleRegister(ActionEvent event) {
        SceneManager.switchScene("/views/register.fxml");
    }
}