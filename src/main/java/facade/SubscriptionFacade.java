package facade;

import model.Subscription;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Arturo on 24-06-2017.
 */

@Local
public interface SubscriptionFacade {

    public void create(Subscription entity);

    public void edit(Subscription entity);

    public void remove(Subscription entity);

    public Subscription find(Object id);

    public List<Subscription> findAll();

    public List<Subscription> findRange(int[] range);

    public int count();

}
