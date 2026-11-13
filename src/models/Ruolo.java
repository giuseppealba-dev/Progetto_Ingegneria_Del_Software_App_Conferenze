package models;

public class Ruolo {
    
    private int id;
    private String nome_ruolo;

    public Ruolo (int id, String nome_ruolo){
        this.id = id;
        this.nome_ruolo = nome_ruolo;
    }

    //costruttore senza id
    public Ruolo(String nome_ruolo){
        this(0, nome_ruolo);
    }


    //Getters
    public int getId(){ return id;}
    public String getNomeRuolo() {return nome_ruolo;}

    //Setters
    public void setId(int id){this.id = id;}
    public void setNomeRuolo(String nome_ruolo) {this.nome_ruolo = nome_ruolo;}



}
