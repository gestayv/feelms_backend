package service;

import facade.AdminFacade;
import facade.FilmFacade;
import json.AdminJson;
import json.FilmJson;
import model.Admin;
import model.Film;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by arturo on 02-05-17.
 */

@Path("/test")
public class TestService {

    @EJB
    AdminFacade adminFacadeEJB;

    @EJB
    FilmFacade filmFacadeEJB;

    Logger logger = Logger.getLogger(TestService.class.getName());

    @GET
    @Produces("text/plain")
    public String getGreeting() {
        return "Hello Warudo!! :D";
    }

    @GET
    @Path("/admins")
    @Produces({"application/xml", "application/json"})
    public List<AdminJson> getAdmins() {
        List<AdminJson> adminJsons = new ArrayList<>();
        List<Admin> admins = adminFacadeEJB.findAll();
        for(Admin admin: admins) {
            adminJsons.add(AdminJson.createJson(admin));
        }
        return adminJsons;
    }

    @GET
    @Path("/admins/{admin_id}")
    @Produces({"application/xml", "application/json"})
    public AdminJson getAdmin(@PathParam("admin_id") Integer admin_id) {
        return AdminJson.createJson(adminFacadeEJB.find(admin_id));
    }

    @GET
    @Path("/admins/{admin_id}/films")
    @Produces({"application/json"})
    public List<FilmJson> getFilm(@PathParam("admin_id") Integer admin_id) {
        List<FilmJson> filmJsons = new ArrayList<>();
        List<Film> films = adminFacadeEJB.find(admin_id).getFilms();
        for(Film film: films) {
            filmJsons.add(FilmJson.createJson(film));
        }
        return filmJsons;
    }


    @GET
    @Path("/films")
    @Produces({"application/xml", "application/json"})
    public List<FilmJson> getFilms() {
        List<FilmJson> filmJsons = new ArrayList<>();
        List<Film> films = filmFacadeEJB.findAll();
        for(Film film: films) {
            filmJsons.add(FilmJson.createJson(film));
        }
        return filmJsons;
    }




}
