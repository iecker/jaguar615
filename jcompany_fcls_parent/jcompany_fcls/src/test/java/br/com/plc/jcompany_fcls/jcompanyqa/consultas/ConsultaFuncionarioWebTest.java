/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.consultas;
/**
 * @author Daniela Amim
 * @foco Validar os cenÃ¡rios bÃ¡sicos (inserir, editar e apagar detalhe)
 * de uma lÃ³gica consulta mestre- mantÃ©m detalhe
 */
import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestMestreDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;

public class ConsultaFuncionarioWebTest extends PlcWebTestCase{
	
	PlcWebTestMestreDetalhe casoDeUso = new PlcWebTestMestreDetalhe("consultaFuncionario.xml");
	
	public void testConsultaFuncionarioWebTest(){
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1:Cadastrar registro");
		redirecionar("/f/n/funcionariomdt");
		verificarTextoPresente("FuncionÃ¡rio");
		digitar("Daniela","corpo:formulario:nome");
		digitar("06/12/1989","corpo:formulario:dataNascimento");
		
		//getSelenium().runScript("document.getElementById('corpo:formulario:departamento').value = '22';");
		
		digitar("22","corpo:formulario:departamento");
		
		
		esperar(2000);
		clicarRadioBox("corpo:formulario:sexo", "0");
		esperar(2000);
		clicarCheckBox("corpo:formulario:temCursoSuperior");
		esperar(3000);
		
		getSelenium().runScript("FCKeditorAPI.GetInstance('corpo:formulario:observacao').SetHTML('Analista de Testes');");
		
		esperar(1000);
		clicarAba("Endereco");
		digitar("Rua A","corpo:formulario:endereco_rua");
		digitar("1","corpo:formulario:endereco_numero");
		digitar("Bairro B","corpo:formulario:endereco_bairro");
		digitar("87654321","corpo:formulario:endereco_cep");
		selecionarOpcao("corpo:formulario:endereco_uf", "Minas Gerais");
		clicarAba("HistoricoFuncional");
		digitar("EstagiÃ¡ria","corpo:formulario:historicoFuncional:0:descricao");
		digitar("1.200,00","corpo:formulario:historicoFuncional:0:salario");
		clicarAba("Dependente");
		digitar("Maria Luiza","corpo:formulario:dependente:0:nome");
		esperar(2000);
		clicarBotaoGravar();
		esperar(2000);
		verificarTextoPresente("A cardinalidade Ã© de 2 a 4 Dependentes.");
		digitar("Ana Gabriela","corpo:formulario:dependente:1:nome");
		esperar(2000);
		clicarBotaoGravar();
		esperar(2000);
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		verificarTextoPresente("Registro gravado com sucesso");
		verificarCampo("22", "input", "corpo:formulario:departamento");
		verificarCampo("jCompany", "input", "lookup_corpo:formulario:departamento");
		
		LOG.info("FLUXO 2: Consultar mestre");
		redirecionar("/f/n/consultafuncionariosmd");
		verificarTextoPresente("Consulta FuncionÃ¡rio");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		getSelenium().click("//tr[@id='"+id+"']/td[5]");
		esperar(3000);
		verificarTextoPresente("Consulta FuncionÃ¡rio");
		clicarBotaoNome("corpo:formulario:novoComponenteconsultafuncionarioDet");
		verificarCampo("EstagiÃ¡ria", "input", "corpo:formulario:historicoFuncional:0:descricao");
		verificarCampo("1.200,00", "input", "corpo:formulario:historicoFuncional:0:salario");
		verificarCampo("Maria Luiza", "input", "corpo:formulario:dependente:0:nome");
		esperar(2000);
		//apagar detalhe e inserir novo
		clicarBotaoNome("corpo:formulario:novoComponenteconsultafuncionarioDet2");
		esperar(3000);
		digitar("Gabriel","corpo:formulario:dependente:2:nome");
		esperar(1000);
		clicarCheckBox("corpo:formulario:dependente:0:indExcPlc");
		esperar(1000);
		clicarBotaoGravar();
		esperar(2000);
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
		esperar(1000);
		verificarCampo("EstagiÃ¡ria", "input", "corpo:formulario:historicoFuncional:0:descricao");
		verificarCampo("1.200,00", "input", "corpo:formulario:historicoFuncional:0:salario");
		esperar(1000);
		verificarCampo("Gabriel", "input", "corpo:formulario:dependente:1:nome");
		
		LOG.info("FLUXO 3: Excluir registro");
		redirecionar("/f/n/funcionariosel");
		esperar(2000);
		clicarCheckBox("corpo:formulario:temCursoSuperior");
		esperar(2000);
		clicarBotaoPesquisar();
		esperar(2000);
		getSelenium().click("//tr[@id='"+id+"']/td[5]");
		verificarTextoPresente("FuncionÃ¡rio");
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
		
	
		
	}

}
