/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.html.HtmlTableLayout;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTable;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcTableAdapter {

	private static PlcTableAdapter INSTANCE = new PlcTableAdapter ();

	private PlcTableAdapter () {
		
	}

	public static PlcTableAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, String classeCSS, String tituloChave, String titulo, String fragmento, String classeTituloCSS, String bundle, String aoSair, String usaFieldset){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (bean.getProperty(HtmlTableLayout.CELL_SPACING_KEY)!=null)
			bean.setProperty(HtmlTableLayout.CELL_SPACING_KEY, 0);
		if (bean.getProperty(HtmlTableLayout.CELL_PADDING_KEY)!=null)
			bean.setProperty(HtmlTableLayout.CELL_PADDING_KEY, 0);
		
		if (!StringUtils.isBlank(classeCSS))
			bean.setProperty(HtmlTableLayout.STYLE_CLASS_KEY, classeCSS);
		else {
			// Tenta assumir default apropriadamente pelo contexto 
			if (contextUtil.getRequestAttribute(PlcConstants.FORM_PATTERN)!=null) {
				String logica = (String)contextUtil.getRequestAttribute(PlcConstants.FORM_PATTERN);
				if (logica.startsWith(FormPattern.Con.name()) || 
		                logica.startsWith(FormPattern.Sel.name()) ||
		                logica.startsWith(FormPattern.Smd.name())) {
		            bean.setProperty(HtmlTableLayout.STYLE_CLASS_KEY, "delimitador tabelaSelecao");
		        } else
		        	bean.setProperty(HtmlTableLayout.STYLE_CLASS_KEY, "delimitador tabelaFormulario");
			} else
				bean.setProperty(HtmlTableLayout.STYLE_CLASS_KEY, "delimitador tabelaFormulario");
		}
		
		if (! StringUtils.isBlank(titulo))
			bean.setProperty(PlcTable.TITULO_KEY, titulo);
		if (! StringUtils.isBlank(titulo))
			bean.setProperty(PlcTable.TITULO_KEY, titulo);

		
		if (!StringUtils.isBlank(fragmento))
			bean.setProperty(PlcTable.FRAGMENTO_KEY, fragmento);

		bean.setProperty(bean.getType().findKey(PlcTagUtil.BUNDLE), PlcTagUtil.getDefaultBundle(bundle));
		if (! StringUtils.isBlank(tituloChave)){
			String valorChave = componenteUtil.createLocalizedMessage(bean, tituloChave, new Object []{});
			
			bean.setProperty(PlcTable.TITULO_KEY, valorChave);
		}
		
		if (!StringUtils.isBlank(classeTituloCSS))
			bean.setProperty(PlcTable.CLASSE_TITULO_CSS_KEY, classeTituloCSS);
		else
			bean.setProperty(PlcTable.CLASSE_TITULO_CSS_KEY, "tabelaTitulo");
		
		bean.setProperty(PlcTable.USA_FIELDSET, usaFieldset);
		
		
		
	}
	
	
}
