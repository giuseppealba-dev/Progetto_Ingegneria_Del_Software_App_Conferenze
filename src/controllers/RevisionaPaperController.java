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
import java.time.LocalDate;
import java.util.List;

public class RevisionaPaperController {

    @FXML private Label titoloLabel;
    @FXML private TextArea testoPaperArea;
    @FXML private TextArea commentoAutoreArea;
    @FXML private TextArea commentoChairArea;
    @FXML private Spinner<Integer> rateSpinner;
    @FXML private Label feedbackLabel;
    @FXML private VBox sottoRevisioniContainer;

    private final RevisioneService revisioneService = new RevisioneService();
    private final ArticoloService articoloService = new ArticoloService();
    private Articolo articolo;

    @FXML
    public void initialize() {
        articolo = SessionManager.getArticoloSelezionato();
        if (articolo == null) {
            titoloLabel.setText("Nessun paper selezionato");
            testoPaperArea.setText("");
            return;
        }
        titoloLabel.setText(articolo.getTitolo());

        // Carica il testo del paper dal file
        String testo = articolo.getFile();
        if (testo != null && (testo.contains("/") || testo.contains("\\"))) {
            try {
                testo = Files.readString(Path.of(testo));
            } catch (Exception e) {
                testo = "Impossibile leggere il file del paper.";
            }
        }
        testoPaperArea.setText(testo);

        // Spinner per rate
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(-3, 3, 0);
        rateSpinner.setValueFactory(valueFactory);
        rateSpinner.getValueFactory().setValue(0); // Forza il valore iniziale a 0

        caricaSottoRevisioni();
    }

    @FXML
    void handleBack(ActionEvent event) {
        SceneManager.switchScene("/views/paperDaRevisionare.fxml");
    }

    @FXML
    void handleInviaRevisione(ActionEvent event) {
        String commentoAutore = commentoAutoreArea.getText().trim();
        String commentoChair = commentoChairArea.getText().trim();
        final int rate = (rateSpinner.getValue() == null) ? 0 : rateSpinner.getValue();

        if (commentoAutore.isEmpty() || commentoChair.isEmpty()) {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Compila tutti i campi prima di inviare la revisione.");
            return;
        }

        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION, "Confermi l'invio della revisione?", ButtonType.YES, ButtonType.NO);
        conferma.setTitle("Conferma invio revisione");
        conferma.setHeaderText(null);
        conferma.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                inviaRevisione(commentoAutore, commentoChair, rate);
            }
        });
    }

    private void inviaRevisione(String commentoAutore, String commentoChair, int rate) {
        int idArticolo = articolo.getId();
        int idRevisore = SessionManager.getLoggedUser().getId();

        Revisione revisione = new Revisione();
        revisione.setIdArticolo(idArticolo);
        revisione.setIdRevisore(idRevisore);
        revisione.setStato("completata");
        revisione.setCommento(commentoAutore);
        revisione.setCommentoChair(commentoChair);
        revisione.setRate(rate);
        revisione.setData(java.sql.Date.valueOf(LocalDate.now()));

        boolean ok = revisioneService.salvaRevisione(revisione);

        if (ok) {
            // Aggiorna lo stato del paper a "Revisionato"
            articoloService.aggiornaStatoArticolo(idArticolo, "Revisionato");

            // Mostra solo popup di conferma, nessun messaggio verde nella label
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Revisione inviata con successo!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Conferma invio");
            alert.showAndWait();

            SceneManager.switchScene("/views/paperDaRevisionare.fxml");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Errore nell'invio della revisione.");
        }
    }

    private void caricaSottoRevisioni() {
        sottoRevisioniContainer.getChildren().clear();
        int articoloId = articolo.getId();

        List<Revisione> sottoRevisioni = new services.RevisioneSottoRevisoreService().getSottoRevisioniByArticolo(articoloId);

        if (sottoRevisioni.isEmpty()) {
            Label noSotto = new Label("Nessuna sottorevisione ricevuta.");
            noSotto.setStyle("-fx-text-fill: gray;");
            sottoRevisioniContainer.getChildren().add(noSotto);
        } else {
            services.UserService userService = new services.UserService();
            for (Revisione rev : sottoRevisioni) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: lightgray; -fx-border-radius: 5;");
                // Recupera nome e cognome del sottorevisore
                models.User user = userService.getUserById(rev.getIdRevisore());
                String nomeCognome = (user != null)
                    ? user.getNome() + " " + user.getCognome()
                    : "SottoRevisore ID: " + rev.getIdRevisore();
                Label info = new Label(nomeCognome);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Button visualizzaBtn = new Button("Visualizza sottorevisione");
                visualizzaBtn.setOnAction(ev -> {
                    SessionManager.setRevisioneSelezionata(rev);
                    SceneManager.switchScene("/views/visualizzaSottoRevisione.fxml");
                });
                row.getChildren().addAll(info, spacer, visualizzaBtn);
                sottoRevisioniContainer.getChildren().add(row);
            }
        }
    }
}