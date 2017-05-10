package facade;

import model.Film;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Local
public interface FilmFacade {

    public void create(Film entity);

    public void edit(Film entity);

    public void remove(Film entity);

    public Film find(Object id);

    public List<Film> findAll();

    public List<Film> findRange(int[] range);

    public int count();


}
