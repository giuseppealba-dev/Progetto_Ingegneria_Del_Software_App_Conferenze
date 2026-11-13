package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import models.Articolo;
import models.User;
import models.UserRuolo;
import services.UserService;
import services.UserRuoloService;
import services.RuoloService;
import services.CodiceRuoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class InvitaSottoRevisoreController {

    @FXML private TextField emailField;
    @FXML private Label feedbackLabel;

    private final UserService userService = new UserService();
    private final UserRuoloService userRuoloService = new UserRuoloService();
    private final RuoloService ruoloService = new RuoloService();
    private final CodiceRuoloService codiceRuoloService = new CodiceRuoloService();

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/paperDaRevisionare.fxml");
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
        int ruoloSottoRevisoreId = ruoloService.getRuoloByNome("Sotto-Revisore").getId();
        int ruoloChairId = ruoloService.getRuoloByNome("Chair").getId();
        int ruoloEditorId = ruoloService.getRuoloByNome("Editor").getId();

        List<UserRuolo> ruoliUtente = userRuoloService.getRuoliUtente(user.getId());
        boolean haRuoloSottoRevisore = false;
        boolean haRuoloChair = false;
        boolean haRuoloEditor = false;
        for (UserRuolo ur : ruoliUtente) {
            if (ur.getConferenzaId() == conferenzaId) {
                if (ur.getRuoloId() == ruoloSottoRevisoreId) haRuoloSottoRevisore = true;
                if (ur.getRuoloId() == ruoloChairId) haRuoloChair = true;
                if (ur.getRuoloId() == ruoloEditorId) haRuoloEditor = true;
            }
        }
        if (haRuoloChair || haRuoloEditor) {
            feedbackLabel.setText("L'utente è già Chair o Editor in questa conferenza.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Assegna ruolo se non già presente
        if (!haRuoloSottoRevisore) {
            userRuoloService.assegnaRuolo(user.getId(), ruoloSottoRevisoreId, conferenzaId);
        }

        // Assegna paper al sottorevisore (anche se già ha il ruolo)
        Articolo articolo = SessionManager.getArticoloSelezionato();
        int revisoreId = SessionManager.getLoggedUser().getId(); // revisore che invita
        boolean ok = new services.SottoRevisioneService().assegnaSottoRevisore(
            articolo.getId(), user.getId(), revisoreId, conferenzaId
        );

        if (ok) {
            feedbackLabel.setText("Sotto-revisore assegnato al paper!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Questo sottorevisore è già assegnato a questo paper.");
            feedbackLabel.setStyle("-fx-text-fill: orange;");
        }
        emailField.clear();
    }
}