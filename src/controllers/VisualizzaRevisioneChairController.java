package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import models.Revisione;
import services.RevisioneService;
import services.ArticoloService;
import utils.SceneManager;
import utils.SessionManager;

public class VisualizzaRevisioneChairController {

    @FXML private Label titoloLabel;
    @FXML private TextArea commentoAutoreArea;
    @FXML private TextArea commentoChairArea;
    @FXML private Label rateLabel;
    @FXML private Label dataLabel;

    private Revisione revisione;
    private final RevisioneService revisioneService = new RevisioneService();
    private final ArticoloService articoloService = new ArticoloService();

    @FXML
    public void initialize() {
        revisione = SessionManager.getRevisioneSelezionata();
        if (revisione == null) return;

        titoloLabel.setText("Revisione per: " + revisione.getIdArticolo());
        commentoAutoreArea.setText(revisione.getCommento());
        commentoChairArea.setText(revisione.getCommentoChair());
        rateLabel.setText(String.valueOf(revisione.getRate()));
        dataLabel.setText(revisione.getData() != null ? revisione.getData().toString() : "");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/visualizzaPaper.fxml");
    }

    @FXML
    void handleAccetta(ActionEvent event) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler accettare questa revisione?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma accettazione revisione");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                revisioneService.aggiornaVisibilitaEStato(revisione.getId(), 1, "accettato");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Revisione accettata e inviata all'autore.", ButtonType.OK);
                alert.showAndWait();
                SceneManager.switchScene("/views/visualizzaPaper.fxml");
            }
        });
    }

    @FXML
    void handleRifiuta(ActionEvent event) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler rifiutare questa revisione?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma rifiuto revisione");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // SOLO aggiorna stato e visibilità, NON eliminare la revisione!
                revisioneService.aggiornaVisibilitaEStato(revisione.getId(), 1, "rifiutato");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Revisione rifiutata e notificata all'autore.", ButtonType.OK);
                alert.showAndWait();
                SceneManager.switchScene("/views/visualizzaPaper.fxml");
            }
        });
    }
}