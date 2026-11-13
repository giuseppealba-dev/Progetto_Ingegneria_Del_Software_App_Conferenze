package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import models.Conferenza;
import models.UserRuolo;
import services.RuoloService;
import services.ConferenzaService;
import services.UserRuoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.*;

public class ListaConferenzeController {

    @FXML
    private VBox tuoiContainer;

    @FXML
    private VBox tutteContainer;

    private final ConferenzaService conferenzaService = new ConferenzaService();
    private final UserRuoloService userRuoloService = new UserRuoloService();
    private final RuoloService ruoloService = new RuoloService();

    @FXML
    public void initialize() {
        tuoiContainer.getChildren().clear();
        tutteContainer.getChildren().clear();

        if (SessionManager.getLoggedUser() == null) {
            SceneManager.switchScene("/views/login.fxml");
            return;
        }

        int userId = SessionManager.getLoggedUser().getId();

        // Recupera tutti i ruoli dell'utente
        List<UserRuolo> userRuoli = userRuoloService.getRuoliUtente(userId);

        // Mostra una voce per ogni (conferenza, ruolo)
        Set<String> mostrati = new HashSet<>();
        for (UserRuolo ur : userRuoli) {
            Conferenza conf = conferenzaService.getConferenzaById(ur.getConferenzaId());
            if (conf != null) {
                String ruolo = ruoloService.getRuoloById(ur.getRuoloId()).getNomeRuolo();
                String key = conf.getId() + "-" + ur.getRuoloId();
                if (!mostrati.contains(key)) {
                    tuoiContainer.getChildren().add(createConferenceItem(conf, ruolo, true));
                    mostrati.add(key);
                }
            }
        }

        // Mostra tutte le altre conferenze a cui NON partecipa
        Set<Integer> mieConferenzeId = new HashSet<>();
        for (UserRuolo ur : userRuoli) {
            mieConferenzeId.add(ur.getConferenzaId());
        }
        for (Conferenza conf : conferenzaService.getTutteLeConferenze()) {
            if (!mieConferenzeId.contains(conf.getId())) {
                tutteContainer.getChildren().add(createConferenceItem(conf, "", false));
            }
        }
    }

    private HBox createConferenceItem(Conferenza conferenza, String ruolo, boolean haiRuolo) {
    HBox box = new HBox(10);
    box.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");
    Label nomeLabel = new Label(conferenza.getTitolo());
    nomeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
    Label ruoloLabel = new Label(haiRuolo ? ruolo : "");
    ruoloLabel.setStyle("-fx-font-size: 13px; -fx-padding: 0 0 0 10;");
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    Button entraButton = new Button(haiRuolo ? "Entra" : "Entra");
    entraButton.setOnAction(e -> {
        SessionManager.setConferenzaSelezionata(conferenza);
        if (haiRuolo) {
            SessionManager.setRuoloCorrenteUtente(ruolo); // <-- AGGIUNGI QUESTA RIGA
            // Navigazione in base al ruolo
            if ("Autore".equalsIgnoreCase(ruolo)) {
                SceneManager.switchScene("/views/homeAutore.fxml");
            } else if ("Revisore".equalsIgnoreCase(ruolo)) {
                SceneManager.switchScene("/views/homeRevisore.fxml");
            } else if ("Sotto-Revisore".equalsIgnoreCase(ruolo)) {
                SceneManager.switchScene("/views/homeSottoRevisore.fxml");
            } else if ("Editor".equalsIgnoreCase(ruolo)) {
                SceneManager.switchScene("/views/homeEditor.fxml");
            } else if ("Chair".equalsIgnoreCase(ruolo)) {
                SceneManager.switchScene("/views/homeChair.fxml");
            } else {
                SceneManager.switchScene("/views/home.fxml");
            }
        } else {
            // Iscrizione come autore
            int userId = SessionManager.getLoggedUser().getId();
            int ruoloAutoreId = ruoloService.getRuoloByNome("Autore").getId();
            userRuoloService.assegnaRuolo(userId, ruoloAutoreId, conferenza.getId());
            SceneManager.switchScene("/views/homeAutore.fxml");
        }
    });

    box.getChildren().addAll(nomeLabel, ruoloLabel, spacer, entraButton);
    return box;
}

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/home.fxml");
    }
}