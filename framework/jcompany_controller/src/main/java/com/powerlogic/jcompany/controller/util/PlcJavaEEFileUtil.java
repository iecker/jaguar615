/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Classe de utilitários que manipula arquivos embutidos em arquivos Java EE como recursos
 * @since jCompany 5.5
 */
@SPlcUtil
@QPlcDefault
public class PlcJavaEEFileUtil {
	
	private static final long serialVersionUID = -3975123986085226038L;

    public PlcJavaEEFileUtil() { 
    	
    }
   	
    @Inject
	protected transient Logger log;
	
	/**
	 * Le um arquivo embalado em WAR ou EAR e devolve um array de objetos com seu conteúdo separado em linhas
	 * @param request Requisição corrente
	 * @param nomeRecurso Nome do arquivo (recurso) dentro do WAR ou EAR 
	 * @return Object[] contendo uma linha em cada posição.
	 */
	public Object[] readLines(HttpServletRequest request, String nomeRecurso)  {
		
		try {
			InputStream is = request.getSession().getServletContext().getResourceAsStream(nomeRecurso);
			List<String> linhas = IOUtils.readLines(is);
			return linhas.toArray();
		} catch (Exception e) {
			throw new PlcException("PlcJavaEEFileUtil", "readLines", e, log, "");
		}

	}
	
	

}
