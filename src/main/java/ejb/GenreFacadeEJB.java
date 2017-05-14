package ejb;

import facade.AbstractFacade;
import facade.GenreFacade;
import model.Genre;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class GenreFacadeEJB extends AbstractFacade<Genre> implements GenreFacade{
    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public GenreFacadeEJB() {
        super(Genre.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }    
}
