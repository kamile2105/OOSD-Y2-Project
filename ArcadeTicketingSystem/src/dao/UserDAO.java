// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package dao;

import db.DBConnection;
import java.sql.*;

/**
 * Data Access Object (DAO) for the Staff/User table.
 * Handles staff account registration and login authentication.
 */

public class UserDAO {

    /**
     * Registers a new staff member by inserting their username and password into the database.
     * @param user The username for the new staff account.
     * @param pass The password for the new staff account.
     */
    public void registerUser(String user, String pass) {
        String sql = "INSERT INTO staff (username, password) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates a staff login by checking if a matching username/password pair exists in the database.
     * @param user The username entered at login.
     * @param pass The password entered at login.
     * @return true if a matching record is found (login successful), false otherwise.
     */
    public boolean loginUser(String user, String pass) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if at least one matching row is found

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false on error to prevent unauthorised access
        }
    }
}