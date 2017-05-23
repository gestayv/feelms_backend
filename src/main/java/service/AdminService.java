package service;

import ejb.*;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    //Arturo: Agregado para actualizar el buscador al ingresar la pelicula
    @EJB
    SearcherEJB searcherEJB;

    //Arturo: logger para registrar cosas
    Logger logger = Logger.getLogger(AdminService.class.getName());
    
    @POST
    @Path("/update/film")
    @Consumes({"application/xml", "application/json"})
    public void updateFilm(NewFilmJson film)
    {
        Film updateFilm = filmFacadeEJB.find(film.getId());
        updateFilm.setTitle(film.getTitle());
        updateFilm.setOriginalTitle(film.getOriginalTitle());
        updateFilm.setLength(film.getLength());
        try {
            updateFilm.setReleaseDate((new SimpleDateFormat("yyyy-MM-dd")).parse(film.getReleaseDate()));
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        updateFilm.setSynopsis(film.getSynopsis());
        //updateFilm.setImgId(film.getImgId());
        updateFilm.setTrailer(film.getTrailer());
        
        //  Comparar keywords, generos, admins y directores antiguos y nuevos,
        //  si no coinciden, hago un update.
        
        filmFacadeEJB.edit(updateFilm);
    }
    
    @POST
    @Path("/add/film")
    @Consumes({"application/xml", "application/json"})
    public void addFilm(NewFilmJson film)
    {
        //  Se crea un film nuevo y se agregan todos los parámetros no conflictivos
        Film newFilm = new Film();
        newFilm.setTitle(film.getTitle());
        newFilm.setOriginalTitle(film.getOriginalTitle());
        newFilm.setLength(film.getLength());
        try {
            newFilm.setReleaseDate((new SimpleDateFormat("yyyy-MM-dd")).parse(film.getReleaseDate()));
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        //newFilm.setReleaseDate(film.getReleaseDate());
        newFilm.setSynopsis(film.getSynopsis());
        //Arturo: Por mientras ignoremos las imagenes ya que debemos
        //generar el id aleatoreamente y que sea unico.
        //Hay que cambiar en el JSON de newFilm el get ImgId por
        //algo tipo booleano, para verificar si sube imagen o no.
        newFilm.setImgId(null);
        //newFilm.setImgId(film.getImgId());
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

            //Arturo: Para agregar la pelicula al buscador sin tener que reiniciar
            //Supondre filmUpdate es la pelicula que se acaba de agregar
            try {
                searcherEJB.addToIndex(filmUpdate);
                logger.log(Level.INFO, "Added film: " + filmUpdate.getTitle());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error al agregar Film al buscador: " + e.getMessage());
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
    /* 
    
    Recibe la id de la película
    debería borrar en este orden: keyword, genre, film
    @GET
    @PathParam("/delete/film/{film_id}")
    public void deleteFilm()
    {
        
    }
    */
}
