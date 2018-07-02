package business;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.Pessoa;


public class AbstractManager<T>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	protected EntityManagerFactory factory = null;

	private EntityManager getEntityManager() {	
		factory = Persistence.createEntityManagerFactory("get-jpa");
		return factory.createEntityManager();			 
	}

	
	public void persist(T t) {
		entityManager = getEntityManager();
	    try {
	      entityManager.getTransaction().begin();
	      entityManager.persist(t);		     
	      entityManager.getTransaction().commit();
	    } finally {
	    	entityManager.close();
	    	factory.close();		      
	    }
	}

	
	public void merge(T t) {
		entityManager = getEntityManager();
	    try {
	      entityManager.getTransaction().begin();
	      entityManager.merge(t);		     
	      entityManager.getTransaction().commit();
	    } finally {
	    	entityManager.close();
	    	factory.close();		      
	    }	
	}
/*
	public T find(Long id){
		entityManager = getEntityManager();
	    T objeto = null;
	    try {
	    	objeto = (T) entityManager.find(Pessoa.class, id);
	    } finally {
	    	entityManager.close();
	    	factory.close();	
	    }
	    return objeto;
	}
	
	
	public void remove(Long id) {
		entityManager = getEntityManager();
	    try {		 
	      entityManager.getTransaction().begin();		 
	      Pessoa pessoa = entityManager.find(Pessoa.class, id);		 
	      entityManager.remove(pessoa);		 
	      entityManager.getTransaction().commit();
	    } finally {
	    	entityManager.close();
	    	factory.close();	
	    }
		
	}*/

	
	
	
	

}
