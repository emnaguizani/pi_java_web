package esprit.tn.services;

import java.util.List;

public interface Iservice <T>{

    public void ajouter(T t);
    public void modifier(T t);

    public void supprimer(T t);

    public List<T> getall();

    public T getone();

}
