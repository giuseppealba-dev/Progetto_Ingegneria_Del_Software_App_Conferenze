package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Revisione;

public class RevisioneSottoRevisoreDAO {
    public boolean salvaRevisione(int articoloId, int sottoRevisoreId, String commento, int rate) {
        String sql = "INSERT INTO revisioni_sottorevisore (id_articolo, id_sottorevisore, commento, rate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, sottoRevisoreId);
            stmt.setString(3, commento);
            stmt.setInt(4, rate);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   public List<Revisione> getSottoRevisioniByArticolo(int articoloId) {
        List<Revisione> lista = new ArrayList<>();
        String sql = "SELECT * FROM revisioni_sottorevisore WHERE id_articolo = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Revisione rev = new Revisione();
                rev.setId(rs.getInt("id"));
                rev.setIdArticolo(rs.getInt("id_articolo"));
                rev.setIdRevisore(rs.getInt("id_sottorevisore")); // usa il campo corretto
                rev.setCommento(rs.getString("commento"));
                rev.setRate(rs.getInt("rate"));
                rev.setData(java.sql.Date.valueOf(rs.getString("data")));
                lista.add(rev);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}