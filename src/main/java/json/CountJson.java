package json;

import java.util.Date;

/**
 * Created by arturo on 11-05-17.
 */
public class CountJson {

    private int count;

    private Date date;

    public CountJson() {

    }

    public CountJson(int count, Date date) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
