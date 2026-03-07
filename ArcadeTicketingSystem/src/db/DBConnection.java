package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
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
