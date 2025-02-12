package esprit.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection cnx;
    private static DatabaseConnection instance;

    public DatabaseConnection() {
        String url = "jdbc:mysql://localhost/signlearn"; // Modification ici
        String username = "root";
        String password = "";

        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
