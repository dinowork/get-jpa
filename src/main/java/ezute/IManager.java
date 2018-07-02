package ezute;

import java.io.Serializable;
import java.util.List;
import java.util.Map;






/**
 * @param <E>
 * @param <ID>
 */
@SuppressWarnings("rawtypes")
public interface IManager<E extends AbstractEntityBasic, T extends Serializable> {

	/**
	 * Metodo para buscar uma entidade por Id
	 *
	 * @param id da entidade
	 * @return {@link E} - entidade do id informado, caso nao exista o id informado, o metodo retornara null
	 * @throws SystemException caso ocorra algum erro de runtime ao efetuar a procura
	 * @throws  
	 */
	public E find(T id) throws SystemException;
	
	/**
	 * Metodo para inserir uma entidade
	 *
	 * @param entity a ser inserida
	 * @return {@link E} - Entidade inserida
	 * @throws SystemException caso ocorra algum erro de runtime
	 */
	public E insert(E entity) throws SystemException;
	
	/**
	 * Metodo para atualizar uma entidade
	 *
	 * @param entity a ser atualizada
	 * @return {@link E} - entidade Atualizada
	 * @throws SystemException caso ocorra algum erro de runtime
	 */
	public E update(E entity) throws SystemException;
	
	/**
	 * Metodo para listar todas as entidades
	 *
	 * @return {@link List<E>} - Lista de entidades
	 * @throws SystemException 
	 */
	public List<E> findAll() throws SystemException;
	
	/**
	 * Metodo para listar todas as entidades que possuem os ids informados
	 *
	 * @param listIds lista de identificadores a serem buscados 
	 * @return {@link List<E>} - Lista de entidades que possuem os ids informados
	 * @throws SystemException caso ocorra algum erro de runtime ao buscar entidade
	 */
	public List<E> findAllByIdIn(List<T> listIds) throws SystemException;
	
	/**
	 * Lista todos dados de uma entidade, ordenado por uma lista de parametros
	 * 
	 * @param name parametros para ordenacao
	 * @return {@link List<E>} Lista ordenado pela lista de parametro
	 * @throws SystemException caso ocorra algum erro de runtime
	 */
	public List<E> findAllAscOrderBy(String...name) throws SystemException;

	/**
	 * Lista todos dados da entidade, ordenado pelo atributo da entidade + nome do campo
	 *
	 * @param nomeAtributoEntidade atributo(entidade)
	 * @param nomeCampo atributo da entidade
	 * @return {@link List<E>} ordenado pelo parametro
	 * @throws SystemException 
	 */
	public List<E> findAllOrderBy(String nomeAtributoEntidade, String nomeCampo) throws SystemException;
	
	/**
	 * Metodo que lista os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e valor (valor do parametro)(entre os valores e utilizado
	 * o operador AND(&&))
	 *
	 * @param query - Mapa contendo os campos e seus valores para fazer a query.
	 * @param maxResult Quantidade de registros a ser limitado a busca
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 */
	public List<E> findByColumnsResultList(Map<String, Object> query, Integer maxResult) throws SystemException;

	/**
	 * Metodo que busca os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e valor (valor do parametro)
	 *
	 * @param query - Mapa contendo os campos e seus valores para fazer a query.
	 * @return {@link @} - Entidade Selecionada
	 * @throws SystemException caso ocorra algum erro de runtime
	 */
	public E findByColumnsSingleResult(Map<String, Object> query) throws SystemException;
	
	/**
	 * Metodo que faz uma busca por todos os registros que possuem
	 * os valores de um campo no operador in()
	 *
	 * @param campo o campo a ser feita a busca usando o operado in()
	 * @param valores a serem adicionados ao in()
	 * @return {@link List<E>} - Lista de Entidades Selecionadas
	 * @throws SystemException 
	 */
	public List<E> findAllByColumnsIn(String campo, List<String> valores) throws SystemException;
	
	/**
	 * Metodo que lista os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e 
	 * valor (valor do parametro)(entre os valores e utilizado o operador AND(&&))
	 *
	 * @param colunas - Mapa contendo os campos e seus valores para fazer a query.
	 * @param entidade - Nome da entidade para ser feita a busca dentro de um objeto 
	 * @param maxResult - Quantidade de registros a ser limitado a busca
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 */
	public List<E> findByColumnsResultList(Map<String, Object> colunas, String entidade, Integer maxResult, String... ordenadoPor)
		throws SystemException;
	
	/**
	 * 
	 * Metodo que lista os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e 
	 * valor (valor do parametro)(entre os valores e utilizado o operador AND(&&))
	 *
	 * @param colunas - Mapa contendo os campos e seus valores para fazer a query.
	 * @param entidade - Nome da entidade para ser feita a busca dentro de um objeto 
	 * @param startPosition Numero de inicio dos registros
	 * @param maxResult - Quantidade de registros a ser limitado a busca
	 * @param order objeto contendo um mapa com as propriedade de ordenacao e os tipos de ordenacao ASC ou DESC de cada uma delas
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 * 
	 * 
	 */
	public List<E> findByColumnsResultListWithPaginator(Map<String, Object> colunas, String entidade, Integer startPosition, Integer maxResult, Order order)
			throws SystemException;
	
	/**
	 * Metodo que tras um registro de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e 
	 * valor (valor do parametro)(entre os valores e utilizado o operador AND(&&))
	 *
	 * @param query - Mapa contendo os campos e seus valores para fazer a query.
	 * @param entidade - Nome da entidade para ser feita a busca dentro de um objeto 
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 */
	public E findByColumnsSingleResult(Map<String, Object> query, String entidade)	throws SystemException;

	/**
	 * Metodo faz a validacao de um campo e um valor , podendo ser usado para validar 
	 * dados dentro outra entidade.
	 * Ira retorna um valor maior que zero, caso o valor para o campo exista 
	 * e 0 caso o valor nao exista.
	 *
	 * @param campo a ser validado
	 * @param valor a ser validado
	 * @param entidade a ser validada
	 * @return {@link Long} - 0 caso nao esteja sendo utilizado  ou 1 ou  mais, caso esteja sendo utilizado
	 * @throws SystemException 
	 */
	public Long validaCampoPorValor(String campo, String valor, String entidade) throws SystemException;

	/**
	 * Metodo para listar todos os registros de uma entidades com um limite de registros.
	 *
	 * @param maxResult - Quantidade limite de registros na busca.
	 * @return {@link List<E>} - Lista de entidades
	 * @throws SystemException Caso ocorra algum erro.
	 */
	public List<E> findAll(Integer maxResult) throws SystemException;
	
	/**
	 * Metodo para listar todos os registros de uma entidades com paginacao.
	 * 
	 * @param startPosition Numero de inicio dos registros  
	 * @param maxResult Quantidade maxima de registros
	 * @param order objeto contendo um mapa com as propriedade de ordenacao e os tipos de ordenacao ASC ou DESC de cada uma delas
	 * @return {@link List<E>} - Lista de entidades
	 * @throws SystemException Caso ocorra algum erro.
	 */
	public List<E> findAllWithPaginator(Integer startPosition, Integer maxResult, Order order) throws SystemException;

	/**
	 * Lista todos dados de uma entidade, ordenado por uma lista de parametros
	 *
	 * @param maxResult Quantidade de registros a ser buscado na base
	 * @param name parametros para ordenacao
	 * @return {@link List<E>} Lista ordenado pela lista de parametro
	 * @throws SystemException caso ocorra algum erro de runtime
	 */
	public List<E> findAllAscOrderBy(Integer maxResult, String...name) throws SystemException;

	/**
	 * Lista todos dados da entidade, ordenado pelo atributo da entidade + nome do campo
	 *
	 * @param nomeAtributoEntidade atributo(entidade)
	 * @param nomeCampo atributo da entidade
	 * @param maxResult Limite de registros 
	 * @return {@link List<E>} ordenado pelo parametro
	 * @throws SystemException 
	 */
	public List<E> findAllOrderBy(String nomeAtributoEntidade, String nomeCampo, Integer maxResult) 
		throws SystemException;
	
	/**
	 * Metodo para efetuar um count
	 * 
	 * @return {@link Long} quantidade de registros
	 * @throws SystemException Caso ocorra algum erro de Runtime
	 */
	public Long count() throws SystemException;
		
	/**
	 * Insercao em lote
	 * @param listEntity
	 * @throws SystemException
	 */
	public void batchInsert(List<E> listEntity) throws SystemException; 
	
	/**
	 * Atualizacao em lote
	 * @param listEntity
	 * @throws SystemException
	 */
	public void batchUpdate(List<E> listEntity) throws SystemException;
	
	/**
	 * Método que lista os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e valor (valor do parametro) usando o operador LIKE.
	 * Caso o seja passado mais de uma coluna será utilizada a condição OR.  
	 *
	 * @param colunas - Mapa contendo os campos e seus valores para fazer a query.
	 * @param maxResult - Quantidade de registros a ser limitado a busca
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 */
	public List<E> findByColumnsWithLikeResultList(Map<String, Object> colunas, Integer maxResult) throws SystemException;

	/**
	 * Método que lista os registros de uma entidade a partir
	 * de um mapa de objeto (nome da coluna) e valor (valor do parametro) usando o operador LIKE.
	 * Caso o seja passado mais de uma coluna será utilizada a condição OR.  
	 *
	 * @param colunas - Mapa contendo os campos e seus valores para fazer a query.
	 * @param entidade - Nome da entidade para ser feita a busca dentro de um objeto 
	 * @param maxResult - Quantidade de registros a ser limitado a busca
	 * @param ordenadoPor - Nome das colunas que serão utilizadas na ordenação.
	 * @return {@link List<E>} - Lista de Entidades selecionadas
	 * @throws SystemException 
	 */
	public List<E> findByColumnsWithLikeResultList(Map<String, Object> colunas, String entidade, Integer maxResult,
			String... ordenadoPor) throws SystemException;
	
	
}

