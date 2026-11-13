package services;

import data.dao.OffertaDAO;
import models.Offerta;
import java.util.List;

public class OffertaService {
    private final OffertaDAO dao = new OffertaDAO();

    public boolean inviaOfferta(int articoloId, int revisoreId) {
        return dao.inviaOfferta(articoloId, revisoreId);
    }

    public List<Offerta> getOffertePerArticolo(int articoloId) {
        return dao.getOffertePerArticolo(articoloId);
    }

    public List<Offerta> getOfferteInviateAllaConferenza(int conferenzaId) {
        return dao.getOfferteInviateAllaConferenza(conferenzaId);
    }

    public boolean aggiornaStatoOfferta(int offertaId, String nuovoStato) {
        return dao.aggiornaStatoOfferta(offertaId, nuovoStato);
    }
}