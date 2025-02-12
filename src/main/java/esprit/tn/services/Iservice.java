package esprit.tn.services;

import java.util.List;

public interface Iservice<T> {
    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    List<T> getAll();
    T getone(int id);  // Modifier ici pour accepter un paramètre d'ID
}

