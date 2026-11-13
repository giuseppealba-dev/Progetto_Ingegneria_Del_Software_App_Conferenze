package controllers;

import services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import services.UserService;
import utils.SceneManager;

public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confermaPasswordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    void handleRegister(ActionEvent event) {
        String nome = firstNameField.getText();
        String cognome = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confermaPassword = confermaPasswordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
            errorLabel.setText("Ci campi sono vuoti");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        //Controllo di verifica formato email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errorLabel.setText("Email non valida");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!password.equals(confermaPassword)) {
            errorLabel.setText("Le password non corrispondono");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }


        // Controllo password sicura: almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale
        // String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        // if (!password.matches(passwordPattern)) {
        //     errorLabel.setText("La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale.");
        //     errorLabel.setStyle("-fx-text-fill: red;");
        //     return;
        // }

        User nuovoUtente = new User(nome, cognome, email, password, "Autore");
        boolean saved = userService.register(nuovoUtente);

        if (saved) {
            // LOGIN AUTOMATICO DOPO REGISTRAZIONE
            User logged = userService.login(email, password);
            if (logged != null) {
                utils.SessionManager.setLoggedUser(logged);
                utils.SceneManager.switchScene("/views/home.fxml");
            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Registrazione avvenuta, ma errore nel login automatico.");
            }
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Email già usata");
        }
    }

    @FXML
    void handleBackToLogin(ActionEvent event) {
        SceneManager.switchScene("/views/autenticazione.fxml");
    }
}