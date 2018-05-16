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

public class ManutencaoEmpresaWebTest extends PlcWebTestCase{
	
	PlcWebTestSubDetalhe casoDeUso = new PlcWebTestSubDetalhe("empresa.xml");
	
	public void testManutencaoEmpresaWebTest(){
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro:");
		redirecionar("/f/n/manutencaoempresamds");
		verificarTextoPresente("ManutenÃ§Ã£o de Empresa");
		digitar("Plc","corpo:formulario:nome");
		digitar("12.000,00","corpo:formulario:capitalSocial");
		digitar("1234567891234567891","corpo:formulario:cnpjBasico");
		digitar("Emp parceira","corpo:formulario:parceiros:0:nomeEmpresa");
		digitar("01/01/2000","corpo:formulario:parceiros:0:dataParceria");
		digitar("100","corpo:formulario:parceiros:0:contrato:0:numeroContrato");
		digitar("contrato associados","corpo:formulario:parceiros:0:contrato:0:escopoContrato");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		String id = casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		
		LOG.info("FLUXO 2: Edita o registro e valida detalhes");
		redirecionar("/f/n/manutencaoempresasel");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		clicarXpath("//tr[@id='"+id+"']//td[3]");
		verificarTextoPresente("ManutenÃ§Ã£o de Empresa");
		//insere um novo detalhe endereco
		clicarBotaoNome("corpo:formulario:novoComponentemanutencaoempresaDet");
		verificarCampo("", "input", "corpo:formulario:endereco:0:bairro");
		verificarCampo("", "input", "corpo:formulario:endereco:0:cep");
		verificarCampo("", "input", "corpo:formulario:endereco:0:logradouro");
		verificarCampo("", "input", "corpo:formulario:endereco:0:numero");
		verificarCampo("[Selecione]", "select", "corpo:formulario:endereco:0:uf");
		digitar("Funcionarios","corpo:formulario:endereco:0:bairro");
		digitar("30987650","corpo:formulario:endereco:0:cep");
		digitar("ParaÃ­ba","corpo:formulario:endereco:0:logradouro");
		digitar("221","corpo:formulario:endereco:0:numero");
		selecionarOpcao("corpo:formulario:endereco:0:uf", "Minas Gerais");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		esperar(1000);
		//recuperar portlet de detalhe por demanda
		getSelenium().click("portletmanutencaoempresaDet2");
		esperar(2000);

		verificarCampo("Emp parceira", "input", "corpo:formulario:parceiros:0:nomeEmpresa");
		verificarCampo("01/01/2000", "input", "corpo:formulario:parceiros:0:dataParceria");
		verificarCampo("100", "input", "corpo:formulario:parceiros:0:contrato:0:numeroContrato");
		verificarCampo("contrato associados", "input", "corpo:formulario:parceiros:0:contrato:0:escopoContrato");
		
		//verificar que ao gravar sem alterar o subdetalhe, nenhuma mensagem de obrigatoriedade Ã© exibida
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 3: Edita o registro e insere novos detalhes");
		redirecionar("/f/n/manutencaoempresasel");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		clicarXpath("//tr[@id='"+id+"']//td[2]");
		verificarTextoPresente("ManutenÃ§Ã£o de Empresa");
		//clicarBotaoLabel("+");
		clicarXpath("//span[@id='portletmanutencaoempresaDet2']");
		esperar(2000);
		clicarXpath("//button[@id='corpo:formulario:novoComponentemanutencaoempresaDet2']");
		esperar(2000);
		digitar("Emp parceira2","corpo:formulario:parceiros:1:nomeEmpresa");
		digitar("02/02/2000","corpo:formulario:parceiros:1:dataParceria");
		digitar("101","corpo:formulario:parceiros:1:contrato:0:numeroContrato");
		digitar("contrato associados2","corpo:formulario:parceiros:1:contrato:0:escopoContrato");
		clicarBotaoGravar();
		verificarTextoNaoPresente("Escopo Contrato Ã© obrigatÃ³rio(a) na linha 1.1.");
		verificarTextoPresente("Registro gravado com sucesso");
		
		//verifica que o primeiro detalhe permanece na tela e o segundo detalhe foi gravado
		verificarCampo("Emp parceira", "input", "corpo:formulario:parceiros:0:nomeEmpresa");
		verificarCampo("01/01/2000", "input", "corpo:formulario:parceiros:0:dataParceria");
		verificarCampo("100", "input", "corpo:formulario:parceiros:0:contrato:0:numeroContrato");
		verificarCampo("contrato associados", "input", "corpo:formulario:parceiros:0:contrato:0:escopoContrato");
		
		verificarCampo("Emp parceira2", "input", "corpo:formulario:parceiros:1:nomeEmpresa");
		verificarCampo("02/02/2000", "input", "corpo:formulario:parceiros:1:dataParceria");
		verificarCampo("101", "input", "corpo:formulario:parceiros:1:contrato:0:numeroContrato");
		verificarCampo("contrato associados2", "input", "corpo:formulario:parceiros:1:contrato:0:escopoContrato");
		
				
		//exclui o registro da aplicaÃ§Ã£o
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
		
		
	}

}