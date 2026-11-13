package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import models.Articolo;
import services.ArticoloService;
import services.PaperFinaleEditorService;
import utils.SceneManager;
import utils.SessionManager;

import java.nio.file.Files;
import java.nio.file.Path;

public class VisualizzaFinaleEditorController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;

    private Articolo articolo;
    private final ArticoloService articoloService = new ArticoloService();
    private final PaperFinaleEditorService paperFinaleEditorService = new PaperFinaleEditorService();

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        if (articolo == null) {
            titoloLabel.setText("Nessun paper selezionato");
            testoPaperArea.setText("");
            return;
        }
        titoloLabel.setText(articolo.getTitolo());
        // Mostra direttamente il testo dell'articolo
        testoPaperArea.setText(articolo.getFile() != null ? articolo.getFile() : "Nessun testo disponibile.");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/papersDaControllare.fxml");
    }

    @FXML
    void handleRifiuta(ActionEvent event) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler rifiutare questo paper?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma rifiuto");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Rimuovi il paper dalla lista dei paper finali per l'editor
                int editorId = SessionManager.getLoggedUser().getId();
                paperFinaleEditorService.rimuoviPaperFinale(articolo.getId(), editorId);
                SceneManager.switchScene("/views/papersDaControllare.fxml");
            }
        });
    }

    @FXML
    void handleCreaVersioneStampabile(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Versione stampabile creata", ButtonType.OK);
        alert.setTitle("Versione stampabile");
        alert.setHeaderText(null);
        alert.showAndWait();
        // Qui puoi aggiungere la logica per generare effettivamente il PDF o altro formato
    }
}