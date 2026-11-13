package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import models.Conferenza;
import services.ConferenzaService;
import utils.SceneManager;
import utils.SessionManager;

public class ModificaDatiConferenzaController {

    @FXML private DatePicker dataPicker;
    @FXML private DatePicker fineConferenzaPicker;
    @FXML private DatePicker scadenzaSubmissionPicker;
    @FXML private DatePicker scadenzaReviewPicker;
    @FXML private DatePicker scadenzaAcceptancePicker;
    @FXML private DatePicker scadenzaFinalSubmissionPicker;
    @FXML private Label feedbackLabel;
    @FXML private Button confermaButton;

    private final ConferenzaService conferenzaService = new ConferenzaService();
    private Conferenza conferenza;

    @FXML
    public void initialize() {
        conferenza = SessionManager.getConferenzaSelezionata();
        if (conferenza == null) {
            feedbackLabel.setText("Nessuna conferenza selezionata.");
            confermaButton.setDisable(true);
            return;
        }
        fineConferenzaPicker.setValue(conferenza.getFineConferenza());
        scadenzaSubmissionPicker.setValue(conferenza.getScadenzaSubmission());
        scadenzaReviewPicker.setValue(conferenza.getScadenzaReview());
        scadenzaAcceptancePicker.setValue(conferenza.getScadenzaAcceptance());
        scadenzaFinalSubmissionPicker.setValue(conferenza.getScadenzaFinalSubmission());
        dataPicker.setValue(conferenza.getData());
    }

    @FXML
    void handleConferma(ActionEvent event) {
        try {
            LocalDate scadenzaSubmission = scadenzaSubmissionPicker.getValue();
            LocalDate scadenzaReview = scadenzaReviewPicker.getValue();
            LocalDate scadenzaAcceptance = scadenzaAcceptancePicker.getValue();
            LocalDate scadenzaFinalSubmission = scadenzaFinalSubmissionPicker.getValue();
            LocalDate fineConferenza = fineConferenzaPicker.getValue();
            LocalDate dataConferenza = dataPicker.getValue();

            // Controllo che nessuna data sia precedente a oggi
            LocalDate oggi = LocalDate.now();
            if (
                scadenzaSubmission.isBefore(oggi) ||
                scadenzaReview.isBefore(oggi) ||
                scadenzaAcceptance.isBefore(oggi) ||
                scadenzaFinalSubmission.isBefore(oggi) ||
                fineConferenza.isBefore(oggi) ||
                dataConferenza.isBefore(oggi)
            ) {
                feedbackLabel.setText("Non puoi inserire date precedenti a quella attuale.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Controllo ordine date
            if (!(scadenzaSubmission.isBefore(scadenzaReview) &&
                scadenzaReview.isBefore(scadenzaAcceptance) &&
                scadenzaAcceptance.isBefore(scadenzaFinalSubmission) &&
                scadenzaFinalSubmission.isBefore(fineConferenza) &&
                fineConferenza.isBefore(dataConferenza))) {
                feedbackLabel.setText("Le date devono essere in ordine cronologico.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            conferenza.setScadenzaSubmission(scadenzaSubmission);
            conferenza.setScadenzaReview(scadenzaReview);
            conferenza.setScadenzaAcceptance(scadenzaAcceptance);
            conferenza.setScadenzaFinalSubmission(scadenzaFinalSubmission);
            conferenza.setFineConferenza(fineConferenza);
            conferenza.setData(dataConferenza);


            boolean ok = conferenzaService.aggiornaConferenza(conferenza);
            if (ok) {
                feedbackLabel.setText("Dati conferenza aggiornati con successo!");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            } else {
                feedbackLabel.setText("Errore nell'aggiornamento dei dati.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            feedbackLabel.setText("Errore nei dati inseriti.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/datiConferenza.fxml");
    }
}