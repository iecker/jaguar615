/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.FacesBeanWrapper;
import org.apache.myfaces.trinidad.component.UIXComponent;
import org.apache.myfaces.trinidadinternal.ui.AttributeKey;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.uinode.UINodeFacesBean;
import org.apache.myfaces.trinidadinternal.uinode.UINodeRendererBase;
import org.apache.myfaces.trinidadinternal.uinode.UIXComponentUINode;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTable;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;

/**
 * Especialização do renderer base UINodeRendererBase para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcTableRenderer extends UINodeRendererBase {

	private static final Logger	log				= Logger.getLogger(PlcTableRenderer.class.getCanonicalName());

	// Definindo o tipo do renderer
	static public final String	RENDERER_TYPE	= "com.powerlogic.jsf.Tabela";

	/**
	 * @since jCompany 5.5 Este inicia a renderização do componente PlcTabela, utilizado em formulários, e delega para classes especialistas em 
	 * JSP+Tiles ou Facelets, conforme a configuração
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		ResponseWriter writer = context.getResponseWriter();

		FacesBean bean = ((PlcTable) component).getFacesBean();

		String titulo = (String) bean.getProperty(PlcTable.TITULO_KEY);
		String classeCSS = (String) bean.getProperty(PlcTable.STYLE_CLASS_KEY);
		String classeTituloCSS = (String) bean.getProperty(PlcTable.CLASSE_TITULO_CSS_KEY);

		PlcTableFaceletsRenderer.getInstance().encodeBeginFacelets(writer, bean, component, titulo, classeCSS, classeTituloCSS);

	}

	/**
	 * @since jCompany 5.5 Este inicia a renderização do componente PlcTabela, utilizado em formulários, e delega para classes especialistas em 
	 * JSP+Tiles ou Facelets, conforme a configuração
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

	    trataStyleClassParaWeblogic(context, component);
		
		super.encodeEnd(context, component);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		FacesBean bean = ((PlcTable) component).getFacesBean();

		ResponseWriter writer = context.getResponseWriter();

		String titulo = (String) bean.getProperty(PlcTable.TITULO_KEY);

		PlcTableFaceletsRenderer.getInstance().encodeEndFacelets(writer, bean, component, titulo);

		msgComponentsUtil.printMessageError(component);

	}

	private void trataStyleClassParaWeblogic(FacesContext context,
			UIComponent component) throws IOException {
		if (component instanceof UIXComponent)
	    {
	      FacesBean bean = ((UIXComponent) component).getFacesBean();

	      // Since we are using instanceof, unwrap the bean if using the public bean wrapper from
	      // the API project
	      for (; bean instanceof FacesBeanWrapper; bean = ((FacesBeanWrapper)bean).getWrappedBean());

	      if (bean instanceof UINodeFacesBean) {
	    	  UINode node = ((UINodeFacesBean) bean).getUINode();
	    	  Object o = node.getAttributeValue(getRenderingContext(context, component), AttributeKey.getAttributeKey("styleClass"));
	    	  if (o==null && node instanceof UIXComponentUINode) {
	    		  ((UIXComponentUINode)node).setAttributeValue(AttributeKey.getAttributeKey("styleClass"), component.getAttributes().get("styleClass"));
	    	  }
	      }	  
	    }
	}

}
