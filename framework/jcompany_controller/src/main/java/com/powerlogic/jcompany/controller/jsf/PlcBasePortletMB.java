/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcPortletMaxAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcPortletMaxBefore;
import com.powerlogic.jcompany.controller.bindingtype.PlcPortletMinAfter;
import com.powerlogic.jcompany.controller.bindingtype.PlcPortletMinBefore;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

/**
 * Controle simples de expansão e retração de componentes de formulário
 */
@QPlcDefault
public class PlcBasePortletMB extends PlcBaseParentMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	/**
	 * @param entityListPlc Lista de objetos para padrões que usam listas principais
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @since jCompany 5.0 Maximiza portlets
	 */
	public String portletMax(Object entityPlc, PlcEntityList entityListPlc)  {

		if (!portletMaxBefore(entityPlc,entityListPlc))
			return defaultNavigationFlow;

		if (plcControleConversacao == null && getPlcRequestControl().getDetCorrPlc() == null)
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_NO_CURRENT_PORTLET);

		// Usa comunicação inter-portlets via request
		contextUtil.setRequestAttribute(
				PlcConstants.GUI.PORTLET.EVT_PORTLET,
				PlcConstants.GUI.PORTLET.EVT_PORTLET_EXPANSAO);

		return portletMaxAfter(entityPlc,entityListPlc);

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" 
	 * são destinados a especializações nos descendentes por Template Methods ou em Extensions via CDI events.
	 * @param entityListPlc 
	 * @param entityPlc 
	 */
	protected boolean portletMaxBefore(Object entityPlc, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcPortletMaxBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 * @param entityListPlc Lista de objetos para padrões que usam listas principais
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 */
	protected String portletMaxAfter(Object entityPlc, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcPortletMaxAfter>(){});
		return defaultNavigationFlow;
	}

	/**
	 * @param entityListPlc Lista de objetos para padrões que usam listas principais
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 * @since jCompany 5.0 Minimiza portlets
	 */
	public String portletMin(Object entityPlc, PlcEntityList entityListPlc)  {

		if (!portletMinBefore(entityPlc,entityListPlc))
			return defaultNavigationFlow;

		if (plcControleConversacao == null
				&& getPlcRequestControl().getDetCorrPlc() == null)
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_NO_CURRENT_PORTLET);

		// Usa comunicação inter-portlets para reusar mesmos controles
		// Struts/Tiles
		contextUtil.setRequestAttribute(
				PlcConstants.GUI.PORTLET.EVT_PORTLET,
				PlcConstants.GUI.PORTLET.EVT_PORTLET_RETRACAO);

		return portletMinAfter(entityPlc,entityListPlc);

	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" 
	 * são destinados a especializações nos descendentes por Template Methods ou em Extensions via CDI events.
	 * @param entityListPlc Lista de objetos para padrões que usam listas principais
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 */
	protected boolean portletMinBefore(Object entityPlc, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcPortletMinBefore>(){});
		return defaultProcessFlow;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" 
	 * são destinados a especializações nos descendentes por Template Methods ou em Extensions via CDI events.
	 * @param entityListPlc Lista de objetos para padrões que usam listas principais
	 * @param entityPlc Entidade para padrões que usam uma entidade principal
	 */
	protected String portletMinAfter(Object entityPlc, PlcEntityList entityListPlc)  {
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcPortletMinAfter>(){});
		return defaultNavigationFlow;
	}

	
	
}
