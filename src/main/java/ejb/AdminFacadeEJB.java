package ejb;

import facade.AbstractFacade;
import facade.AdminFacade;
import java.util.List;
import model.Admin;

import javax.ejb.Stateless;
import javax.persistence.*;

/**
 * Created by arturo on 10-05-17.
 */


@Stateless
public class AdminFacadeEJB extends AbstractFacade<Admin> implements AdminFacade {

    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public AdminFacadeEJB() {
        super(Admin.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public Admin findByNameAndPass(String name, String pass) {
        Query query = em.createNativeQuery(
                    "SELECT *\n" +
                    "FROM admins \n" +
                    "WHERE username=?1 AND password=?2");
        
        query.setParameter(1, name);
        query.setParameter(2, pass);
        
        List<Object[]> objects = query.getResultList();
        
        Admin adminSearch = new Admin();
        
        if(objects.size() == 0)
        {
            adminSearch.setId(-1);
            adminSearch.setUsername("-");
            adminSearch.setPassword("-");
            adminSearch.setFirstName("-");
            adminSearch.setLastName("-");            
        }
        else
        {
            Object[] resultado = objects.get(0);
            adminSearch.setId(((Long)resultado[0]).intValue());
            adminSearch.setUsername((resultado[1]).toString());
            adminSearch.setPassword((resultado[2]).toString());
            adminSearch.setFirstName((resultado[3]).toString());
            adminSearch.setLastName((resultado[4]).toString());
        }
        return adminSearch;
    }

}
