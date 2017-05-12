package ejb;

import facade.AbstractFacade;
import facade.TweetCountFacade;
import json.CountJson;
import json.RankJson;
import model.TweetCount;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arturo on 11-05-17.
 */

@Stateless
public class TweetCountFacadeEJB extends AbstractFacade<TweetCount> implements TweetCountFacade {

    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public TweetCountFacadeEJB() {
        super(TweetCount.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }


    @Override
    public List<RankJson> findTop(int amount, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days - 1);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = em.createQuery("" +
                        "SELECT f.id, f.title, SUM(t.count)" +
                        "FROM Film f JOIN f.tweetCounts t " +
                        "WHERE t.date BETWEEN :dateBegin AND :dateEnd " +
                        "GROUP BY f.id ORDER BY SUM(t.count) DESC"
        );

        query.setParameter("dateBegin", formatedDateBegin, TemporalType.DATE);
        query.setParameter("dateEnd", formatedDateEnd, TemporalType.DATE);

        query.setMaxResults(amount);

        List<Object[]> objects = query.getResultList();

        List<RankJson> rank = new ArrayList<RankJson>();

        for(Object[] obj: objects) {
            RankJson r = new RankJson((int) obj[0], (String) obj[1], (Long) obj[2]);
            rank.add(r);
        }

        return rank;
    }

    @Override
    public List<CountJson> findCount(int filmId, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days - 1);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = em.createQuery(
                "SELECT t.count, t.date FROM TweetCount t JOIN t.film f " +
                        "WHERE f.id = :id AND t.date BETWEEN :dateBegin AND :dateEnd " +
                        "ORDER BY t.date");

        query.setParameter("id", filmId);
        query.setParameter("dateBegin", formatedDateBegin, TemporalType.DATE);
        query.setParameter("dateEnd", formatedDateEnd, TemporalType.DATE);

        List<Object[]> objects = query.getResultList();

        List<CountJson> countJsons = new ArrayList<CountJson>();

        for(Object[] obj: objects) {
            countJsons.add(new CountJson((int) obj[0], (Date) obj[1]));
        }

        return countJsons;
    }


}
