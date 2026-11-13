package utils;

import models.Articolo;
import models.Conferenza;
import models.User;
import models.Revisione;

public class SessionManager {
    private static User loggedUser = null;
    private static Conferenza conferenzaSelezionata = null;
    private static String ruoloCorrenteUtente = null;
    private static Articolo articoloSelezionato;
    private static Revisione revisioneSelezionata;

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static Conferenza getConferenzaSelezionata() {
        return conferenzaSelezionata;
    }

    public static void setConferenzaSelezionata(Conferenza conf) {
        conferenzaSelezionata = conf;
        ruoloCorrenteUtente = null; // resetta il ruolo corrente quando cambi conferenza
    }

    public static String getRuoloCorrenteUtente() {
        return ruoloCorrenteUtente;
    }

    public static void setRuoloCorrenteUtente(String ruolo) {
        ruoloCorrenteUtente = ruolo;
    }

    public static void setArticoloSelezionato(Articolo art) {
        articoloSelezionato = art;
    }
    public static Articolo getArticoloSelezionato() {
        return articoloSelezionato;
    }
    public static void setRevisioneSelezionata(Revisione rev) {
        revisioneSelezionata = rev;
    }
    public static Revisione getRevisioneSelezionata() {
        return revisioneSelezionata;
    }

    public static void clearSession() {
        loggedUser = null;
        conferenzaSelezionata = null;
        ruoloCorrenteUtente = null;
    }
}