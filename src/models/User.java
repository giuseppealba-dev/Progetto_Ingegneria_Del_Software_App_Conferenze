package models;

import java.util.List;

/**
 * Modello User che rappresenta la tabella users.
 */
public class User {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash; // Salva solo l'hash della password
    private String ruolo;

    // Costruttore completo
    public User(int id, String nome, String cognome, String email, String passwordHash, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
    }

    // Costruttore senza id (per inserimento)
    public User(String nome, String cognome, String email, String passwordHash, String ruolo) {
        this(0, nome, cognome, email, passwordHash, ruolo);
    }

    // Costruttore vuoto richiesto per JDBC e DAO
    public User() {
        
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRuolo() { return ruolo; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}