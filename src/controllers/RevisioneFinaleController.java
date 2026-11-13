package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import models.Articolo;
import models.Revisione;
import services.ArticoloService;
import services.RevisioneService;
import data.dao.PaperFinaleEditorDAO;
import data.dao.UserRuoloDAO;
import utils.SceneManager;
import utils.SessionManager;

import java.util.List;

public class RevisioneFinaleController {

    @FXML private Label statoLabel;
    @FXML private Button inviaEditorBtn;
    @FXML private TextArea testoPaperArea;
    @FXML private TextArea commentoRevisoreArea;
    @FXML private Label rateLabel;

    private Articolo articolo;
    private Revisione revisione;
    private final ArticoloService articoloService = new ArticoloService();
    private final RevisioneService revisioneService = new RevisioneService();

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        revisione = SessionManager.getRevisioneSelezionata();
        if (articolo == null || revisione == null) return;

        // Stato: accettato, rifiutato, inviato
        String stato = revisione.getStato();
        statoLabel.setText(stato != null ? stato.toUpperCase() : "N/A");

        // Disabilita il pulsante se rifiutato o già inviato
        inviaEditorBtn.setDisable("rifiutato".equalsIgnoreCase(stato) || "inviato".equalsIgnoreCase(stato));

        // Carica testo paper (modificabile)
        testoPaperArea.setText(articolo.getFile());

        // Carica commento revisore e rate
        commentoRevisoreArea.setText(revisione.getCommento());
        rateLabel.setText(String.valueOf(revisione.getRate()));
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/visualizzaMieiPapers.fxml");
    }

    @FXML
    void handleInviaEditor(ActionEvent event) {
        // Aggiorna testo articolo se modificato
        String nuovoTesto = testoPaperArea.getText();
        articoloService.aggiornaTestoArticolo(articolo.getId(), nuovoTesto);

        // Recupera lista editor della conferenza
        UserRuoloDAO userRuoloDAO = new UserRuoloDAO();
        List<Integer> editorIds = userRuoloDAO.getEditorIdsByConferenza(articolo.getIdConferenza());

        if (editorIds.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nessun editor trovato per questa conferenza!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Inserisci l'articolo per ogni editor nella tabella paper_finali_editor
        PaperFinaleEditorDAO paperFinaleEditorDAO = new PaperFinaleEditorDAO();
        for (int editorId : editorIds) {
            paperFinaleEditorDAO.inserisciPaperFinale(articolo.getId(), editorId);
        }

        // Aggiorna stato revisione a "inviato"
        revisioneService.aggiornaStatoRevisione(revisione.getId(), "inviato");

        // Mostra conferma e torna alla lista
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Articolo inviato a tutti gli editor!", ButtonType.OK);
        alert.showAndWait();
        SceneManager.switchScene("/views/visualizzaMieiPapers.fxml");
    }
}