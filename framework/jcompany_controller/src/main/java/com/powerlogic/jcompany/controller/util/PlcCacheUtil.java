/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.semaphoro.PlcReaderWriter;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * jCompany 1.0 Serviço de caching implementado segundo o padrão (Design Pattern) Singleton.
 * e utilizando algoritmo para garantia de Thread-Safed
 *
 * Para usar esta classe, basta a linha:
 * 	PlcCacheUtil servCache = PlcCacheUtil.getInstance();
 * ... 
 * servCache.adicionaObjeto(chaveString, <qualquer descendente de object>);
 * ... 
 * Object obj = servCache.recuperaObjeto(chaveString);<p>
 * IMPORTANTE: É a única classe com sufixo "Service" que usa DP Singleton (não extensível), para manutenção
 * da compatibilidade com versões anteriores.
 * 
 * @since jCompany 1.0
*/
@SPlcUtil
@QPlcDefault
public class PlcCacheUtil implements Serializable {

	@Inject
	protected transient  Logger log;
	
	public PlcCacheUtil(){	
		
	}

 	/**
 	 * Classe utilitária para manter singletons de caching
 	 */
	private PlcReaderWriter lock = new PlcReaderWriter();
		
	/**
	* Mapa de Objects - Caching de Objetos
	*/
	private HashMap cacheObject = new HashMap();
	
	/**
	* Mapa de contadores
	*/
	private HashMap controleEmail = new HashMap();
	
	/**
	* Variavel de bloqueio 
	*/
	private boolean bloqueioEnvioEmail = false;

	/**
	 * @return mapa de objetos em caching
	 */
	protected HashMap getCacheObject(){
		return cacheObject;
	}


	protected void setCacheObject(HashMap newVal){
		cacheObject=newVal;
	}

	
	/**
	* Adiciona um objeto qualquer ao caching
	* @param chave do Objeto no Caching
	* @param obj para ficar em caching
	*/
	public void putObject(String chave, Object obj) {
				
		log.debug( "########## Entrou no AdicionaObjeto");

		try {

			lock.gravacao();

			// Operaçãoes de atualização do caching thread-safed.

			HashMap objetos = getCacheObject();

			objetos.put(chave, obj);

		} finally {
			lock.gravacaoCompleta();
		}
		
	}
	
	/**
	 * Recupera objeto do caching
	 *
	 * @param  chave para recuperar objeto do caching
	 * @return Objeto em caching. Retorna null se não achou.
	*/
	public Object getObject(String chave){
	
		if (log.isDebugEnabled())
			log.debug( "########## Entrou no RecuperaObjeto para total"+
						size());

		try {   
			 
			lock.leitura();
			HashMap conteudo = getCacheObject();
			if (conteudo.containsKey(chave)) {
				return conteudo.get(chave);
			} else {
				log.debug( "Nao achou");
				return null;
			}
			 
		 } finally {
			 lock.leituraCompleta(); 
		 }
		
	}
	
	/**
	 * Operação padrão para recuperação thread-safed do total de 
	 * entradas no caching de Conteúdo de Objetos
	 *
	 * @return Total de objetos correntemente em caching
	*/
	public int size() {

		 try {   
			 
			 lock.leitura();
             
			 if (getCacheObject() != null) {
				 
				 // Recuperar Cache de OutputStream
				 HashMap m = getCacheObject();

				 return m.size();
			 } else
				 return 0;
			 
		 } finally {   lock.leituraCompleta(); }
		
	}
	
	/**
	* Limpa todos os registros do cache.
	*/
	public void initObject() {
		
		try {  
			 
			lock.gravacao();
			// Operaçãoes de atualização do caching thread-safed.
			setCacheObject(new HashMap());
			 
		} finally {   
			lock.gravacaoCompleta(); 
		}
		
	}
	
	/**
	* Limpa todos os registros do cache. Deve ser chamado somente ao final da aplicação
	*/
	public void removeAll() {
		
		HashMap m = getCacheObject();
		m.clear();
		
	}

	/**
	 * Recupera objeto do caching
	 *
	 * @param  chave para recuperar objeto do caching
	 * @return Objeto em caching. Retorna null se não achou.
	*/
	public static Object get(Object chave){
	
		PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		return cacheUtil.getObject(chave.toString());
		
	}	
	
    /**
     * @since jCompany 2.7.1
     * Mantém erros de inicialização no caching, para impedir uso da aplicação inválida.
     * Estas mensagens são validadas em cada action e disparadas para o usuário, caso existam, 
     * impedindo o uso indevido da aplicação
     * @param msg mensagem de erro conforme passada
     * @param he Exceção disparada em tempo de inicialização
     */
    public void addInitFatalError(String msg, Throwable he) {
        
        log.debug( "########## Entrou em adicionaErroFatalInicializacao");
        
        // Recupera se existir ou cria
        Map errosInicializacao;
        PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
        Object ret = cacheUtil.getObject(PlcConstants.ERRO.ERRO_INICIALIZACAO_CHAVE_CACHE);
        if (ret==null)
            errosInicializacao = new HashMap();
        else
            errosInicializacao = (Map) ret;
        
        errosInicializacao.put(msg,he);
        
        cacheUtil.putObject(PlcConstants.ERRO.ERRO_INICIALIZACAO_CHAVE_CACHE,errosInicializacao);
        
    }
    
    
	/**
	 * Limpa erros de inicialização
	*/
    public void cleanInitFatalError() {
        log.debug( "########## Entrou em limpaErroFatalInicializacao");
        PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
        cacheUtil.putObject(PlcConstants.ERRO.ERRO_INICIALIZACAO_CHAVE_CACHE,null);
        
    }
  
	
}