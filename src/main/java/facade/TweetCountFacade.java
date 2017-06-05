package facade;

import json.CountJson;
import json.RankJson;
import json.SentimentJson;
import model.TweetCount;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by arturo on 11-05-17.
 */

@Local
public interface TweetCountFacade {

    public void create(TweetCount entity);

    public void edit(TweetCount entity);

    public void remove(TweetCount entity);

    public TweetCount find(Object id);

    public List<TweetCount> findAll();

    public List<TweetCount> findRange(int[] range);

    public int count();

    //Especial para Ranking
    public List<RankJson> findTop(int amount, int days);

    //Especial para tomar listado de conteos en un rango de dias
    public List<CountJson> findCount(int filmId, int days);

    public SentimentJson findSentiment(int filmId, int days);
    
    public List<RankJson> findGenre(int amount, int days);
}
