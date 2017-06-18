package ejb;

import facade.AbstractFacade;
import facade.TweetCountFacade;
import java.math.BigDecimal;

import json.*;
import model.TweetCount;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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

    @Override
    public SentimentJson findSentiment(int filmId, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days - 1);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = em.createQuery("SELECT SUM(t.count), SUM(t.pos), SUM(t.neg) " +
                "FROM TweetCount t JOIN t.film f WHERE f.id = :id AND t.date BETWEEN :dateBegin AND :dateEnd ");

        query.setParameter("id", filmId);
        query.setParameter("dateBegin", formatedDateBegin, TemporalType.DATE);
        query.setParameter("dateEnd", formatedDateEnd, TemporalType.DATE);

        List<Object[]> objects = query.getResultList();

        if(!objects.isEmpty()) {
            Object[] obj = objects.get(0);

            long count = (long) obj[0];
            long posCount = (long) obj[1];
            long negCount = (long) obj[2];

            double pos = ((double) posCount) / count;
            double neg = ((double) negCount) / count;

            return new SentimentJson(filmId, pos, neg);
        }

        return null;
    }
    
    @Override
    public List<RankJson> findGenre(int amount, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days - 1);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = em.createNativeQuery(
                "SELECT conteo.genre_id, conteo.name, SUM(conteo.count) as tweets\n" +
                "FROM \n" +
                    "(SELECT date, count, genre_id, genre_films.name\n" +
                    "FROM tweet_counts tc\n" +
                    "JOIN\n" +
                        "(SELECT genre_id, film_id, name\n" +
                        "FROM genres g JOIN film_has_genre fg\n" +
                        "WHERE g.id = fg.genre_id\n" +
                        "GROUP BY genre_id, film_id, name) as genre_films\n" +
                    "WHERE tc.film_id = genre_films.film_id\n" +
                    "AND tc.date BETWEEN ?1 AND ?2 \n" +
                    "GROUP BY date, count, genre_id, genre_films.name) as conteo\n" +
                "GROUP BY conteo.genre_id\n" +
                "ORDER BY tweets DESC LIMIT ?3");
        
        
        
        query.setParameter(1, formatedDateBegin, TemporalType.DATE);
        query.setParameter(2, formatedDateEnd, TemporalType.DATE);
        query.setParameter(3, amount);
       
        List<Object[]> objects = query.getResultList();
       
        List<RankJson> rank = new ArrayList<>();
       
        for(Object[] obj : objects)
        {
            int id = ((Long)obj[0]).intValue();
            String name = (String) obj[1];
            long count = ((BigDecimal)obj[2]).longValueExact();
            rank.add(new RankJson(id, name, count));
        }
       
        return rank;
    }

    @Override
    public MapData findCountByCountry(int filmId, int days) {
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDate dateBegin = dateEnd.minusDays(days - 1);
        Date formatedDateBegin = Date.from(dateBegin.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formatedDateEnd = Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = em.createNativeQuery("SELECT twctry.country_id AS country_id, SUM(twctry.count) AS count \n" +
                "FROM films f\n" +
                "INNER JOIN tweet_counts twc ON f.id = twc.film_id\n" +
                "INNER JOIN tweets_countries twctry ON twctry.tweet_count_id = twc.id\n" +
                "WHERE f.id = ?1 AND twc.date BETWEEN ?2 AND ?3\n" +
                "GROUP BY twctry.country_id");

        query.setParameter(1, filmId);
        query.setParameter(2, formatedDateBegin, TemporalType.DATE);
        query.setParameter(3, formatedDateEnd, TemporalType.DATE);

        List<Object[]> objects = query.getResultList();

        if (objects.size() <= 0) {
            return null;
        }

        List<Integer> range;
        List<CountryData> countryData = new ArrayList<CountryData>();

        for(Object[] objs: objects) {
            String country_id = (String) objs[0];
            int count = ((BigDecimal) objs[1]).intValue();
            countryData.add(new CountryData(country_id, count));
        }

        //Empieza a calcular rangos y esas cosas
        int max = 0;
        for(CountryData cd: countryData) {
            if(cd.getCount() > max) {
                max = cd.getCount();
            }
        }

        if(max < 5) {
            range = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        } else {
            int divisions;
            int size = countryData.size();
            if(size < 5) {
                divisions = size;
            } else if (size <= 25) {
                divisions = 5;
            } else {
                divisions = ((Double) Math.sqrt((double) size)).intValue();
            }

            range = new ArrayList<Integer>();

            int dec = max / divisions;
            for(int i = 0; i < divisions; i++) {
                range.add(max - (dec * i));
            }

            Collections.reverse(range);

        }

        return new MapData(range, countryData);
    }

}
