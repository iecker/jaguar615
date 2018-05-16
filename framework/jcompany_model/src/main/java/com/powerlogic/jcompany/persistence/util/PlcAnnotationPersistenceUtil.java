/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.ANOTACAO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

/**
 * Classe de Utilitários para acesso a meta-dados de anotação EJB3 e Hibernate
 * @since jCompany 3.0
 */
@SPlcUtil
@QPlcDefault
public class PlcAnnotationPersistenceUtil  implements Serializable{
	
	@Inject
	protected transient Logger log;

	private static final long serialVersionUID = 8535697015353850994L;
	
	public PlcAnnotationPersistenceUtil() { 
		
	}
	

	/**
	 * Recebe uma coleção de NamedQueries e devolve a NamedQuery padrão QBE
	 * @since jCompany 3.0
	 * @param nqs Coleção de NamedQueries
	 * @return NamedQuery com sufixo padrão propQBE
	 */
	public NamedQuery getNamedQuerySelDefault(NamedQueries nqs)  {
		
		log.debug( "########## Entrou em getAnotacaoPropQBEPadrao");
		
		for (int i = 0; i < nqs.value().length; i++) {
			NamedQuery nq =nqs.value()[i];
			if (nq.name().endsWith(PlcConstants.ANOTACAO.SUFIXO_QUERYSEL_PADRAO))
				return nq;
		}
		return null;
	}
	
	
	/**
	 * Recebe uma coleção de NamedQueries e devolve a NamedQuery padrão de Relatório
	 * @param nqs Coleção de NamedQueries
	 * @return NamedQuery com sufixo padrão propQBE
	 * @since jCompany 3.0
	 */
	public NamedQuery getNamedQuerySelReport(NamedQueries nqs, String reportNamedQueryCorrente)  {
		
		log.debug( "########## Entrou em getNamedQuerySelRel");
		
		/*
		 * Se o parâmetro reportNamedQueryCorrente estiver null é porque é o mestre, portanto recupera com o namedQuery padão de relatório
		 */
		if (( reportNamedQueryCorrente  == null ) ||  ( reportNamedQueryCorrente.equals("")))
			reportNamedQueryCorrente = PlcConstants.ANOTACAO.SUFIXO_RELATORIO_PADRAO;
		
		
		for (int i = 0; i < nqs.value().length; i++) {
			NamedQuery nq =nqs.value()[i];
			if (nq.name().indexOf(reportNamedQueryCorrente) >1)
				return nq;
		}
		return null;
	}


	/**
	 * jCompany 3.0 Pega anotaçoes que contenham ".naoDeveExistir" (TOKEN Reservado)
	 * @param classe
	 * @return lista de annotations "NaoDeveExistir"
	 * 
	 */
	public List<NamedQuery> getNamedQueriesNoSimilar(Class<? extends Object> classe)  {
		log.debug( "########## Entrou em getAnotacaoQueryNaoDeveExistir");
		
		List<NamedQuery> anotacoesNaoDeveExistir = new ArrayList<NamedQuery>();
		
		String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
		
		NamedQueries nqs = (NamedQueries) classe.getAnnotation(NamedQueries.class);
		
		if (nqs == null) {
			NamedQuery nq = (NamedQuery)getNamedQueryNoSimilar(classe,nomeClasseSemPackage);
			if (nq != null)
				anotacoesNaoDeveExistir.add(nq);
		} else {
			
			for (int i = 0; i < nqs.value().length; i++) {
				NamedQuery nq = nqs.value()[i];
				if (nq.name().indexOf(nomeClasseSemPackage+ANOTACAO.SEPARADOR_QUERY+ANOTACAO.SUFIXO_NAO_DEVE_EXISTIR_PADRAO)>-1)
					anotacoesNaoDeveExistir.add(nq);
			}
			
		}
		
		return anotacoesNaoDeveExistir;
		
	}
	
	/**
	 * Pega uma anotação de query naoDeveExistir para uma classe.
	 * @since jCompany 3.0
	 * @param classe Classe com anotação
	 * @param nomeClasseSemPackage Nome da classe sem pacote
	 * @return Anotação NamedQuery para a classe ou null se não encontrar
	 */
	protected NamedQuery getNamedQueryNoSimilar(Class<? extends Object> classe, String nomeClasseSemPackage) {
		log.debug( "########## Entrou em getAnotacaoQueryNaoDeveExistir");
		
		NamedQuery nq = (NamedQuery) classe.getAnnotation(NamedQuery.class);
		if (nq!=null && nq.name().indexOf(nomeClasseSemPackage+ANOTACAO.SEPARADOR_QUERY+ANOTACAO.SUFIXO_NAO_DEVE_EXISTIR_PADRAO)>-1)
			return nq;
		else
			return null;
	}
	
	/**
	 * Pega uma anotação de query por nome para uma  classe
	 * @since jCompany 3.0
	 * @param classe Classe com anotação
	 * @param apiQuerySel Nome do NamedQuery desejado
	 * @return NamedQuery para nome passado ou null se não encontrar
	 */
	public NamedQuery getNamedQueryByName(Class classe, String apiQuerySel)  {
		
		log.debug( "########## Entrou em getNamedQueryPorNome");
			
		String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
		
		NamedQueries nqs = (	NamedQueries) classe.getAnnotation(NamedQueries.class);
		
		if (nqs == null) {
			NamedQuery nq = (NamedQuery) classe.getAnnotation(NamedQuery.class);
			if (nq != null && nq.name().equals(nomeClasseSemPackage+ANOTACAO.SEPARADOR_QUERY+apiQuerySel))
				return nq;
		} else {
			
			for (int i = 0; i < nqs.value().length; i++) {
				NamedQuery nq = nqs.value()[i];
				if (nq.name().endsWith(nomeClasseSemPackage+ANOTACAO.SEPARADOR_QUERY+apiQuerySel))
					return nq;
			}
			
		}
		
		return null;
	}
	

	/**
	 * Devolve query de edição padrão, se existir, seguindo a convenção '[nome da classe sem pacote].queryEdita'
	 * @param vo Vo a ser investigado
	 * @return Query de edição ou null, se não encontrou anotação
	 */
	public String getQueryEditDefault(Class classe)  {

		NamedQuery nq = getNamedQueryByName(classe,ANOTACAO.SUFIXO_QUERYEDITA_PADRAO);
		
		if (nq == null)
			return null;
		else
			return nq.query();
	}

	
	/**
	 * Verifica se método tem anotação para transacao de leitura
	 * @since jCompany 3.0
	 * @return String O identificador da fábrica ou null se não tiver anotação PlcTransactional
	 */
	public PlcTransactional getTransactional(Method method) {
		log.debug( "########## Entrou em getAnotacaoTransacaoGravacao");
		
		return method.getAnnotation(PlcTransactional.class);
	}
		
}
