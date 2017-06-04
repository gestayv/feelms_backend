package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by arturo on 11-05-17.
 */
@Entity
@Table(name = "tweet_counts")
@NamedQuery(name="TweetCount.findAll", query = "SELECT t FROM TweetCount t")
public class TweetCount implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "pos", nullable = false)
    private int pos;

    @Column(name = "neg", nullable = false)
    private int neg;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }
}
