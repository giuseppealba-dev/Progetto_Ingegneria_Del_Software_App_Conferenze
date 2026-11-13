package services;

import data.dao.SottoRevisioneDAO;
import java.util.List;

public class SottoRevisioneService {
    private final SottoRevisioneDAO dao = new SottoRevisioneDAO();

    public boolean assegnaSottoRevisore(int articoloId, int sottoRevisoreId, int revisoreId, int conferenzaId) {
        return dao.assegnaSottoRevisore(articoloId, sottoRevisoreId, revisoreId, conferenzaId);
    }

    public List<Integer> getArticoliAssegnatiASottoRevisore(int sottoRevisoreId, int conferenzaId) {
        return dao.getArticoliAssegnatiASottoRevisore(sottoRevisoreId, conferenzaId);
    }

    public boolean rinunciaSottoRevisione(int articoloId, int sottoRevisoreId) {
        return dao.rinunciaSottoRevisione(articoloId, sottoRevisoreId);
    }
}
