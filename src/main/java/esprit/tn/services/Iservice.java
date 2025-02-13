package esprit.tn.services;

import java.util.List;

public interface Iservice<T> {
    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t); // Vérifiez bien que supprimer attend un paramètre de type T
    List<T> getAll();
    T getone(int id);
}


