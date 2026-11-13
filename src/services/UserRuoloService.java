package services;

import data.dao.UserRuoloDAO;
import models.UserRuolo;
import java.util.List;

/**
 * Service per la logica di business su user_ruolo.
 */
public class UserRuoloService {
    private UserRuoloDAO userRuoloDAO = new UserRuoloDAO();

    public boolean assegnaRuolo(int userId, int ruoloId, int conferenzaId) {
        UserRuolo ur = new UserRuolo(userId, ruoloId, conferenzaId);
        return userRuoloDAO.save(ur);
    }

    public List<UserRuolo> getRuoliUtente(int userId) {
        return userRuoloDAO.getByUserId(userId);
    }

    public List<UserRuolo> getRuoliConferenza(int conferenzaId) {
        return userRuoloDAO.getByConferenzaId(conferenzaId);
    }

    public boolean rimuoviAssegnazione(int userId, int ruoloId, int conferenzaId) {
    return userRuoloDAO.delete(userId, ruoloId, conferenzaId);
}

}