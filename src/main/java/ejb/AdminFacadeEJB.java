package ejb;

import facade.AbstractFacade;
import facade.AdminFacade;
import model.Admin;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
