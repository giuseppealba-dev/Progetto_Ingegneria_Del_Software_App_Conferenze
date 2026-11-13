package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import models.User;
import models.UserRuolo;
import services.UserService;
import services.UserRuoloService;
import services.RuoloService;
import services.CodiceRuoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class InvitaRevisoreController {

    @FXML private TextField emailField;
    @FXML private Label feedbackLabel;

    private final UserService userService = new UserService();
    private final UserRuoloService userRuoloService = new UserRuoloService();
    private final RuoloService ruoloService = new RuoloService();
    private final CodiceRuoloService codiceRuoloService = new CodiceRuoloService();

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeChair.fxml");
    }

    @FXML
    void handleInvia(ActionEvent event) {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            feedbackLabel.setText("Inserisci un'email.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        User user = userService.getUserByEmail(email);
        if (user == null) {
            feedbackLabel.setText("Utente non registrato.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();
        int ruoloRevisoreId = ruoloService.getRuoloByNome("Revisore").getId();
        int ruoloChairId = ruoloService.getRuoloByNome("Chair").getId();
        int ruoloEditorId = ruoloService.getRuoloByNome("Editor").getId();

        List<UserRuolo> ruoliUtente = userRuoloService.getRuoliUtente(user.getId());
        boolean haRuoloRevisore = false;
        boolean haRuoloChair = false;
        boolean haRuoloEditor = false;
        for (UserRuolo ur : ruoliUtente) {
            if (ur.getConferenzaId() == conferenzaId) {
                if (ur.getRuoloId() == ruoloRevisoreId) haRuoloRevisore = true;
                if (ur.getRuoloId() == ruoloChairId) haRuoloChair = true;
                if (ur.getRuoloId() == ruoloEditorId) haRuoloEditor = true;
            }
        }
        if (haRuoloRevisore) {
            feedbackLabel.setText("L'utente è già Revisore in questa conferenza.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        if (haRuoloChair || haRuoloEditor) {
            feedbackLabel.setText("L'utente è già Chair o Editor in questa conferenza.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Salva il codice per quell'email
        codiceRuoloService.setUltimoCodicePerUtente(email, "R");

        feedbackLabel.setText("Invito inviato! Codice: R");
        feedbackLabel.setStyle("-fx-text-fill: green;");
        emailField.clear();
    }
}