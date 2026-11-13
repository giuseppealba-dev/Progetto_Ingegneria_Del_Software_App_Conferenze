package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import models.Articolo;
import services.ArticoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.nio.file.Files;
import java.nio.file.Path;

public class RevisionaSottoRevisoreController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;
    @FXML private TextArea commentoArea;
    @FXML private Spinner<Integer> rateSpinner;
    @FXML private Label feedbackLabel;

    private Articolo articolo;

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        if (articolo == null) {
            titoloLabel.setText("Nessun paper selezionato");
            testoPaperArea.setText("");
            return;
        }
        titoloLabel.setText(articolo.getTitolo());

        // Carica il testo del file del paper
        try {
            String testo = Files.readString(Path.of(articolo.getFile()));
            testoPaperArea.setText(testo);
        } catch (Exception e) {
            testoPaperArea.setText("Impossibile leggere il file del paper.");
        }

        // Spinner per rate da -3 a +3
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(-3, 3, 0);
        rateSpinner.setValueFactory(valueFactory);
        rateSpinner.setEditable(true);

        feedbackLabel.setText("");
    }

    @FXML
    void handleInviaRevisione(ActionEvent event) {
        String commento = commentoArea.getText().trim();
        int rate = rateSpinner.getValue();
        int articoloId = articolo.getId();
        int sottoRevisoreId = SessionManager.getLoggedUser().getId();

        if (commento.isEmpty()) {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Il commento è obbligatorio.");
            return;
        }

        // Mostra popup di conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma invio");
        alert.setHeaderText("Sei sicuro di voler inviare la sotto-revisione?");
        alert.setContentText("Una volta inviata, non potrai più modificarla.");

        ButtonType buttonSi = new ButtonType("Sì", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonSi, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonSi) {
                inviaRevisione(articoloId, sottoRevisoreId, commento, rate);
            }
            // Se clicca "No" non succede nulla
        });
    }

    private void inviaRevisione(int articoloId, int sottoRevisoreId, String commento, int rate) {
        boolean ok = new services.RevisioneSottoRevisoreService().salvaRevisione(articoloId, sottoRevisoreId, commento, rate);

        if (ok) {
            new services.SottoRevisioneService().rinunciaSottoRevisione(articoloId, sottoRevisoreId);
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Sotto-revisione inviata al revisore!");
            SceneManager.switchScene("/views/paperDaSottoRevisionare.fxml");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Errore nell'invio della sotto-revisione.");
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/paperDaSottoRevisionare.fxml");
    }
}