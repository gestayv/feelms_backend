package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Entity
@Table(name="genres")
@NamedQuery(name="Genre.findAll", query="SELECT g FROM Genre g")
public class Genre implements Serializable {

    @Id
    @Column(name="id", unique=true, nullable=false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;    
    
    @ManyToMany
    @JoinTable(name = "film_has_genre",
    joinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "film_id", referencedColumnName = "id"))
    private List<Film> films;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}
