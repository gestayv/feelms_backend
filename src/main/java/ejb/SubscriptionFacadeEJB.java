package ejb;

import facade.AbstractFacade;
import facade.SubscriptionFacade;
import model.Subscription;

import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Properties;

/**
 * Created by Arturo on 24-06-2017.
 */

@Stateless
public class SubscriptionFacadeEJB extends AbstractFacade<Subscription> implements SubscriptionFacade{

    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public SubscriptionFacadeEJB() {
        super(Subscription.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }


}
