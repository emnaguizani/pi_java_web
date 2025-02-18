package esprit.tn.services;

import java.util.List;

public interface IserviceF<T>
{
    public void ajouterF(T t);
    public void modifierF(T t);
    public void supprimerF(T t);
    public List<T> getallF();
}
