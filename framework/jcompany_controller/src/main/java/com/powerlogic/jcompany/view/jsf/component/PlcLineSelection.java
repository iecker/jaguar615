/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.FacesBean.Type;
import org.apache.myfaces.trinidad.bean.PropertyKey;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcLineRenderer;

/**
 * Especialização do componente base PlcLinha para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcLineSelection extends PlcLine {

	protected static final Logger		logVisao				= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Define o Tipo a Família para o componente 
	 */
	public static final String			COMPONENT_TYPE			= "com.powerlogic.jsf.componente.PlcLinhaSelecao";

	public static final String			COMPONENT_FAMILY		= "com.powerlogic.jsf.componente.link";

	public static final FacesBean.Type	TYPE					= new FacesBean.Type(PlcLine.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	public static final PropertyKey		ITEM_EXPRESSION_KEY		= TYPE.registerKey("itemExpression", String.class);

	public static final PropertyKey		ACTION_KEY				= TYPE.registerKey("action", String.class);

	public static final PropertyKey		LINK_EDICAO_KEY			= TYPE.registerKey("linkEdicao", String.class);

	public static final PropertyKey		PROP_AGREGADA_KEY		= TYPE.registerKey("propAgregada", String.class);

	public static final PropertyKey		PROP_AGREGADA_CAMPO_KEY	= TYPE.registerKey("propAgregadaCampo", String.class);

	public static final PropertyKey		LINK_EDICAO_CT_KEY		= TYPE.registerKey("linkEdicaoCT", String.class);

	public static final PropertyKey		TOOLTIP_KEY				= TYPE.registerKey("toolTip", String.class);

	public static final PropertyKey		ID_ESPECIFICO_KEY		= TYPE.registerKey("idEspecifico", String.class);

	public static final PropertyKey		REDIRECT_MODAL			= TYPE.registerKey("redirectModal", String.class);

	public static final PropertyKey		TITLE_MODAL				= TYPE.registerKey("titleModal", String.class);

	static {
		//Registra o tipo do componente
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcLineRenderer.RENDERER_TYPE);
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected Type getBeanType() {

		return PlcLineSelection.TYPE;
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {


		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (contextUtil.isLooping()) {

			int i = 0;
			if ((Integer.valueOf((String) contextUtil.getCurrentLineNumber()) % 2 == 0))
				getFacesBean().setProperty(PlcLineSelection.STYLE_CLASS_KEY, "linhapar");
			else
				getFacesBean().setProperty(PlcLineSelection.STYLE_CLASS_KEY, "linhaimpar ui-widget-default");
		} else
			getFacesBean().setProperty(PlcLineSelection.STYLE_CLASS_KEY, "linhaFormulario");
		super.encodeBegin(context);

	}

}
