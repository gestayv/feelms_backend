package json;


import model.Director;
import model.Film;
import model.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */
public class FilmJson implements Serializable{

    private int id;

    private String title;

    private String originalTitle;

    private Date releaseDate;

    private int length;

    private String synopsis;

    private DirectorJson director = null;

    private List<GenreJson> genres = null;

    private String imgId;

    private String trailer;

    public static FilmJson createJson(Film film) {
        FilmJson filmJson = FilmJson.createPartialJson(film);
        filmJson.director = DirectorJson.createPartialJson(film.getDirector());

        filmJson.genres = new ArrayList<GenreJson>();
        List<Genre> genreOrg = film.getGenres();

        for(Genre genre: genreOrg) {
            filmJson.genres.add(GenreJson.createPartialJson(genre));
        }

        return filmJson;
    }

    public static FilmJson createPartialJson(Film film) {
        FilmJson filmJson = new FilmJson();

        filmJson.id = film.getId();
        filmJson.title = film.getTitle();
        filmJson.originalTitle = film.getOriginalTitle();
        filmJson.releaseDate = film.getReleaseDate();
        filmJson.length = film.getLength();
        filmJson.synopsis = film.getSynopsis();
        filmJson.imgId = film.getImgId();
        filmJson.trailer = film.getTrailer();

        return filmJson;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public DirectorJson getDirector() {
        return director;
    }

    public void setDirector(DirectorJson director) {
        this.director = director;
    }

    public List<GenreJson> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreJson> genres) {
        this.genres = genres;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
