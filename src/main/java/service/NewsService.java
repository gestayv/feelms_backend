package service;

import ejb.MailEJB;
import facade.SubscriptionFacade;
import json.NewSubscription;
import model.Subscription;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arturo on 25-06-2017.
 */

@Path("/news")
public class NewsService {

    @EJB
    SubscriptionFacade subscriptionFacadeEJB;

    @EJB
    MailEJB mailEJB;

    Logger logger = Logger.getLogger(NewsService.class.getName());

    @POST
    @Path("/suscribe")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void subscribe(NewSubscription sub) {

        int validMail = 0;
        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(sub.getMail());
        if(m.find() && !sub.getFirst_name().isEmpty() && !sub.getLast_name().isEmpty()) {

            logger.log(Level.INFO, "Email Regex Pass");

            List<Subscription> subs = subscriptionFacadeEJB.findAll();
            int found = 0;
            for(Subscription s: subs) {
                if(s.getMail().equals(sub.getMail())) {
                    found = 1;
                    break;
                }
            }
            if(found == 0) {
                Subscription subData = new Subscription(sub.getFirst_name(), sub.getLast_name(), sub.getMail());
                subscriptionFacadeEJB.create(subData);
                logger.log(Level.INFO, "Email subscribed");

                try {
                    mailEJB.sendWelcomeMail(sub.getMail());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }

    }

    @POST
    @Path("/unsuscribe")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void unsubscribe(NewSubscription sub) {

        int validMail = 0;
        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(sub.getMail());
        if(m.find()) {

            logger.log(Level.INFO, "Email Regex Pass");

            Subscription userSub = null;

            List<Subscription> subs = subscriptionFacadeEJB.findAll();
            int found = 0;
            for(Subscription s: subs) {
                if(s.getMail().equals(sub.getMail())) {
                    userSub = s;
                    break;
                }
            }
            if(userSub != null) {
                subscriptionFacadeEJB.remove(userSub);
                logger.log(Level.INFO, "Email unsubscribed");
                try {
                    mailEJB.sendGoodbyeMail(sub.getMail());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }


}
