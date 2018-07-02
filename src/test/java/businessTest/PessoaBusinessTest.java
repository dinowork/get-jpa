package businessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import manager.PessoaManager;
import model.Pessoa;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PessoaBusinessTest {
	
	PessoaManager dao = new PessoaManager();
	
	@Test
	public void t1_salvar() throws Exception {
		Pessoa salvaPessoa = new Pessoa();
		String nome = "Marias";
		salvaPessoa.setNome(nome);
		salvaPessoa.setDataNascimento(new Date());
		salvaPessoa.setEmail("pedro@jota.com.br");
		dao.salvar(salvaPessoa);		
		Pessoa consultaPessoa = dao.consultarPorNome(nome);
		assertEquals(consultaPessoa.getNome(),nome);
	}
	
	@Test
	public void t2_consultarPorNome() throws Exception {
		Pessoa lPessoa = dao.consultarPorNome("Pedro");
		assertEquals(lPessoa.getNome(), "Pedro");	
	}
	
	@Test
	public void t3_consultar() throws Exception {
		List<Pessoa>  lPessoa = dao.consultar();
		assertTrue(lPessoa.size() > 0);
	}
	
	@Test
	public void t4_excluir() throws Exception {
		dao.excluir(7L);		
	}
	
}
