package service;

import facade.FilmFacade;
import facade.TweetCountFacade;
import json.CountJson;
import json.FilmJson;
import model.Film;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by arturo on 12-05-17.
 */

@Path("/films")
public class FilmService {

    @EJB
    FilmFacade filmFacadeEJB;

    @EJB
    TweetCountFacade tweetCountFacadeEJB;

    Logger logger = Logger.getLogger(FilmService.class.getName());

    //Obtiene todos los films
    @GET
    @Produces({"application/xml", "application/json"})
    public List<FilmJson> getFilms() {
        List<FilmJson> filmJsons = new ArrayList<>();
        List<Film> films = filmFacadeEJB.findAll();
        for(Film film: films) {
            filmJsons.add(FilmJson.createJson(film));
        }
        return filmJsons;
    }

    //Obtiene un film en particular, incluye su director y genero
    @GET
    @Path("/{film_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public FilmJson getFilm(@PathParam("film_id") int filmId) {
        return FilmJson.createJson(filmFacadeEJB.find(filmId));
    }

    //Obtiene los conteos de tweets en un rango desde ayer hasta X dias atras (7 = 1 semana)
    @GET
    @Path("/{film_id}/tweets/count/{days}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<CountJson> getCount(@PathParam("film_id") int filmId, @PathParam("days") int days) {
        return tweetCountFacadeEJB.findCount(filmId, days);
    }

}
