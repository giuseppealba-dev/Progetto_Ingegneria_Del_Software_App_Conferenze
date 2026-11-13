package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Articolo;
import services.PaperFinaleEditorService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class PapersDaControllareController {
    @FXML private VBox papersContainer;

    private final PaperFinaleEditorService paperFinaleEditorService = new PaperFinaleEditorService();

    @FXML
    public void initialize() {
        papersContainer.getChildren().clear();
        int editorId = SessionManager.getLoggedUser().getId();

        List<Articolo> papers = paperFinaleEditorService.getPapersFinaliByEditor(editorId);

        if (papers.isEmpty()) {
            Label noPapers = new Label("Nessun paper finale da controllare.");
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
                Button visualizzaBtn = new Button("Visualizza");
                visualizzaBtn.setOnAction(e -> {
                    SessionManager.setArticoloSelezionato(art);
                    SceneManager.switchScene("/views/visualizzaFinale.fxml");
                });
                row.getChildren().addAll(titolo, spacer, visualizzaBtn);
                papersContainer.getChildren().add(row);
            }
        }
    }

    @FXML
    void handleBack() {
        SceneManager.switchScene("/views/homeEditor.fxml");
    }
}
