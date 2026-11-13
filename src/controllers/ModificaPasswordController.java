package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import models.User;
import services.UserService;
import utils.SceneManager;
import utils.SessionManager;

public class ModificaPasswordController {

    @FXML private PasswordField vecchiaPasswordField;
    @FXML private PasswordField nuovaPasswordField;
    @FXML private PasswordField confermaNuovaPasswordField;
    @FXML private Label feedbackLabel;

    private final UserService userService = new UserService();

    @FXML
    void handleHome(ActionEvent event) {
        SceneManager.switchScene("/views/home.fxml");
    }

    @FXML
    void handleConfirmPassword(ActionEvent event) {
        String vecchiaPassword = vecchiaPasswordField.getText();
        String nuovaPassword = nuovaPasswordField.getText();
        String confermaNuovaPassword = confermaNuovaPasswordField.getText();

        User utenteLoggato = SessionManager.getLoggedUser();

        if (utenteLoggato == null) {
            feedbackLabel.setText("Errore: utente non autenticato");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (vecchiaPassword.isEmpty() || nuovaPassword.isEmpty() || confermaNuovaPassword.isEmpty()) {
            feedbackLabel.setText("Ci sono campi vuoti");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (userService.login(utenteLoggato.getEmail(), vecchiaPassword) == null) {
            feedbackLabel.setText("La vecchia password è errata");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!nuovaPassword.equals(confermaNuovaPassword)) {
            feedbackLabel.setText("Le nuove password non corrispondono");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (vecchiaPassword.equals(nuovaPassword)) {
            feedbackLabel.setText("La nuova password deve essere diversa dalla vecchia");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }


        // Controllo password sicura: almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale
        // String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        // if (!nuovaPassword.matches(passwordPattern)) {
        //     feedbackLabel.setText("La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale.");
        //     feedbackLabel.setStyle("-fx-text-fill: red;");
        //     return;
        // }

        boolean successo = userService.updatePassword(utenteLoggato.getEmail(), nuovaPassword);
        if (successo) {
            // Aggiorna l'oggetto utente in sessione
            User nuovoUtente = userService.login(utenteLoggato.getEmail(), nuovaPassword);
            SessionManager.setLoggedUser(nuovoUtente);

            feedbackLabel.setText("Password modificata con successo");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Errore durante la modifica");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }
}