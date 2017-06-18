package json;

import java.util.List;

/**
 * Created by Arturo on 17-06-2017.
 */
public class MapData {

    private List<Integer> range;
    private List<CountryData> countryData;

    private MapData() {

    }

    public MapData(List<Integer> range, List<CountryData> countryData) {
        this.range = range;
        this.countryData = countryData;
    }

    public List<Integer> getRange() {
        return range;
    }

    public void setRange(List<Integer> range) {
        this.range = range;
    }

    public List<CountryData> getCountryData() {
        return countryData;
    }

    public void setCountryData(List<CountryData> countryData) {
        this.countryData = countryData;
    }
}
