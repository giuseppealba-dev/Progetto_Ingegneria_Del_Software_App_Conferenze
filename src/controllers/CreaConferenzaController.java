package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import utils.SceneManager;
import utils.SessionManager;
import models.Conferenza;
import models.User;
import services.ConferenzaService;
import services.RuoloService;
import services.UserRuoloService;
import data.dao.ArgomentoConferenzaDAO;
import models.ArgomentoConferenza;
import javafx.event.ActionEvent;
import java.time.LocalDate;

public class CreaConferenzaController {

    @FXML private TextField nameConferenceField;
    @FXML private DatePicker conferenzaDeadline;
    @FXML private DatePicker paperSubmissionDeadline;
    @FXML private DatePicker reviewDeadline;
    @FXML private DatePicker acceptanceDeadline;
    @FXML private DatePicker finalSubmissionDeadline;
    @FXML private TextField minReviewersField;
    @FXML private TextField maxPapersField;
    @FXML private TextField acceptanceRateField;
    @FXML private TextField locationField;
    @FXML private DatePicker dataPicker;
    @FXML private TextField conferenceTimeField;
    @FXML private TextField topicsField;

    // Label per errori spezzati
    @FXML private Label feedbackLabel1;
    @FXML private Label feedbackLabel2;
    @FXML private Label feedbackLabel3;
    @FXML private Label feedbackLabel4;
    @FXML private Label feedbackLabel5;
    @FXML private Label feedbackLabel6;
    @FXML private Label feedbackLabel; // per altri errori

    private final ConferenzaService conferenzaService = new ConferenzaService();

    @FXML
    private void handleCreateConference() {
        nascondiFeedbackDate();
        feedbackLabel.setText(""); // pulisci eventuali altri errori

        // Controllo campi vuoti
        if (
            isEmpty(nameConferenceField) ||
            conferenzaDeadline.getValue() == null ||
            paperSubmissionDeadline.getValue() == null ||
            reviewDeadline.getValue() == null ||
            acceptanceDeadline.getValue() == null ||
            finalSubmissionDeadline.getValue() == null ||
            isEmpty(minReviewersField) ||
            isEmpty(maxPapersField) ||
            isEmpty(acceptanceRateField) ||
            isEmpty(locationField) ||
            dataPicker.getValue() == null ||
            isEmpty(conferenceTimeField) ||
            isEmpty(topicsField)
        ) {
            feedbackLabel.setText("Ci sono campi vuoti");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        // Controllo che nessuna data sia precedente a oggi
        LocalDate oggi = LocalDate.now();
        if (
            conferenzaDeadline.getValue().isBefore(oggi) ||
            paperSubmissionDeadline.getValue().isBefore(oggi) ||
            reviewDeadline.getValue().isBefore(oggi) ||
            acceptanceDeadline.getValue().isBefore(oggi) ||
            finalSubmissionDeadline.getValue().isBefore(oggi) ||
            dataPicker.getValue().isBefore(oggi)
        ) {
            feedbackLabel.setText("Non puoi inserire date precedenti a quella attuale.");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        // Controllo che i campi revisori e paper siano numeri interi
        int minRevisori, maxArticoli;
        try {
            minRevisori = Integer.parseInt(minReviewersField.getText().trim());
            maxArticoli = Integer.parseInt(maxPapersField.getText().trim());
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Inserire solo numeri interi nei campi revisori e paper per revisore.");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        // Controllo tasso di competenza
        int acceptanceRateInt;
        try {
            acceptanceRateInt = Integer.parseInt(acceptanceRateField.getText().trim());
            if (acceptanceRateInt < 0 || acceptanceRateInt > 100) {
                feedbackLabel.setText("Il tasso di competenza deve essere un intero tra 0 e 100.");
                feedbackLabel.setTextFill(Color.RED);
                return;
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Il tasso di competenza deve essere un intero tra 0 e 100.");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        // Controllo formato ora (HH:mm)
        String ora = conferenceTimeField.getText().trim();
        if (!ora.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            feedbackLabel.setText("L'ora deve essere nel formato HH:mm (es: 09:30).");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        try {
            String titolo = nameConferenceField.getText().trim();
            String descrizione = "";
            LocalDate fineConferenza = conferenzaDeadline.getValue();
            LocalDate scadenzaSubmission = paperSubmissionDeadline.getValue();
            LocalDate scadenzaReview = reviewDeadline.getValue();
            LocalDate scadenzaAcceptance = acceptanceDeadline.getValue();
            LocalDate scadenzaFinalSubmission = finalSubmissionDeadline.getValue();
            LocalDate dataConferenza = dataPicker.getValue();
            String luogo = locationField.getText().trim();
            String topics = topicsField.getText().trim();
            User organizzatore = SessionManager.getLoggedUser();
            if (organizzatore == null) {
                feedbackLabel.setText("Errore: utente non autenticato.");
                feedbackLabel.setTextFill(Color.RED);
                return;
            }

            // CONTROLLO ORDINE DATE
            if (!(scadenzaSubmission.isBefore(scadenzaReview) &&
                scadenzaReview.isBefore(scadenzaAcceptance) &&
                scadenzaAcceptance.isBefore(scadenzaFinalSubmission) &&
                scadenzaFinalSubmission.isBefore(fineConferenza) &&
                fineConferenza.isBefore(dataConferenza))) {
                mostraFeedbackDate();
                return;
            }

            Conferenza conf = new Conferenza(
                titolo,
                descrizione,
                fineConferenza,
                scadenzaSubmission,
                scadenzaReview,
                scadenzaAcceptance,
                scadenzaFinalSubmission,
                minRevisori,
                maxArticoli,
                acceptanceRateInt,
                luogo,
                ora,
                topics,
                dataConferenza,
                organizzatore.getId(),
                false
            );

            int conferenzaId = conferenzaService.creaConferenzaERestituisciId(conf);
            if (conferenzaId > 0) {
                int userId = organizzatore.getId();
                int ruoloChairId = new RuoloService().getRuoloByNome("Chair").getId();
                new UserRuoloService().assegnaRuolo(userId, ruoloChairId, conferenzaId);

                String[] argomenti = topics.split(",");
                ArgomentoConferenzaDAO argDao = new ArgomentoConferenzaDAO();
                for (String arg : argomenti) {
                    String nomeArg = arg.trim();
                    if (!nomeArg.isEmpty()) {
                        argDao.save(new ArgomentoConferenza(conferenzaId, nomeArg));
                    }
                }

                feedbackLabel.setTextFill(Color.GREEN);
                feedbackLabel.setText("Conferenza creata con successo!");
                nascondiFeedbackDate();
                clearFields();
            } else {
                feedbackLabel.setText("Errore nella creazione della conferenza.");
                feedbackLabel.setTextFill(Color.RED);
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Valori numerici non validi.");
            feedbackLabel.setTextFill(Color.RED);
        }
    }

    private boolean isEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private void clearFields() {
        nameConferenceField.clear();
        conferenzaDeadline.setValue(null);
        paperSubmissionDeadline.setValue(null);
        reviewDeadline.setValue(null);
        acceptanceDeadline.setValue(null);
        finalSubmissionDeadline.setValue(null);
        minReviewersField.clear();
        maxPapersField.clear();
        acceptanceRateField.clear();
        locationField.clear();
        dataPicker.setValue(null);
        conferenceTimeField.clear();
        topicsField.clear();
    }

    private void mostraFeedbackDate() {
        feedbackLabel1.setText("Le date devono essere in ordine:");
        feedbackLabel2.setText("1. Scadenza invio paper");
        feedbackLabel3.setText("2. Termine revisione");
        feedbackLabel4.setText("3. Termine accettazione papers");
        feedbackLabel5.setText("4. Termine sottomissione papers finali");
        feedbackLabel6.setText("5. Termine conferenza, 6. Data");
        feedbackLabel1.setVisible(true);
        feedbackLabel2.setVisible(true);
        feedbackLabel3.setVisible(true);
        feedbackLabel4.setVisible(true);
        feedbackLabel5.setVisible(true);
        feedbackLabel6.setVisible(true);
    }

    private void nascondiFeedbackDate() {
        feedbackLabel1.setVisible(false);
        feedbackLabel2.setVisible(false);
        feedbackLabel3.setVisible(false);
        feedbackLabel4.setVisible(false);
        feedbackLabel5.setVisible(false);
        feedbackLabel6.setVisible(false);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        SceneManager.switchScene("/views/home.fxml");
    }
}