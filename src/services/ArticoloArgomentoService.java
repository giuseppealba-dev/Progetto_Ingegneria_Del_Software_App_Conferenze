package services;

import data.dao.ArticoloArgomentoDAO;
import java.util.List;

public class ArticoloArgomentoService {
    private final ArticoloArgomentoDAO dao = new ArticoloArgomentoDAO();

    public List<Integer> getArgomentiByArticolo(int articoloId) {
        return dao.getArgomentiByArticolo(articoloId);
    }
}