/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTable;

/**
 * Renderiza tag tabela em grupo tecnologico JSF+Facelets
 */
public class PlcTableFaceletsRenderer {

	private static PlcTableFaceletsRenderer	INSTANCE	= null;

	private PlcTableFaceletsRenderer() {

	}

	public static PlcTableFaceletsRenderer getInstance() {

		if (INSTANCE == null)
			INSTANCE = new PlcTableFaceletsRenderer();
		return INSTANCE;
	}

	/**
	 * Inicia a renderização do componente PlcTabela para o grupo tecnológico Facelets 
	 * @param writer
	 * @param bean 
	 * @param component 
	 * @param titulo
	 * @param classeCSS
	 * @param classeTituloCSS
	 */
	public void encodeBeginFacelets(ResponseWriter writer, FacesBean bean, UIComponent component, String titulo, String classeCSS, String classeTituloCSS) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		//Somente renderiza legend da tabela se for declarado, e não for layout de tabFolder ou assistente apresentando detalhes.
		String usaFieldset = (String) bean.getProperty(PlcTable.USA_FIELDSET);

		if ("S".equals(usaFieldset) || (StringUtils.isEmpty(usaFieldset) && titulo != null && contextUtil.getRequestAttribute("tabFolderAutomaticoPlc") == null)) {

			writer.startElement("fieldset", component);
			writer.writeAttribute("class", "plc-fieldset", null);
			writer.startElement("legend", component);
			// div para poder usar jquery ui themes
			writer.writeAttribute("class", "plc-fieldset-legend", null);
			writer.write(titulo);
			writer.endElement("legend");
		}

		bean.setProperty(PlcTable.STYLE_CLASS_KEY, "plc-table-tabsel " + classeCSS);

	}

	/**
	 * Renderiza finalização do componente PlcTabula para o grupo tecnológico Facelets
	 * @param writer
	 * @param bean
	 * @param component
	 * @param titulo
	 * @throws IOException
	 */
	public void encodeEndFacelets(ResponseWriter writer, FacesBean bean, UIComponent component, String titulo) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String usaFieldset = (String) bean.getProperty(PlcTable.USA_FIELDSET);
		//Somente renderiza legend da tabela se for declarado, e não for layout de tabFolder ou assistente apresentando detalhes.
		if ("S".equals(usaFieldset) || (StringUtils.isEmpty(usaFieldset) && titulo != null && contextUtil.getRequestAttribute("tabFolderAutomaticoPlc") == null)) {
			writer.endElement("fieldset");
		}

	}
}
