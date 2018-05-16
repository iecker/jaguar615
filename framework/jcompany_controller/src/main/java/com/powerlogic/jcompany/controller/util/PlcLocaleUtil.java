/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;

/**
 * jCompany 2.0. Auxiliar para internacionalização
 * @since jCompany 2.0
 * @author alvim
 */

@SPlcUtil
@QPlcDefault
public class PlcLocaleUtil {

	@Inject
	protected transient Logger log;
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
 	public PlcLocaleUtil() { 	}   

    /**
     * Se não houver locale ou bundle default na sessão corrente, utiliza bundle sem extensão e locale pt_BR.
     */
    public void availableDefaultLocale(HttpSession session) {
    	//TODO - Realizar I18n somente para JSF 2.0 e ver se este trecho abaixo ainda faz sentido.

       log.debug( "########## Entrou em disponibilizaLocalePadrao");

		//Configura idioma único para a aplicação
		checkUniqueLocale(session);
    
    }

 	/**
 	 * Configura o idioma único para mensagens da aplicação de acordo com o parâmetro localeUnico definido em metadados
 	 */
 	public void checkUniqueLocale(HttpSession httpSession) {

		log.debug( "Vai configurar idioma unico");
		
		String idiomaUnico = null;
		
		if (idiomaUnico == null) {
			// Verifica em anotacoes
			try {
				PlcConfigApplication appConfig = configUtil.getConfigApplication().application();
				//FIXME - Ver porque nao esta pegando a configuração real
				if ((appConfig!=null && appConfig.i18n().localeUnique()))
					idiomaUnico = appConfig.i18n().languages()[0];
			} catch (Exception e) {
				// nao achou passa sem a configuração
				log.debug( "Erro ao tentar aplicar o idioma unico "+e,e);
			}
			
		}
			
		if(idiomaUnico != null && !idiomaUnico.equals("#")) {
			//Ver como registrar para que nao seja possivel para a aplicacao assumir um outro idioma
		}
 	}

 	/**
 	 * Configura o locale da aplicacão de acordo com o parâmetro localeUnico definido no web.xml.<br>
 	 * @param servletContext Contexto da aplicacao
 	 */
 	public void configAppUniqueLocale (ServletContext servletContext) {

 		   log.debug( "Vai configurar locale unico para aplicacao");
 		   
 		   try {
			
	 			String[] idiomas = new String[0];
	 			if (configUtil.getConfigApplication().application().i18n()!=null)
	 				idiomas = configUtil.getConfigApplication().application().i18n().languages();
				
	 			if(idiomas.length==1){
	 	 			setAppLocale(idiomas[0]);
	 			}else{
	  	 			setAppLocale("pt_BR");
	 			}
 			
 		  } catch (Exception e) {
 				log.error( "Nao conseguir setar locale unico na aplicacao "+e, e);
 		  }

	}


 	/**
 	 * Seta o locale da aplicação.
 	 * @param locale Novo locale para a aplicação. Exemplo: pt_BR, en_US, es_ES.
 	 */
 	public void setAppLocale(String locale){
 	    if(locale != null && !locale.equals(""))
 	        setAppLocale(mountLocale(locale));
 	}

 	/**
 	 * Seta o locale da aplicação.
 	 * @param locale Novo objeto Locale para a aplicação.
 	 */
 	public void setAppLocale(Locale locale){
 	    if(locale != null)
 	        Locale.setDefault(locale);

 	}

 	/**
 	 * Recebe o locale como texto (pt_BR, en_US, es_ES) e gera um objeto locale.<br>
 	 * Se nenhum locale for informado assume locale default como "pt_BR".
 	 * @param strLocale Locale no formato texto. Exemplo: pt_BR, en_US, es_ES.
 	 * @return Objeto Locale conforme String passado
 	 */
 	public Locale mountLocale(String strLocale){

	 	 Locale locale = new Locale("pt","BR");
	    if(strLocale != null && !strLocale.equals("#")){
		    String language 	= "";
		    String country	= "";
	       String[] locs = StringUtils.split(strLocale,"_");
	       language = locs[0];
	       if(locs.length > 1)
	           country = locs[1];
	       locale = new Locale(language, country);
	    }

	    return locale;
 	}

}
