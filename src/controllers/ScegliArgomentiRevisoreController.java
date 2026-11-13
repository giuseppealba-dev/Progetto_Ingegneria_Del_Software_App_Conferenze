package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import models.Conferenza;
import models.ArgomentoConferenza;
import data.dao.ArgomentoConferenzaDAO;
import data.dao.ArgomentoRevisoreDAO;
import utils.SceneManager;
import utils.SessionManager;

import java.util.*;

public class ScegliArgomentiRevisoreController {

    @FXML
    private VBox topicsContainer;

    private List<CheckBox> checkBoxes = new ArrayList<>();
    private List<ArgomentoConferenza> argomenti = new ArrayList<>();

    @FXML
    public void initialize() {
        Conferenza conf = SessionManager.getConferenzaSelezionata();
        if (conf == null) return;

        ArgomentoConferenzaDAO argDao = new ArgomentoConferenzaDAO();
        argomenti = argDao.getByConferenzaId(conf.getId());

        for (ArgomentoConferenza arg : argomenti) {
            CheckBox cb = new CheckBox(arg.getNome());
            checkBoxes.add(cb);
            topicsContainer.getChildren().add(cb);
        }
    }

    @FXML
    void handleConferma(ActionEvent event) {
        List<ArgomentoConferenza> selezionati = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selezionati.add(argomenti.get(i));
            }
        }
        if (selezionati.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleziona almeno un argomento.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Argomenti selezionati memorizzati correttamente.", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ArgomentoRevisoreDAO arDao = new ArgomentoRevisoreDAO();
            int userId = SessionManager.getLoggedUser().getId();
            int conferenzaId = SessionManager.getConferenzaSelezionata().getId();
            for (ArgomentoConferenza arg : selezionati) {
                arDao.save(userId, conferenzaId, arg.getId());
            }
            // Torna alla home del ruolo revisore
            SceneManager.switchScene("/views/homeRevisore.fxml");
        }
        // Se CANCEL o chiusura popup, non salvare nulla
    }

}