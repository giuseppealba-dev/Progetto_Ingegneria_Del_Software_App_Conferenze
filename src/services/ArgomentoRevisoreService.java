package services;

import data.dao.ArgomentoRevisoreDAO;
import java.util.List;

public class ArgomentoRevisoreService {
    private final ArgomentoRevisoreDAO dao = new ArgomentoRevisoreDAO();

    public List<Integer> getArgomentiByUserAndConferenza(int userId, int conferenzaId) {
        return dao.getArgomentiByUserAndConferenza(userId, conferenzaId);
    }
}