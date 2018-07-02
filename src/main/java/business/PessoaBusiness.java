package business;

import java.util.List;

import manager.PessoaManager;
import model.Pessoa;

public class PessoaBusiness {

	PessoaManager mPessoa = new PessoaManager();

	public void salvar(Pessoa pessoa) throws Exception {
		mPessoa.salvar(pessoa);		
	}
	
	public void alterar(Pessoa pessoa) throws Exception {	
		mPessoa.alterar(pessoa);		
	}
	
	public void exluir(Pessoa pessoa) throws Exception {	
		mPessoa.salvar(pessoa);		
	}
	
	public Pessoa consultarPorId(Long id) throws Exception {	
		return mPessoa.consultarPorId(id);
	}

	
}
