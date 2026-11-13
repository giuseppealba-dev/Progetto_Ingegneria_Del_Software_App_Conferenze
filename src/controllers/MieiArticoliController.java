package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import models.Articolo;
import services.ArticoloService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class MieiArticoliController {

    @FXML
    private VBox papersContainer;

    private final ArticoloService articoloService = new ArticoloService();

    @FXML
    public void initialize() {
        papersContainer.getChildren().clear();
        int userId = SessionManager.getLoggedUser().getId();
        int conferenzaId = SessionManager.getConferenzaSelezionata().getId();

        List<Articolo> articoli = articoloService.getArticoliByAutoreEConferenza(userId, conferenzaId);

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

                javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                Button visualizzaBtn = new Button("Visualizza");
                visualizzaBtn.setOnAction(e -> visualizzaArticolo(art));

                row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                papersContainer.getChildren().add(row);
            }
        }
    }


    private void visualizzaArticolo(Articolo articolo) {
        utils.SessionManager.setArticoloSelezionato(articolo);
        SceneManager.switchScene("/views/visualizzaMieiPapers.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeAutore.fxml");
    }
}