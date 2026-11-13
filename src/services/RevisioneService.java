package services;

import models.Revisione;
import data.dao.RevisioneDAO;
import java.util.List;

public class RevisioneService {
    private final RevisioneDAO revisioneDAO = new RevisioneDAO();

    public List<Revisione> getRevisioniByArticolo(int articoloId) {
        return revisioneDAO.getRevisioniByArticolo(articoloId);
    }

    public boolean salvaRevisione(Revisione revisione) {
        return revisioneDAO.salvaRevisione(revisione);
    }

    public List<Revisione> getTutteRevisioniByArticolo(int articoloId) {
    return revisioneDAO.getTutteRevisioniByArticolo(articoloId);
    }

    
    public boolean aggiornaVisibilitaEStato(int revisioneId, int visibileAutore, String stato) {
        return revisioneDAO.aggiornaVisibilitaEStato(revisioneId, visibileAutore, stato);
    }

    public boolean aggiornaStatoRevisione(int revisioneId, String stato) {
        return revisioneDAO.aggiornaStatoRevisione(revisioneId, stato);
    }

    public boolean eliminaRevisione(int revisioneId) {
        return revisioneDAO.eliminaRevisione(revisioneId);
    }


    public Revisione getRevisioneFinaleByArticolo(int articoloId) {
        // Restituisce la revisione con stato accettato o inviato
        return revisioneDAO.getRevisioneFinaleByArticolo(articoloId);
    }
}

