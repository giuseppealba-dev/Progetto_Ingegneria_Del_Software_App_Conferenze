package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import models.Conferenza;
import models.User;
import services.UserService;
import utils.SceneManager;
import utils.SessionManager;

public class DatiConferenzaController {

    @FXML private Label titoloLabel;
    @FXML private Label descrizioneLabel;
    @FXML private Label fineConferenza;
    @FXML private Label scadenzaSubmissionLabel;
    @FXML private Label scadenzaReviewLabel;
    @FXML private Label scadenzaAcceptanceLabel;
    @FXML private Label scadenzaFinalSubmissionLabel;
    @FXML private Label minRevisoriLabel;
    @FXML private Label maxArticoliLabel;
    @FXML private Label acceptanceRateLabel;
    @FXML private Label luogoLabel;
    @FXML private Label oraLabel;
    @FXML private Label topicsLabel;
    @FXML private Label organizzatoreLabel;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        Conferenza conf = SessionManager.getConferenzaSelezionata();
        if (conf == null) {
            titoloLabel.setText("Nessuna conferenza selezionata");
            return;
        }

        titoloLabel.setText(conf.getTitolo());
        descrizioneLabel.setText("Descrizione: " + (conf.getDescrizione() == null ? "" : conf.getDescrizione()));
        fineConferenza.setText("Termine conferenza: " + conf.getFineConferenza());
        scadenzaSubmissionLabel.setText("Scadenza invio papers: " + conf.getScadenzaSubmission());
        scadenzaReviewLabel.setText("Scadenza revisione papers: " + conf.getScadenzaReview());
        scadenzaAcceptanceLabel.setText("Scadenza accettazione papers: " + conf.getScadenzaAcceptance());
        scadenzaFinalSubmissionLabel.setText("Scadenza sottomissione papers finali: " + conf.getScadenzaFinalSubmission());
        minRevisoriLabel.setText("Numero minimo revisori per paper: " + conf.getMinRevisori());
        maxArticoliLabel.setText("Numero massimo paper per revisore: " + conf.getMaxArticoli());
        acceptanceRateLabel.setText("Tasso di accettazione: " + conf.getAcceptanceRate() + "%");
        luogoLabel.setText("Luogo: " + conf.getLuogo());
        oraLabel.setText("Ora: " + conf.getOra());
        topicsLabel.setText("Argomenti: " + conf.getTopics());


        User organizzatore = userService.getUserById(conf.getOrganizzatoreId());
        organizzatoreLabel.setText("Organizzatore: " +
            (organizzatore != null
                ? organizzatore.getNome() + " " + organizzatore.getCognome() + " <" + organizzatore.getEmail() + ">"
                : "N/A"));
    }

    @FXML
    void handleBack(ActionEvent event) {
        String ruolo = utils.SessionManager.getRuoloCorrenteUtente();
        if (ruolo == null) {
            utils.SceneManager.switchScene("/views/autenticazione.fxml");
            return;
        }
        switch (ruolo) {
            case "Chair" -> utils.SceneManager.switchScene("/views/homeChair.fxml");
            case "Editor" -> utils.SceneManager.switchScene("/views/homeEditor.fxml");
            case "Revisore" -> utils.SceneManager.switchScene("/views/homeRevisore.fxml");
            case "Sotto-Revisore" -> utils.SceneManager.switchScene("/views/homeSottoRevisore.fxml");
            case "Autore" -> utils.SceneManager.switchScene("/views/homeAutore.fxml");
            default -> utils.SceneManager.switchScene("/views/home.fxml");
        }
    }
}