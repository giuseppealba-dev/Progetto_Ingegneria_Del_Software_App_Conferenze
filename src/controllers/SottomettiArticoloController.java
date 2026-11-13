package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import models.Conferenza;
import models.ArgomentoConferenza;
import data.dao.ArgomentoConferenzaDAO;
import services.ArticoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.*;

public class SottomettiArticoloController {

    @FXML private TextField titoloField;
    @FXML private TextArea testoArticoloArea;
    @FXML private VBox topicsContainer;
    @FXML private Label feedbackLabel;

    private List<CheckBox> checkBoxes = new ArrayList<>();
    private List<ArgomentoConferenza> argomenti = new ArrayList<>();

    @FXML
    public void initialize() {
        Conferenza conf = SessionManager.getConferenzaSelezionata();
        if (conf == null) return;

        ArgomentoConferenzaDAO argDao = new ArgomentoConferenzaDAO();
        argomenti = argDao.getByConferenzaId(conf.getId());

        for (ArgomentoConferenza arg : argomenti) {
            CheckBox cb = new CheckBox(arg.getNome());
            checkBoxes.add(cb);
            topicsContainer.getChildren().add(cb);
        }
    }

    @FXML
    void handleInvia(ActionEvent event) {
        Conferenza conferenza = SessionManager.getConferenzaSelezionata();
        if (conferenza == null) {
            feedbackLabel.setText("Errore: nessuna conferenza selezionata.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Blocco la sottomissione se la data attuale è dopo la scadenza invio paper
        if (java.time.LocalDate.now().isAfter(conferenza.getScadenzaSubmission())) {
            feedbackLabel.setText("La scadenza per l'invio dei paper è terminata.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String titolo = titoloField.getText().trim();
        String testo = testoArticoloArea.getText().trim();
        if (titolo.isEmpty() || testo.isEmpty()) {
            feedbackLabel.setText("Compila tutti i campi.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        List<Integer> argomentiSelezionati = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                argomentiSelezionati.add(argomenti.get(i).getId());
            }
        }
        if (argomentiSelezionati.isEmpty()) {
            feedbackLabel.setText("Seleziona almeno un argomento.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean ok = new ArticoloService().sottomettiArticoloTesto(
            titolo,
            testo,
            SessionManager.getLoggedUser().getId(),
            conferenza.getId(),
            argomentiSelezionati
        );

        if (ok) {
            feedbackLabel.setText("Articolo sottomesso con successo!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            titoloField.clear();
            testoArticoloArea.clear();
            for (CheckBox cb : checkBoxes) cb.setSelected(false);
        } else {
            feedbackLabel.setText("Errore nella sottomissione.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeAutore.fxml");
    }
}