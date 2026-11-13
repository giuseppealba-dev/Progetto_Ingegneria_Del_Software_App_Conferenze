package data.dao;

import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.Revisione;

public class RevisioneDAO {

    public int contaRevisioniCompletatePerArticolo(int articoloId) {
        String sql = "SELECT COUNT(*) FROM revisioni WHERE id_articolo = ? AND (stato = 'completata' OR stato = 'accettato' OR stato = 'inviato')";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Revisione> getRevisioniByArticolo(int articoloId) {
        List<Revisione> lista = new ArrayList<>();
        String sql = "SELECT * FROM revisioni WHERE id_articolo = ? AND visibile_autore = 1";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Revisione rev = new Revisione();
                rev.setId(rs.getInt("id"));
                rev.setIdArticolo(rs.getInt("id_articolo"));
                rev.setIdRevisore(rs.getInt("id_revisore"));
                rev.setStato(rs.getString("stato"));
                rev.setCommento(rs.getString("commento"));
                rev.setCommentoChair(rs.getString("commento_chair"));
                rev.setRate(rs.getInt("rate"));
                rev.setData(rs.getDate("data"));
                rev.setVisibileAutore(rs.getInt("visibile_autore"));
                lista.add(rev);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }


    public boolean salvaRevisione(Revisione revisione) {
        String sql = "INSERT INTO revisioni (id_articolo, id_revisore, stato, commento, commento_chair, rate, data, visibile_autore) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisione.getIdArticolo());
            stmt.setInt(2, revisione.getIdRevisore());
            stmt.setString(3, revisione.getStato());
            stmt.setString(4, revisione.getCommento());
            stmt.setString(5, revisione.getCommentoChair());
            stmt.setInt(6, revisione.getRate());
            stmt.setDate(7, new java.sql.Date(revisione.getData().getTime()));
            stmt.setInt(8, 0); // Non visibile all'autore
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean inviaRevisioneAllAutore(int revisioneId) {
        String sql = "UPDATE revisioni SET visibile_autore = 1 WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisioneId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Revisione> getTutteRevisioniByArticolo(int articoloId) {
        List<Revisione> lista = new ArrayList<>();
        String sql = "SELECT * FROM revisioni WHERE id_articolo = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Revisione rev = new Revisione();
                rev.setId(rs.getInt("id"));
                rev.setIdArticolo(rs.getInt("id_articolo"));
                rev.setIdRevisore(rs.getInt("id_revisore"));
                rev.setStato(rs.getString("stato"));
                rev.setCommento(rs.getString("commento"));
                rev.setCommentoChair(rs.getString("commento_chair"));
                rev.setRate(rs.getInt("rate"));
                rev.setData(rs.getDate("data"));
                rev.setVisibileAutore(rs.getInt("visibile_autore"));
                lista.add(rev);

            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }


    public boolean aggiornaVisibilitaEStato(int revisioneId, int visibileAutore, String stato) {
        String sql = "UPDATE revisioni SET visibile_autore = ?, stato = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, visibileAutore);
            stmt.setString(2, stato);
            stmt.setInt(3, revisioneId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean aggiornaStatoRevisione(int revisioneId, String stato) {
        String sql = "UPDATE revisioni SET stato = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stato);
            stmt.setInt(2, revisioneId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminaRevisione(int revisioneId) {
        String sql = "DELETE FROM revisioni WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisioneId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


    public Revisione getRevisioneFinaleByArticolo(int articoloId) {
        String sql = "SELECT * FROM revisioni WHERE id_articolo = ? AND (stato = 'accettato' OR stato = 'inviato') ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Revisione rev = new Revisione();
                rev.setId(rs.getInt("id"));
                rev.setIdArticolo(rs.getInt("id_articolo"));
                rev.setIdRevisore(rs.getInt("id_revisore"));
                rev.setStato(rs.getString("stato"));
                rev.setCommento(rs.getString("commento"));
                rev.setCommentoChair(rs.getString("commento_chair"));
                rev.setRate(rs.getInt("rate"));
                rev.setData(rs.getDate("data"));
                rev.setVisibileAutore(rs.getInt("visibile_autore"));
                return rev;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}