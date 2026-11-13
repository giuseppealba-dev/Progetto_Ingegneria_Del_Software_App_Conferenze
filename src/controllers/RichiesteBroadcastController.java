package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import models.Articolo;
import services.OffertaService;
import utils.SceneManager;
import utils.SessionManager;

public class RichiesteBroadcastController {
    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;

    private final OffertaService offertaService = new OffertaService();
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
    void handleFaiOfferta(ActionEvent event) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler fare un'offerta per questo paper?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma invio offerta");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                int revisoreId = SessionManager.getLoggedUser().getId();
                boolean ok = offertaService.inviaOfferta(articolo.getId(), revisoreId);
                if (!ok) {
                    new Alert(Alert.AlertType.ERROR, "Hai già inviato un'offerta per questo paper.", ButtonType.OK).showAndWait();
                }
                SceneManager.switchScene("/views/richiesteRevisione.fxml");
            }
        });
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/richiesteRevisione.fxml");
    }
}