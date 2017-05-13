package service;

import ejb.SearcherEJB;
import facade.FilmFacade;
import json.FilmJson;
import model.Film;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by arturo on 13-05-17.
 */
@Path("/search")
public class SearchService {

    @EJB
    SearcherEJB searcherEJB;

    @EJB
    FilmFacade filmFacadeEJB;

    Logger logger = Logger.getLogger(SearchService.class.getName());

    /*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return searcherEJB.testMsg();
    }
    */

    @GET
    @Path("/{query}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FilmJson> searchFilms(@PathParam("query") String query, @PathParam("limit") int limit) {
        String[] q = query.trim().split("\\+");
        String preparedQuery = String.join(" ", q);

        List<FilmJson> filmJsons = new ArrayList<FilmJson>();

        try {
            List<Integer> arr = searcherEJB.searchIndex(preparedQuery, limit);
            List<Film> films = filmFacadeEJB.findList(arr);

            filmJsons = new ArrayList<FilmJson>();

            for(Film film: films) {
                filmJsons.add(FilmJson.createPartialJson(film));
            }

            return filmJsons;

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return filmJsons;
    }


}
