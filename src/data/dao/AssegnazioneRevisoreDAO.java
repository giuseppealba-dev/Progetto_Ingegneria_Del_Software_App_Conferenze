package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssegnazioneRevisoreDAO {

    public int contaRevisoriPerArticolo(int articoloId) {
        String sql = "SELECT COUNT(*) FROM assegnazioni_revisori WHERE id_articolo = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int contaArticoliPerRevisoreInConferenza(int revisoreId, int conferenzaId) {
        String sql = """
            SELECT COUNT(*) FROM assegnazioni_revisori ar
            JOIN articoli a ON ar.id_articolo = a.id
            WHERE ar.id_revisore = ? AND a.id_conferenza = ?
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisoreId);
            stmt.setInt(2, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean assegnaRevisore(int articoloId, int revisoreId) {
        // Inserisce direttamente con stato 'Assegnato'
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

    public List<Integer> getRevisoriAssegnati(int articoloId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_revisore FROM assegnazioni_revisori WHERE id_articolo = ? AND stato = 'Assegnato'";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("id_revisore"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean rimuoviAssegnazione(int articoloId, int revisoreId) {
        String sql = "DELETE FROM assegnazioni_revisori WHERE id_articolo = ? AND id_revisore = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, revisoreId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean inserisciProposta(int articoloId, int revisoreId) {
        // Controlla che non esista già una proposta o assegnazione attiva
        String checkSql = "SELECT COUNT(*) FROM assegnazioni_revisori WHERE id_articolo = ? AND id_revisore = ? AND stato IN ('Assegnato', 'proposto')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, articoloId);
            checkStmt.setInt(2, revisoreId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Esiste già una proposta o assegnazione attiva
                return false;
            }
        } catch (SQLException e) { e.printStackTrace(); return false; }

        // Se non esiste, inserisci la proposta
        String sql = "INSERT INTO assegnazioni_revisori (id_articolo, id_revisore, stato) VALUES (?, ?, 'proposto')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, revisoreId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean aggiornaStatoProposta(int articoloId, int revisoreId, String nuovoStato) {
        String sql = "UPDATE assegnazioni_revisori SET stato = ? WHERE id_articolo = ? AND id_revisore = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setInt(2, articoloId);
            stmt.setInt(3, revisoreId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodo per escludere revisori già proposti o assegnati
    public List<Integer> getRevisoriPropostiOAssegnati(int articoloId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_revisore FROM assegnazioni_revisori WHERE id_articolo = ? AND stato IN ('Assegnato', 'proposto')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("id_revisore"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}