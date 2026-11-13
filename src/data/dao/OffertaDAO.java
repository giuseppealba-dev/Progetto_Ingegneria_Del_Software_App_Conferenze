package data.dao;

import models.Offerta;
import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffertaDAO {
    public boolean inviaOfferta(int articoloId, int revisoreId) {
        String checkSql = "SELECT COUNT(*) FROM offerte WHERE id_articolo = ? AND id_revisore = ? AND stato = 'inviata'";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, articoloId);
            checkStmt.setInt(2, revisoreId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }

        String sql = "INSERT INTO offerte (id_articolo, id_revisore, stato) VALUES (?, ?, 'inviata')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, revisoreId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Integer> getRevisoriOffertisi(int articoloId) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_revisore FROM offerte WHERE id_articolo = ? AND stato = 'inviata'";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) lista.add(rs.getInt("id_revisore"));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Offerta> getOffertePerArticolo(int articoloId) {
        List<Offerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM offerte WHERE id_articolo = ? AND stato = 'inviata'";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Offerta o = new Offerta();
                o.setId(rs.getInt("id"));
                o.setIdArticolo(rs.getInt("id_articolo"));
                o.setIdRevisore(rs.getInt("id_revisore"));
                o.setStato(rs.getString("stato"));
                o.setDataOfferta(rs.getTimestamp("data_offerta"));
                lista.add(o);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Offerta> getOfferteInviateAllaConferenza(int conferenzaId) {
        List<Offerta> lista = new ArrayList<>();
        String sql = """
            SELECT o.* FROM offerte o
            JOIN articoli a ON o.id_articolo = a.id
            WHERE a.id_conferenza = ? AND o.stato = 'inviata'
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Offerta o = new Offerta();
                o.setId(rs.getInt("id"));
                o.setIdArticolo(rs.getInt("id_articolo"));
                o.setIdRevisore(rs.getInt("id_revisore"));
                o.setStato(rs.getString("stato"));
                o.setDataOfferta(rs.getTimestamp("data_offerta"));
                lista.add(o);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean aggiornaStatoOfferta(int offertaId, String nuovoStato) {
        String sql = "UPDATE offerte SET stato = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setInt(2, offertaId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}