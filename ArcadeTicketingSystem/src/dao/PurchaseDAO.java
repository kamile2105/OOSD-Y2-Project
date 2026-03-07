package dao;

import db.DBConnection;
import java.sql.*;

public class PurchaseDAO {

    // create (to redeem a prize)
    public void addPurchase(int customerId, int prizeId) {
        String sql = "INSERT INTO purchase (customer_id, prize_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, prizeId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read (inner join with multiple tables)
    public ResultSet getPurchaseHistory() {
        String sql = """
            SELECT 
                pu.purchase_id,
                c.name AS customer_name,
                pr.prize_name,
                pr.ticket_cost,
                pu.purchase_date
            FROM purchase pu
            INNER JOIN customer c ON pu.customer_id = c.customer_id
            INNER JOIN prize pr ON pu.prize_id = pr.prize_id
        """;

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            return st.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // delete
    public void deletePurchase(int purchaseId) {
        String sql = "DELETE FROM purchase WHERE purchase_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, purchaseId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}