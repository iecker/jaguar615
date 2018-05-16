/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.crud;

/**
 * @author Daniela Amim
 * @foco Validar os cenÃ¡rios bÃ¡sicos (cadastrar, editar e excluir) de uma crud com componente em um projeto
 * facelets.
 */

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestCrud;
import com.powerlogic.jcompany.qa.Constantes.REGEX;

public class DepartamentoWebTest extends PlcWebTestCase{
	
	PlcWebTestCrud casoDeUso= new PlcWebTestCrud("departamento.xml");
	
	public void testDepartamentoWebTest(){
		 
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro");
		redirecionar("/f/n/departamentoman");
		verificarTextoPresente("Departamento");
		digitar("Departamento XX", "corpo:formulario:descricao");
		//preencher os campos do componente
		digitar("Rua A","corpo:formulario:endereco_rua");
		digitar("122","corpo:formulario:endereco_numero");
		digitar("Bairro XX","corpo:formulario:endereco_bairro");
		digitar("12345678","corpo:formulario:endereco_cep");
		selecionarOpcao("corpo:formulario:endereco_uf", "Minas Gerais");
		esperar(2000);
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		
		LOG.info("FLUXO 2: Alterar o registro e faz validaÃ§Ã£o de campo obrigatÃ³rio no componente");
		redirecionar("/f/n/departamentoman?id="+id);
		verificarTextoPresente("ManutenÃ§Ã£o de Departamento");
		esperar(2000);
		//preenche o vinculado
		digitar("22","corpo:formulario:departamentoPai");
		esperar(3000);
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		esperar(1000);
		//verifica se o campo do vinculado estÃ¡ preenchido
		verificarCampo("22", "input", "corpo:formulario:departamentoPai");
		verificarCampo("jCompany", "input", "lookup_corpo:formulario:departamentoPai");
		//modifica o campo uf
		selecionarOpcao("corpo:formulario:endereco_uf", "[Selecione]");
		esperar(2000);
		clicarBotaoGravar();
		verificarTextoPresente(" Uf Ã© obrigatÃ³rio(a)");
		selecionarOpcao("corpo:formulario:endereco_uf", "Minas Gerais");
		esperar(2000);
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 3: Pesquisa o registro novamente usando argumento de pesquisa");
		redirecionar("//f/n/departamentosel");
		esperar(1000);
		digitar("22","corpo:formulario:departamentoPai");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		verificarCampo("22", "input", "corpo:formulario:departamentoPai");
		verificarCampo("jCompany", "input", "lookup_corpo:formulario:departamentoPai");
		verificarTextoNaoPresente("Erro ao processar pesquisa");
		esperar(3000);
		clicarXpath("//tr[@id='"+id+"']//td[2]");
		verificarTextoPresente("ManutenÃ§Ã£o de Departamento");
		
		//apaga o registro da aplicaÃ§Ã£o
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();		
		
		
		
	}

}
