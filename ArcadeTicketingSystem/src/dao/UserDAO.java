package dao;

import db.DBConnection;
import java.sql.*;

public class UserDAO {

    // create/ register a new user
    public void registerUser(String user, String pass) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        // update to db connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read
    public boolean loginUser(String user, String pass) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // returns true if match found

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}