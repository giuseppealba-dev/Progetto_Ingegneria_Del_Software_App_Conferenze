package data.dao;

import utils.DatabaseManager;
import java.sql.*;

public class CodiceRuoloDAO {
    public String getDescrizioneByCodice(String codice) {
        String sql = "SELECT descrizione FROM codiceRuolo WHERE codice = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codice);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("descrizione");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
