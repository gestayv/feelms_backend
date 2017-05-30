/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import model.KeyTerm;
import model.Film;

import java.util.List;
/**
 *
 * @author ichigo
 */
public class KeyTermJson {
    
    private int id;
    
    private String term;
    
    private FilmJson film;

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

    public FilmJson getFilm() {
        return film;
    }

    public void setFilm(FilmJson film) {
        this.film = film;
    }
    
    public static KeyTermJson createJson(KeyTerm keyterm)
    {
        KeyTermJson keytermJson = createPartialJson(keyterm);
        keytermJson.setId(keyterm.getId());
        keytermJson.setTerm(keyterm.getTerm());
        keytermJson.setFilm(FilmJson.createPartialJson(keyterm.getFilm()));
        return keytermJson;
    }
    
    public static KeyTermJson createPartialJson(KeyTerm keyterm)
    {
        KeyTermJson keytermJson = new KeyTermJson();
        keytermJson.setId(keyterm.getId());
        keytermJson.setTerm(keyterm.getTerm());
        return keytermJson;
    }

}
