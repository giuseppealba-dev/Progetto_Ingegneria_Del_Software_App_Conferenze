package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import models.Articolo;
import models.Revisione;
import services.ArticoloService;
import services.RevisioneService;
import utils.SceneManager;
import utils.SessionManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VisualizzaMieiPapersController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;
    @FXML private VBox revisioniContainer;

    private Articolo articolo;
    private final ArticoloService articoloService = new ArticoloService();
    private final RevisioneService revisioneService = new RevisioneService();

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        if (articolo == null) {
            titoloLabel.setText("Nessun paper selezionato");
            testoPaperArea.setText("");
            return;
        }
        titoloLabel.setText(articolo.getTitolo());

        // Carica il testo del paper
        try {
            String testo = java.nio.file.Files.readString(java.nio.file.Path.of(articolo.getFile()));
            testoPaperArea.setText(testo);
        } catch (Exception e) {
            testoPaperArea.setText("Impossibile leggere il file del paper.");
        }

        // Carica revisioni
        revisioniContainer.getChildren().clear();
        List<Revisione> revisioni = revisioneService.getRevisioniByArticolo(articolo.getId());
        if (revisioni.isEmpty()) {
            Label noRev = new Label("Nessuna revisione presente.");
            noRev.setStyle("-fx-text-fill: gray;");
            revisioniContainer.getChildren().add(noRev);
        } else {
            for (Revisione rev : revisioni) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");
                Label titolo = new Label(articolo.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Button visualizzaBtn = new Button("Visualizza Revisione");
                visualizzaBtn.setOnAction(ev -> {
                    SessionManager.setArticoloSelezionato(articolo);
                    SessionManager.setRevisioneSelezionata(rev);
                    SceneManager.switchScene("/views/revisioneFinale.fxml");
                });
                row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                revisioniContainer.getChildren().add(row);
            }
        }
    }

    

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/mieiArticoli.fxml");
    }
}
