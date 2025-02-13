package esprit.tn.service;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Niveau;
import esprit.tn.entities.Status;
import esprit.tn.main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourService implements CourServiceInterface<Cours> {

    Connection con ;
    public CourService() {
        con= DatabaseConnection.getInstance().getCon();
    }
    @Override
    public void add(Cours cours) {

        String req="INSERT INTO  cours ( title, description,date_creation,formateur_id,statut,niveau,video_paths) VALUES (?,?,?,?,?,?,?)";


        try{
            PreparedStatement stm= con.prepareStatement(req);
            stm.setString(1, cours.getTitle());
            stm.setString(2, cours.getDescription());
            stm.setString(3, cours.getDateCreation());
            stm.setInt(4, cours.getIdFormateur());
            stm.setString(5, cours.getStatus().name());
            stm.setString(6, cours.getNiveau().name());
            stm.setString(7, String.join(",", cours.getVideoPaths()));
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public List<Cours> findAll() {
        List<Cours> courses=new ArrayList<>();
        String req="SELECT * FROM cours";


        try {
            PreparedStatement stm= con.prepareStatement(req);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                Cours crs=new Cours();
                crs.setIdCours(rs.getInt("id_cours"));
                crs.setTitle(rs.getString("title"));
                crs.setDescription(rs.getString("description"));
                crs.setDateCreation(rs.getString("date_creation"));
                crs.setIdFormateur(rs.getInt("formateur_id"));
                crs.setStatus(Status.valueOf(rs.getString("statut")));
                crs.setNiveau(Niveau.valueOf(rs.getString("niveau")));
                crs.setVideoPaths(Collections.singletonList(rs.getString("video_paths")));
                courses.add(crs);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return courses;

    }

    @Override
    public Cours findById(int id) {
        List<Cours> courses=new ArrayList<>();
        String req="SELECT * FROM cours WHERE id_cours = ?";

        try {
            PreparedStatement stm= con.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                Cours crs=new Cours();
                crs.setIdCours(rs.getInt("id_cours"));
                crs.setTitle(rs.getString("title"));
                crs.setDescription(rs.getString("description"));
                crs.setDateCreation(rs.getString("date_creation"));
                crs.setIdFormateur(rs.getInt("formateur_id"));
                crs.setStatus(Status.valueOf(rs.getString("statut")));
                crs.setNiveau(Niveau.valueOf(rs.getString("niveau")));
                crs.setVideoPaths(Collections.singletonList(rs.getString("video_paths")));
                return crs;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        String req="DELETE FROM cours WHERE id_cours = ?";

        try {
            PreparedStatement stm= con.prepareStatement(req);

            stm.setInt(1, id);
            stm.executeUpdate();
            System.out.println("Course with ID " + id + " deleted successfully.");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void update(Cours cours) {
        String req = "UPDATE cours SET title = ?, description = ?, date_creation = ?, formateur_id = ?, statut = ?, niveau = ?, video_paths = ? WHERE id_cours= ?";

        try {
            PreparedStatement stm = con.prepareStatement(req);
            stm.setString(1, cours.getTitle());
            stm.setString(2, cours.getDescription());
            stm.setString(3, cours.getDateCreation());
            stm.setInt(4, cours.getIdFormateur());
            stm.setString(5, cours.getStatus().name());
            stm.setString(6, cours.getNiveau().name());
            stm.setString(7, String.join(",", cours.getVideoPaths()));
            stm.setInt(8, cours.getIdCours());

            stm.executeUpdate();

                System.out.println("Course with ID " + cours.getIdCours() + " updated successfully.");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
