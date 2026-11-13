package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.User;
import services.UserService;
import utils.SceneManager;
import utils.SessionManager;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label forgotPasswordLabel;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        forgotPasswordLabel.setOnMouseEntered(e ->
            forgotPasswordLabel.setStyle("-fx-underline: true; -fx-text-fill: #3366cc; -fx-cursor: hand;")
        );
        forgotPasswordLabel.setOnMouseExited(e ->
            forgotPasswordLabel.setStyle("-fx-underline: false; -fx-text-fill: #3366cc; -fx-cursor: hand;")
        );
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email e password sono obbligatori");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        User user = userService.login(email, password);
        if (user != null) {
            SessionManager.setLoggedUser(user);
            SceneManager.switchScene("/views/home.fxml");
        } else {
            errorLabel.setText("Email o password non validi");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void handlePasswordRecovery(MouseEvent event) {
        SceneManager.switchScene("/views/recuperaPassword.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/autenticazione.fxml");
    }
}