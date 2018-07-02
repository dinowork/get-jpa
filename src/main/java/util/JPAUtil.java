package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JPAUtil {
	
  private static final String PERSISTENCE_UNIT_NAME = "get-jpa";
  private static EntityManagerFactory factory;

  public static EntityManagerFactory getEntityManagerFactory() {
    if (factory == null) {
      factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    return factory;
  }

  public static void shutdown() {
    if (factory != null) {
      factory.close();
    }
  }
  
  public static EntityManager getEntityManager() {	  
	    EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE_UNIT_NAME");
	    return factory.createEntityManager();
  }
  
  
}
