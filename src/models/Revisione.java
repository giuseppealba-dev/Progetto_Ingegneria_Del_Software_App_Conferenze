package models;

import java.util.Date;

public class Revisione {
    private int id;
    private int idArticolo;
    private int idRevisore;
    private String stato;
    private String commento;
    private String commentoChair;
    private int rate;
    private Date data;
    private String nomeRevisore;
    private int visibileAutore;

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdArticolo() { return idArticolo; }
    public void setIdArticolo(int idArticolo) { this.idArticolo = idArticolo; }
    
    public int getIdRevisore() { return idRevisore; }
    public void setIdRevisore(int idRevisore) { this.idRevisore = idRevisore; }
    
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    
    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }
    
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    
    public String getNomeRevisore() { return nomeRevisore; }
    public void setNomeRevisore(String nomeRevisore) { this.nomeRevisore = nomeRevisore; }

    public String getCommentoChair() {return commentoChair;}
    public void setCommentoChair(String commentoChair) {this.commentoChair = commentoChair;}

    public int getRate() {return rate;}
    public void setRate(int rate) {this.rate = rate;}

    public int getVisibileAutore() { return visibileAutore; }
    public void setVisibileAutore(int visibileAutore) { this.visibileAutore = visibileAutore; }
}