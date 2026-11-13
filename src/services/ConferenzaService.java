package services;

import data.dao.ConferenzaDAO;
import models.Conferenza;

import java.util.List;

public class ConferenzaService {

    private final ConferenzaDAO conferenzaDAO = new ConferenzaDAO();

    public boolean creaConferenza(Conferenza conferenza) {
        return conferenzaDAO.save(conferenza);
    }

    public Conferenza getConferenzaById(int id) {
        return conferenzaDAO.getById(id);
    }

    public List<Conferenza> getConferenzeUtente(int userId) {
        return conferenzaDAO.getConferenzeByUserId(userId);
    }

    public List<Conferenza> getTutteLeConferenze() {
        return conferenzaDAO.getAll();
    }

    public boolean aggiornaConferenza(Conferenza conferenza) {
        return conferenzaDAO.update(conferenza);
    }

    public int creaConferenzaERestituisciId(Conferenza conferenza) {
        return conferenzaDAO.saveAndReturnId(conferenza);
    }


    public void aggiornaBroadcast(int conferenzaId, boolean attivo) {
        conferenzaDAO.aggiornaBroadcast(conferenzaId, attivo);
    }
}