package ejb;

import facade.AbstractFacade;
import facade.FilmFacade;
import model.Film;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by arturo on 10-05-17.
 */

@Stateless
public class FilmFacadeEJB extends AbstractFacade<Film> implements FilmFacade {

    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public FilmFacadeEJB() {
        super(Film.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

}
