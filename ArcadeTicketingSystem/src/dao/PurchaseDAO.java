// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package dao;

import db.DBConnection;
import java.sql.*;

/**
 * Data Access Object (DAO) for the Purchase table.
 * Handles recording prize redemptions and retrieving purchase history.
 */
public class PurchaseDAO {

    /**
     * Records a new prize redemption (purchase) in the database.
     * Called when a customer spends their tickets to claim a prize.
     *
     * @param customerId The ID of the customer redeeming the prize.
     * @param prizeId    The ID of the prize being redeemed.
     */
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

    /**
     * Retrieves the full purchase history using an INNER JOIN across three tables.
     * Joins purchase → customer and purchase → prize to get readable names rather than just IDs.
     * @return A ResultSet containing purchase_id, customer_name, prize_name, ticket_cost,
     *         and purchase_date. Returns null if an error occurs.
     */
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


    /**
     * Deletes a purchase record from the database by its ID.
     * Can be used by staff to reverse or remove an incorrect redemption.
     *
     * @param purchaseId The purchase_id of the record to remove.
     */
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