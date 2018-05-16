/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdt;
/**
*@author Daniela Amim
*@foco AutomaÃ§Ã£o do backlog 99009
 */

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestMestreDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;


public class CandidatoWebTest extends PlcWebTestCase{
	
	PlcWebTestMestreDetalhe casoDeUso = new PlcWebTestMestreDetalhe("candidato.xml");
	
	public void testCandidatoWebTest(){
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro");
		//verifica que ao acessar a lÃ³gica nenhuma exceÃ§Ã£o Ã© exibida
		redirecionar("/f/n/candidatomdt");
		verificarTextoPresente("Candidato");
		verificarTextoNaoPresente("lazy");
		digitar("Luiz InÃ¡cio","corpo:formulario:nome");
		digitar("06/06/2006","corpo:formulario:dataNascimento");
		digitar("Trazer melhorias para os trabalhadores","corpo:formulario:ideologia");
		clicarAba("HistoricoPolitico");
		digitar("Governador","corpo:formulario:historicoPolitico:0:cargoAnterior");
		digitar("PSDB","corpo:formulario:historicoPolitico:0:partidoAnterior");
		digitar("NÃ£o atendia","corpo:formulario:historicoPolitico:0:motivoSaida");
		clicarAba("Partido");
		digitar("PT","corpo:formulario:partido:0:nome");
		digitar("10","corpo:formulario:partido:0:forca");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		
		LOG.info("FLUXO 2: Pesquisa registro e edita");
		redirecionar("/f/n/candidatomdt?&id="+id);
		verificarTextoPresente("Candidato");
		//verifica que o select do detalhe por demanda nÃ£o foi realizado
		verificarTextoNaoPresente("HISTORICO_POLITICO");
		
		//verifica na ediÃ§Ã£o se Ã© possÃ­vel inserir novos detalhes
		clicarAba("HistoricoPolitico");
		//verifica que o select do detalhe e do mestre nÃ£o foram realizados ao recuperar o detalhe por demanda
		verificarTextoNaoPresente("candidatoe0_.ID_CANDIDATO");
		verificarTextoNaoPresente("partido0_.ID_CANDIDATO");
		esperar(2000);
		clicarBotaoNovo();
		esperar(2000);
		verificarCampo("Governador", "input", "corpo:formulario:historicoPolitico:0:cargoAnterior");
		verificarCampo("PSDB", "input", "corpo:formulario:historicoPolitico:0:partidoAnterior");
		verificarCampo("NÃ£o atendia", "input", "corpo:formulario:historicoPolitico:0:motivoSaida");
		verificarCampo("", "input", "corpo:formulario:historicoPolitico:1:cargoAnterior");		
		verificarCampo("", "input", "corpo:formulario:historicoPolitico:1:partidoAnterior");
		verificarCampo("", "input", "corpo:formulario:historicoPolitico:1:motivoSaida");
		digitar("cargo teste", "corpo:formulario:historicoPolitico:1:cargoAnterior");
		digitar("Governador3","corpo:formulario:historicoPolitico:2:cargoAnterior");
		digitar("PSDB3","corpo:formulario:historicoPolitico:2:partidoAnterior");
		digitar("NÃ£o atendia3","corpo:formulario:historicoPolitico:2:motivoSaida");
		clicarBotaoGravar();
		verificarTextoPresente("Partido Anterior Ã© obrigatÃ³rio(a) na linha 2. [ HistoricoPolitico ]");
		verificarTextoPresente("Motivo Saida Ã© obrigatÃ³rio(a) na linha 2. [ HistoricoPolitico ]");
		digitar("teste", "corpo:formulario:historicoPolitico:1:partidoAnterior");
		digitar("novo teste", "corpo:formulario:historicoPolitico:1:motivoSaida");
		clicarBotaoGravar();
		esperar(2000);
		verificarTextoPresente("Registro gravado com sucesso");
		
		//apaga o detalhe da Ãºltima aba, clicando no checkbox e em seguida no botÃ£o gravar
		clicarAba("Partido");
		clicarCheckBox("corpo:formulario:partido:0:indExcPlc");
		clicarBotaoLabel("Gravar");
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
		
		LOG.info("FLUXO 3: Clonar registro");
		clicarBotaoLabel("Clonar");
		verificarTextoPresente("Clonagem realizada com sucesso. Altere as informaÃ§Ãµes e submeta a gravaÃ§Ã£o para criar um novo registro. ");
		esperar(3000);
		//verifica se os detalhes por demanda foram clonados
		verificarCampo("Governador", "input", "corpo:formulario:historicoPolitico:0:cargoAnterior");
		verificarCampo("PSDB", "input", "corpo:formulario:historicoPolitico:0:partidoAnterior");
		verificarCampo("NÃ£o atendia", "input", "corpo:formulario:historicoPolitico:0:motivoSaida");
		verificarCampo("cargo teste", "input", "corpo:formulario:historicoPolitico:1:cargoAnterior");
		verificarCampo("teste", "input", "corpo:formulario:historicoPolitico:1:partidoAnterior");
		verificarCampo("novo teste", "input", "corpo:formulario:historicoPolitico:1:motivoSaida");
		digitar("Dani","corpo:formulario:nome");
		clicarBotaoGravar();
		esperar(2000);
		verificarTextoPresente("Registro gravado com sucesso");
		
		//exclui registro clonado
		casoDeUso.excluirRegistro();
		
		LOG.info("FLUXO 4: Pesquisa registro e exclui");
		redirecionar("/f/n/candidatomdt?&id="+id);
		verificarTextoPresente("Candidato");
		casoDeUso.excluirRegistro();
		
		//deconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
		
		
	}

}
