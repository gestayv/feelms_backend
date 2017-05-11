package json;

import java.io.Serializable;

/**
 * Created by arturo on 11-05-17.
 */
public class RankJson implements Serializable {

    private int filmId;

    private String filmTitle;

    private int tweetCount;


    public static RankJson createJson(TopTweetsJson top) {
        RankJson rank = new RankJson();
        rank.filmId = top.getFilmId();
        rank.filmTitle = top.getFilmTitle();
        rank.tweetCount = top.getTweetCount().intValue();

        return rank;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }
}
