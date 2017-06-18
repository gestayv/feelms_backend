package json;

/**
 * Created by Arturo on 17-06-2017.
 */
public class CountryData {

    private String id;
    private int count;

    private CountryData() {

    }

    public CountryData(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
