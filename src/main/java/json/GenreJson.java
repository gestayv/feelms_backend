package json;

import model.Film;
import model.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */
public class GenreJson implements Serializable {

    private int id;

    private String name;

    private List<FilmJson> films = null;

    public static GenreJson createJson(Genre genre) {
        GenreJson genreJson = createPartialJson(genre);

        genreJson.films = new ArrayList<FilmJson>();
        List<Film> orgFilms = genre.getFilms();

        for(Film film: orgFilms) {
            genreJson.films.add(FilmJson.createPartialJson(film));
        }

        return genreJson;
    }

    public static GenreJson createPartialJson(Genre genre) {
        GenreJson genreJson = new GenreJson();
        genreJson.id = genre.getId();
        genreJson.name = genre.getName();

        return genreJson;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilmJson> getFilms() {
        return films;
    }

    public void setFilms(List<FilmJson> films) {
        this.films = films;
    }
}
