package esprit.tn.services;

import esprit.tn.entities.Reclamation;

import java.util.List;

public interface IserviceR<T>
{
    // interface pour la gestion des reclamations
    public void ajouter(T t);
    public void modifier(T t);
    public void supprimer(T t);
    public List<T> getall();
    public List<Reclamation> rechercherRec(String critere);
    //public void trier(T t);
}
