package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Arturo on 24-06-2017.
 */

@Entity
@Table(name = "subscription")
@NamedQuery(name="Subscription.findAll", query = "SELECT s FROM Subscription s")
public class Subscription implements Serializable{


    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "mail", nullable = false)
    private String mail;

    private Subscription() {

    }

    public Subscription(String firstName, String lastName, String mail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
