package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SottoRevisioneDAO {
    public boolean assegnaSottoRevisore(int articoloId, int sottoRevisoreId, int revisoreId, int conferenzaId) {
        String sql = "INSERT INTO sottorevisioni (id_articolo, id_sottorevisore, id_revisore, id_conferenza) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, sottoRevisoreId);
            stmt.setInt(3, revisoreId);
            stmt.setInt(4, conferenzaId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Se già presente, ignora l'errore
            if (e.getMessage().contains("UNIQUE")) return false;
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getArticoliAssegnatiASottoRevisore(int sottoRevisoreId, int conferenzaId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_articolo FROM sottorevisioni WHERE id_sottorevisore = ? AND id_conferenza = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sottoRevisoreId);
            stmt.setInt(2, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) lista.add(rs.getInt("id_articolo"));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }


    public boolean rinunciaSottoRevisione(int articoloId, int sottoRevisoreId) {
        String sql = "DELETE FROM sottorevisioni WHERE id_articolo = ? AND id_sottorevisore = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, sottoRevisoreId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}