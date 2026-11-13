package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Articolo;
import services.ArticoloService;
import services.SottoRevisioneService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class PaperDaSottoRevisionareController {
    @FXML private VBox papersContainer;

    private final ArticoloService articoloService = new ArticoloService();
    private final SottoRevisioneService sottoRevisioneService = new SottoRevisioneService();

    @FXML
    public void initialize() {
        papersContainer.getChildren().clear();
        int sottoRevisoreId = SessionManager.getLoggedUser().getId();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();

        List<Integer> idArticoli = sottoRevisioneService.getArticoliAssegnatiASottoRevisore(sottoRevisoreId, conferenzaId);
        if (idArticoli.isEmpty()) {
            Label noPapers = new Label("Nessun paper da sottorevisionare.");
            noPapers.setStyle("-fx-text-fill: gray;");
            papersContainer.getChildren().add(noPapers);
        } else {
            for (Integer id : idArticoli) {
                Articolo art = articoloService.getArticoloById(id);
                if (art == null) continue;
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");
                Label titolo = new Label(art.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button revisionaBtn = new Button("Revisiona");
                revisionaBtn.setOnAction(e -> {
                    SessionManager.setArticoloSelezionato(art);
                    // Sostituisci con la tua view di revisione sottorevisore
                    SceneManager.switchScene("/views/revisionaSottoRevisore.fxml");
                });

                Button rinunciaBtn = new Button("Rinuncia");
                rinunciaBtn.setOnAction(e -> {
                    Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler rinunciare a questo paper?", ButtonType.YES, ButtonType.NO);
                    conferma.setTitle("Conferma rinuncia");
                    conferma.setHeaderText(null);
                    conferma.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            sottoRevisioneService.rinunciaSottoRevisione(art.getId(), sottoRevisoreId);
                            initialize(); // aggiorna la lista
                        }
                    });
                });

                row.getChildren().addAll(titolo, spacer, revisionaBtn, rinunciaBtn);
                papersContainer.getChildren().add(row);
            }
        }
    }

    @FXML
    void handleBack() {
        SceneManager.switchScene("/views/homeSottoRevisore.fxml");
    }
}