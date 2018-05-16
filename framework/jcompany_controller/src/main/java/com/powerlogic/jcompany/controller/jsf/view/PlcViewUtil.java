/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.view;

import java.io.IOException;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

/**
 * Componente para retornar valores para rodapé e mensagens   
 */
@Named("plcComponenteVisual")
@ApplicationScoped
public class PlcViewUtil {

	
	/**
	* Serviço injetado e gerenciado pelo CDI
	*/
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	public int getMaxInactiveInterval() {
	    return ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getMaxInactiveInterval();
	}

	public int getHoras() {
	    return new Date().getHours();
	}
	
	public String getMinutos() {
		int minutes = new Date().getMinutes();
		return minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
	}
	
	public long getTotalMemory (){
		return Runtime.getRuntime().totalMemory() / 1024;
	}
	
	public long getFreeMemory (){
		return Runtime.getRuntime().freeMemory() / 1024;
	} 
	
	/**
	 * Usado para mensagens por cor na geralMensagemPlc.xhtml
	 */
	private Object [] listaCores = new Object [] { 
		new String [] {"erro","msgVermelhoPlc"},
		new String [] {"sucesso","msgAzulPlc"},
		new String [] {"advertencia","msgAmareloPlc"},
		new String [] {"livre","msgVerdePlc"}
	};
	
	public void setListaCores(String [] listaCores) {
		this.listaCores = listaCores;
	}

	public Object [] getListaCores() {
		return listaCores;
	}
	
	public String getTituloPagina (){
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		String titulo = elUtil.evaluateExpressionGet("#{requestScope.plcURLComBarra}", String.class);
		if (titulo.startsWith("/"))
			titulo = titulo.replace("/", "");
		return (titulo + ".titulo").toLowerCase();
	}
	
	public String getMensagem(String valorChave) {

		PlcI18nUtil i18nUtilI = (PlcI18nUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		return i18nUtilI.mountLocalizedMessageAnyBundle(contextUtil.getRequest(), valorChave, null);

	}
	
	public String getLogginQA () throws IOException{
		
		HttpServletRequest request = contextUtil.getRequest();
		
		long t1 = ((Long)request.getAttribute("tempo_inicio")).longValue();
		long t2 = System.currentTimeMillis();
		long tempoTotal = t2-t1;
		
		String log = request.getAttribute("loggingQA") + "\n" + "TEMPO TOTAL: " + tempoTotal + " milisegundos ";

		return log.replace('-','_');
		
	}
	
}
