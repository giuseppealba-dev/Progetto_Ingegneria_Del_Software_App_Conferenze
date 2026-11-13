package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:app.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Database connesso: " + new java.io.File("app.db").getAbsolutePath());
            initializeDatabase(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    // Ogni volta che serve una connessione, ne crea una nuova
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void initializeDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // 1. USERS
            String sqlUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    cognome TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    ruolo TEXT NOT NULL DEFAULT 'Autore'
                );
            """;
            stmt.execute(sqlUsers);

            // 2. Ruolo (definizione ruoli)
            String sqlRuolo = """
                CREATE TABLE IF NOT EXISTS ruolo (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL UNIQUE
                );
            """;
            stmt.execute(sqlRuolo);

            // Inserimento ruoli di base se non ci sono
            String sqlSeedRoles = """
                INSERT OR IGNORE INTO ruolo(nome) VALUES
                  ('Autore'),
                  ('Revisore'),
                  ('Chair'),
                  ('Sotto-Revisore'),
                  ('Editor');
            """;
            stmt.execute(sqlSeedRoles);

            // 3. USER_RUOLO (chiave primaria composta)
            String sqlUserRuolo = """
                CREATE TABLE IF NOT EXISTS user_ruolo (
                    user_id INTEGER NOT NULL,
                    ruolo_id INTEGER NOT NULL,
                    conferenza_id INTEGER NOT NULL,
                    PRIMARY KEY(user_id, ruolo_id, conferenza_id),
                    FOREIGN KEY(user_id) REFERENCES users(id),
                    FOREIGN KEY(ruolo_id) REFERENCES ruolo(id),
                    FOREIGN KEY(conferenza_id) REFERENCES conferenze(id)
                );
            """;
            stmt.execute(sqlUserRuolo);

            // 4. CONFERENZE
            String sqlConferenze = """
                CREATE TABLE IF NOT EXISTS conferenze (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titolo TEXT NOT NULL,
                    descrizione TEXT,
                    fine_conferenza DATE,
                    scadenza_submission DATE NOT NULL,
                    scadenza_review DATE NOT NULL,
                    scadenza_acceptance DATE NOT NULL,
                    scadenza_final_submission DATE NOT NULL,
                    min_revisori INTEGER NOT NULL,
                    max_articoli INTEGER NOT NULL,
                    acceptance_rate REAL NOT NULL,
                    luogo TEXT NOT NULL,
                    ora TEXT NOT NULL,
                    topics TEXT NOT NULL,
                    data DATE,
                    broadcast_attivo BOOLEAN DEFAULT FALSE,
                    organizzatore_id INTEGER NOT NULL,
                    FOREIGN KEY (organizzatore_id) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlConferenze);


             // 5. CODICE RUOLO
            String sqlCodiceRuolo = """
                CREATE TABLE IF NOT EXISTS codiceRuolo (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    codice TEXT NOT NULL UNIQUE,
                    descrizione TEXT NOT NULL
                );
            """;
            stmt.execute(sqlCodiceRuolo);

            // Inserimento codici di base se non ci sono
            String sqlSeedCodici = """
                INSERT OR IGNORE INTO codiceRuolo(codice, descrizione) VALUES
                ('C', 'Chair'),
                ('R', 'Revisore'),
                ('E', 'Editor'),
                ('S', 'Sotto-Revisore');
            """;
            stmt.execute(sqlSeedCodici);


            // 6. ARGOMENTI CONFERENZA
            String sqlArgomentiConferenza = """
                CREATE TABLE IF NOT EXISTS argomenti_conferenza (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    conferenza_id INTEGER NOT NULL,
                    nome TEXT NOT NULL,
                    FOREIGN KEY (conferenza_id) REFERENCES conferenze(id)
                );
            """;
            stmt.execute(sqlArgomentiConferenza);

            // 7. ARGOMENTI REVISORE
            String sqlArgomentiRevisore = """
                CREATE TABLE IF NOT EXISTS argomenti_revisore (
                    user_id INTEGER NOT NULL,
                    conferenza_id INTEGER NOT NULL,
                    argomento_id INTEGER NOT NULL,
                    PRIMARY KEY (user_id, conferenza_id, argomento_id),
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (conferenza_id) REFERENCES conferenze(id),
                    FOREIGN KEY (argomento_id) REFERENCES argomenti_conferenza(id)
                );
            """;
            stmt.execute(sqlArgomentiRevisore);

            // 8.tabella articoli:
            String sqlArticoli = """
                CREATE TABLE IF NOT EXISTS articoli (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_autore INTEGER NOT NULL,
                    id_conferenza INTEGER NOT NULL,
                    titolo TEXT NOT NULL,
                    file TEXT NOT NULL,
                    stato TEXT,
                    FOREIGN KEY (id_autore) REFERENCES users(id),
                    FOREIGN KEY (id_conferenza) REFERENCES conferenze(id)
                );
            """;
            stmt.execute(sqlArticoli);

            try {
                stmt.execute("ALTER TABLE articoli ADD COLUMN stato TEXT");
            } catch (SQLException e) {
                // Ignora errore se la colonna esiste già
            }

            //9.articolo_argomento
            String sqlArticoloArgomento = """
                CREATE TABLE IF NOT EXISTS articolo_argomento (
                articolo_id INTEGER NOT NULL,
                argomento_id INTEGER NOT NULL,
                PRIMARY KEY (articolo_id, argomento_id),
                FOREIGN KEY (articolo_id) REFERENCES articoli(id),
                FOREIGN KEY (argomento_id) REFERENCES argomenti_conferenza(id)
            );
            """;
            stmt.execute(sqlArticoloArgomento);



            // 10.Tabella assegnazioni_revisori: collega articoli e revisori
            String sqlAssegnazioniRevisori = """
                CREATE TABLE IF NOT EXISTS assegnazioni_revisori (
                    id_articolo INTEGER NOT NULL,
                    id_revisore INTEGER NOT NULL,
                    stato TEXT NOT NULL,
                    PRIMARY KEY (id_articolo, id_revisore),
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_revisore) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlAssegnazioniRevisori);

            //11.revisioni
            String sqlRevisioni = """
                CREATE TABLE IF NOT EXISTS revisioni (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_articolo INTEGER NOT NULL,
                    id_revisore INTEGER NOT NULL,
                    stato TEXT NOT NULL,
                    commento TEXT,
                    commento_chair TEXT,
                    rate INTEGER,
                    data DATE,
                    visibile_autore INTEGER DEFAULT 0,
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_revisore) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlRevisioni);

            
            //12.offerte
            String sqlOfferte = """
                CREATE TABLE IF NOT EXISTS offerte (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_articolo INTEGER NOT NULL,
                    id_revisore INTEGER NOT NULL,
                    stato TEXT NOT NULL CHECK(stato IN ('inviata', 'accettata', 'rifiutata')),
                    data_offerta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE(id_articolo, id_revisore),
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_revisore) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlOfferte);


            // 13. Tabella sottorevisioni
            String sqlSottoRevisioni = """
                CREATE TABLE IF NOT EXISTS sottorevisioni (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_articolo INTEGER NOT NULL,
                    id_sottorevisore INTEGER NOT NULL,
                    id_revisore INTEGER NOT NULL,
                    id_conferenza INTEGER NOT NULL,
                    stato TEXT DEFAULT 'assegnata',
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_sottorevisore) REFERENCES users(id),
                    FOREIGN KEY (id_revisore) REFERENCES users(id),
                    FOREIGN KEY (id_conferenza) REFERENCES conferenze(id)
                );
            """;
            stmt.execute(sqlSottoRevisioni);


            // 14. Tabella revisioni_sottorevisore
            String sqlRevisioniSottoRevisore = """
                CREATE TABLE IF NOT EXISTS revisioni_sottorevisore (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_articolo INTEGER NOT NULL,
                    id_sottorevisore INTEGER NOT NULL,
                    commento TEXT,
                    rate INTEGER,
                    data DATE DEFAULT CURRENT_DATE,
                    stato TEXT DEFAULT 'inviata',
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_sottorevisore) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlRevisioniSottoRevisore);



            //15. Tabella paper inviati all'editor
            String sqlPaperFinaliEditor = """
                CREATE TABLE IF NOT EXISTS paper_finali_editor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_articolo INTEGER NOT NULL,
                    id_editor INTEGER NOT NULL,
                    data_invio DATE DEFAULT CURRENT_DATE,
                    stato TEXT DEFAULT 'inviato',
                    FOREIGN KEY (id_articolo) REFERENCES articoli(id),
                    FOREIGN KEY (id_editor) REFERENCES users(id)
                );
            """;
            stmt.execute(sqlPaperFinaliEditor);

            System.out.println("Tabelle create o verificate correttamente.");
        }
    }

    // Non serve più mantenere una connessione globale, ma puoi comunque fornire questo metodo se vuoi chiudere connessioni manuali
    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}