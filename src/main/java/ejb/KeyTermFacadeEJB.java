package ejb;

import facade.AbstractFacade;
import facade.KeyTermFacade;
import model.KeyTerm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class KeyTermFacadeEJB extends AbstractFacade<KeyTerm> implements KeyTermFacade{
    
    @PersistenceContext(unitName="feelmsPU")
    private EntityManager em;

    public KeyTermFacadeEJB() {
        super(KeyTerm.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }
}
