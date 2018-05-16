/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.integration;

import java.util.List;

import com.powerlogic.jcompany.commons.integration.jmonitor.PlcClientDTO;



public interface IPlcJMonitorApi {
	
	public enum NIVEL_LOG {
		INFO ,
		ERROR,
		FATAL;
	}
	
	public enum CICLO_VIDA {
		INICIO ,
		FIM;
	}
	
	/**
	 * Registra todas as constantes necessárias para os Appenders funcionarem
	 * @param ignorar URLs a ignorar em interpretações
	 * @param enviarQueryString Se é para enviar a parte QueryString de URLs para o jMonitor
	 * @param enderecoJMS O endereço de um serviço JMS (opcional, podendo usar o de baixo)
	 * @param enderecoSMTP O endereço de um serviço SMTP (opcional, podendo usar o de cima)
	 * @param emailRemetente O email do remetente para envio de mensagens de exceção
	 * @param emailFatal A lista de emails para receber mensagens de nível FATAL
	 * @param emailError A lista de emails para receber mensagens de nível ERROR
	 * @param modoExecucao "P", "T" ou "D", conforme definido no web.xml
	 * @param indPseudoProducao Indicador que liberar algumas funções para testes do jMonitor, sem alterar o funcionamento total do modo "P" (simulador)
	 * @param nomeAplicacao Nome da aplicacao conforme configurada
	 * @param siglaAplicacao Sigla da aplicacao conforme configurada
	 * @param uriHTMLBaseEmail Endereco de arquivo HTML para uso como base no envio de email (template)
	 * @return True se os Appenders foram iniciados corretamente
	 */
	public boolean inicia(String ignorar, boolean enviarQueryString,
			String enderecoJMS, String enderecoSMTP,String emailRemetente,String emailFatal,String emailError,
			String modoExecucao,boolean indPseudoProducao,String nomeAplicacao, String siglaAplicacao, 
			String uriHTMLBaseEmail,String nomeServidorAplicacao,
			String ipServidorAplicacao);
	

	/**
	 * 	Se tem jCompany Monitor, então programa para enviar log.info() a cada 5 minutos, para atualizar o
	 *  "tempo no ar" (disponibilidade) da aplicação
	 */
	public void iniciaMonitoriaAtivaAplicacao();

	/**
	 * Envia mensagens de log por JMS para ser armazenada em SGBD-R.
	 * @param tipo Apenas um identificador para agrupamento de consultas
	 * @param nivelLog níveis típicos de log
	 * @param mensagem A mensagem a ser registrada
	 */
	public void log(String tipo,NIVEL_LOG nivelLog,String mensagem);
	
	/**
	 * Registro de informações de um "cliente/usuário", utilizados no registro de cliques (estatísticas)
	 * @param requestURI
	 * @param contextPath
	 * @param queryString
	 * @param acao
	 * @param evento
	 * @param clienteDTO
	 * @param listaMsgErro
	 * @param entidade
	 * @param args
	 * @return
	 */
	public boolean logRequisicao(String requestURI, String contextPath, String queryString,String acao, String evento, 
			PlcClientDTO clienteDTO, List listaMsgErro, Object entidade, List args);

	/**
	 * Registra operacões em escopo de sessão do usuário (cliente), como inicio e fim.
	 * @param cli POJO encapsulando dados para mensagens
	 */
	public void logSessao(String idSessao,CICLO_VIDA cicloVida);

}
