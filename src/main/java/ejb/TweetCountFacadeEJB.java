package ejb;

import facade.AbstractFacade;
import facade.TweetCountFacade;
import json.TopTweetsJson;
import model.TweetCount;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    public List<TopTweetsJson> findTop(int amount, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());



        TypedQuery<TopTweetsJson> query = em.createQuery("" +
                "SELECT NEW json.TopTweetsJson(f.id, f.title, SUM(t.count))" +
                "FROM Film f JOIN f.tweetCounts t WHERE t.date BETWEEN :dateBegin AND :dateEnd GROUP BY f.id ORDER BY SUM(t.count) DESC",
                TopTweetsJson.class
        );

        query.setParameter("dateBegin", formatedDateBegin, TemporalType.DATE);
        query.setParameter("dateEnd", formatedDateEnd, TemporalType.DATE);

        List<TopTweetsJson> topTweetsJsons = query.getResultList();

        int size = topTweetsJsons.size();

        for(int i = 0; i < size; i++) {
            topTweetsJsons.get(i).setId(i);
        }

        /*
        Query query = em.createQuery("" +
                        "SELECT f.id, f.title, SUM(t.count)" +
                        "FROM Film f JOIN f.tweetCounts t WHERE t.date BETWEEN :dateBegin AND :dateEnd GROUP BY f.id ORDER BY SUM(t.count) DESC",
                TopTweetsJson.class
        );

        query.setParameter("dateBegin", formatedDateBegin, TemporalType.DATE);
        query.setParameter("dateEnd", formatedDateEnd, TemporalType.DATE);

        List<Object[]> objects = query.getResultList();
        */

        return topTweetsJsons;
    }
}
