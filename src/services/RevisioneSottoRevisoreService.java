package services;

import java.util.List;

import data.dao.RevisioneSottoRevisoreDAO;
import models.Revisione;

public class RevisioneSottoRevisoreService {
   
    private final RevisioneSottoRevisoreDAO dao = new RevisioneSottoRevisoreDAO();

    public boolean salvaRevisione(int articoloId, int sottoRevisoreId, String commento, int rate) {
        return dao.salvaRevisione(articoloId, sottoRevisoreId, commento, rate);
    }


    public List<Revisione> getSottoRevisioniByArticolo(int articoloId) {
        return dao.getSottoRevisioniByArticolo(articoloId);
    }
}