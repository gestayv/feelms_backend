package json;

import java.io.Serializable;

/**
 * Created by arturo on 11-05-17.
 */
public class TopTweetsJson implements Serializable {

    private int id;

    private int filmId;

    private String filmTitle;

    private Long tweetCount;


    public TopTweetsJson(int film_id, String film_title, Long tweet_count) {
        this.filmId = film_id;
        this.filmTitle = film_title;
        this.tweetCount = tweet_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Long getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(Long tweetCount) {
        this.tweetCount = tweetCount;
    }
}
