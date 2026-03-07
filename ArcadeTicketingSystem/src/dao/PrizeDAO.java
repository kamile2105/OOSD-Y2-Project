package dao;

import db.DBConnection;
import model.Prize;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrizeDAO {

    public void addPrize(Prize p) {
        String sql = "INSERT INTO prize (prize_name, ticket_cost, stock) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getPrizeName());
            ps.setInt(2, p.getTicketCost());
            ps.setInt(3, p.getStock());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Prize> getAllPrizes() {
        List<Prize> prizes = new ArrayList<>();
        String sql = "SELECT * FROM prize";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Prize p = new Prize(
                        rs.getInt("prize_id"),
                        rs.getString("prize_name"),
                        rs.getInt("ticket_cost"),
                        rs.getInt("stock")
                );
                prizes.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prizes;
    }

    public void updatePrize(Prize p) {
        String sql = "UPDATE prize SET prize_name=?, ticket_cost=?, stock=? WHERE prize_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getPrizeName());
            ps.setInt(2, p.getTicketCost());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getPrizeId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePrize(int prizeId) {
        String sql = "DELETE FROM prize WHERE prize_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prizeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}