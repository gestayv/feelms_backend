package model;

import javax.persistence.*;

/**
 * Created by arturo on 10-05-17.
 */

@Entity
@Table(name="admins")
//Ver si resulta sin esto
@NamedQuery(name="Admin.findAll", query="SELECT a FROM admins a")
public class Admin {

    @Id
    @Column(name="id", unique=true, nullable=false)
    private int id;

    @Column(name="username", unique=true, nullable=false)
    private String username;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

}
