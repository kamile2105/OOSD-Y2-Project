// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

// This is the database connection file
package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            // all the details to successfully connect to my database
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/arcade_ticketing",
                    "root",
                    "Kamilyte21!DL"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
