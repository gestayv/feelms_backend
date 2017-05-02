package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by arturo on 02-05-17.
 */

@Path("/test")
public class TestService {

    @GET
    @Produces("text/plain")
    public String getGreeting() {
        return "Hello Warudo!! :D";
    }

}
