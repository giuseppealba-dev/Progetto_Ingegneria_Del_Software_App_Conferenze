package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;
import models.Articolo;
import models.Conferenza;
import models.User;
import models.Offerta;
import services.ArticoloService;
import services.OffertaService;
import services.UserService;
import services.AssegnazioneRevisoreService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElencoPapersChairController {

    @FXML
    private VBox papersContainer;
    @FXML 
    private Button broadcastButton;
    private boolean broadcastAttivo;

    private final ArticoloService articoloService = new ArticoloService();
    private final OffertaService offertaService = new OffertaService();

    @FXML
    public void initialize() {
        papersContainer.getChildren().clear();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();

        List<Articolo> articoli = articoloService.getArticoliByConferenza(conferenzaId);

        if (articoli.isEmpty()) {
            Label noPapers = new Label("Nessun paper sottomesso.");
            noPapers.setStyle("-fx-text-fill: gray;");
            papersContainer.getChildren().add(noPapers);
        } else {
            for (Articolo art : articoli) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");

                Label titolo = new Label(art.getTitolo());
                titolo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

                String stato = calcolaStatoArticolo(art.getId());
                Label statoLabel = new Label("Stato: " + stato);
                statoLabel.setStyle("-fx-font-size: 13px; -fx-padding: 0 0 0 10;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button visualizzaBtn = new Button("Visualizza");
                visualizzaBtn.setOnAction(e -> {
                    SessionManager.setArticoloSelezionato(art);
                    SceneManager.switchScene("/views/visualizzaPaper.fxml");
                });

                row.getChildren().addAll(titolo, statoLabel, spacer, visualizzaBtn);
                papersContainer.getChildren().add(row);
            }
        }

        // --- SEZIONE OFFERTE ---
        Label offerteLabel = new Label("Offerte ricevute dai revisori:");
        offerteLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20 0 0 0;");
        papersContainer.getChildren().add(offerteLabel);

        List<Offerta> offerte = offertaService.getOfferteInviateAllaConferenza(conferenzaId);
        UserService userService = new UserService();
        for (Offerta offerta : offerte) {
            Articolo art = articoloService.getArticoloById(offerta.getIdArticolo());
            User revisore = userService.getUserById(offerta.getIdRevisore());
            HBox row = new HBox(10);
            row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");

            Label titolo = new Label(art.getTitolo() + " - Offerta da: " + revisore.getNome() + " " + revisore.getCognome());
            titolo.setStyle("-fx-font-size: 14px;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button accettaBtn = new Button("Accetta");
            Button rifiutaBtn = new Button("Rifiuta");

            accettaBtn.setOnAction(e -> {
                offertaService.aggiornaStatoOfferta(offerta.getId(), "accettata");
                new AssegnazioneRevisoreService().assegnaRevisore(art.getId(), revisore.getId());
                initialize();
            });

            rifiutaBtn.setOnAction(e -> {
                offertaService.aggiornaStatoOfferta(offerta.getId(), "rifiutata");
                initialize();
            });

            row.getChildren().addAll(titolo, spacer, accettaBtn, rifiutaBtn);
            papersContainer.getChildren().add(row);
        }

        Conferenza conferenza = SessionManager.getConferenzaSelezionata();
        broadcastAttivo = conferenza.isBroadcastAttivo();
        aggiornaTestoBroadcastButton();
    }

    private String calcolaStatoArticolo(int articoloId) {
        int numRevisori = articoloService.getNumeroRevisoriAssegnati(articoloId);
        int numRevisioniCompletate = articoloService.getNumeroRevisioniCompletate(articoloId);

        if (numRevisori == 0) {
            return "Da revisionare";
        } else if (numRevisioniCompletate == 0) {
            return "In revisione";
        } else {
            return "Revisionato";
        }
    }

    @FXML
    void handleDistribuzioneAutomatica(ActionEvent event) {
        int conferenzaId = utils.SessionManager.getConferenzaSelezionata().getId();
        Conferenza conferenza = new services.ConferenzaService().getConferenzaById(conferenzaId);
        int minRevisori = conferenza.getMinRevisori();
        int maxArticoli = conferenza.getMaxArticoli();
        double acceptanceRate = conferenza.getAcceptanceRate();

        List<Articolo> articoli = articoloService.getArticoliByConferenza(conferenzaId);
        List<User> revisori = new services.UserService().getRevisoriByConferenza(conferenzaId);

        Map<Integer, Integer> revisorePaperCount = new HashMap<>();
        for (User revisore : revisori) {
            revisorePaperCount.put(revisore.getId(), new services.AssegnazioneRevisoreService().getNumeroArticoliAssegnati(revisore.getId(), conferenzaId));
        }

        for (Articolo articolo : articoli) {
            List<Integer> argomentiPaper = new services.ArticoloArgomentoService().getArgomentiByArticolo(articolo.getId());
            Set<Integer> revisoriAssegnati = new HashSet<>(new services.AssegnazioneRevisoreService().getRevisoriAssegnati(articolo.getId()));

            int assegnati = revisoriAssegnati.size();
            if (assegnati >= minRevisori) continue;

            for (User revisore : revisori) {
                if (assegnati >= minRevisori) break;
                if (revisoriAssegnati.contains(revisore.getId())) continue;
                if (revisorePaperCount.get(revisore.getId()) >= maxArticoli) continue;

                List<Integer> argomentiRevisore = new services.ArgomentoRevisoreService().getArgomentiByUserAndConferenza(revisore.getId(), conferenzaId);

                long coperti = argomentiPaper.stream().filter(argomentiRevisore::contains).count();
                double percentuale = argomentiPaper.isEmpty() ? 0 : (coperti * 100.0 / argomentiPaper.size());

                if (percentuale >= acceptanceRate) {
                    boolean ok = new services.AssegnazioneRevisoreService().assegnaRevisore(articolo.getId(), revisore.getId());
                    if (ok) {
                        revisorePaperCount.put(revisore.getId(), revisorePaperCount.get(revisore.getId()) + 1);
                        assegnati++;
                    }
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Distribuzione automatica completata.", ButtonType.OK);
        alert.showAndWait();
        initialize();
    }

    private void aggiornaTestoBroadcastButton() {
        if (broadcastAttivo) {
            broadcastButton.setText("Manda papers in broadcast: Disattiva");
        } else {
            broadcastButton.setText("Manda papers in broadcast: Attiva");
        }
    }

    @FXML
    void handleBroadcast(ActionEvent event) {
        broadcastAttivo = !broadcastAttivo;
        Conferenza conferenza = SessionManager.getConferenzaSelezionata();
        conferenza.setBroadcastAttivo(broadcastAttivo);
        new services.ConferenzaService().aggiornaBroadcast(conferenza.getId(), broadcastAttivo);
        aggiornaTestoBroadcastButton();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, broadcastAttivo ? "Broadcast attivato!" : "Broadcast disattivato!", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeChair.fxml");
    }
}