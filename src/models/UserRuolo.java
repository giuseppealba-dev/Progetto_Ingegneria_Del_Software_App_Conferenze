package models;

/**
 * Modello per la tabella user_ruolo.
 */
public class UserRuolo {
    private int userId;
    private int ruoloId;
    private int conferenzaId;

    public UserRuolo(int userId, int ruoloId, int conferenzaId) {
        this.userId = userId;
        this.ruoloId = ruoloId;
        this.conferenzaId = conferenzaId;
    }


    //Getters
    public int getUserId() { return userId; }
    public int getRuoloId() { return ruoloId; }
    public int getConferenzaId() { return conferenzaId; }


    //Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setRuoloId(int ruoloId) { this.ruoloId = ruoloId; }
    public void setConferenzaId(int conferenzaId) { this.conferenzaId = conferenzaId; }
}