package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by arturo on 10-05-17.
 */

@Entity
@Table(name="admins")
//Ver si resulta sin esto
@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a")
public class Admin implements Serializable {

    @Id
    @Column(name="id", unique=true, nullable=false)
    private int id;

    @Column(name="username", unique=true, nullable=false, length=45)
    private String username;

    @Column(name="password", nullable=false, length=45)
    private String password;

    @Column(name="first_name", nullable=false, length=45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @OneToMany
    @JoinColumn(name= "admin_id", referencedColumnName = "id")
    private List<Film> films;

    @OneToMany
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private List<Genre> genres;

    
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
