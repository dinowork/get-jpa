package ezute;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Super class of entities
 * @author rootdeveloper
 *
 * @param <T>
 */
@MappedSuperclass
public abstract class AbstractEntityBasic <T extends Serializable> implements Serializable {
	
	private static final long serialVersionUID = 8154849507989443059L;
	
	/**
	 * definicao do metodo que acessa o id.
	 * 
	 * @return {@link T}
	 */
	public abstract T getId ();
	
	/**
	 * definicao do metodo que popula o id.
	 * 
	 * @param id valor que sera atribuido ao id
	 */
	public abstract void setId (T id);
	
	/**
	 * Data criacao do registro
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;

	/**
	 * Data modificacao do registro
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_MODIFICACAO", nullable = false)
	private Date dataModificacao;
	
	/**
	 * Situação Registro (ATIVO ou INATIVO)
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_REGISTRO", nullable = false)
	private SituacaoRegistroEnum situacaoRegistro;

	/**
	 * Metodo de gravacao do novo registro. Nao pode ser invocado diretamente
	 */
	@PrePersist
	public void prePersist() {
		if(dataCriacao == null) {
			dataCriacao = new Date();
		}
		dataModificacao = dataCriacao;
	}

	/**
	 * Metodo de atualizado do registro. Nao pode ser invocado diretamente
	 */
	@PreUpdate
	public void preUpdate() {
		dataModificacao = new Date();
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao != null ? (Date) dataCriacao.clone() : null;
	}

	/**
	 * @param dataCriacao
	 *            the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao != null ? (Date) dataCriacao.clone() : null;
	}

	/**
	 * @return the dataModificacao
	 */
	public Date getDataModificacao() {
		return dataModificacao != null ? (Date) dataModificacao.clone() : null;
	}

	/**
	 * @param dataModificacao
	 *            the dataModificacao to set
	 */
	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao != null ? (Date) dataModificacao.clone() : null;
	}

	/**
	 * 
	 * @return situacaoRegistro
	 */
	public SituacaoRegistroEnum getSituacaoRegistro() {
		return situacaoRegistro;
	}

	/**
	 * 
	 * @param situacaoRegistro
	 */
	public void setSituacaoRegistro(SituacaoRegistroEnum situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}

}

