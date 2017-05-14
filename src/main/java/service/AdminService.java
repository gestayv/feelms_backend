package service;

import facade.AdminFacade;
import facade.DirectorFacade;
import facade.FilmFacade;
import facade.GenreFacade;
import facade.TweetCountFacade;

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
import java.util.logging.Logger;

import ejb.AdminFacadeEJB;
import ejb.FilmFacadeEJB;
import ejb.DirectorFacadeEJB;
import ejb.GenreFacadeEJB;
import facade.KeyTermFacade;

import json.NewFilmJson;
import json.CountJson;
import json.FilmJson;

import model.Film;
import model.Admin;
import model.Director;
import model.Genre;
import model.KeyTerm;

@Path("/admin")
public class AdminService {
    
    @EJB
    FilmFacade filmFacadeEJB;
    
    @EJB
    AdminFacade adminFacadeEJB;
    
    @EJB
    DirectorFacade directorFacadeEJB;
    
    @EJB
    GenreFacade genreFacadeEJB;
    
    @EJB
    KeyTermFacade keyTermFacadeEJB;
    
    @POST
    @Path("/{admin_id}/add/film")
    @Consumes({"application/xml", "application/json"})
    public void addFilm(NewFilmJson film)
    {
        //  Se crea un film nuevo y se agregan todos los parámetros no conflictivos
        Film newFilm = new Film();
        newFilm.setTitle(film.getTitle());
        newFilm.setOriginalTitle(film.getOriginalTitle());
        newFilm.setLength(film.getLength());
        newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setSynopsis(film.getSynopsis());
        newFilm.setImgId(film.getImgId());
        newFilm.setTrailer(film.getTrailer());
        
        // Buscar: Director, genero, admin. 
        Director dir = directorFacadeEJB.find(film.getDirector());
        Admin admin = adminFacadeEJB.find(film.getAdmin());
        
        //  Si ninguno es nulo no se crea la nueva película
        if(dir != null || admin != null)
        {
            newFilm.setDirector(dir);
            newFilm.setAdmin(admin);
            
            List<Genre> genres = new ArrayList<>();
            for(int g:  film.getGenres())
            {
                //  Se obtienen todos los generos para asignarlos a la lista de}
                //  la película.
                Genre genre = genreFacadeEJB.find(g);
                if(genre != null)
                {
                    genres.add(genre);
                }
            }
            newFilm.setGenres(genres);
            
            //  Se crea la película en la bd
            filmFacadeEJB.create(newFilm);
            
            //  Ahora se actualizan las tablas restantes:
            //  Necesito saber la id de la última película agregada
            //  por lo que obtengo todas las películas, saco la última de la lista
            //  y edito los genre.
            List<Film> filmsUpdate = filmFacadeEJB.findAll();
            
            Film filmUpdate = filmsUpdate.get(filmsUpdate.size() - 1);
            
            for (Genre g : genres) {
                List<Film> films = g.getFilms();
                films.add(filmUpdate);
                filmFacadeEJB.edit(filmUpdate);
                genreFacadeEJB.edit(g);
            }
            
            //  Utilizo la misma película para el caso de las keywords
            //  Por ahora se pueden repetir, arreglar para sprint 2.
            List<KeyTerm> keyTerms = new ArrayList<KeyTerm>();
            for (String k: film.getKeywords()) {
                KeyTerm keyT = new KeyTerm();
                keyT.setTerm(k);
                keyT.setFilm(filmUpdate);
                keyTermFacadeEJB.create(keyT);
            }
        }
    }
}
