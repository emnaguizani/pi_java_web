package esprit.tn.service;

import esprit.tn.entities.Cours;

import java.util.List;

public interface CourServiceInterface<T> {
    public void add(T t);

    public List<T> findAll();
    public T findById(int id);
    public void delete(int id);
    public void update(T t);
}
