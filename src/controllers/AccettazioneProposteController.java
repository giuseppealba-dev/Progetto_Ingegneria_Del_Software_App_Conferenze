package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import models.Articolo;
import services.AssegnazioneRevisoreService;
import utils.SceneManager;
import utils.SessionManager;

public class AccettazioneProposteController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;

    private final AssegnazioneRevisoreService assegnazioneRevisoreService = new AssegnazioneRevisoreService();
    private Articolo articolo;

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        if (articolo == null) return;
        titoloLabel.setText(articolo.getTitolo());
        try {
            String testo = java.nio.file.Files.readString(java.nio.file.Path.of(articolo.getFile()));
            testoPaperArea.setText(testo);
        } catch (Exception e) {
            testoPaperArea.setText("Impossibile leggere il file del paper.");
        }
    }

    @FXML
    void handleAccetta(ActionEvent event) {
        int revisoreId = SessionManager.getLoggedUser().getId();
        boolean ok = assegnazioneRevisoreService.accettaProposta(articolo.getId(), revisoreId);
        if (ok) {
            new Alert(Alert.AlertType.INFORMATION, "Proposta accettata! Ora puoi revisionare il paper.", ButtonType.OK).showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Errore nell'accettazione della proposta.", ButtonType.OK).showAndWait();
        }
        SceneManager.switchScene("/views/richiesteRevisione.fxml");
    }

    @FXML
    void handleRifiuta(ActionEvent event) {
        int revisoreId = SessionManager.getLoggedUser().getId();
        boolean ok = assegnazioneRevisoreService.rifiutaProposta(articolo.getId(), revisoreId);
        if (ok) {
            new Alert(Alert.AlertType.INFORMATION, "Proposta rifiutata.", ButtonType.OK).showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Errore nel rifiuto della proposta.", ButtonType.OK).showAndWait();
        }
        SceneManager.switchScene("/views/richiesteRevisione.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/richiesteRevisione.fxml");
    }
}