package service;

import facade.TweetCountFacade;
import json.RankJson;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by arturo on 12-05-17.
 */

@Path("/top")
public class RankingService {

    @EJB
    TweetCountFacade tweetCountFacadeEJB;

    Logger logger = Logger.getLogger(TestService.class.getName());

    @GET
    @Path("/{amount}/days/{days}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RankJson> getTop(@PathParam("amount") int amount, @PathParam("days") int days) {
        return tweetCountFacadeEJB.findTop(amount, days);
    }
    
    @GET
    @Path("/genres/{amount}/days/{days}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RankJson> getTopGenre(@PathParam("amount") int amount, @PathParam("days") int days) 
    {
        return tweetCountFacadeEJB.findGenre(amount, days);
    }
}
