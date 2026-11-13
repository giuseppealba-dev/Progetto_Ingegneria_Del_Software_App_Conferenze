package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticoloArgomentoDAO {
    public boolean save(int articoloId, int argomentoId) {
        String sql = "INSERT INTO articolo_argomento (articolo_id, argomento_id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, argomentoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Integer> getArgomentiByArticolo(int articoloId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT argomento_id FROM articolo_argomento WHERE articolo_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("argomento_id"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
