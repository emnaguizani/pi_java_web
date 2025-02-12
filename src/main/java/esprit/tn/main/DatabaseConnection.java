package esprit.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection cnx;

    // Informations de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/signlearn?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Constructeur privé pour empêcher l'instanciation multiple
    private DatabaseConnection() {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connexion à la base de données réussie.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Erreur : Driver MySQL introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données !");
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir l'instance unique (Singleton)
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Méthode pour récupérer la connexion
    public Connection getCnx() {
        return cnx;
    }

    // Méthode pour fermer la connexion proprement
    public void closeConnection() {
        if (cnx != null) {
            try {
                cnx.close();
                System.out.println("✅ Connexion fermée proprement.");
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors de la fermeture de la connexion !");
                e.printStackTrace();
            }
        }
    }
}
