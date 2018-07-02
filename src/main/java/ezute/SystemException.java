package ezute;

/**
 * Classe responsavel pelas exceptions do manager.
 * 
 * @author rootdeveloper
 */
public class SystemException extends Exception {

	/**
	 *Construtor da classe ManagerExceptions
	 **/
	public SystemException() {
		super();
	}
	/**
	 * 
	 * @param message Mensagem de Erro
	 * @param cause Mensagem de Erro
	 */
	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * 
	 * @param message Mensagem de Erro
	 */
	public SystemException(String message) {
		super(message);
	}
	/**
	 * 
	 * @param cause Mensagem de Erro
	 */
	public SystemException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -5793850268363093942L;

}

