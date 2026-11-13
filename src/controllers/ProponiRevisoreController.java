package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import models.User;
import models.Articolo;
import services.UserService;
import services.AssegnazioneRevisoreService;
import utils.SceneManager;
import utils.SessionManager;
import models.Conferenza;

import java.util.List;

public class ProponiRevisoreController {

    @FXML private VBox revisoriContainer;

    private final UserService userService = new UserService();
    private final AssegnazioneRevisoreService assegnazioneRevisoreService = new AssegnazioneRevisoreService();

    @FXML
    public void initialize() {
        revisoriContainer.getChildren().clear();

        Articolo articolo = SessionManager.getArticoloSelezionato();
        Conferenza conferenza = SessionManager.getConferenzaSelezionata();
        if (articolo == null || conferenza == null) return;

        int autoreId = articolo.getIdAutore();
        int maxArticoli = conferenza.getMaxArticoli();

        // Ottieni tutti i revisori della conferenza
        List<User> revisori = userService.getRevisoriByConferenza(conferenza.getId());

        // Ottieni revisori già proposti o assegnati a questo articolo
        List<Integer> revisoriPropostiOAssegnati = assegnazioneRevisoreService.getRevisoriPropostiOAssegnati(articolo.getId());

        boolean almenoUno = false;
        for (User revisore : revisori) {
            // Escludi l'autore (anche se è revisore)
            if (revisore.getId() == autoreId) continue;

            // Escludi chi ha già raggiunto il massimo di paper
            int numArticoli = assegnazioneRevisoreService.getNumeroArticoliAssegnati(revisore.getId(), conferenza.getId());
            if (numArticoli >= maxArticoli) continue;

            // Escludi revisori già proposti o assegnati a questo articolo
            if (revisoriPropostiOAssegnati.contains(revisore.getId())) continue;

            almenoUno = true;
            HBox row = new HBox(10);
            row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");
            Label nome = new Label(revisore.getNome() + " " + revisore.getCognome() + " (" + revisore.getEmail() + ")");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Button proponiBtn = new Button("Proponi");
            proponiBtn.setOnAction(e -> {
                boolean ok = assegnazioneRevisoreService.proponiRevisore(articolo.getId(), revisore.getId());
                Alert alert;
                if (ok) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Proposta inviata al revisore!", ButtonType.OK);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Errore nell'invio della proposta.", ButtonType.OK);
                }
                alert.showAndWait();
                SceneManager.switchScene("/views/visualizzaPaper.fxml");
            });
            row.getChildren().addAll(nome, spacer, proponiBtn);
            revisoriContainer.getChildren().add(row);
        }

        if (!almenoUno) {
            Label noRevisori = new Label("Nessun revisore disponibile.");
            noRevisori.setStyle("-fx-text-fill: gray;");
            revisoriContainer.getChildren().add(noRevisori);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/visualizzaPaper.fxml");
    }
}