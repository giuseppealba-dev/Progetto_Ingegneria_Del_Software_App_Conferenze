package models;

import java.time.LocalDate;

public class PaperFinaleEditor {
    private int id;
    private int idArticolo;
    private int idEditor;
    private LocalDate dataInvio;
    private String stato;

    // Costruttore completo (usato per oggetti letti dal DB)
    public PaperFinaleEditor(int id, int idArticolo, int idEditor, LocalDate dataInvio, String stato) {
        this.id = id;
        this.idArticolo = idArticolo;
        this.idEditor = idEditor;
        this.dataInvio = dataInvio;
        this.stato = stato;
    }

    // Costruttore per inserimento: dataInvio = oggi, stato = "inviato" di default
    public PaperFinaleEditor(int idArticolo, int idEditor) {
        this.idArticolo = idArticolo;
        this.idEditor = idEditor;
        this.dataInvio = LocalDate.now();
        this.stato = "inviato";
    }

    // Getters
    public int getId() { return id; }
    public int getIdArticolo() { return idArticolo; }
    public int getIdEditor() { return idEditor; }
    public LocalDate getDataInvio() { return dataInvio; }
    public String getStato() { return stato; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdArticolo(int idArticolo) { this.idArticolo = idArticolo; }
    public void setIdEditor(int idEditor) { this.idEditor = idEditor; }
    public void setDataInvio(LocalDate dataInvio) { this.dataInvio = dataInvio; }
    public void setStato(String stato) { this.stato = stato; }
}
