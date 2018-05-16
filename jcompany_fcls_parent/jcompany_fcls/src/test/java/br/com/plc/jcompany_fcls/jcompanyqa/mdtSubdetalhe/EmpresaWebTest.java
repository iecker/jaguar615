/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdtSubdetalhe;
/**
*@author Daniela Amim
*@foco Automatizar os backlogs 98935 e 98936
*/
import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestSubDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;

public class EmpresaWebTest extends PlcWebTestCase{
	
	PlcWebTestSubDetalhe casoDeUso=new PlcWebTestSubDetalhe("empresa.xml");
	
	public void testEmpresaWebTest(){
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro:");
		redirecionar("/f/n/empresamds");
		esperar(3000);
		verificarTextoPresente("Empresa");
		digitar("Powerlogic","corpo:formulario:nome");
		digitar("123.000,00","corpo:formulario:capitalSocial");
		digitar("345678910111412315","corpo:formulario:cnpjBasico");
		clicarAba("Endereco");
		digitar("Funcionarios","corpo:formulario:endereco:0:bairro");
		digitar("30987650","corpo:formulario:endereco:0:cep");
		digitar("ParaÃ­ba","corpo:formulario:endereco:0:logradouro");
		digitar("221","corpo:formulario:endereco:0:numero");
		selecionarOpcao("corpo:formulario:endereco:0:uf", "Minas Gerais");
		digitar("Complemento1","corpo:formulario:endereco:0:complemento:0:dadosComplementares");
		clicarBotaoGravar();
		esperar(3000);
		verificarTextoPresente("Registro gravado com sucesso");
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		
		LOG.info("FLUXO 2: Edita o registro e valida detalhes");
		redirecionar("/f/n/empresasel");
		esperar(2000);
		clicarBotaoPesquisar();
		esperar(2000);
		
		clicarXpath("//tr[@id='"+id+"']//td[3]");
		verificarTextoPresente("Empresa");
		clicarAba("Endereco");
		esperar(2000);
		
		//verifica que o detalhe foi recuperado corretamente
		verificarCampo("Funcionarios", "input", "corpo:formulario:endereco:0:bairro");
		verificarCampo("30987650", "input", "corpo:formulario:endereco:0:cep");
		verificarCampo("ParaÃ­ba", "input", "corpo:formulario:endereco:0:logradouro");
		verificarCampo("221", "input", "corpo:formulario:endereco:0:numero");
		verificarCampo("Minas Gerais", "select", "corpo:formulario:endereco:0:uf");
		verificarCampo("Complemento1", "input", "corpo:formulario:endereco:0:complemento:0:dadosComplementares");
		
		//verificar que ao gravar sem alterar o subdetalhe, nenhuma mensagem de obrigatoriedade Ã© exibida
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		//apaga um detalhe e verifica se aba corrente Ã© endereco
		clicarCheckBox("corpo:formulario:endereco:0:indExcPlc");
		clicarBotaoGravar();
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
		String eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlc').value;");
		assertTrue("Endereco".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Endereco\" presente", "OK");
		
		//insere um novo detalhe endereco
		clicarBotaoNovo();
		verificarCampo("", "input", "corpo:formulario:endereco:0:bairro");
		verificarCampo("", "input", "corpo:formulario:endereco:0:cep");
		verificarCampo("", "input", "corpo:formulario:endereco:0:logradouro");
		verificarCampo("", "input", "corpo:formulario:endereco:0:numero");
		verificarCampo("[Selecione]", "select", "corpo:formulario:endereco:0:uf");
		verificarCampo("", "input", "corpo:formulario:endereco:0:complemento:0:dadosComplementares");
		digitar("Funcionarios","corpo:formulario:endereco:0:bairro");
		digitar("30987650","corpo:formulario:endereco:0:cep");
		digitar("ParaÃ­ba","corpo:formulario:endereco:0:logradouro");
		digitar("221","corpo:formulario:endereco:0:numero");
		esperar(3000);
		selecionarOpcao("corpo:formulario:endereco:0:uf", "Minas Gerais");
		digitar("Complemento1","corpo:formulario:endereco:0:complemento:0:dadosComplementares");
		esperar(3000);
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		/*//verifica se aba corrente Ã© endereco
		eval = getSelenium().getEval("selenium.browserbot.getDocument().getElementById('detCorrPlc').value;");
		assertTrue("Endereco".equalsIgnoreCase(eval));
		LOG.debug("Verificando aba corrente \"Endereco\" presente", "OK");*/
				
		//insere um novo detalhe parceiro
		clicarAba("Parceiros");
		esperar(3000);
		clicarBotaoNovo();
		esperar(2000);
		digitar("empresa de consultoria","corpo:formulario:parceiros:0:nomeEmpresa");
		digitar("03/01/1999","corpo:formulario:parceiros:0:dataParceria");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 3: Edita o registro e insere novos detalhes");
		redirecionar("/f/n/empresasel");
		esperar(2000);
		clicarBotaoPesquisar();
		esperar(2000);
		clicarXpath("//tr[@id='"+id+"']/td[2]");
		verificarTextoPresente("Empresa");
		clicarAba("Endereco");
		esperar(2000);
		clicarBotaoNovo();
		esperar(1000);
		digitar("Novo","corpo:formulario:endereco:1:bairro");
		digitar("30190580","corpo:formulario:endereco:1:cep");
		digitar("Teste","corpo:formulario:endereco:1:logradouro");
		digitar("1","corpo:formulario:endereco:1:numero");
		selecionarOpcao("corpo:formulario:endereco:1:uf", "Minas Gerais");
		digitar("Complemento2","corpo:formulario:endereco:1:complemento:0:dadosComplementares");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		//verifica que o primeiro detalhe permanece na tela e o segundo detalhe foi gravado
		verificarCampo("Funcionarios", "input", "corpo:formulario:endereco:0:bairro");
		verificarCampo("30987650", "input", "corpo:formulario:endereco:0:cep");
		verificarCampo("ParaÃ­ba", "input", "corpo:formulario:endereco:0:logradouro");
		verificarCampo("221", "input", "corpo:formulario:endereco:0:numero");
		verificarCampo("Minas Gerais", "select", "corpo:formulario:endereco:0:uf");
		verificarCampo("Complemento1", "input", "corpo:formulario:endereco:0:complemento:0:dadosComplementares");
		
		verificarCampo("Novo", "input", "corpo:formulario:endereco:1:bairro");
		verificarCampo("30190580", "input", "corpo:formulario:endereco:1:cep");
		verificarCampo("Teste", "input", "corpo:formulario:endereco:1:logradouro");
		verificarCampo("1", "input", "corpo:formulario:endereco:1:numero");
		verificarCampo("Minas Gerais", "select", "corpo:formulario:endereco:1:uf");
		verificarCampo("Complemento2", "input", "corpo:formulario:endereco:1:complemento:0:dadosComplementares");
		
		//insere novos detalhes na aba parceiros
		clicarAba("Parceiros");
		esperar(2000);
		clicarBotaoNovo();
		esperar(1000);
		digitar("empresa de testers","corpo:formulario:parceiros:1:nomeEmpresa");
		digitar("01/03/1999","corpo:formulario:parceiros:1:dataParceria");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		

		//verifica que o primeiro detalhe permanece na tela e o segundo detalhe foi gravado
		verificarCampo("empresa de consultoria", "input", "corpo:formulario:parceiros:0:nomeEmpresa");
		verificarCampo("03/01/1999", "input", "corpo:formulario:parceiros:0:dataParceria");

		verificarCampo("empresa de testers", "input", "corpo:formulario:parceiros:1:nomeEmpresa");
		verificarCampo("01/03/1999", "input", "corpo:formulario:parceiros:1:dataParceria");

		
		//exclui o registro da aplicaÃ§Ã£o
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
		
	}

}



