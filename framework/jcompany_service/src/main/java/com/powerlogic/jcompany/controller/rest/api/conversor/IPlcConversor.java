/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.api.conversor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

public interface IPlcConversor<C> {

	/**
	 * Metodo invocado apos a execução do controle, para escrever a resposta,
	 * caso tenha sido um objeto.
	 * 
	 * Faz a conversão de um objeto, para o formato esperado pela requisição.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 * @param outputStream
	 *            Stream para escrita da resposta da requisição.
	 */
	public void writeEntity(C controller, OutputStream outputStream);

	/**
	 * Metodo invocado antes da execução de metodos PUT|POST|DELETE para a
	 * leitura do corpo da requisição.
	 * 
	 * Faz a conversão do corpo da requisição, em um Objeto esperado pelo
	 * controle.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 * @param inputStream
	 *            Stream para leitura do conteúdo da requisição.
	 */
	public void readEntity(C controller, InputStream inputStream);

	/**
	 * Metodo invocado apos a execução do controle, para escrever a resposta,
	 * caso tenha sido uma coleção de objetos.
	 * 
	 * Faz a conversão de um objeto, para o formato esperado pela requisição.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 * @param outputStream
	 *            Stream para escrita da resposta da requisição.
	 */
	public void writeEntityCollection(C controller, OutputStream outputStream);

	/**
	 * Metodo invocado antes da execução de metodos PUT|POST|DELETE para a
	 * leitura do corpo da requisição.
	 * 
	 * Faz a conversão do corpo da requisição, em uma lista de Objetos esperados
	 * pelo controle.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 * @param inputStream
	 *            Stream para leitura do conteúdo da requisição.
	 */
	public void readEntityCollection(C controller, InputStream inputStream);
	
	/**
	 * Metodo invocado para tratamento de erro, para o caso de ocorrer uma
	 * exceção na execução do controle.
	 * 
	 * Faz a conversão da exceção em uma formato, que a requisição entenda.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 * @param outputStream
	 *            Stream para escrita da resposta da requisição.
	 * @param exception
	 *            Exceção ocorrida na execução do controle.
	 */
	public void writeException(C container, OutputStream outputStream, Throwable ex);
	
	public void writeException(C controller, OutputStream outputStream, PlcMessage exception);
	
	public void writeMessages(OutputStream outputStream);
	
	/**
	 * Metodo invocado antes da execução de todos os metodos para a conversão de
	 * parâmetros da requisição, em objetos no controle.
	 * 
	 * @param controller
	 *            Controle que atende a requisição.
	 */
	public void readParameters(C controller);
	
	public Map<String, List<String>> getMessages() ;
}

