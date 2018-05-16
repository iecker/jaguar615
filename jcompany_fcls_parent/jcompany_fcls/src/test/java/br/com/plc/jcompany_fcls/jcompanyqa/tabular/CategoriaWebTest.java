/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.tabular;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestTabular;

/**
* @author Daniela Amim
* @foco Teste referente a automaÃ§Ã£o do backlog 96626, homologaÃ§Ã£o de lÃ³gicas com chave natural 
* 
*/

public class CategoriaWebTest extends PlcWebTestCase {

	PlcWebTestTabular casoDeUso = new PlcWebTestTabular("categoria.xml");
	
	public void testCategoriaWebTest() {
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro");
		redirecionar("/f/n/categoriatab");
		verificarTextoPresente("Categoria");
		clicarBotaoNovo();
		digitar("02","corpo:formulario:plcLogicaItens:1:idNatural_sigla");
		digitar("B","corpo:formulario:plcLogicaItens:1:nome");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		//Pesquisa registro		
		clicarBotaoPesquisar();
		verificarCampo("02", "input", "corpo:formulario:plcLogicaItens:1:idNatural_sigla");
		verificarCampo("B", "input", "corpo:formulario:plcLogicaItens:1:nome");
		
		LOG.info("FLUXO 2: Verificar mensagem de registros duplicados");
		clicarBotaoNovo();
		digitar("02","corpo:formulario:plcLogicaItens:2:idNatural_sigla");
		digitar("B","corpo:formulario:plcLogicaItens:2:nome");
		clicarBotaoGravar();
		verificarTextoPresente("Tentativa de inserir registros duplicados. Termo informado: B");
		clicarBotaoPesquisar();
		
		LOG.info("FLUXO 3: Verificar mensgaem de campo obrigatÃ³rio");
		clicarBotaoNovo();
		digitar("TESTE","corpo:formulario:plcLogicaItens:2:nome");
		clicarBotaoGravar();
		verificarTextoPresente("Sigla Ã© obrigatÃ³rio(a) na linha 3.");
		clicarBotaoPesquisar();
		
		//exclui o registro
		clicarCheckBox("corpo:formulario:plcLogicaItens:1:indExcPlc");
		clicarBotaoGravar();
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 3: Verificar se existem checkbox marcado");
		//verifica se checkbox estÃ¡ marcado apÃ³s excluir e clicar em novo
		clicarBotaoNovo();
		StringBuffer sb = new StringBuffer();
		sb.append("function contaCheckBox(){");
			sb.append("var i = 0;");
			sb.append("var elem;");	
			sb.append("for (i = 0; true; i++){");
				sb.append("elem = selenium.browserbot.getDocument().getElementById('corpo:formulario:plcLogicaItens:' +i+ ':indExcPlc');");
				sb.append("if (!elem){");
					sb.append("break;");
				sb.append("}");
			sb.append("}");
			sb.append("return i;");
		sb.append("}");
		sb.append("contaCheckBox();");
		String quantidadeCheckExclusao = getSelenium().getEval(sb.toString());
		assertTrue(Integer.valueOf(quantidadeCheckExclusao)==1);
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
	}

}
