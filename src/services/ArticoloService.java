package services;

import data.dao.ArticoloDAO;
import data.dao.ArticoloArgomentoDAO;
import data.dao.AssegnazioneRevisoreDAO;
import data.dao.RevisioneDAO;
import models.Articolo;
import models.Conferenza;
import utils.DatabaseManager;
import data.dao.ConferenzaDAO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticoloService {
    private final ArticoloDAO articoloDAO = new ArticoloDAO();
    private final ArticoloArgomentoDAO articoloArgomentoDAO = new ArticoloArgomentoDAO();
    private final AssegnazioneRevisoreDAO assegnazioneRevisoreDAO = new AssegnazioneRevisoreDAO();
    private final RevisioneDAO revisioneDAO = new RevisioneDAO();
    private final ConferenzaDAO conferenzaDAO = new ConferenzaDAO();


    public List<Articolo> getArticoliByAutoreEConferenza(int autoreId, int conferenzaId) {
        return articoloDAO.getByAutoreEConferenza(autoreId, conferenzaId);
    }

    public List<Articolo> getArticoliByConferenza(int conferenzaId) {
        return articoloDAO.getByConferenza(conferenzaId);
    }

    public boolean sottomettiArticolo(String titolo, File file, int idAutore, int idConferenza, List<Integer> argomentiIds) {
        try {
            String uploadsDir = "uploads";
            java.io.File dir = new java.io.File(uploadsDir);
            if (!dir.exists()) dir.mkdirs();

            String fileDest = uploadsDir + "/" + System.currentTimeMillis() + "_" + file.getName();
            Files.copy(file.toPath(), new java.io.File(fileDest).toPath(), StandardCopyOption.REPLACE_EXISTING);

            Articolo articolo = new Articolo(idAutore, idConferenza, titolo, fileDest);
            int articoloId = articoloDAO.saveAndReturnId(articolo);

            if (articoloId > 0) {
                for (Integer argId : argomentiIds) {
                    articoloArgomentoDAO.save(articoloId, argId);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNumeroRevisoriAssegnati(int articoloId) {
        return assegnazioneRevisoreDAO.contaRevisoriPerArticolo(articoloId);
    }

    public int getNumeroRevisioniCompletate(int articoloId) {
        return revisioneDAO.contaRevisioniCompletatePerArticolo(articoloId);
    }

    public boolean sottomettiArticoloTesto(String titolo, String testo, int idAutore, int idConferenza, List<Integer> argomentiIds) {
        try {
            String uploadsDir = "uploads";
            java.io.File dir = new java.io.File(uploadsDir);
            if (!dir.exists()) dir.mkdirs();

            String fileDest = uploadsDir + "/" + System.currentTimeMillis() + "_" + titolo.replaceAll("\\s+", "_") + ".txt";
            java.nio.file.Files.writeString(java.nio.file.Path.of(fileDest), testo);

            Articolo articolo = new Articolo(idAutore, idConferenza, titolo, fileDest);
            int articoloId = articoloDAO.saveAndReturnId(articolo);

            if (articoloId > 0) {
                for (Integer argId : argomentiIds) {
                    articoloArgomentoDAO.save(articoloId, argId);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Articolo> getArticoliDaRevisionare(int revisoreId, int conferenzaId) {
        // Restituisce solo i paper assegnati a questo revisore e NON ancora revisionati
        return articoloDAO.getArticoliDaRevisionare(revisoreId, conferenzaId);
    }

    public boolean aggiornaStatoArticolo(int articoloId, String nuovoStato){
        return articoloDAO.aggiornaStatoArticolo(articoloId,nuovoStato);
    }


    public String getTestoArticolo(int articoloId) {
        return articoloDAO.getTestoArticolo(articoloId);
    }
    public void aggiornaTestoArticolo(int articoloId, String nuovoTesto) {
        articoloDAO.aggiornaTestoArticolo(articoloId, nuovoTesto);
    }




    public List<Articolo> getArticoliBroadcastPerRevisore(int revisoreId, int conferenzaId) {
        Conferenza conf = conferenzaDAO.getById(conferenzaId);
        if (!conf.isBroadcastAttivo()) return Collections.emptyList();
        return articoloDAO.getByConferenza(conferenzaId);
    }


    public List<Articolo> getProposteRevisionePerRevisore(int revisoreId, int conferenzaId) {
        return articoloDAO.getProposteRevisionePerRevisore(revisoreId, conferenzaId);
    }


    public Articolo getArticoloById(int id) {
        String sql = "SELECT * FROM articoli WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Articolo art = new Articolo();
                art.setId(rs.getInt("id"));
                art.setIdAutore(rs.getInt("id_autore"));
                art.setIdConferenza(rs.getInt("id_conferenza"));
                art.setTitolo(rs.getString("titolo"));
                art.setFile(rs.getString("file"));
                return art;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}