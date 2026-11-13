package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import models.User;
import models.UserRuolo;
import services.UserRuoloService;
import services.RuoloService;
import services.CodiceRuoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class InserisciRuoloController {

    @FXML private TextField codiceField;
    @FXML private Label ultimoCodiceLabel;
    @FXML private Label feedbackLabel;

    private final CodiceRuoloService codiceRuoloService = new CodiceRuoloService();
    private final UserRuoloService userRuoloService = new UserRuoloService();
    private final RuoloService ruoloService = new RuoloService();

    @FXML
    void handleBack(ActionEvent event) {
        int userId = SessionManager.getLoggedUser().getId();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();
        List<UserRuolo> ruoliUtente = userRuoloService.getRuoliUtente(userId);

        int ruoloAutoreId = ruoloService.getRuoloByNome("Autore").getId();
        int ruoloChairId = ruoloService.getRuoloByNome("Chair").getId();
        int ruoloEditorId = ruoloService.getRuoloByNome("Editor").getId();
        int ruoloRevisoreId = ruoloService.getRuoloByNome("Revisore").getId();
        int ruoloSottoRevisoreId = ruoloService.getRuoloByNome("Sotto-Revisore").getId();

        // Cerca il ruolo principale nella conferenza selezionata
        boolean isAutore = false;
        Integer ruoloPrincipale = null;
        for (UserRuolo ur : ruoliUtente) {
            if (ur.getConferenzaId() == conferenzaId) {
                if (ur.getRuoloId() == ruoloAutoreId) isAutore = true;
                // Salva il primo ruolo diverso da Autore trovato come principale
                if (ur.getRuoloId() != ruoloAutoreId && ruoloPrincipale == null) {
                    ruoloPrincipale = ur.getRuoloId();
                }
            }
        }

        if (isAutore && ruoloPrincipale == null) {
            SceneManager.switchScene("/views/homeAutore.fxml");
        } else if (ruoloPrincipale != null) {
            if (ruoloPrincipale == ruoloChairId) {
                SceneManager.switchScene("/views/homeChair.fxml");
            } else if (ruoloPrincipale == ruoloEditorId) {
                SceneManager.switchScene("/views/homeEditor.fxml");
            } else if (ruoloPrincipale == ruoloRevisoreId) {
                SceneManager.switchScene("/views/homeRevisore.fxml");
            } else if (ruoloPrincipale == ruoloSottoRevisoreId) {
                SceneManager.switchScene("/views/homeSottoRevisore.fxml");
            } else {
                SceneManager.switchScene("/views/home.fxml");
            }
        } else {
            SceneManager.switchScene("/views/home.fxml");
        }
    }

    @FXML
    public void initialize() {
        String ultimoCodice = codiceRuoloService.getUltimoCodicePerUtente(SessionManager.getLoggedUser().getEmail());
        if (ultimoCodice != null) {
            ultimoCodiceLabel.setText("Ultimo codice ricevuto: " + ultimoCodice);
        } else {
            ultimoCodiceLabel.setText("Ultimo codice ricevuto: -");
        }
    }

    @FXML
    void handleConferma(ActionEvent event) {
        String codice = codiceField.getText().trim().toUpperCase();
        User user = SessionManager.getLoggedUser();
        if (codice.isEmpty()) {
            feedbackLabel.setText("Inserisci un codice.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        String ruolo = codiceRuoloService.getDescrizioneByCodice(codice);
        if (ruolo == null) {
            feedbackLabel.setText("Codice non valido.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String ultimoCodice = codiceRuoloService.getUltimoCodicePerUtente(user.getEmail());
        if (ultimoCodice == null || !ultimoCodice.equalsIgnoreCase(codice)) {
            feedbackLabel.setText("Codice non corrispondente a quello ricevuto.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();
        int userId = user.getId();

        List<UserRuolo> ruoliUtente = userRuoloService.getRuoliUtente(userId);
        int ruoloAutoreId = ruoloService.getRuoloByNome("Autore").getId();
        int ruoloChairId = ruoloService.getRuoloByNome("Chair").getId();
        int ruoloEditorId = ruoloService.getRuoloByNome("Editor").getId();
        int ruoloRevisoreId = ruoloService.getRuoloByNome("Revisore").getId();
        int ruoloSottoRevisoreId = ruoloService.getRuoloByNome("Sotto-Revisore").getId();

        java.util.Set<Integer> ruoliNellaConferenza = new java.util.HashSet<>();
        for (UserRuolo ur : ruoliUtente) {
            if (ur.getConferenzaId() == conferenzaId) {
                ruoliNellaConferenza.add(ur.getRuoloId());
            }
        }

        if (codice.equals("C") || codice.equals("E")) {
            if (!(ruoliNellaConferenza.size() == 1 && ruoliNellaConferenza.contains(ruoloAutoreId))) {
                feedbackLabel.setText("Per diventare " + ruolo + " devi essere solo Autore nella conferenza.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            userRuoloService.rimuoviAssegnazione(userId, ruoloAutoreId, conferenzaId);
            int nuovoRuoloId = codice.equals("C") ? ruoloChairId : ruoloEditorId;
            userRuoloService.assegnaRuolo(userId, nuovoRuoloId, conferenzaId);
            user.setRuolo(ruolo);

            // Mostra popup di conferma e poi vai alla home del ruolo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ruolo cambiato");
            alert.setHeaderText(null);
            alert.setContentText("Ruolo cambiato in " + ruolo + "!");
            alert.showAndWait();

            if (codice.equals("C")) {
                SceneManager.switchScene("/views/homeChair.fxml");
            } else {
                SceneManager.switchScene("/views/homeEditor.fxml");
            }
            codiceRuoloService.setUltimoCodicePerUtente(user.getEmail(), null);
            ultimoCodiceLabel.setText("Ultimo codice ricevuto: -");
            return;
        }
        else if (codice.equals("R")) {
            int nuovoRuoloId = ruoloRevisoreId;
            if (ruoliNellaConferenza.contains(nuovoRuoloId)) {
                feedbackLabel.setText("Hai già il ruolo " + ruolo + " in questa conferenza.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            userRuoloService.assegnaRuolo(userId, nuovoRuoloId, conferenzaId);
            // Apri la schermata di scelta argomenti
            codiceRuoloService.setUltimoCodicePerUtente(user.getEmail(), null);
            SceneManager.switchScene("/views/scegliArgomentiRevisore.fxml");
            return;
        }
        else if (codice.equals("S")) {
            int nuovoRuoloId = ruoloSottoRevisoreId;
            if (ruoliNellaConferenza.contains(nuovoRuoloId)) {
                feedbackLabel.setText("Hai già il ruolo " + ruolo + " in questa conferenza.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            userRuoloService.assegnaRuolo(userId, nuovoRuoloId, conferenzaId);
            feedbackLabel.setText("Ruolo " + ruolo + " aggiunto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        }

        codiceRuoloService.setUltimoCodicePerUtente(user.getEmail(), null);
        ultimoCodiceLabel.setText("Ultimo codice ricevuto: -");
    }
}