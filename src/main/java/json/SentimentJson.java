package json;

/**
 * Created by arturo on 04-06-17.
 */
public class SentimentJson {

    private int filmId;
    private double pos;
    private double neg;

    private SentimentJson() {

    }

    public SentimentJson(int filmId, double pos, double neg) {
        this.filmId = filmId;
        this.pos = pos;
        this.neg = neg;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public double getPos() {
        return pos;
    }

    public void setPos(double pos) {
        this.pos = pos;
    }

    public double getNeg() {
        return neg;
    }

    public void setNeg(double neg) {
        this.neg = neg;
    }
}
