package ejb;

import facade.AbstractFacade;
import facade.FilmFacade;
import model.Film;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

    @Override
    public List<Film> findList(List<Integer> list) {
        TypedQuery<Film> query = em.createQuery("SELECT f FROM " +
                "Film f WHERE f.id IN :ids", Film.class);
        query.setParameter("ids", list);

        return query.getResultList();
    }
}
