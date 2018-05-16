/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdtSubdetalhe;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestSubDetalhe;

/**
* @author Daniela Amim
* @foco Teste referente a automaÃ§Ã£o do backlog 96626, homologaÃ§Ã£o de lÃ³gicas com chave natural 
* 
*/
public class PedidoWebTest extends PlcWebTestCase{
	
	PlcWebTestSubDetalhe casoDeUso = new PlcWebTestSubDetalhe("pedido.xml");
	
	public void testPedidoWebTest(){
		
		//faz login na aplicaÃ§Ã£o
		casoDeUso.doLogin();
		
		LOG.info("FLUXO 1: Cadastrar registro");
		redirecionar("/f/n/pedidomds");
		verificarTextoPresente("Pedido");
		digitar("111","corpo:formulario:idNatural_numero");
		digitar("AfrÃ¢nio Neto","corpo:formulario:solicitante");
		digitar("123","corpo:formulario:tipoDocumento");
		digitar("CNH","corpo:formulario:tipoDocumentosigla");
		clicarAba("Produtos");
		digitar("1","corpo:formulario:produtos:0:idNatural_numeroProduto");
		digitar("Pasta de Dente","corpo:formulario:produtos:0:descricao");
		digitar("500","corpo:formulario:produtos:0:quantidade");
		digitar("1","corpo:formulario:produtos:0:comprador:0:idNatural_codigoComprador");
		digitar("SÃ¡vio Grossi","corpo:formulario:produtos:0:comprador:0:nome");
		//volta para a aba do mestre e verifica se o vinculado foi recuperado
		//clicarAba("Pedido");
		clicarXpath("//a/span[@id='span-tab-1']");
		verificarCampo("123", "input", "corpo:formulario:tipoDocumento");
		verificarCampo("CNH", "input", "corpo:formulario:tipoDocumentosigla");
		verificarCampo("Carteira Nacional de HabilitaÃ§Ã£o", "input", "lookup_corpo:formulario:tipoDocumento");
		clicarBotaoGravar();
		esperar(2000);
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 2: Recupera o registro");
		redirecionar("//f/n/pedidomds?&evento=y&numero=111");
		//verifica os valores do vinculado
		verificarCampo("123", "input", "corpo:formulario:tipoDocumento");
		verificarCampo("CNH", "input", "corpo:formulario:tipoDocumentosigla");
		verificarCampo("Carteira Nacional de HabilitaÃ§Ã£o", "input", "lookup_corpo:formulario:tipoDocumento");
		//insere outro detalhe/subdetalhe
		clicarAba("Produtos");
		verificarCampo("1", "input", "corpo:formulario:produtos:0:idNatural_numeroProduto");
		verificarCampo("Pasta de Dente", "input", "corpo:formulario:produtos:0:descricao");
		verificarCampo("500", "input", "corpo:formulario:produtos:0:quantidade");
		verificarCampo("1", "input", "corpo:formulario:produtos:0:comprador:0:idNatural_codigoComprador");
		verificarCampo("SÃ¡vio Grossi", "input", "corpo:formulario:produtos:0:comprador:0:nome");
		clicarBotaoNovo();
		digitar("2","corpo:formulario:produtos:1:idNatural_numeroProduto");
		digitar("Sabonete","corpo:formulario:produtos:1:descricao");
		digitar("250","corpo:formulario:produtos:1:quantidade");
		digitar("2","corpo:formulario:produtos:1:comprador:0:idNatural_codigoComprador");
		digitar("Novato","corpo:formulario:produtos:1:comprador:0:nome");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		//apaga um detalhe e um subdetalhe
		clicarCheckBox("corpo:formulario:produtos:0:indExcPlc");
		clicarCheckBox("corpo:formulario:produtos:1:comprador:0:idNatural_codigoComprador");
		clicarBotaoGravar();
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
		verificarCampo("2", "input", "corpo:formulario:produtos:0:idNatural_numeroProduto");
		verificarCampo("Sabonete", "input", "corpo:formulario:produtos:0:descricao");
		verificarCampo("250", "input", "corpo:formulario:produtos:0:quantidade");
		
		LOG.info("FLUXO 3: Clona o registro");
		clicarBotaoLabel("Clonar");
		verificarTextoPresente("Clonagem realizada com sucesso. Altere as informaÃ§Ãµes e submeta a gravaÃ§Ã£o para criar um novo registro.");
		//verifica que a chave natural nÃ£o foi clonada
		verificarCampo("", "input", "corpo:formulario:idNatural_numero");
		verificarCampo("","input","corpo:formulario:produtos:0:idNatural_numeroProduto");
		digitar("22","corpo:formulario:idNatural_numero");
		digitar("21","corpo:formulario:produtos:0:idNatural_numeroProduto");
		digitar("20","corpo:formulario:produtos:0:comprador:0:idNatural_codigoComprador");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		//verifica os valores do vinculado
		verificarCampo("123", "input", "corpo:formulario:tipoDocumento");
		verificarCampo("CNH", "input", "corpo:formulario:tipoDocumentosigla");
		verificarCampo("Carteira Nacional de HabilitaÃ§Ã£o", "input", "lookup_corpo:formulario:tipoDocumento");
		//verifica os valores do detalhe
		verificarCampo("21", "input", "corpo:formulario:produtos:0:idNatural_numeroProduto");
		verificarCampo("Sabonete", "input", "corpo:formulario:produtos:0:descricao");
		verificarCampo("250", "input", "corpo:formulario:produtos:0:quantidade");
		
		LOG.info("FLUXO 4: Pesquisa o registro novamente usando argumento de pesquisa");
		redirecionar("//f/n/pedidosel");
		digitar("123","corpo:formulario:tipoDocumento");
		digitar("CNH","corpo:formulario:tipoDocumentosigla");
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(3000);
		verificarTextoNaoPresente("Erro ao processar pesquisa");
		esperar(3000);
		verificarCampo("123", "input", "corpo:formulario:tipoDocumento");
		verificarCampo("CNH", "input", "corpo:formulario:tipoDocumentosigla");
		verificarCampo("Carteira Nacional de HabilitaÃ§Ã£o", "input", "lookup_corpo:formulario:tipoDocumento");
		esperar(3000);
		clicarXpath("//tr[@id='1']//td[2]");
		esperar(1000);
		verificarTextoPresente("Pedido");
				
		//exclui o registro que foi clonado
		casoDeUso.excluirRegistro();
		
		LOG.info("FLUXO 4: Recupera o registro e exclui");
		redirecionar("//f/n/pedidomds?&evento=y&numero=111");
		casoDeUso.excluirRegistro();
		
		//desconecta da aplicaÃ§Ã£o
		casoDeUso.desconectar();
		
	}

}
