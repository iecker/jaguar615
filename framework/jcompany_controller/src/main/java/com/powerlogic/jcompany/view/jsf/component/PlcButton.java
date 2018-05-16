/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.AttachedObjects;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;

/**
 * Especialização do componente base CoreCommandButton para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcButton extends CoreCommandButton {

	protected static final Logger		logVisao					= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected static final Logger		log							= Logger.getLogger(PlcButton.class.getCanonicalName());

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY			= "com.powerlogic.jsf.componente.acao";

	static public final String			COMPONENT_TYPE				= "com.powerlogic.jsf.componente.PlcBotao";

	static public final FacesBean.Type	TYPE						= new FacesBean.Type(CoreCommandButton.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		VALIDA_FORM					= TYPE.registerKey("validaForm", Boolean.class);

	static public final PropertyKey		LIMPA_FORM					= TYPE.registerKey("limpaForm", Boolean.class);

	static public final PropertyKey		URL_ICONE_KEY				= TYPE.registerKey("urlIcone", String.class);

	static public final PropertyKey		EVENTO_KEY					= TYPE.registerKey("evento", String.class);

	static public final PropertyKey		ALERTA_EXCLUIR_KEY			= TYPE.registerKey("alertaExcluir", String.class);

	static public final PropertyKey		ALERTA_EXCLUIR_DETALHE_KEY	= TYPE.registerKey("alertaExcluirDetalhe", String.class);

	static public final PropertyKey		BUNDLE						= TYPE.registerKey("bundle", String.class);

	static public final PropertyKey		ID_PLC						= TYPE.registerKey("idPlc", String.class);

	static public final PropertyKey		FW_PLC						= TYPE.registerKey("fwPlc", String.class);

	static public final PropertyKey		RENDERED_EXPRESSION			= TYPE.registerKey("renderedExpression", String.class);

	static public final PropertyKey		RIA_USA						= TYPE.registerKey("riaUsa", String.class);

	static public final PropertyKey		CLIENT_BEHAVIORS			= TYPE.findKey("clientBehaviors");

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	/**
	 * Se for um botão padrão jCompany defini uma hotkey, ou uma funçao JS adequada 
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {

		super.encodeEnd(context);

	}

	@Override
	public boolean isRendered() {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String renderedExpression = (String) getProperty(RENDERED_EXPRESSION);

		ServletContext servletContext = contextUtil.getApplicationContext();

		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");

		if (renderedExpression != null && "all".equals(client)) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			this.setRendered(elUtil.evaluateExpressionGet(renderedExpression, Boolean.class));
		}

		return super.isRendered();
	}

	@Override
	public void setValueExpression(String name, ValueExpression expression) {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		ServletContext servletContext = contextUtil.getApplicationContext();

		String client = servletContext.getInitParameter("org.apache.myfaces.trinidad.CLIENT_STATE_METHOD");

		if ("rendered".equals(name) && expression != null && "all".equals(client)) {

			String expressionString = expression.getExpressionString();

			if (expression.isLiteralText()) {
				try {
					this.setRendered(Boolean.valueOf(expressionString).booleanValue());
				} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
				this.setRendered(elUtil.evaluateExpressionGet(expressionString, Boolean.class));
				this.setProperty(RENDERED_EXPRESSION, expressionString);
			}
		} else
			super.setValueExpression(name, expression);
	}

	
	@Override
	public Map<String, List<ClientBehavior>> getClientBehaviors() {
		
		return super.getClientBehaviors();
	}
	
	public void setActionAjaxBehavior(AjaxBehavior actionAjaxBehavior) {
		this.actionAjaxBehavior = actionAjaxBehavior;
	}

	public AjaxBehavior getActionAjaxBehavior() {
		return actionAjaxBehavior;
	}

	private AjaxBehavior actionAjaxBehavior;
	
	
	
	
}
