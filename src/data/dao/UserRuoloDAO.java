package data.dao;

import models.UserRuolo;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la tabella user_ruolo.
 */
public class UserRuoloDAO {

    /**
     * Salva una nuova associazione user-ruolo-conferenza.
     */
    public boolean save(UserRuolo ur) {
        String sql = "INSERT INTO user_ruolo (user_id, ruolo_id, conferenza_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ur.getUserId());
            stmt.setInt(2, ur.getRuoloId());
            stmt.setInt(3, ur.getConferenzaId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ottiene tutte le associazioni per un dato userId.
     */
    public List<UserRuolo> getByUserId(int userId) {
        List<UserRuolo> lista = new ArrayList<>();
        String sql = "SELECT * FROM user_ruolo WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToUserRuolo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Ottiene tutte le associazioni per una data conferenza.
     */
    public List<UserRuolo> getByConferenzaId(int conferenzaId) {
        List<UserRuolo> lista = new ArrayList<>();
        String sql = "SELECT * FROM user_ruolo WHERE conferenza_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToUserRuolo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Elimina una specifica associazione user-ruolo-conferenza.
     */
    public boolean delete(int userId, int ruoloId, int conferenzaId) {
        String sql = "DELETE FROM user_ruolo WHERE user_id = ? AND ruolo_id = ? AND conferenza_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, ruoloId);
            stmt.setInt(3, conferenzaId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mappa un ResultSet su un oggetto UserRuolo.
     */
    private UserRuolo mapResultSetToUserRuolo(ResultSet rs) throws SQLException {
        return new UserRuolo(
            rs.getInt("user_id"),
            rs.getInt("ruolo_id"),
            rs.getInt("conferenza_id")
        );
    }

    /**
     * Recupera la lista degli ID degli utenti che sono Editor in una conferenza.
     * Serve per ottenere gli editor per l'invio dei paper.
     */
    public List<Integer> getEditorIdsByConferenza(int conferenzaId) {
        List<Integer> editorIds = new ArrayList<>();
        String sql = """
            SELECT ur.user_id
            FROM user_ruolo ur
            JOIN ruolo r ON ur.ruolo_id = r.id
            WHERE r.nome = 'Editor' AND ur.conferenza_id = ?
        """;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                editorIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return editorIds;
    }



    public int getRuoloIdByName(String nomeRuolo) {
        String sql = "SELECT id FROM ruolo WHERE nome = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeRuolo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

}
