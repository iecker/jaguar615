/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.jcompanyqa.mdt;

import com.powerlogic.jcompany.qa.PlcWebTestCase;
import com.powerlogic.jcompany.qa.PlcWebTestMestreDetalhe;
import com.powerlogic.jcompany.qa.Constantes.REGEX;

/**
 * 
 * @author moises_paula
 *
 */
public class AlbumWebTest extends PlcWebTestCase {

	PlcWebTestMestreDetalhe casoDeUso = new PlcWebTestMestreDetalhe("album.xml");
	
	public void testAlbumWebTest() {

		casoDeUso.doLogin();

		LOG.info("FLUXO 1:Cadastrar registro");		
		redirecionar("/f/n/albummdt");
		verificarTextoPresente("Album");		
		digitar("Daniela","corpo:formulario:nome");
		digitar("Picasso","corpo:formulario:artista");
		digitar("5","corpo:formulario:quantidadeFaixas");
		
		LOG.info("FLUXO 2:Cadastrar detalhes");
		esperar(3000);
		clicarAba("Musica");
		esperar(3000);
		digitar("Faixa 1","corpo:formulario:musica:0:nome");
		digitar("Faixa 2","corpo:formulario:musica:1:nome");
		digitar("Faixa 3","corpo:formulario:musica:2:nome");
		digitar("Faixa 4","corpo:formulario:musica:3:nome");
		digitar("Autor 1","corpo:formulario:musica:0:compositor");
		digitar("Autor 2","corpo:formulario:musica:1:compositor");
		digitar("Autor 3","corpo:formulario:musica:2:compositor");
		digitar("Autor 4","corpo:formulario:musica:3:compositor");
		//gravar registro
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 3:Incluir arquivo");
		clicarXpath("//a/span[@id='span-tab-1']");
		//clicarAba("Album");
		digitar("/home/hudson/arquivos_teste/teste.txt", "corpo:formulario:uploadFile_encarte");
		clicarBotaoLabel("Upload");
		verificarCampo("/home/hudson/arquivos_teste/teste.txt", "input", "corpo:formulario:colecaoMultiplosArquivos_encarte:0:nomeArquivo_encarte");
		clicarBotaoGravar();		
		String id=casoDeUso.getRegEx(REGEX.REGEX_ID_MESTRE);
		verificarTextoPresente("Registro gravado com sucesso");
		
		LOG.info("FLUXO 4:Recuperar registro e inserir arquivos");
		clicarBotaoAbrir();
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(3000);
		clicarXpath("//tr[@id='" + id +"']/td[1]");
		esperar(1000);
		verificarCampo("teste.txt", "input", "corpo:formulario:colecaoMultiplosArquivos_encarte:0:nomeArquivo_encarte");
		//inserir outro arquivo
		digitar("/home/hudson/arquivos_teste/teste2.txt", "corpo:formulario:uploadFile_encarte");
		clicarBotaoLabel("Upload");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
		verificarCampo("teste2.txt", "input", "corpo:formulario:colecaoMultiplosArquivos_encarte:1:nomeArquivo_encarte");
				
		
		//editar o registro, verificar arquivos anexados e apagar um arquivo anexado
		clicarBotaoAbrir();
		esperar(3000);
		clicarBotaoPesquisar();
		esperar(2000);
		clicarXpath("//tr[@id='" + id +"']/td[1]");
		verificarTextoPresente("Album");
		verificarCampo("teste.txt","input","corpo:formulario:colecaoMultiplosArquivos_encarte:0:nomeArquivo_encarte");
		verificarCampo("teste2.txt","input","corpo:formulario:colecaoMultiplosArquivos_encarte:1:nomeArquivo_encarte");
		//apaga um arquivo anexado
		clicarCheckBox("corpo:formulario:colecaoMultiplosArquivos_encarte:1:excluiArquivo_encarte");
		clicarBotaoGravar();
		verificarTextoPresente("Registro gravado com sucesso");
				
		LOG.info("FLUXO 5: Alterar os detalhes ");
		clicarAba("Musica");
		//verifica se os detalhe continuam preenchidos
		verificarCampo("Faixa 1", "input", "corpo:formulario:musica:0:nome");
		verificarCampo("Autor 1", "input", "corpo:formulario:musica:0:compositor");
		verificarCampo("Faixa 2", "input", "corpo:formulario:musica:1:nome");
		verificarCampo("Autor 2", "input", "corpo:formulario:musica:1:compositor");
		verificarCampo("Faixa 3", "input", "corpo:formulario:musica:2:nome");
		verificarCampo("Autor 3", "input", "corpo:formulario:musica:2:compositor");
		verificarCampo("Faixa 4", "input", "corpo:formulario:musica:3:nome");
		verificarCampo("Autor 4", "input", "corpo:formulario:musica:3:compositor");
		
		//altera o primeiro detalhe e apaga o segundo detalhe
		digitar("Alterado","corpo:formulario:musica:0:nome");
		clicarCheckBox("corpo:formulario:musica:1:indExcPlc");
		clicarBotaoGravar();
		
		clicarConfirmacao("Tem certeza de que deseja excluir os registros selecionados?", true);
		verificarTextoPresente("Registro gravado com sucesso. Detalhes e/ou SubDetalhes foram excluÃ­dos.");
		
		verificarCampo("Alterado", "input", "corpo:formulario:musica:0:nome");
		verificarCampo("Autor 1", "input", "corpo:formulario:musica:0:compositor");
		verificarCampo("Faixa 3", "input", "corpo:formulario:musica:1:nome");
		verificarCampo("Autor 3", "input", "corpo:formulario:musica:1:compositor");
		verificarCampo("Faixa 4", "input", "corpo:formulario:musica:2:nome");
		verificarCampo("Autor 4", "input", "corpo:formulario:musica:2:compositor");
		
		LOG.info("FLUXO 6: Excluindo registro");		
		clicarBotaoLabel("Excluir");
		clicarConfirmacao("Tem certeza que deseja excluir?", true);
		verificarTextoPresente("Registro excluÃ­do com sucesso.");
		
	}
	
}
