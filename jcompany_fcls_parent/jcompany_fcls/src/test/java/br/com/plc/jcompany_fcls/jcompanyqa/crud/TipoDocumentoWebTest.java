/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.crud;
/**
* @author Daniela Amim
* @foco Teste referente a automaÃ§Ã£o do backlog 96626, homologaÃ§Ã£o de lÃ³gicas com chave natural 
* 
*/
import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestCrud;

public class TipoDocumentoWebTest extends PlcWebTestCase{
	
	PlcWebTestCrud casoDeUso = new PlcWebTestCrud("tipodocumento.xml");
	
	public void testTipoDocumentoWebTest(){
		
		//faz login na aplicaÃ§Ã£o 
		casoDeUso.doLogin();
		  
		LOG.info("FLUXO 1: Cadastrar registro");
		redirecionar("/f/n/tipodocumentoman");
		digitar("1","corpo:formulario:idNatural_codigo");
		digitar("CPF","corpo:formulario:idNatural_sigla");
		digitar("cpf","corpo:formulario:descricao");
		selecionarOpcao("corpo:formulario:categoria", "A");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		verificarCampo("1", "input", "corpo:formulario:idNatural_codigo");
		verificarCampo("CPF", "input", "corpo:formulario:idNatural_sigla");
		verificarCampo("A", "select", "corpo:formulario:categoria");
		
		LOG.info("Pesquisa o registro");
		redirecionar("/f/n/tipodocumentosel");
		esperar(1000);
		clicarBotaoPesquisar();
		esperar(1000);
		
		clicarXpath("//tbody/tr[@id='0']/td[2]");
		
		verificarTextoPresente("ManutenÃ§Ã£o de Tipo Documento");
		verificarCampo("A", "select", "corpo:formulario:categoria");
		//clona o registro e verifica que a chave natural nÃ£o foi clonada
		clicarBotaoLabel("Clonar");
		verificarTextoPresente("Clonagem realizada com sucesso. Altere as informaÃ§Ãµes e submeta a gravaÃ§Ã£o para criar um novo registro.");
		verificarCampo("", "input", "corpo:formulario:idNatural_codigo");
		verificarCampo("", "input", "corpo:formulario:idNatural_sigla");
		digitar("clonado","corpo:formulario:descricao");
		clicarBotaoGravar();
		verificarTextoPresente("Codigo Ã© obrigatÃ³rio(a)");
		verificarTextoPresente("Sigla Ã© obrigatÃ³rio(a)");
		digitar("1","corpo:formulario:idNatural_codigo");
		digitar("CPF","corpo:formulario:idNatural_sigla");
		clicarBotaoGravar();
		esperar(2000);
		verificarTextoPresente("JÃ¡ existe um registro com uma chave informada.");
		digitar("2","corpo:formulario:idNatural_codigo");
		digitar("CNPJ","corpo:formulario:idNatural_sigla");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		verificarCampo("2", "input", "corpo:formulario:idNatural_codigo");
		verificarCampo("CNPJ", "input", "corpo:formulario:idNatural_sigla");
		verificarCampo("A", "select", "corpo:formulario:categoria");
		
		//exclui o registro que foi clonado
		casoDeUso.excluirRegistro();
		
		LOG.info("FLUXO 3: Recupera o registro e exclui");
		redirecionar("/f/n/tipodocumentoman?&evento=y&codigo=1&sigla=CPF");
		verificarTextoPresente("ManutenÃ§Ã£o de Tipo Documento");
		verificarCampo("1", "input", "corpo:formulario:idNatural_codigo");
		verificarCampo("CPF", "input", "corpo:formulario:idNatural_sigla");
		
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
	}
	

}
