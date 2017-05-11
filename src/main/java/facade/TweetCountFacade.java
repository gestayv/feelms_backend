package facade;

import json.RankJson;
import json.TopTweetsJson;
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

}
