package models;

import java.sql.Timestamp;

public class Offerta {
    private int id;
    private int idArticolo;
    private int idRevisore;
    private String stato;
    private Timestamp dataOfferta;

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdArticolo() { return idArticolo; }
    public void setIdArticolo(int idArticolo) { this.idArticolo = idArticolo; }
    public int getIdRevisore() { return idRevisore; }
    public void setIdRevisore(int idRevisore) { this.idRevisore = idRevisore; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public Timestamp getDataOfferta() { return dataOfferta; }
    public void setDataOfferta(Timestamp dataOfferta) { this.dataOfferta = dataOfferta; }
}