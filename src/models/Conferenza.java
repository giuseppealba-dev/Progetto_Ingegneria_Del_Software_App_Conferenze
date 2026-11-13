package models;

import java.time.LocalDate;

public class Conferenza {
    
    private int id;
    private String titolo;
    private String descrizione;
    private LocalDate fineConferenza;
    private LocalDate scadenzaSubmission;
    private LocalDate scadenzaReview;
    private LocalDate scadenzaAcceptance;
    private LocalDate scadenzaFinalSubmission;
    private int minRevisori;
    private int maxArticoli;
    private double acceptanceRate;
    private String luogo;
    private String ora;
    private LocalDate data;
    private String topics;
    private int organizzatoreId;
    private boolean broadcastAttivo = false;

    // Costruttore con id
    public Conferenza(int id, String titolo, String descrizione, LocalDate fineConferenza,
                      LocalDate scadenzaSubmission, LocalDate scadenzaReview, LocalDate scadenzaAcceptance,
                      LocalDate scadenzaFinalSubmission, int minRevisori, int maxArticoli, double acceptanceRate,
                      String luogo, String ora, String topics,LocalDate data, int organizzatoreId, boolean broadcastAttivo) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.fineConferenza = fineConferenza;
        this.scadenzaSubmission = scadenzaSubmission;
        this.scadenzaReview = scadenzaReview;
        this.scadenzaAcceptance = scadenzaAcceptance;
        this.scadenzaFinalSubmission = scadenzaFinalSubmission;
        this.minRevisori = minRevisori;
        this.maxArticoli = maxArticoli;
        this.acceptanceRate = acceptanceRate;
        this.luogo = luogo;
        this.ora = ora;   
        this.topics = topics;
        this.data = data;
        this.organizzatoreId = organizzatoreId;  
        this.broadcastAttivo = broadcastAttivo;  
    }

    // Costruttore senza id (per inserimento)
    public Conferenza(String titolo, String descrizione, LocalDate fineConferenza,
                      LocalDate scadenzaSubmission, LocalDate scadenzaReview, LocalDate scadenzaAcceptance,
                      LocalDate scadenzaFinalSubmission, int minRevisori, int maxArticoli, double acceptanceRate,
                      String luogo, String ora, String topics,LocalDate data, int organizzatoreId, boolean broadcastAttivo) {
        this(0, titolo, descrizione,fineConferenza, scadenzaSubmission, scadenzaReview, scadenzaAcceptance,
             scadenzaFinalSubmission, minRevisori, maxArticoli, acceptanceRate, luogo, ora, topics,data, organizzatoreId,broadcastAttivo);
    }

    // Getters
    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public LocalDate getFineConferenza() {return fineConferenza;}
    public LocalDate getScadenzaSubmission() { return scadenzaSubmission; }
    public LocalDate getScadenzaReview() { return scadenzaReview; }
    public LocalDate getScadenzaAcceptance() { return scadenzaAcceptance; }
    public LocalDate getScadenzaFinalSubmission() { return scadenzaFinalSubmission; }
    public int getMinRevisori() { return minRevisori; }
    public int getMaxArticoli() { return maxArticoli; }
    public double getAcceptanceRate() { return acceptanceRate; }
    public String getLuogo() { return luogo; }
    public String getOra() { return ora; }
    public String getTopics() { return topics; }
    public LocalDate getData() { return data; }
    public int getOrganizzatoreId() { return organizzatoreId; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setFineConferenza(LocalDate fineConferenza){this.fineConferenza = fineConferenza;}
    public void setScadenzaSubmission(LocalDate scadenzaSubmission) { this.scadenzaSubmission = scadenzaSubmission; }
    public void setScadenzaReview(LocalDate scadenzaReview) { this.scadenzaReview = scadenzaReview; }
    public void setScadenzaAcceptance(LocalDate scadenzaAcceptance) { this.scadenzaAcceptance = scadenzaAcceptance; }
    public void setScadenzaFinalSubmission(LocalDate scadenzaFinalSubmission) { this.scadenzaFinalSubmission = scadenzaFinalSubmission; }
    public void setMinRevisori(int minRevisori) { this.minRevisori = minRevisori; }
    public void setMaxArticoli(int maxArticoli) { this.maxArticoli = maxArticoli; }
    public void setAcceptanceRate(double acceptanceRate) { this.acceptanceRate = acceptanceRate; }
    public void setLuogo(String luogo) { this.luogo = luogo; }
    public void setOra(String ora) { this.ora = ora; }
    public void setTopics(String topics) { this.topics = topics; }
    public void setOrganizzatoreId(int organizzatoreId) { this.organizzatoreId = organizzatoreId; }
    public void setData(LocalDate data) { this.data = data; }
    public boolean isBroadcastAttivo() {
        return broadcastAttivo;
    }

    public void setBroadcastAttivo(boolean broadcastAttivo) {
        this.broadcastAttivo = broadcastAttivo;
    }



}