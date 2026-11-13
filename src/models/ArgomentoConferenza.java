package models;

public class ArgomentoConferenza {
    private int id;
    private int conferenzaId;
    private String nome;

    public ArgomentoConferenza(int id, int conferenzaId, String nome) {
        this.id = id;
        this.conferenzaId = conferenzaId;
        this.nome = nome;
    }
    public ArgomentoConferenza(int conferenzaId, String nome) {
        this(0, conferenzaId, nome);
    }
    public int getId() { return id; }
    public int getConferenzaId() { return conferenzaId; }
    public String getNome() { return nome; }
}