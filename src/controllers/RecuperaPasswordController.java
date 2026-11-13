package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.SceneManager;
import services.UserService;

public class RecuperaPasswordController {

    @FXML private TextField emailField;
    @FXML private Label feedbackLabel;

    private final UserService userService = new UserService();

    @FXML
    void handleBackToLogin(ActionEvent event) {
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    void handlePasswordReset(ActionEvent event) {
        String email = emailField.getText();
        if (email.isEmpty()) {
            feedbackLabel.setText("Inserisci un'email");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        } else {
            boolean exists = userService.emailExists(email);
            if (exists) {
                feedbackLabel.setText("Email inviata");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            } else {
                feedbackLabel.setText("Email non è registrata");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }
}
