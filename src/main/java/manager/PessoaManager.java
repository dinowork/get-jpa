package manager;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import model.Pessoa;

public class PessoaManager {
	
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

		  public List<Pessoa> consultar() {
			  EntityManager entityManager = getEntityManager();
			  List<Pessoa> results;
			    try {		 			  	  
					  CriteriaBuilder builder = entityManager.getCriteriaBuilder();
					  CriteriaQuery<Pessoa> query = builder.createQuery(Pessoa.class);
					  Root<Pessoa> from = query.from(Pessoa.class);
					  CriteriaQuery<Pessoa> select = query.select(from);					   
					  TypedQuery<Pessoa> typedQuery = entityManager.createQuery(select);
					  results = typedQuery.getResultList();
			    } finally {
			    	entityManager.close();
			    	factory.close();	
			    }
			  return results;
		  }
		  
		  public Pessoa consultarPorNome(String nome) {
			  EntityManager entityManager = getEntityManager();
			  Pessoa results;
			    try {		 			  	  					  
					CriteriaBuilder builder = entityManager.getCriteriaBuilder();
					CriteriaQuery<Pessoa> query = builder.createQuery(Pessoa.class);
					Root<Pessoa> from = query.from(Pessoa.class);
					Predicate predicate = builder.equal(from.get("nome"), nome);
					TypedQuery<Pessoa> typedQuery = entityManager.createQuery(query.select(from).where(predicate));
					results = typedQuery.getSingleResult();
			    } finally {
			    	entityManager.close();
			    	factory.close();	
			    }
			  return results;
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
