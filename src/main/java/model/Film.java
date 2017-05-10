package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Entity
@Table(name = "films")
@NamedQuery(name="Film.findAll", query = "SELECT f FROM Film f")
public class Film implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "length")
    private int length;

    @Column(name = "synopsis")
    private String synopsis;

    @ManyToOne
    @JoinColumn(name="director_id")
    private Director director;

    @ManyToMany(mappedBy = "films")
    private List<Genre> genres;

    @Column(name = "img_id", length = 60)
    private String imgId;

    @Column(name = "trailer", length = 100)
    private String trailer;


    //Getter and setters


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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
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
