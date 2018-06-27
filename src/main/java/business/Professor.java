package business;

import javax.persistence.*;

@Entity
public class Professor {
	
	@Id
    @SequenceGenerator(name="prof_seq",sequenceName="prof_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="prof_seq")
    @Column(name = "ID", updatable=false)
	private Long id;
	
	@Column(name="NOME")
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	

}
