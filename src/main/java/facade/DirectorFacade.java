package facade;

import java.util.List;
import javax.ejb.Local;
import model.Director;


@Local
public interface DirectorFacade {
    public void create(Director entity);

    public void edit(Director entity);

    public void remove(Director entity);

    public Director find(Object id);

    public List<Director> findAll();

    public List<Director> findRange(int[] range);

    public int count();
}
