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

public class VisualizzaPaperController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;
    @FXML private VBox revisioniContainer;
    @FXML private VBox revisoriAssegnatiContainer;

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

        // Carica il testo del file del paper
        try {
            String testo = Files.readString(Path.of(articolo.getFile()));
            testoPaperArea.setText(testo);
        } catch (Exception e) {
            testoPaperArea.setText("Impossibile leggere il file del paper.");
        }

        // --- CARICA REVISORI ASSEGNATI ---
        revisoriAssegnatiContainer.getChildren().clear();
        List<Integer> revisoriIds = new services.AssegnazioneRevisoreService().getRevisoriAssegnati(articolo.getId());
        if (revisoriIds.isEmpty()) {
            Label noRev = new Label("Nessun revisore assegnato.");
            noRev.setStyle("-fx-text-fill: gray;");
            revisoriAssegnatiContainer.getChildren().add(noRev);
        } else {
            services.UserService userService = new services.UserService();
            for (Integer id : revisoriIds) {
                models.User revisore = userService.getUserById(id);
                String nome = revisore != null ? revisore.getNome() + " " + revisore.getCognome() + " <" + revisore.getEmail() + ">" : "ID: " + id;
                Label lbl = new Label(nome);
                revisoriAssegnatiContainer.getChildren().add(lbl);
            }
        }

        // --- CARICA REVISIONI ---
        revisioniContainer.getChildren().clear();
        List<Revisione> revisioni = revisioneService.getTutteRevisioniByArticolo(articolo.getId())
            .stream()
            .filter(rev -> !"accettato".equalsIgnoreCase(rev.getStato()) && !"rifiutato".equalsIgnoreCase(rev.getStato()))
            .toList();
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
                    visualizzaBtn.setOnAction(ev -> visualizzaRevisione(rev));
                    row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                    revisioniContainer.getChildren().add(row);
                }
            }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/elencoPapersChair.fxml");
    }

    @FXML
    void handleProponiRevisore(ActionEvent event) {
        SceneManager.switchScene("/views/proponiRevisore.fxml");
    }

    private void visualizzaRevisione(Revisione revisione) {
        SessionManager.setRevisioneSelezionata(revisione);
        SceneManager.switchScene("/views/visualizzaRevisioneChair.fxml");
    }
}