package data.dao;

import models.Articolo;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperFinaleEditorDAO {

    public List<Articolo> getPapersFinaliByEditor(int editorId) {
        List<Articolo> result = new ArrayList<>();
        String query = """
            SELECT a.*
            FROM paper_finali_editor p
            JOIN articoli a ON p.id_articolo = a.id
            WHERE p.id_editor = ?;
        """;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, editorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Articolo a = new Articolo(
                    rs.getInt("id"),
                    rs.getInt("id_autore"),
                    rs.getInt("id_conferenza"),
                    rs.getString("titolo"),
                    rs.getString("file"),
                    rs.getString("stato")
                );
                result.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void inserisciPaperFinale(int idArticolo, int idEditor) {
        String sql = "INSERT INTO paper_finali_editor (id_articolo, id_editor) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idArticolo);
            stmt.setInt(2, idEditor);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void rimuoviPaperFinale(int articoloId, int editorId) {
        String sql = "DELETE FROM paper_finali_editor WHERE id_articolo = ? AND id_editor = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            stmt.setInt(2, editorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
