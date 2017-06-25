package service;

import facade.SubscriptionFacade;
import json.NewSubscription;
import model.Subscription;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * Created by Arturo on 25-06-2017.
 */

@Path("/news")
public class NewsService {

    @EJB
    SubscriptionFacade subscriptionFacadeEJB;

    @POST
    @Path("/suscribe")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void subscribe(NewSubscription sub) {
        Subscription subData = new Subscription(sub.getFirst_name(), sub.getLast_name(), sub.getMail());
        subscriptionFacadeEJB.create(subData);
    }


}
