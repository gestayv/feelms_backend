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
        
        Director dir = directorFacadeEJB.find(film.getDirector());
        Admin admin = adminFacadeEJB.find(film.getAdmin());
        
        updateFilm.setDirector(dir);
        updateFilm.setAdmin(admin);
        
        //  Comparar keywords, generos antiguos y nuevos,
        //  si no coinciden, hago un update.
        
        //  Comparación de genres
        List<Genre> genres = new ArrayList<>();
        for(int g : film.getGenres())
        {
            Genre genre = genreFacadeEJB.find(g);
            if(!updateFilm.getGenres().contains(genre))
            {
                genres.add(genre);
            }
        }
        
/*======  Esta parte del código es horriblemente sobrecomplicada pero funcional ====*/
        //  Entero para indicar si el elemento se encuentra o no.
        int found = 0;
        //  Se crean listas:
        //  Una lista con todos los géneros correspondientes a los valores a ser actualizados
        List<Genre> nueva = new ArrayList<>();
        for(int id : film.getGenres())
        {
            nueva.add(genreFacadeEJB.find(id));
        }
        //  Una lista con todos los géneros previos al update.
        List<Genre> original = updateFilm.getGenres();
        
        //  Una lista con los valores que se deben eliminar y los que se deben agregar.
        List<Genre> delG = new ArrayList<>();
        List<Genre> addG = new ArrayList<>();
        
        //  Comparo los géneros originales y los géneros nuevos
        for (Genre go : original) {
            //  Por cada género original comparo con los nuevos
            for(Genre gn : nueva)
            {
                if(go.getId() == gn.getId())
                {
                    found = 1;
                }
            }
            //  Si no está en los nuevos, lo agrego a la lista de eliminación
            if(found == 0)
            {
                delG.add(go);
            }
            found = 0;
        }
        
        //  Comparo los géneros nuevos con los originales
        for (Genre gn : nueva) {
            //  Por cada nuevo lo busco en los originales
            for(Genre go : original)
            {
                if(gn.getId() == go.getId())
                {
                    found = 1;
                }
            }
            //  Si no está en los originales lo agrego a la lista de añadir.
            if(found == 0)
            {
                addG.add(gn);
            }
            found = 0;
        }
        
        found = 0;
        int pos = 0;
        //  Hago los updates necesarios en film_has_genre por medio del ejb de género
        if(delG.size() > 0)
        {
            for(Genre g : delG)
            {
                Genre modify = genreFacadeEJB.find(g.getId());
                List<Film> films = modify.getFilms();
                for(Film f : films)
                {
                    if(f.getId() == updateFilm.getId())
                    {
                        break;
                    }
                    pos++;
                }
                films.remove(pos);
                modify.setFilms(films);
                filmFacadeEJB.edit(updateFilm);
                genreFacadeEJB.edit(modify);
                pos = 0;
            }
        }
        if(addG.size() > 0)
        {
            for(Genre g : addG)
            {
                Genre modify = genreFacadeEJB.find(g.getId());
                List<Film> films = modify.getFilms();
                films.add(updateFilm);
                modify.setFilms(films);
                filmFacadeEJB.edit(updateFilm);
                genreFacadeEJB.edit(modify);
            }
        }
        
        //  Comparación de keywords
        found = 0;
        List<KeyTerm> delK = new ArrayList<>();
        List<KeyTerm> addK = new ArrayList<>();
        List<KeyTerm> originalK = updateFilm.getKeyTerms();
        List<KeyTerm> listaFinal = new ArrayList<>();
        //  Buscar en todos los kt originales...
        for(KeyTerm kt : originalK)
        {
            for(String s : film.getKeywords())
            {
                if(kt.getTerm().equals(s))
                {
                    found = 1;
                    listaFinal.add(kt);
                    break;
                }
            }
            if(found != 1)
            {
                delK.add(kt);
            }
            found = 0;
        }
        
        for(String s : film.getKeywords())
        {
            for(KeyTerm kt : originalK)
            {
                if(s.equals(kt.getTerm()))
                {
                    found = 1;
                    break;
                }
            }
            if(found != 1)
            {
                KeyTerm keyT = new KeyTerm();
                keyT.setTerm(s);
                keyT.setFilm(updateFilm);
                addK.add(keyT);
            }
            found = 0;
        }
        
        if(!delK.isEmpty())
        {
            for(KeyTerm kt : delK)
            {
                keyTermFacadeEJB.remove(kt);
            }
        }
        
        if(!addK.isEmpty())
        {
            for(KeyTerm kt : addK)
            {
                keyTermFacadeEJB.create(kt);
                int largo = keyTermFacadeEJB.findAll().size();
                KeyTerm keyT = keyTermFacadeEJB.findAll().get(largo - 1);
                listaFinal.add(keyT);
            }
        }
        
/*==== Aquí termina la parte horriblemente sobrecomplicada =====*/
        updateFilm.setGenres(genres);
        updateFilm.setKeyTerms(listaFinal);
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
