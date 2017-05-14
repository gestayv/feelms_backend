package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="key_terms")
@NamedQuery(name="KeyTerm.findAll", query="SELECT a FROM Admin a")
public class KeyTerm {
    @Id
    @Column(name="id", unique=true, nullable=false)
    private int id;

    @Column(name="term", unique=false, nullable=false, length=45)
    private String term;
    
    @ManyToOne
    @JoinColumn(name="film_id")
    private Film film;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

}
