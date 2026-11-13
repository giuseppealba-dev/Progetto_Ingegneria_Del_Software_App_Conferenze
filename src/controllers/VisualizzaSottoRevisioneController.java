package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Revisione;
import models.User;
import services.UserService;
import utils.SceneManager;
import utils.SessionManager;

public class VisualizzaSottoRevisioneController {
    @FXML private Label sottoRevisoreLabel;
    @FXML private TextArea commentoArea;
    @FXML private Label rateLabel;
    @FXML private Label dataLabel;

    @FXML
    public void initialize() {
        Revisione rev = SessionManager.getRevisioneSelezionata();
        if (rev == null) return;

        // Recupera nome sottorevisore se vuoi mostrare nome/cognome
        UserService userService = new UserService();
        User user = userService.getUserById(rev.getIdRevisore());
        if (user != null) {
            sottoRevisoreLabel.setText(user.getNome() + " " + user.getCognome() + " <" + user.getEmail() + ">");
        } else {
            sottoRevisoreLabel.setText("ID: " + rev.getIdRevisore());
        }

        commentoArea.setText(rev.getCommento());
        rateLabel.setText(String.valueOf(rev.getRate()));
        dataLabel.setText(rev.getData() != null ? rev.getData().toString() : "");
    }

    @FXML
    void handleBack() {
        SceneManager.switchScene("/views/revisionaPaper.fxml");
    }
}