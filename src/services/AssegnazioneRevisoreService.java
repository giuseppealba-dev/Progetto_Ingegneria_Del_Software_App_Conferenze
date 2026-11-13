package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import data.dao.AssegnazioneRevisoreDAO;
import models.Conferenza;
import utils.DatabaseManager;

public class AssegnazioneRevisoreService {
    private final AssegnazioneRevisoreDAO dao = new AssegnazioneRevisoreDAO();

    public boolean assegnaRevisoreSeConsente(int articoloId, int revisoreId, Conferenza conferenza) {
        int revisoriAssegnati = dao.contaRevisoriPerArticolo(articoloId);
        int articoliAssegnati = dao.contaArticoliPerRevisoreInConferenza(revisoreId, conferenza.getId());

        if (revisoriAssegnati >= conferenza.getMinRevisori()) {
            // L'articolo ha già il numero minimo di revisori
            return false;
        }
        if (articoliAssegnati >= conferenza.getMaxArticoli()) {
            // Il revisore ha già il massimo di articoli
            return false;
        }
        return dao.assegnaRevisore(articoloId, revisoreId);
    }

    public boolean rinunciaRevisione(int articoloId, int revisoreId) {
        return dao.rimuoviAssegnazione(articoloId, revisoreId);
    }

    public int getNumeroArticoliAssegnati(int revisoreId, int conferenzaId) {
        return dao.contaArticoliPerRevisoreInConferenza(revisoreId, conferenzaId);
    }

    public List<Integer> getRevisoriAssegnati(int articoloId) {
        return dao.getRevisoriAssegnati(articoloId);
    }

    public boolean assegnaRevisore(int articoloId, int revisoreId) {
        String checkSql = "SELECT stato FROM assegnazioni_revisori WHERE id_articolo = ? AND id_revisore = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, articoloId);
            checkStmt.setInt(2, revisoreId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String stato = rs.getString("stato");
                if ("proposto".equalsIgnoreCase(stato)) {
                    // Aggiorna lo stato a Assegnato
                    String updateSql = "UPDATE assegnazioni_revisori SET stato = 'Assegnato' WHERE id_articolo = ? AND id_revisore = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, articoloId);
                        updateStmt.setInt(2, revisoreId);
                        updateStmt.executeUpdate();
                        return true;
                    }
                } else {
                    // Esiste già una assegnazione con stato diverso, non fare nulla
                    return false;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); return false; }

        // Se non esiste, inserisci nuova assegnazione
        String sql = "INSERT INTO assegnazioni_revisori (id_articolo, id_revisore, stato) VALUES (?, ?, 'Assegnato')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, revisoreId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean proponiRevisore(int articoloId, int revisoreId) {
        // Inserisci in assegnazioni_revisori con stato "proposto"
        return dao.inserisciProposta(articoloId, revisoreId);
    }

    public boolean accettaProposta(int articoloId, int revisoreId) {
        // Aggiorna lo stato a "Assegnato"
        return dao.aggiornaStatoProposta(articoloId, revisoreId, "Assegnato");
    }

    public boolean rifiutaProposta(int articoloId, int revisoreId) {
        return dao.aggiornaStatoProposta(articoloId, revisoreId, "rifiutato");
    }

    // Metodo per escludere revisori già proposti o assegnati
    public List<Integer> getRevisoriPropostiOAssegnati(int articoloId) {
        return dao.getRevisoriPropostiOAssegnati(articoloId);
    }
}