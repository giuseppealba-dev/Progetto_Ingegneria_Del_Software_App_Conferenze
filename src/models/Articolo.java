package models;

public class Articolo {
    private int id;
    private int idAutore;
    private int idConferenza;
    private String titolo;
    private String file;
    private String stato;

    public Articolo(int id, int idAutore, int idConferenza, String titolo, String file) {
        this.id = id;
        this.idAutore = idAutore;
        this.idConferenza = idConferenza;
        this.titolo = titolo;
        this.file = file;
    }

    public Articolo(int idAutore, int idConferenza, String titolo, String file) {
        this(0, idAutore, idConferenza, titolo, file);
    }

    public Articolo(int id, int idAutore, int idConferenza, String titolo, String file, String stato) {
        this.id = id;
        this.idAutore = idAutore;
        this.idConferenza = idConferenza;
        this.titolo = titolo;
        this.file = file;
        this.stato = stato;
    }


    public Articolo() {
    // Costruttore vuoto
}


    //Getters
    public int getId() { return id; }
    public int getIdAutore() { return idAutore; }
    public int getIdConferenza() { return idConferenza; }
    public String getTitolo() { return titolo; }
    public String getFile() { return file; }
    public String getStato() { return stato; }

    //Setters
    public void setId(int id){this.id = id;}
    public void setIdAutore(int idAutore){this.idAutore = idAutore;}
    public void setIdConferenza(int idConferenza){this.idConferenza = idConferenza;}
    public void setTitolo(String titolo){this.titolo = titolo;}
    public void setFile(String file){this.file = file;}
    public void setStato(String stato) { this.stato = stato; }
    
}