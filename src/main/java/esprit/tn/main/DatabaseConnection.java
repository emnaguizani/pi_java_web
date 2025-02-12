package esprit.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    Connection con;
    public static DatabaseConnection instance ;
    public DatabaseConnection(){
        String Url="jdbc:mysql://localhost/signlearn";
        String Username="root";
        String Password="";
        try {
            con= DriverManager.getConnection(Url,Username,Password);
            System.out.println("Connextion etablie");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance= new DatabaseConnection();
        }
        return instance;
    }

    public Connection getCon() {
        return con;
    }
}
