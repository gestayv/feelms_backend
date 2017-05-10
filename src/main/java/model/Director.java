package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Entity
@Table(name = "directors")
@NamedQuery(name="Director.findAll", query = "SELECT d FROM Director d")
public class Director implements Serializable {

    @Id
    @Column(name="id", unique=true, nullable=false)
    private int id;

    @Column(name="first_name", nullable=false, length=45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @OneToMany(mappedBy = "director")
    private List<Film> films;

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

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}
