package data.dao;

import models.ArgomentoConferenza;
import utils.DatabaseManager;
import java.sql.*;
import java.util.*;

public class ArgomentoConferenzaDAO {
    public boolean save(ArgomentoConferenza arg) {
        String sql = "INSERT INTO argomenti_conferenza (conferenza_id, nome) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, arg.getConferenzaId());
            stmt.setString(2, arg.getNome());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ArgomentoConferenza> getByConferenzaId(int conferenzaId) {
        List<ArgomentoConferenza> lista = new ArrayList<>();
        String sql = "SELECT * FROM argomenti_conferenza WHERE conferenza_id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new ArgomentoConferenza(
                    rs.getInt("id"),
                    rs.getInt("conferenza_id"),
                    rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}