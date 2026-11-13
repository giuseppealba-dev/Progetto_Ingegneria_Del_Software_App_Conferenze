package data.dao;

import models.Conferenza;
import utils.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConferenzaDAO {

    /**
     * Salva una nuova conferenza nel database.
     */
    public boolean save(Conferenza conf) {
        String sql = """
            INSERT INTO conferenze (
            titolo, descrizione, fine_conferenza, scadenza_submission, scadenza_review,
            scadenza_acceptance, scadenza_final_submission, min_revisori, max_articoli, acceptance_rate,
            luogo, ora, topics, data, organizzatore_id, broadcast_attivo
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conf.getTitolo());
            stmt.setString(2, conf.getDescrizione());
            stmt.setString(3, conf.getFineConferenza().toString());
            stmt.setString(4, conf.getScadenzaSubmission().toString());
            stmt.setString(5, conf.getScadenzaReview().toString());
            stmt.setString(6, conf.getScadenzaAcceptance().toString());
            stmt.setString(7, conf.getScadenzaFinalSubmission().toString());
            stmt.setInt(8, conf.getMinRevisori());
            stmt.setInt(9, conf.getMaxArticoli());
            stmt.setDouble(10, conf.getAcceptanceRate());
            stmt.setString(11, conf.getLuogo());
            stmt.setString(12, conf.getOra());
            stmt.setString(13, conf.getTopics());
            stmt.setString(14, conf.getData() != null ? conf.getData().toString() : null);
            stmt.setInt(15, conf.getOrganizzatoreId());
            stmt.setBoolean(16, conf.isBroadcastAttivo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restituisce tutte le conferenze.
     */
    public List<Conferenza> getAll() {
        List<Conferenza> lista = new ArrayList<>();
        String sql = "SELECT * FROM conferenze";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToConferenza(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Restituisce una conferenza dato l'id.
     */
    public Conferenza getById(int id) {
        String sql = "SELECT * FROM conferenze WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToConferenza(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Restituisce tutte le conferenze a cui partecipa un utente (ha almeno un ruolo).
     */
    public List<Conferenza> getConferenzeByUserId(int userId) {
        List<Conferenza> lista = new ArrayList<>();
        String sql = """
            SELECT c.*
            FROM conferenze c
            JOIN user_ruolo ur ON c.id = ur.conferenza_id
            WHERE ur.user_id = ?
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToConferenza(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Aggiorna una conferenza esistente.
     */
    public boolean update(Conferenza conf) {
        String sql = """
            UPDATE conferenze SET
                titolo = ?, descrizione = ?, fine_conferenza = ?,
                scadenza_submission = ?, scadenza_review = ?, scadenza_acceptance = ?,
                scadenza_final_submission = ?, min_revisori = ?, max_articoli = ?, acceptance_rate = ?,
                luogo = ?, ora = ?, topics = ?, data = ?, organizzatore_id = ?, broadcast_attivo = ?
            WHERE id = ?
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conf.getTitolo());
            stmt.setString(2, conf.getDescrizione());
            stmt.setString(3, conf.getFineConferenza().toString());
            stmt.setString(4, conf.getScadenzaSubmission().toString());
            stmt.setString(5, conf.getScadenzaReview().toString());
            stmt.setString(6, conf.getScadenzaAcceptance().toString());
            stmt.setString(7, conf.getScadenzaFinalSubmission().toString());
            stmt.setInt(8, conf.getMinRevisori());
            stmt.setInt(9, conf.getMaxArticoli());
            stmt.setDouble(10, conf.getAcceptanceRate());
            stmt.setString(11, conf.getLuogo());
            stmt.setString(12, conf.getOra());
            stmt.setString(13, conf.getTopics());
            stmt.setString(14, conf.getData() != null ? conf.getData().toString() : null);
            stmt.setInt(15, conf.getOrganizzatoreId());
            stmt.setBoolean(16, conf.isBroadcastAttivo());
            stmt.setInt(17, conf.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo di utilità per mappare un ResultSet in un oggetto Conferenza.
     */
    private Conferenza mapResultSetToConferenza(ResultSet rs) throws SQLException {
        return new Conferenza(
            rs.getInt("id"),
            rs.getString("titolo"),
            rs.getString("descrizione"),
            LocalDate.parse(rs.getString("fine_conferenza")),
            LocalDate.parse(rs.getString("scadenza_submission")),
            LocalDate.parse(rs.getString("scadenza_review")),
            LocalDate.parse(rs.getString("scadenza_acceptance")),
            LocalDate.parse(rs.getString("scadenza_final_submission")),
            rs.getInt("min_revisori"),
            rs.getInt("max_articoli"),
            rs.getDouble("acceptance_rate"),
            rs.getString("luogo"),
            rs.getString("ora"),
            rs.getString("topics"),
            LocalDate.parse(rs.getString("data")),
            rs.getInt("organizzatore_id"),
            rs.getBoolean("broadcast_attivo")
        );
    }

    /**
     * Salva una nuova conferenza e restituisce l'id generato.
     */
    public int saveAndReturnId(Conferenza conf) {
        String sql = """
            INSERT INTO conferenze (
                titolo, descrizione, fine_conferenza, scadenza_submission, scadenza_review,
                scadenza_acceptance, scadenza_final_submission, min_revisori, max_articoli, acceptance_rate,
                luogo, ora, topics,data, organizzatore_id, broadcast_attivo
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conf.getTitolo());
            stmt.setString(2, conf.getDescrizione());
            stmt.setString(3, conf.getFineConferenza().toString());
            stmt.setString(4, conf.getScadenzaSubmission().toString());
            stmt.setString(5, conf.getScadenzaReview().toString());
            stmt.setString(6, conf.getScadenzaAcceptance().toString());
            stmt.setString(7, conf.getScadenzaFinalSubmission().toString());
            stmt.setInt(8, conf.getMinRevisori());
            stmt.setInt(9, conf.getMaxArticoli());
            stmt.setDouble(10, conf.getAcceptanceRate());
            stmt.setString(11, conf.getLuogo());
            stmt.setString(12, conf.getOra());
            stmt.setString(13, conf.getTopics());
            stmt.setString(14, conf.getData() != null ? conf.getData().toString() : null);
            stmt.setInt(15, conf.getOrganizzatoreId());
            stmt.setBoolean(16, conf.isBroadcastAttivo());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void aggiornaBroadcast(int conferenzaId, boolean attivo) {
        String sql = "UPDATE conferenze SET broadcast_attivo = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, attivo);
            stmt.setInt(2, conferenzaId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}