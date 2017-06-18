package service;

import com.sun.javafx.geom.Edge;
import ejb.Neo4jEJB;
import ejb.TweetCountFacadeEJB;
import facade.FilmFacade;
import facade.TweetCountFacade;
import json.*;
import model.Film;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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

    @EJB
    Neo4jEJB neo4jEJB;

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

    //Obtiene la informacion del grafo para las ids de peliculas dadas
    //FORMATO EJ: /films/graph/1&2&4....&24/from/7
    //(Toma toda la lista de numeros separados por & y busca los que twittearon los ultimos X dias)
    @GET
    @Path("/graph/{film_ids}/from/{days}")
    @Produces({MediaType.APPLICATION_JSON})
    public GraphJson getGraph(@PathParam("film_ids") String filmIds, @PathParam("days") int days) {
        String[] strIds = filmIds.trim().split("&");
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (String strid: strIds) {
            if(StringUtils.isNumeric(strid)) {
                int val = Integer.parseInt(strid);
                if(!ids.contains(val)) {
                    ids.add(val);
                }
            }
        }

        if(!ids.isEmpty() && days > 0) {
            GraphJson g = neo4jEJB.getGraph(ids, days);
            logger.log(Level.INFO, "Sending Graph Json: " + g.getNodes().size() + " " + g.getLinks().size());

            return g;
        } else {
            return null;
        }
    }

    //Obtiene los porcentajes de tweets positivos y negativos para un film
    //film_id es la id del film. Days son la cantidad de dias partiendo de ayer (1 = solo ayer)
    //days debe ser mayor o igual a 1
    @GET
    @Path("/{film_id}/sentiments/{days}")
    @Produces({MediaType.APPLICATION_JSON})
    public SentimentJson getSentiments(@PathParam("film_id") int filmId, @PathParam("days") int days) {
        if(days >= 1) {
            return tweetCountFacadeEJB.findSentiment(filmId, days);
        } else {
            return null;
        }
    }


    //Para obtener la informacion del mapa correspondiente para la pelicula
    @GET
    @Path("/{film_id}/map/{days}")
    @Produces((MediaType.APPLICATION_JSON))
    public MapData getMapData(@PathParam("film_id") int filmId, @PathParam("days") int days) {
        if (days >= 1) {
            return tweetCountFacadeEJB.findCountByCountry(filmId, days);
        } else {
            return null;
        }
    }
    
    

}
