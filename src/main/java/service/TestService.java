package service;

import facade.AdminFacade;
import model.Admin;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by arturo on 02-05-17.
 */

@Path("/test")
public class TestService {

    @EJB
    AdminFacade adminFacadeEJB;

    Logger logger = Logger.getLogger(TestService.class.getName());

    @GET
    @Produces("text/plain")
    public String getGreeting() {
        return "Hello Warudo!! :D";
    }

    @GET
    @Path("/admins")
    @Produces({"application/xml", "application/json"})
    public List<Admin> getAdmins() {
        return adminFacadeEJB.findAll();
    }


}
