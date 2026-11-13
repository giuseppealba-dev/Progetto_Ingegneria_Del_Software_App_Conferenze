package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import models.Articolo;
import services.ArticoloService;
import services.RevisioneService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class PaperDaRevisionareController {

    @FXML
    private VBox papersContainer;

    private final ArticoloService articoloService = new ArticoloService();
    private final RevisioneService revisioneService = new RevisioneService();

    @FXML
    public void initialize() {
        papersContainer.getChildren().clear();
        int revisoreId = SessionManager.getLoggedUser().getId();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();

        // Ottieni tutti i paper assegnati e ACCETTATI NON ancora revisionati
        List<Articolo> papers = articoloService.getArticoliDaRevisionare(revisoreId, conferenzaId);

        if (papers.isEmpty()) {
            Label noPapers = new Label("Nessun paper da revisionare.");
            noPapers.setStyle("-fx-text-fill: gray;");
            papersContainer.getChildren().add(noPapers);
        } else {
            for (Articolo art : papers) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");

                Label titolo = new Label(art.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button assegnaSottoBtn = new Button("Assegna sotto-revisore");
                assegnaSottoBtn.setOnAction(e -> assegnaSottoRevisore(art));

                Button revisionaBtn = new Button("Revisiona");
                revisionaBtn.setOnAction(e -> revisionaPaper(art));

                Button rinunciaBtn = new Button("Rinuncia");
                rinunciaBtn.setOnAction(e -> rinunciaPaper(art));

                row.getChildren().addAll(titolo, spacer, assegnaSottoBtn, revisionaBtn, rinunciaBtn);
                papersContainer.getChildren().add(row);
            }
        }
    }

    private void assegnaSottoRevisore(Articolo articolo) {
        SessionManager.setArticoloSelezionato(articolo);
        SceneManager.switchScene("/views/invitaSottoRevisore.fxml");
    }

    private void revisionaPaper(Articolo articolo) {
        SessionManager.setArticoloSelezionato(articolo);
        SceneManager.switchScene("/views/revisionaPaper.fxml");
    }

    private void rinunciaPaper(Articolo articolo) {
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rinunciare?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma rinuncia");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                int revisoreId = SessionManager.getLoggedUser().getId();
                boolean ok = new services.AssegnazioneRevisoreService().rinunciaRevisione(articolo.getId(), revisoreId);
                if (ok) {
                    initialize();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nella rinuncia.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeRevisore.fxml");
    }
}