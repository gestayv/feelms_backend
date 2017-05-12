package json;

import java.io.Serializable;

/**
 * Created by arturo on 11-05-17.
 */
public class RankJson implements Serializable {

    private int filmId;

    private String filmTitle;

    private int tweetCount;

    public RankJson() {

    }

    public RankJson(int filmId, String filmTitle, Long tweetCount) {
        this.filmTitle = filmTitle;
        this.filmId = filmId;
        this.tweetCount = tweetCount.intValue();
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
