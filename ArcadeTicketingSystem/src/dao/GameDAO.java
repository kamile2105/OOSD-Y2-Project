package dao;

import db.DBConnection;
import model.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public void addGame(Game g) {
        String sql = "INSERT INTO game (game_name, cost_per_play, tickets_rewarded) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, g.getGameName());
            ps.setInt(2, g.getCostPerPlay());
            ps.setInt(3, g.getTicketsRewarded());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM game";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Game g = new Game(
                        rs.getInt("game_id"),
                        rs.getString("game_name"),
                        rs.getInt("cost_per_play"),
                        rs.getInt("tickets_rewarded")
                );
                games.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public void updateGame(Game g) {
        String sql = "UPDATE game SET game_name=?, cost_per_play=?, tickets_rewarded=? WHERE game_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, g.getGameName());
            ps.setInt(2, g.getCostPerPlay());
            ps.setInt(3, g.getTicketsRewarded());
            ps.setInt(4, g.getGameId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGame(int gameId) {
        String sql = "DELETE FROM game WHERE game_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}