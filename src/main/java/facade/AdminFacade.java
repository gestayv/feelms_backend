package facade;

import model.Admin;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Local
public interface AdminFacade {

    public void create(Admin entity);

    public void edit(Admin entity);

    public void remove(Admin entity);

    public Admin find(Object id);

    public List<Admin> findAll();

    public List<Admin> findRange(int[] range);

    public int count();

    public Admin findByNameAndPass(String name, String pass);
}
