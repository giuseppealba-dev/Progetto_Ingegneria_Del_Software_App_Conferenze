package services;

import data.dao.CodiceRuoloDAO;

public class CodiceRuoloService {
    private final CodiceRuoloDAO dao = new CodiceRuoloDAO();

    public String getDescrizioneByCodice(String codice) {
        return dao.getDescrizioneByCodice(codice);
    }

    // Simulazione: salva l'ultimo codice per utente (email)
    private static final java.util.Map<String, String> ultimiCodici = new java.util.HashMap<>();
    public void setUltimoCodicePerUtente(String email, String codice) {
        if (codice == null) {
            ultimiCodici.remove(email);
        } else {
            ultimiCodici.put(email, codice);
        }
    }
    public String getUltimoCodicePerUtente(String email) {
        return ultimiCodici.get(email);
    }
}