package ezute;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManager {

	private EntityManager getEntityManager() {
	    EntityManagerFactory factory = null;
	    EntityManager entityManager = null;
	    try {
	      factory = Persistence.createEntityManagerFactory("get-jpa");
	      entityManager = (EntityManager) factory.createEntityManager();
	    } finally {
	      factory.close();
	    }
	    return entityManager;		
	}
	
	
}
