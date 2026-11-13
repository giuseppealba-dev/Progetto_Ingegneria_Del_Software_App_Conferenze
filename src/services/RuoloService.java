package services;

import data.dao.RuoloDAO;
import models.Ruolo;
import java.util.List;

/**
 * Service per la logica di business dei ruoli.
 */
public class RuoloService {
    private RuoloDAO ruoloDAO = new RuoloDAO();

    public List<Ruolo> getAllRuoli() {
        return ruoloDAO.getAll();
    }

    public Ruolo getRuoloById(int id) {
        return ruoloDAO.getById(id);
    }

    public Ruolo getRuoloByNome(String nome) {
        return ruoloDAO.getByNome(nome);
    }

    public boolean creaRuolo(String nome) {
        Ruolo ruolo = new Ruolo(nome);
        return ruoloDAO.save(ruolo);
    }
}