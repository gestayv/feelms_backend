package ejb;

import facade.AbstractFacade;
import facade.DirectorFacade;
import model.Director;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DirectorFacadeEJB extends AbstractFacade<Director> implements DirectorFacade{
@PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public DirectorFacadeEJB() {
        super(Director.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }    
}
