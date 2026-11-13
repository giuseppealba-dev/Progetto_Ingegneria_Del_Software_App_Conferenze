package data.dao;

import models.Articolo;
import utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticoloDAO {

    // Restituisce tutti gli articoli di una conferenza
    public List<Articolo> getByConferenza(int conferenzaId) {
        List<Articolo> lista = new ArrayList<>();
        String sql = "SELECT * FROM articoli WHERE id_conferenza = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Articolo art = new Articolo();
                art.setId(rs.getInt("id"));
                art.setIdAutore(rs.getInt("id_autore"));
                art.setIdConferenza(rs.getInt("id_conferenza"));
                art.setTitolo(rs.getString("titolo"));
                art.setFile(rs.getString("file"));
                lista.add(art);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Restituisce tutti gli articoli di un autore in una conferenza
    public List<Articolo> getByAutoreEConferenza(int autoreId, int conferenzaId) {
        List<Articolo> lista = new ArrayList<>();
        String sql = "SELECT * FROM articoli WHERE id_autore = ? AND id_conferenza = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, autoreId);
            stmt.setInt(2, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Articolo(
                        rs.getInt("id"),
                        rs.getInt("id_autore"),
                        rs.getInt("id_conferenza"),
                        rs.getString("titolo"),
                        rs.getString("file")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Salva un nuovo articolo e ritorna il suo ID
    public int saveAndReturnId(Articolo articolo) {
        String sql = "INSERT INTO articoli (id_autore, id_conferenza, titolo, file) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, articolo.getIdAutore());
            stmt.setInt(2, articolo.getIdConferenza());
            stmt.setString(3, articolo.getTitolo());
            stmt.setString(4, articolo.getFile());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Articoli assegnati al revisore che non ha ancora revisionato
    public List<Articolo> getArticoliDaRevisionare(int revisoreId, int conferenzaId) {
        List<Articolo> lista = new ArrayList<>();
        String sql = """
            SELECT a.* FROM articoli a
            JOIN assegnazioni_revisori ar ON a.id = ar.id_articolo
            WHERE ar.id_revisore = ? AND a.id_conferenza = ?
            AND ar.stato = 'Assegnato'
            AND a.id NOT IN (SELECT id_articolo FROM revisioni WHERE id_revisore = ?)
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisoreId);
            stmt.setInt(2, conferenzaId);
            stmt.setInt(3, revisoreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Articolo art = new Articolo();
                art.setId(rs.getInt("id"));
                art.setIdAutore(rs.getInt("id_autore"));
                art.setIdConferenza(rs.getInt("id_conferenza"));
                art.setTitolo(rs.getString("titolo"));
                art.setFile(rs.getString("file"));
                lista.add(art);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean aggiornaStatoArticolo(int articoloId, String nuovoStato) {
        String sql = "UPDATE articoli SET stato = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setInt(2, articoloId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTestoArticolo(int articoloId) {
        String sql = "SELECT file FROM articoli WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("file");
        } catch (SQLException e) { e.printStackTrace(); }
        return "";
    }

    public void aggiornaTestoArticolo(int articoloId, String nuovoTesto) {
        String sql = "UPDATE articoli SET file = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoTesto);
            stmt.setInt(2, articoloId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Proposte di revisione per revisore (stato "proposto")
    public List<Articolo> getProposteRevisionePerRevisore(int revisoreId, int conferenzaId) {
        List<Articolo> lista = new ArrayList<>();
        String sql = """
            SELECT a.*
            FROM articoli a
            JOIN assegnazioni_revisori ar ON a.id = ar.id_articolo
            WHERE ar.id_revisore = ? AND a.id_conferenza = ? AND ar.stato = 'proposto'
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, revisoreId);
            stmt.setInt(2, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Articolo art = new Articolo();
                art.setId(rs.getInt("id"));
                art.setIdAutore(rs.getInt("id_autore"));
                art.setIdConferenza(rs.getInt("id_conferenza"));
                art.setTitolo(rs.getString("titolo"));
                art.setFile(rs.getString("file"));
                lista.add(art);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // 🔽 Metodo 1: Trova editor della conferenza dell'articolo
    public Integer getEditorByArticolo(int articoloId) {
        String sql = """
            SELECT ur.user_id
            FROM articoli a
            JOIN user_ruolo ur ON ur.conferenza_id = a.id_conferenza
            JOIN ruolo r ON ur.ruolo_id = r.id
            WHERE a.id = ? AND r.nome = 'Editor'
            LIMIT 1
        """;
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articoloId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔽 Metodo 2: Invia articolo finale all'editor (registrando "notifica")
    public boolean inviaArticoloFinaleAllEditor(int idArticolo) {
        Integer editorId = getEditorByArticolo(idArticolo);
        if (editorId == null) return false;

        String sql = "INSERT INTO notifiche (id_utente, id_articolo, tipo, messaggio, letto) VALUES (?, ?, 'inviato_all_editor', 'Articolo inviato all\'editor', false)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, editorId);
            stmt.setInt(2, idArticolo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔽 Metodo 3: Finalizza e invia articolo aggiornato
    public boolean finalizzaEInvia(int idArticolo) {
        boolean statoAggiornato = aggiornaStatoArticolo(idArticolo, "finale_inviato");
        if (!statoAggiornato) return false;

        return inviaArticoloFinaleAllEditor(idArticolo);
    }
}
