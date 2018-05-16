/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdt;
/**
* @author Daniela Amim
* @foco Teste referente a automaÃ§Ã£o do backlog 103072, homologaÃ§Ã£o de detalhe com argumento 
* 
*/
import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestMestreDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;

public class VerificaPesquisaDetalheWebTest extends PlcWebTestCase{
	
	PlcWebTestMestreDetalhe casoDeUso = new PlcWebTestMestreDetalhe("candidato.xml");
	
	String eval;
	
	public void testVerificaPesquisaDetalheWebTest(){
		
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
		clicarAba("Partido");
		digitar("PT","corpo:formulario:partido:0:nome");
		digitar("10","corpo:formulario:partido:0:forca");
		digitar("PSDB","corpo:formulario:partido:1:nome");
		digitar("1","corpo:formulario:partido:1:forca");
		digitar("PL","corpo:formulario:partido:2:nome");
		digitar("2","corpo:formulario:partido:2:forca");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		
		LOG.info("FLUXO 2: Pesquisa registro e insere novos detalhes");
		redirecionar("/f/n/candidatosel?fwPlc=candidatomdt&");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		clicarXpath("//tbody/tr[@id='"+id+"']/td[2]");
		verificarTextoPresente("Candidato");
		clicarAba("Partido");
		esperar(3000);
		clicarBotaoNovo();
		digitar("PMDB","corpo:formulario:partido:3:nome");
		digitar("3","corpo:formulario:partido:3:forca");
		digitar("SL","corpo:formulario:partido:4:nome");
		digitar("6","corpo:formulario:partido:4:forca");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		//pesquisa o detalhe
	//	TODO - 6.0 - Aguardando implementaÃ§Ã£o de pesquisa em detalhe com argumentos no novo padrÃ£o de QBE
	//	digitar("P","corpo:formulario:partido_nome");
	//	clicarBotaoLabel("Pesq. Detalhe");
		
		//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Partido".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Partido\" presente", "OK");
		
		//verifica se os campos exibidos estÃ£o corretos
		verificarCampo("PT", "input", "corpo:formulario:partido:0:nome");
		verificarCampo("10", "input", "corpo:formulario:partido:0:forca");
		verificarCampo("PSDB", "input", "corpo:formulario:partido:1:nome");
		verificarCampo("1", "input", "corpo:formulario:partido:1:forca");
		verificarCampo("PL", "input", "corpo:formulario:partido:2:nome");
		verificarCampo("2", "input", "corpo:formulario:partido:2:forca");
		
		//pesquisa outro detalhe
//		digitar("S","corpo:formulario:partido_nome");
//		clicarBotaoLabel("Pesq. Detalhe");
		
		//verificar paginaÃ§Ã£o
 //   	verificarTextoPresente("PÃ¡gina 1 de 1");
    	//garante que a aba corrente Ã© partido
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Partido".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Partido\" presente", "OK");
		
		//verifica se os campos exibidos estÃ£o corretos
	//	verificarCampo("SL", "input", "corpo:formulario:partido:0:nome");
	//	verificarCampo("6", "input", "corpo:formulario:partido:0:forca");
		
		//excluir registro
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
	
		
	}

}
