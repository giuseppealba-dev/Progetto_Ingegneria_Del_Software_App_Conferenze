package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArgomentoRevisoreDAO {
    public boolean save(int userId, int conferenzaId, int argomentoId) {
        String sql = "INSERT INTO argomenti_revisore (user_id, conferenza_id, argomento_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, conferenzaId);
            stmt.setInt(3, argomentoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Integer> getArgomentiByUserAndConferenza(int userId, int conferenzaId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT argomento_id FROM argomenti_revisore WHERE user_id = ? AND conferenza_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("argomento_id"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}