package data.dao;

import models.Ruolo;
import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class RuoloDAO {
    

    public List<Ruolo> getAll() {
        List<Ruolo> ruoli = new ArrayList<>();
        String sql = "SELECT * FROM ruolo";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ruoli.add(new Ruolo(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ruoli;
    }


    public Ruolo getById(int id) {
        String sql = "SELECT * FROM ruolo WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ruolo(rs.getInt("id"), rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


     public Ruolo getByNome(String nome) {
        String sql = "SELECT * FROM ruolo WHERE nome = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ruolo(rs.getInt("id"), rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Ruolo ruolo) {
        String sql = "INSERT INTO ruolo (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ruolo.getNomeRuolo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
