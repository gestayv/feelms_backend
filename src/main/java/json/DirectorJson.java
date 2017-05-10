package json;


import model.Director;
import model.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */
public class DirectorJson {

    private int id;

    private String firstName;

    private String lastName;

    private List<FilmJson> films = null;


    public static DirectorJson createJson(Director director) {
        DirectorJson directorJson = DirectorJson.createPartialJson(director);

        directorJson.films = new ArrayList<FilmJson>();
        List<Film> orgFilms = director.getFilms();

        for(Film film: orgFilms) {
            directorJson.films.add(FilmJson.createPartialJson(film));
        }

        return directorJson;
    }

    public static DirectorJson createPartialJson(Director director) {
        DirectorJson directorJson = new DirectorJson();

        directorJson.id = director.getId();
        directorJson.firstName = director.getFirstName();
        directorJson.lastName = director.getLastName();

        return directorJson;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<FilmJson> getFilms() {
        return films;
    }

    public void setFilms(List<FilmJson> films) {
        this.films = films;
    }
}
