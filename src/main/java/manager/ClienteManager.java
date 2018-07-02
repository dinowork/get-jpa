package manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Pessoa;

public class ClienteManager {
	
	EntityManagerFactory factory = null;
        
		  private EntityManager getEntityManager() {	
		    factory = Persistence.createEntityManagerFactory("get-jpa");
		    return factory.createEntityManager();			 
		  }

		  public Pessoa salvar(Pessoa pessoa) throws Exception {
		    EntityManager entityManager = getEntityManager();
		    try {
		      entityManager.getTransaction().begin();
		      entityManager.persist(pessoa);		     
		      entityManager.getTransaction().commit();
		    } finally {
		    	entityManager.close();
		    	factory.close();		      
		    }
		    return pessoa;
		  }

		  public Pessoa alterar(Pessoa pessoa) throws Exception {
			    EntityManager entityManager = getEntityManager();
			    try {
			       entityManager.getTransaction().begin();			    
			       pessoa = entityManager.merge(pessoa);			      
			      entityManager.getTransaction().commit();
			    } finally {
			    	entityManager.close();
			    	factory.close();		      
			    }
			    return pessoa;
		  }
		 
		  public void excluir(Long id) {
		    EntityManager entityManager = getEntityManager();
		    try {		 
		      entityManager.getTransaction().begin();		 
		      Pessoa pessoa = entityManager.find(Pessoa.class, id);		 
		      entityManager.remove(pessoa);		 
		      entityManager.getTransaction().commit();
		    } finally {
		    	entityManager.close();
		    	factory.close();	
		    }
		  }

		 
		  public Pessoa consultarPorId(Long id) {
		    EntityManager entityManager = getEntityManager();
		    Pessoa pessoa = null;
		    try {
		      pessoa = entityManager.find(Pessoa.class, id);
		    } finally {
		    	entityManager.close();
		    	factory.close();	
		    }
		    return pessoa;
		  }

}
