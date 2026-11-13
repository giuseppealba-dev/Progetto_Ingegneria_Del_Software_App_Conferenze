package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Conferenza;
import models.UserRuolo;
import models.User;
import models.Ruolo;
import services.UserRuoloService;
import services.RuoloService;
import services.UserService;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class ListaPartecipantiController {

    @FXML
    private VBox partecipantiContainer;

    private final UserRuoloService userRuoloService = new UserRuoloService();
    private final RuoloService ruoloService = new RuoloService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        partecipantiContainer.getChildren().clear();

        Conferenza conferenza = SessionManager.getConferenzaSelezionata();
        if (conferenza == null) {
            partecipantiContainer.getChildren().add(new Label("Nessuna conferenza selezionata."));
            return;
        }

        List<UserRuolo> partecipanti = userRuoloService.getRuoliConferenza(conferenza.getId());
        if (partecipanti == null || partecipanti.isEmpty()) {
            partecipantiContainer.getChildren().add(new Label("Nessun partecipante per questa conferenza."));
            return;
        }

        Map<Integer, User> utenti = new HashMap<>();
        Map<Integer, ArrayList<String>> ruoliUtente = new HashMap<>();

        for (UserRuolo ur : partecipanti) {
            User user = userService.getUserById(ur.getUserId());
            Ruolo ruolo = ruoloService.getRuoloById(ur.getRuoloId());
            if (user != null && ruolo != null) {
                utenti.put(user.getId(), user);
                ruoliUtente.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(ruolo.getNomeRuolo());
            }
        }

        for (Integer userId : utenti.keySet()) {
            User user = utenti.get(userId);
            String ruoli = String.join(", ", ruoliUtente.get(userId));
            HBox row = new HBox(20);
            row.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");
            Label nome = new Label(user.getNome() + " " + user.getCognome());
            Label email = new Label("<" + user.getEmail() + ">");
            email.setStyle("-fx-text-fill: #555; -fx-font-size: 12px; -fx-padding: 0 0 0 10;");
            Label ruoloLabel = new Label(ruoli);
            ruoloLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 0 10;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            row.getChildren().addAll(nome, email, spacer, ruoloLabel);
            partecipantiContainer.getChildren().add(row);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/homeChair.fxml");
    }
}