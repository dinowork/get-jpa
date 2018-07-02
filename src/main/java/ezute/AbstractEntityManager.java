package ezute;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;


/**
 * Classe abstrata que contem os metodos básicos dos managers do sistema
 * 
 * @author rootdeveloper
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused" })
public abstract class AbstractEntityManager<E extends AbstractEntityBasic, T extends Serializable> 
	implements IManager<E, T> {
	
	private static final String MATCH_MODE_START = "%";

	@PersistenceContext
	protected EntityManager entityManager;
	
	private CriteriaBuilder builder;
	
	private static final Logger LOGGER = Logger.getLogger(AbstractEntityManager.class.getName());
	
	protected Class<E> persistentClass;
	
	private Root<E> root;
	
	private CriteriaQuery<E> criteriaQuery;
	
	private static final String ENTIDADE = "Entidade:";

	/**
	 * Construtor do AbstractEntityManager
	 *
	 * @param klass Entidade a ser persistida
	 */
	public AbstractEntityManager(Class<E> klass) {
		persistentClass = klass;
	}

	@PostConstruct
	private void init() {
		builder = entityManager.getCriteriaBuilder();
		criteriaQuery = builder.createQuery(persistentClass);
		root = criteriaQuery.from(persistentClass);
	}

	@Override
	public E find(T id) throws SystemException {
		try {
			// Caso o Id fornecido nao seja encontrado, o metodo retorna null.
			return entityManager.find(persistentClass, id);
		} catch(Exception e) {
			throw new SystemException(ENTIDADE + (id == null ? "" : persistentClass.getClass().getSimpleName()), e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public E insert(E entity) throws SystemException {
		try {
			entityManager.persist(entity);
			entityManager.flush();
			return entity;
		} catch(Exception e) {
			throw new SystemException(ENTIDADE + (entity == null ? "" : entity.getClass().getSimpleName()), e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public E update(E entityBasic) throws SystemException {
		try {
			validarEntidadeNaoNula(entityBasic);
			entityBasic = entityManager.merge(entityBasic);
			entityManager.flush();
			return entityBasic;
		} catch(Exception e) {
			throw new SystemException(ENTIDADE + (entityBasic == null ? "" : entityBasic.getClass().getSimpleName()), e);
		}
	}
	
	@Override
	public List<E> findAll(Integer maxResult) throws SystemException {
		try {
			return buildTypedQuery(null, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public List<E> findAll() throws SystemException {
		return findAll(null);
	}
	
	@Override
	public List<E> findAllWithPaginator(Integer startPosition, Integer maxResult, ezute.Order order) throws SystemException {
		try {
			createOrderBy(order);	
			return buildTypedQuery(startPosition, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public List<E> findAllByIdIn(List<T> listIds) throws SystemException {
		if(listIds != null && !listIds.isEmpty()) {
			try {
				EntityType<E> type = entityManager.getMetamodel().entity(persistentClass);
				
				TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery.where(
					root.get((SingularAttribute<? super E, T>) type.getDeclaredAttribute("id")).in(listIds)));
				return typedQuery.getResultList();
			} catch(Exception e) {
				throw new SystemException("ERRO AO EFETURAR CONSULTA NA BASE", e);
			}
		}
		throw new SystemException("NAO FOI POSSIVEL EFETUAR A CONSULTA");
	}
	
	@Override
	public List<E> findAllAscOrderBy(String... name) throws SystemException {
		return findAllAscOrderBy(null, name);
	}
	
	@Override
	public List<E> findAllAscOrderBy(Integer maxResult, String... name) throws SystemException {
		try {
			createOrderBy(name);
						
			return buildTypedQuery(null, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}
	
	@Override
	public List<E> findAllOrderBy(String nomeAtributoEntidade, String nomeCampo) throws SystemException {
		return findAllOrderBy(nomeAtributoEntidade, nomeCampo, null);
	}
	
	@Override
	public List<E> findAllOrderBy(String nomeAtributoEntidade, String nomeCampo, Integer maxResult)
		throws SystemException {

		try {
			criteriaQuery.orderBy(builder.asc(root.get(nomeAtributoEntidade).get(nomeCampo)));
			
			return buildTypedQuery(null, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}
	
	@Override
	public List<E> findByColumnsResultList(Map<String, Object> colunas, String entidade, Integer maxResult, String ...ordenadoPor )
		throws SystemException {

		try { 
			List<Predicate> predicates = buildPredicates(colunas, entidade);
			createOrderBy(ordenadoPor);			
			criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));			
			return buildTypedQuery(null, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}

	private List<Predicate> buildPredicates(Map<String, Object> colunas, String entidade) {
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, Object> item : colunas.entrySet()) {
			String chave = item.getKey();
			if(entidade == null) {
				predicates.add(builder.equal(root.get(chave), colunas.get(chave)));
			} else {
				predicates.add(builder.equal(root.get(entidade).get(chave), colunas.get(chave)));
			}
		}
		return predicates;
	}

	@Override
	public List<E> findByColumnsResultListWithPaginator(Map<String, Object> colunas, String entidade,
			Integer startPosition, Integer maxResult, ezute.Order order) throws SystemException {
		
		try { 
			List<Predicate> predicates = buildPredicates(colunas, entidade);
			createOrderBy(order);
			criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
			return buildTypedQuery(startPosition, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public List<E> findByColumnsResultList(Map<String, Object> query, Integer maxResult) throws SystemException {
		return findByColumnsResultList(query, null, maxResult);
	}
	
	@Override
	public List<E> findAllByColumnsIn(String campo, List<String> valores) throws SystemException {
		if(valores != null && !valores.isEmpty()) {
			try {
				criteriaQuery.where(root.get(campo).in(valores));
				return buildTypedQuery(null, null).getResultList();
			} catch(Exception e) {
				throw new SystemException(e);
			}
		}
		throw new SystemException("NAO FOI POSSIVEL EFETUAR A CONSULTA");
	}
	
	@Override
	public Long validaCampoPorValor(String campo, String valor, String entidade) throws SystemException {
		try {
			builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> query =  builder.createQuery(Long.class);
			root = query.from(persistentClass);
			
			if(entidade == null) {
				query.select(builder.count(root.get(campo)));
				query.where(builder.equal(root.get(campo), valor));
			} else {
				query.select(builder.count(root.get(entidade).get(campo)));
				query.where(builder.equal(root.get(entidade).get(campo), valor));
			}
			
			TypedQuery<Long> tpQuery = entityManager.createQuery(query);
			List<Long> retorno = tpQuery.getResultList();
			
			return retorno.isEmpty() ? null : retorno.get(0);
		} catch(Exception e) {
			throw new SystemException(e);
		}	
	}
	
	@Override
	public E findByColumnsSingleResult(Map<String, Object> query, String entidade) throws SystemException {
		List<E> result = findByColumnsResultList(query, entidade, null);
		
		try {
			validaSingleResult(result);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERRO AO REALIZAR VALIDACAO DO RESULTADO DA CONSULTA", e);
		}
		return result.get(0);
		
	}
	
	@Override
	public E findByColumnsSingleResult(Map<String, Object> query) throws SystemException {
		List<E> result = findByColumnsResultList(query, null);
		
		validaSingleResult(result);
		
		try {
			validaSingleResult(result);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "ERRO AO REALIZAR VALIDACAO DO RESULTADO DA CONSULTA", e);
		}
		return result.get(0);
		
	}
	
	/**
	 * Metodo que faz insert em Massa
	 * @param listEntity - Lista de entidades
	 * @throws SystemException - Caso ocorra algum erro durante o processo
	 */
	public void batchInsert(List<E> listEntity) throws SystemException {
		int size = listEntity.size();
		E entity = null;
		try {
			int i = 1;
			for(; i <= size; i++) {
				entity = listEntity.get(i - 1);
				entityManager.persist(entity);
				if(i % 30 == 0) {
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			//preciso subtrair i por 1 pois quando sai do for adiciona 1
			if(size != 0 && ((i - 1) % 30 != 0)) {
				entityManager.flush();
				entityManager.clear();
			}
		} catch(Exception e) {
			throw new SystemException(ENTIDADE + (entity == null ? "" : entity.getClass().getSimpleName()), e);
		}
	}

	/**
	 * Metodo que faz atualizacao em Massa
	 *
	 * @param listEntity - Lista de entidades
	 * @throws SystemException - Caso ocorra algum erro durante o processo
	 */
	public void batchUpdate(List<E> listEntity) throws SystemException {
		int size = listEntity.size();
		E entity = null;
		try {
			int i = 1;
			for(; i <= size; i++) {
				entity = listEntity.get(i - 1);
				validarEntidadeNaoNula(entity);
				entity = entityManager.merge(entity);
				if(i % 30 == 0) {
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			//preciso subtrair i por 1 pois quando sai do for adiciona 1
			if(size != 0 && ((i - 1) % 30 != 0)) {
				entityManager.flush();
				entityManager.clear();
			}
		} catch(Exception e) {
			throw new SystemException(ENTIDADE + (entity == null ? "" : entity.getClass().getSimpleName()), e);
		}
	}

	/**
	 * Valida se entidade esta nula
	 *
	 * @param entityBasic Entidade a ser validada
	 */
	private void validarEntidadeNaoNula(E entityBasic) {
		if(entityBasic == null || entityBasic.getId() == null) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Metodo responsavel por garantir o retorno de um elemento a partir de uma lista
	 * @param result Lista a ser validade
	 * @throws SystemException caso ocorra algum erro na consulta
	 * @throws ObjetoNaoEncontradoException quando nao localizar elemento
	 */
	protected void validaSingleResult(List<E> result) throws SystemException {
		if(result.isEmpty()) {
			throw new SystemException("NAO HOUVE RESULTADOS PARA A CONSULTA");
		} else if(result.size() > 1) {
			Set<E> uniqueResult = new HashSet<>(result);
			if(uniqueResult.size() > 1) {
				throw new SystemException("RESULTADO RETORNOU MAIS DE UM ELEMENTO");
			}
		}
	}
	
	/**
	 * Cria uma lista de predicados com os parametros seperados
	 * em sublistas de 100
	 * @param listaDeParametros - lista de id 
	 * @param klass - o Class da entidade
	 * @param root 
	 * @param attribute - SingularAttribute
	 * @return {@link Predicate} - Retorna a lista de 
	 * predicados com os atributos separados por listas de 100
	 */
	protected Predicate[] criaPredicados(
		List<?> listaDeParametros, Class<E> klass, Root<E> root, SingularAttribute<? super E, ?> attribute) {

		Predicate predicate = null;
		EntityType<E> type = entityManager.getMetamodel().entity(klass);
		List<Predicate> predicates = new ArrayList<>();
		//index conta 
		int index = 0;
		List subList = new ArrayList();
		
		for(Object item : listaDeParametros) {
			index++;
			subList.add(item);
			if(index % 1000 == 0) {
				predicate = root.get(type.getSingularAttribute(attribute.getName())).in(subList);
				predicates.add(predicate);
				subList = new ArrayList();
			} 
		}
		if(!subList.isEmpty()) {
			predicate = root.get(type.getSingularAttribute(attribute.getName())).in(subList);
			predicates.add(predicate);
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	@Override
	public Long count() throws SystemException {
		try {
			builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			root =  query.from(persistentClass);			
			
			query.select(builder.count(root));

			TypedQuery<Long> typedQuery = entityManager.createQuery(query);

			List<Long> lista = typedQuery.getResultList();
			return lista.get(0);
		} catch(Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	/**
	 * Cria uma lista de {@link Selection} cujo atributos sao campos de um VO
	 * @param builder {@link CriteriaBuilder} sendo utilizado na query
	 * @param entidade que esta sendo feita a consulta
	 * @param root da consulta
	 * @param objectValue VO que contem alguns campos da entidade
	 * @return {@link CompoundSelection} da ser utilizado na consulta
	 * @throws SystemException se os campos no VO nao existem na entidade
	 */
	protected CompoundSelection createCompoundSelection(CriteriaBuilder builder, 
			Class entidade, Root root, Class objectValue) throws SystemException {
		
		EntityType type = entityManager.getMetamodel().entity(entidade);
		List<Selection<?>> lista = new ArrayList<>();
		for (Field field : objectValue.getDeclaredFields()) {
			try {
				if (field.getName() != null) {
					lista.add(root.get(type.getSingularAttribute(field.getName())));
				}
			} catch (Exception e) {
				throw new SystemException (e);
			}
		}
		return builder.construct(objectValue, lista.toArray(new Selection<?>[lista.size()]));
	}
	
	/**
	 * Obtem o domain da classe
	 *
	 * @return {@link Class}
	 */
	protected Class getDomainClass() {
		if(persistentClass == null) {
			ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
			persistentClass = (Class<E>) thisType.getActualTypeArguments()[0];
		}
		return persistentClass;
	}
	
	@Override
	public List<E> findByColumnsWithLikeResultList(Map<String, Object> query, Integer maxResult) throws SystemException {
		return findByColumnsWithLikeResultList(query, null, maxResult);
	}
	
	@Override
	public List<E> findByColumnsWithLikeResultList(Map<String, Object> colunas, String entidade, Integer maxResult, String ...ordenadoPor ) throws SystemException {

		try { 
			List<Predicate> predicates = new ArrayList<>();
			for (Map.Entry<String, Object> item : colunas.entrySet()) {
				String chave = item.getKey();
				String param = colunas.get(chave) + MATCH_MODE_START;
				if(entidade == null) {
					predicates.add(builder.like(root.get(chave), param));
				} else {	
					predicates.add(builder.like(root.get(entidade).get(chave), param));
				}
			}			
			createOrderBy(ordenadoPor);

			criteriaQuery.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));			
			
			return buildTypedQuery(null, maxResult).getResultList();
		} catch(Exception e) {
			throw new SystemException(e);
		}
	}

	private void createOrderBy(String... ordenadoPor) {
		if(ordenadoPor != null) {
			if(ordenadoPor.length == 1) {
				criteriaQuery.orderBy(builder.asc(root.get(ordenadoPor[0])));
			} else if(ordenadoPor.length > 1) {
				List<Order> ordenacao = new ArrayList<>();
				for(String campo : ordenadoPor) {
					ordenacao.add(builder.asc(root.get(campo)));
				}
				criteriaQuery.orderBy(ordenacao);
			}
		}
	}
	
	private void createOrderBy(ezute.Order order) {
		if (order != null) {
			for ( Entry<SortOrder, String> entry : order.getValues().entrySet()) {
				SortOrder tipo = entry.getKey();
				String propertieName = entry.getValue();
				
				if (propertieName != null && !"".equals(propertieName)) {
					if (SortOrder.ASCENDING.equals(tipo)) {
						criteriaQuery.orderBy(builder.asc(root.get(propertieName)));
					} else {
						criteriaQuery.orderBy(builder.desc(root.get(propertieName)));
					}
				}
			}
		}
	}
	
	private TypedQuery<E> buildTypedQuery(Integer startPosition, Integer maxResult) {
		TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);
		if(startPosition != null && startPosition > 0) {
			typedQuery.setFirstResult(startPosition);
		}
		if(maxResult != null && maxResult > 0) {
			typedQuery.setMaxResults(maxResult);
		}
		return typedQuery;
	}

	public CriteriaBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(CriteriaBuilder builder) {
		this.builder = builder;
	}
}
