package facade;

import java.util.List;
import javax.ejb.Local;
import model.KeyTerm;

@Local
public interface KeyTermFacade {
    
    public void create(KeyTerm entity);

    public void edit(KeyTerm entity);

    public void remove(KeyTerm entity);

    public KeyTerm find(Object id);

    public List<KeyTerm> findAll();

    public List<KeyTerm> findRange(int[] range);

    public int count();  
}
