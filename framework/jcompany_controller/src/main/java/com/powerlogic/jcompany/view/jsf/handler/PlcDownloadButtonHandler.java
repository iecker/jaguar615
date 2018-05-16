/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização da tag base CoreCommandButtonTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um botão no padrão JSF/Trinidad! 
 * @Exemplo plcf:botaoAcao id="botaoAcaoAbrir" acao="abre" destino="abre" label="jcompany.evt.abrir" botaoArrayID="ABRIR" immediate="true" validaForm="false" limpaForm="false" rendered="#{requestScope.exibeAbrirPlc=='S' and requestScope.exibe_jcompany_evt_abrir!='N'}" hotKey="#{requestScope.abrirHotKey}"
 * @Tag botao!
 */
public class PlcDownloadButtonHandler extends PlcButtonHandler {

	protected static final Logger	logVisao	= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	public PlcDownloadButtonHandler(ComponentConfig config) {

		super(config);
	}

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		super.setAttributes(ctx, instance);

		FacesBean bean = ((PlcButton) instance).getFacesBean();

		if (this.acao == null) {
			String acaoAction = componentUtil.transformProperty(PlcJsfConstantes.PLC_ACTION_KEY, "downloadFile", false);
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			bean.setProperty(PlcButton.ACTION_EXPRESSION_KEY, elUtil.createMethodExpression(acaoAction, String.class, null));
		}

		String bundle = componentPropertiesUtil.getPropertyBundle(bean);
		if (this.label == null) {
			String label = componentUtil.createLocalizedMessage(bean, "jcompany.evt.download", new Object[] {});
			//setLabel("jcompany.evt.download");
			bean.setProperty(CoreCommandButton.TEXT_KEY, label);
		}

		//setBotaoArrayID("DOWNLOAD");
		bean.setProperty(PlcButton.VALIDA_FORM, Boolean.FALSE);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
