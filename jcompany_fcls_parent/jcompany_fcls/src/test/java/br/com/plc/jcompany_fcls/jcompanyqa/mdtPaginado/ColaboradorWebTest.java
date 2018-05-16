/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdtPaginado;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestMestreDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;
import com.thoughtworks.selenium.Selenium;


/**
* @author Daniela Amim
* @foco Teste referente a automaÃ§Ã£o do backlog 96629, homologaÃ§Ã£o de detalhe paginado em facelets 
* 
*/

public class ColaboradorWebTest extends PlcWebTestCase{
	
	PlcWebTestMestreDetalhe casoDeUso = new PlcWebTestMestreDetalhe("colaborador.xml");
	String eval;
	Selenium sel;
	public void testColaboradorWebTest(){
		 sel = getSelenium();
		
		//faz login na aplicaÃ§Ã£o 
    	casoDeUso.doLogin();
    	
    	//FLUXO 1: Cadastrar Colaborador
    	String id = cadastraColaborador();
    	
    	//FLUXO 2: Pesquisa e valida navegador do detalhe paginado
		verificaNavegador(id);
    	//navega para a prÃ³xima pÃ¡gina
		navegaProxima();
		//navega pÃ¡gina anterior
    	navegaAnterior();
		//navega para a Ãºltima pÃ¡gina
    	navegaUltima();
    	//navega para a primeira pÃ¡gina
    	navegaPrimeira();
    	
    	//FLUXO 3: Valida navegador do detalhe paginado
    	verificaComboNavegador();
    	//seleciona combo navegador para a pÃ¡gina 2
    	selecionaProxima();
    	//selecionar combo navegador para a pÃ¡gina 1
    	selecionaAnterior();
    	
    	//FLUXO 4: Alterar detalhe paginado e verificar mensagem de alerta alteraÃ§Ã£o
    	verificaAlteracao();
    	
    	//FLUXO 5: Verificar BotÃ£o Visualizar Documento
    	visualizaDocumento(id);
    	
    	//FLUXO 6: Excluir Detalhe Paginado
    	excluiDetalhe();
    	
    	//FLUXO 7: Ecluir registro e desconectar
    	excluiRegistro();
	}
	
	private void excluiRegistro(){
		LOG.info("FLUXO 7: Excluir registro e desconectar");
		casoDeUso.excluirRegistro();
		casoDeUso.desconectar();
	}
	
	private void excluiDetalhe(){
		LOG.info("FLUXO 6: Excluir Detalhe Paginado");
		clicarAba("Promocao");
		clicarCheckBox("corpo:formulario:promocao:1:indExcPlc");
		clicarCheckBox("corpo:formulario:promocao:2:indExcPlc");
		clicarCheckBox("corpo:formulario:promocao:3:indExcPlc");
    	clicarBotaoGravar();
    	clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
    	verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
    	//verifica se os detalhes que nÃ£o foam excluÃ­dos estÃ£o corretos
    	verificarCampo("01/01/2001", "input", "corpo:formulario:promocao:0:dataPromocao");
    	verificarCampo("2.000,00", "input", "corpo:formulario:promocao:0:salario");
	}
	
	private void visualizaDocumento(String id){
		LOG.info("FLUXO 5: Verificar BotÃ£o Visualizar Documento");
		clicarBotaoPesquisar();
    	clicarXpath("//tbody/tr[@id='"+id+"']/td[3]");
    	verificarTextoPresente("Colaborador");
    	clicarBotaoLabel("Vis. Documento");
    	//verifica se os detalhes exibidos estÃ£o corretos
    	verificarCampo("jCompany", "span", "corpo:formulario:setor:0:nome");
    	verificarCampo("0089", "span", "corpo:formulario:setor:0:ramal");
    	verificarCampo("JC", "span", "corpo:formulario:setor:0:sigla");
    	verificarCampo("eCompany", "span", "corpo:formulario:setor:1:nome");
    	verificarCampo("0083", "span", "corpo:formulario:setor:1:ramal");
    	verificarCampo("EC", "span", "corpo:formulario:setor:1:sigla");	
    	verificarCampo("01/01/2001", "span", "corpo:formulario:promocao:0:dataPromocao");
    	verificarCampo("2.000,00", "span", "corpo:formulario:promocao:0:salario");
    	verificarCampo("01/01/2004", "span", "corpo:formulario:promocao:1:dataPromocao");
    	verificarCampo("4.000,00", "span", "corpo:formulario:promocao:1:salario");
    	verificarCampo("01/01/2009", "span", "corpo:formulario:promocao:2:dataPromocao");
    	verificarCampo("6.000,00", "span", "corpo:formulario:promocao:2:salario");
    	verificarCampo("01/01/2010", "span", "corpo:formulario:promocao:3:dataPromocao");
    	verificarCampo("10.000,00", "span", "corpo:formulario:promocao:3:salario");
    	clicarBotaoLabel("Editar Documento");
    	//verifica campo para garantir que voltou para a aba do mestre
    	verificarCampo("Luiz Carlos JosÃ©", "input", "corpo:formulario:nome");
	}
	
	private void verificaAlteracao(){
		LOG.info("FLUXO 4: Alterar detalhe paginado e verificar mensagem de alerta alteraÃ§Ã£o");
		clicarAba("Setor");
		//navega para a Ãºltima pÃ¡gina
    	//clicarBotaoLabel(">>");
		getSelenium().click("//button[@type='button' and @onclick=\"return _chain('plc.jq(\\'#detCorrPlc\\').val(\\'setor\\');plc.jq(\\'#detCorrPlcPaginado\\').val(\\'setor\\')','TrPage._autoSubmit(\\'corpo:formulario\\',\\'corpo:formulario:navLast\\',event,1);return false;',this,event,true)\"]");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 2 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//altera algum registro do paginado sem gravar
		digitar("Alterado","corpo:formulario:setor:0:nome");
		clicarBotaoAbrir();
		clicarConfirmacao("Existem modificaÃ§Ãµes pendentes neste formulÃ¡rio. Tem certeza de que deseja prosseguir sem salvÃ¡-las?", true);	
	}
	
	private void selecionaAnterior(){
		//seleciona pÃ¡gina 1 no navegador
		String selects = sel.getEval(
                "var result = [];"+
                "var elements = selenium.browserbot.getDocument().getElementsByTagName('select');                 "+
                "     for (var i = 0; i < elements.length; i++) {                                                 "+
                "           var campo = '';                                                                       "+
                "           campo = elements[i].name;                                                             "+
                "           result.push( campo );                                                                 "+
                "     } 																						  "+
                "result;" 
                );

    	String[] campos = StringUtils.split(selects, ",");
    	selecionarOpcao(campos[2], "1");
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("jCompany", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0089", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("JC", "input", "corpo:formulario:setor:0:sigla");
    	verificarCampo("eCompany", "input", "corpo:formulario:setor:1:nome");
    	verificarCampo("0083", "input", "corpo:formulario:setor:1:ramal");
    	verificarCampo("EC", "input", "corpo:formulario:setor:1:sigla");
	}
	
	private void selecionaProxima(){
		//seleciona pÃ¡gina 2 no navegador
		esperar(2000);
		clicarAba("Setor");
    	String selects = sel.getEval(
                "var result = [];"+
                "var elements = selenium.browserbot.getDocument().getElementsByTagName('select');                 "+
                "     for (var i = 0; i < elements.length; i++) {                                                 "+
                "           var campo = '';                                                                       "+
                "           campo = elements[i].name;                                                             "+
                "           result.push( campo );                                                                 "+
                "     } 																						  "+
                "result;" 
                );

    	String[] campos = StringUtils.split(selects, ",");
    	selecionarOpcao(campos[2], "2");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 2 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("Suporte", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0097", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("SP", "input", "corpo:formulario:setor:0:sigla");
	}
	
	private void verificaComboNavegador(){
		LOG.info("FLUXO 3: Valida navegador do detalhe paginado");
		clicarAba("Promocao");
		clicarBotaoNovo();
		digitar("01/01/2011", "corpo:formulario:promocao:4:dataPromocao");
    	digitar("11.000,00", "corpo:formulario:promocao:4:salario");
    	clicarBotaoGravar();
    	verificarTextoPresente("Registro gravado com sucesso");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© promocao
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Promocao".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Promocao\" presente", "OK");
    	verificarCampo("01/01/2001", "input", "corpo:formulario:promocao:0:dataPromocao");
    	verificarCampo("2.000,00", "input", "corpo:formulario:promocao:0:salario");
    	verificarCampo("01/01/2004", "input", "corpo:formulario:promocao:1:dataPromocao");
    	verificarCampo("4.000,00", "input", "corpo:formulario:promocao:1:salario");
    	verificarCampo("01/01/2009", "input", "corpo:formulario:promocao:2:dataPromocao");
    	verificarCampo("6.000,00", "input", "corpo:formulario:promocao:2:salario");
    	verificarCampo("01/01/2010", "input", "corpo:formulario:promocao:3:dataPromocao");
    	verificarCampo("10.000,00", "input", "corpo:formulario:promocao:3:salario");
	}
	
	private void navegaPrimeira(){
		//navega para a primeira pÃ¡gina
    	//clicarBotaoLabel("<<");
		getSelenium().click("//button[@type='button' and @onclick=\"return _chain('plc.jq(\\'#detCorrPlc\\').val(\\'setor\\');plc.jq(\\'#detCorrPlcPaginado\\').val(\\'setor\\')','TrPage._autoSubmit(\\'corpo:formulario\\',\\'corpo:formulario:navFirst\\',event,1);return false;',this,event,true)\"]");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("jCompany", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0089", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("JC", "input", "corpo:formulario:setor:0:sigla");
    	verificarCampo("eCompany", "input", "corpo:formulario:setor:1:nome");
    	verificarCampo("0083", "input", "corpo:formulario:setor:1:ramal");
    	verificarCampo("EC", "input", "corpo:formulario:setor:1:sigla");	
	}
	
	private void navegaUltima(){
		//navega para a Ãºltima pÃ¡gina
    	//clicarBotaoLabel(">>");
		getSelenium().click("//button[@type='button' and @onclick=\"return _chain('plc.jq(\\'#detCorrPlc\\').val(\\'setor\\');plc.jq(\\'#detCorrPlcPaginado\\').val(\\'setor\\')','TrPage._autoSubmit(\\'corpo:formulario\\',\\'corpo:formulario:navLast\\',event,1);return false;',this,event,true)\"]");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 2 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("Suporte", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0097", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("SP", "input", "corpo:formulario:setor:0:sigla");
	}
	
	private void navegaAnterior() {
		//navega para a pÃ¡gina anterior utilizando o navegador
    	//clicarBotaoLabel("<");
		getSelenium().click("//button[@type='button' and @onclick=\"return _chain('plc.jq(\\'#detCorrPlc\\').val(\\'setor\\');plc.jq(\\'#detCorrPlcPaginado\\').val(\\'setor\\')','TrPage._autoSubmit(\\'corpo:formulario\\',\\'corpo:formulario:navPrevious\\',event,1);return false;',this,event,true)\"]");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("jCompany", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0089", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("JC", "input", "corpo:formulario:setor:0:sigla");
    	verificarCampo("eCompany", "input", "corpo:formulario:setor:1:nome");
    	verificarCampo("0083", "input", "corpo:formulario:setor:1:ramal");
    	verificarCampo("EC", "input", "corpo:formulario:setor:1:sigla");
	}

	private void navegaProxima() {
		//navega para a prÃ³xima pÃ¡gina utilizando o navegador
		getSelenium().click("//button[@type='button' and @onclick=\"return _chain('plc.jq(\\'#detCorrPlc\\').val(\\'setor\\');plc.jq(\\'#detCorrPlcPaginado\\').val(\\'setor\\')','TrPage._autoSubmit(\\'corpo:formulario\\',\\'corpo:formulario:navNext\\',event,1);return false;',this,event,true)\"]");
    	//clicarBotaoLabel(">");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 2 de 2");
    	//garante que a aba corrente Ã© setor
    	eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("Suporte", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0097", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("SP", "input", "corpo:formulario:setor:0:sigla");
	}

	private void verificaNavegador(String id) {
		LOG.info("FLUXO 2: Pesquisa e valida navegador do detalhe");
		redirecionar("/f/n/colaboradorsel?fwPlc=colaboradormdt&");
    	clicarBotaoPesquisar();
    	esperar(2000);
    	clicarXpath("//tbody/tr[@id='"+id+"']/td[1]");
    	verificarTextoPresente("Colaborador");
    	clicarAba("Setor");
    	//verificar paginaÃ§Ã£o
    	verificarTextoPresente("PÃ¡gina 1 de 2");
    	//garante que a aba corrente Ã© setor
    	String eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlcPaginado').value;");
		assertTrue("Setor".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Setor\" presente", "OK");
		//verifica se os registros exibidos estÃ£o corretos
    	verificarCampo("jCompany", "input", "corpo:formulario:setor:0:nome");
    	verificarCampo("0089", "input", "corpo:formulario:setor:0:ramal");
    	verificarCampo("JC", "input", "corpo:formulario:setor:0:sigla");
    	verificarCampo("eCompany", "input", "corpo:formulario:setor:1:nome");
    	verificarCampo("0083", "input", "corpo:formulario:setor:1:ramal");
    	verificarCampo("EC", "input", "corpo:formulario:setor:1:sigla");
	}

	private String cadastraColaborador() {
		LOG.info("FLUXO 1: Cadastrar Colaborador");
		redirecionar("/f/n/colaboradormdt"); 
    	//preenche os campos da tela
    	digitar("Luiz Carlos JosÃ©", "corpo:formulario:nome");
    	digitar("30152419071", "corpo:formulario:cpf");
    	digitar("12/08/2008", "corpo:formulario:dataCadastro");
    	digitar("luiz@carlos.com.br", "corpo:formulario:email");
    	digitar("Daniela", "corpo:formulario:responsavelCadastro");
    	selecionarOpcao("corpo:formulario:estadoCivil", "Casado(a)");
    	selecionarOpcao("corpo:formulario:sexo", "Masculino");
    	clicarAba("Setor");
    	digitar("jCompany", "corpo:formulario:setor:0:nome");
    	digitar("0089", "corpo:formulario:setor:0:ramal");
    	digitar("JC", "corpo:formulario:setor:0:sigla");
    	digitar("eCompany", "corpo:formulario:setor:1:nome");
    	digitar("0083", "corpo:formulario:setor:1:ramal");
    	digitar("EC", "corpo:formulario:setor:1:sigla");
    	digitar("Suporte", "corpo:formulario:setor:2:nome");
    	digitar("0097", "corpo:formulario:setor:2:ramal");
    	digitar("SP", "corpo:formulario:setor:2:sigla");
    	clicarAba("Promocao");
    	digitar("01/01/2001", "corpo:formulario:promocao:0:dataPromocao");
    	digitar("2.000,00", "corpo:formulario:promocao:0:salario");
    	digitar("01/01/2004", "corpo:formulario:promocao:1:dataPromocao");
    	digitar("4.000,00", "corpo:formulario:promocao:1:salario");
    	digitar("01/01/2009", "corpo:formulario:promocao:2:dataPromocao");
    	digitar("6.000,00", "corpo:formulario:promocao:2:salario");
    	digitar("01/01/2010", "corpo:formulario:promocao:3:dataPromocao");
    	digitar("10.000,00", "corpo:formulario:promocao:3:salario");
    	clicarBotaoGravar();
    	esperar(10000);
    	verificarTextoPresente("Registro gravado com sucesso");
    	String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE); 
		return id;
	}

}
