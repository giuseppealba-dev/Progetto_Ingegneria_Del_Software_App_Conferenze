package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import models.Articolo;
import services.ArticoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class RichiesteRevisioneController {

    @FXML private VBox proposteContainer;
    @FXML private VBox broadcastContainer;

    private final ArticoloService articoloService = new ArticoloService();

    @FXML
    public void initialize() {
        // Carica proposte di revisione (assegnate dal chair) SOLO per la conferenza selezionata
        proposteContainer.getChildren().clear();
        int revisoreId = SessionManager.getLoggedUser().getId();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();
        List<Articolo> proposte = articoloService.getProposteRevisionePerRevisore(revisoreId, conferenzaId);
        if (proposte.isEmpty()) {
            Label noProp = new Label("Nessuna proposta di revisione.");
            noProp.setStyle("-fx-text-fill: gray;");
            proposteContainer.getChildren().add(noProp);
        } else {
            for (Articolo art : proposte) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");
                Label titolo = new Label(art.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Button visualizzaBtn = new Button("Visualizza");
                visualizzaBtn.setOnAction(ev -> {
                    SessionManager.setArticoloSelezionato(art);
                    SceneManager.switchScene("/views/accettazioneProposte.fxml");
                });
                row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                proposteContainer.getChildren().add(row);
            }
        }

        // Carica articoli in broadcast SOLO per la conferenza selezionata
        broadcastContainer.getChildren().clear();
        List<Articolo> broadcast = articoloService.getArticoliBroadcastPerRevisore(revisoreId, conferenzaId);
        if (broadcast.isEmpty()) {
            Label noBc = new Label("Nessun articolo in broadcast.");
            noBc.setStyle("-fx-text-fill: gray;");
            broadcastContainer.getChildren().add(noBc);
        } else {
            for (Articolo art : broadcast) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");
                Label titolo = new Label(art.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button visualizzaBtn = new Button("Visualizza");
                visualizzaBtn.setOnAction(ev -> {
                    SessionManager.setArticoloSelezionato(art);
                    SceneManager.switchScene("/views/richiesteBroadcast.fxml");
                });
                

                row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                broadcastContainer.getChildren().add(row);
            }
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeRevisore.fxml");
    }
}