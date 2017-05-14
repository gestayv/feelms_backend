package facade;

import java.util.List;
import javax.ejb.Local;
import model.Genre;

@Local
public interface GenreFacade {


    public void create(Genre entity);

    public void edit(Genre entity);

    public void remove(Genre entity);

    public Genre find(Object id);

    public List<Genre> findAll();

    public List<Genre> findRange(int[] range);

    public int count();    
}
