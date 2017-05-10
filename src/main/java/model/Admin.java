package model;

import javax.persistence.*;
import java.io.Serializable;

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
}
