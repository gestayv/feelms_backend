package json;

import model.Admin;
import model.Film;
import model.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */
public class AdminJson implements Serializable {

    private int id;

    private String username;

    private String firstName;

    private String lastName;

    private List<FilmJson> films = null;

    private List<GenreJson> genres = null;

    public static AdminJson createJson(Admin admin) {
        AdminJson adminJson = AdminJson.createPartialJson(admin);

        adminJson.films = new ArrayList<FilmJson>();
        List<Film> orgFilms = admin.getFilms();

        for(Film film: orgFilms) {
            adminJson.films.add(FilmJson.createPartialJson(film));
        }

        adminJson.genres = new ArrayList<GenreJson>();
        List<Genre> genreOrg = admin.getGenres();

        for(Genre genre: genreOrg) {
            adminJson.genres.add(GenreJson.createPartialJson(genre));
        }

        return adminJson;

    }

    public static AdminJson createPartialJson(Admin admin) {
        AdminJson adminJson = new AdminJson();
        adminJson.id = admin.getId();
        adminJson.firstName = admin.getFirstName();
        adminJson.lastName = admin.getLastName();

        return adminJson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<GenreJson> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreJson> genres) {
        this.genres = genres;
    }
}
