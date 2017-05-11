package json;

import java.io.Serializable;

/**
 * Created by arturo on 11-05-17.
 */
public class TopTweetsJson implements Serializable {

    private int filmId;

    private String filmTitle;

    private int tweetCount;


    public TopTweetsJson(int film_id, String film_title, Long tweet_count) {
        this.filmId = film_id;
        this.filmTitle = film_title;
        this.tweetCount = tweet_count.intValue();
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
