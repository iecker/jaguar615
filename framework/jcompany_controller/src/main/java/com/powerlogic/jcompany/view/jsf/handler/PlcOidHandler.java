/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para mostrar o id do registro atual, no formato texto "string".!
 * @Exemplo <plcf:oid id="cidade_id" autoRecuperacao="S"/>!
 * @Tag oid!
 */
import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.adapter.PlcOidAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcOid;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcOidHandler extends PlcTextHandler {

	protected static final Logger	log	= Logger.getLogger(PlcOidHandler.class.getCanonicalName());

	private TagAttribute			autoRecuperacao;

	public PlcOidHandler(ComponentConfig config) {

		super(config);
		autoRecuperacao = getAttribute("autoRecuperacao");

	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);

		FacesBean bean = ((PlcOid) instance).getFacesBean();

		String _ajudaChave = (String) PlcTagUtil.getValue(this.ajudaChave, ctx);
		String _ajuda = (String) PlcTagUtil.getValue(this.ajuda, ctx);

		if (StringUtils.isBlank(_ajudaChave) && StringUtils.isBlank(_ajuda)) {
			componentPropertiesUtil.setPropertyShortDesc(PlcOid.SHORT_DESC_KEY, bean, null, "label.id", null);
			componentPropertiesUtil.setPropertyLabel(PlcOid.LABEL_KEY, bean, null, "label.id", null);
		}

		super.setAttributes(ctx, instance);

		String _tamanho = (String) PlcTagUtil.getValue(this.tamanho, ctx);
		if (StringUtils.isBlank(_tamanho)) {
			bean.setProperty(PlcOid.COLUMNS_KEY, new Long("8"));
			bean.setProperty(PlcOid.MAXIMUM_LENGTH_KEY, new Long("8"));
		}

		String id = (String) bean.getProperty(PlcOid.ID_KEY);
		bean.setProperty(PlcOid.ID_KEY, "inibeFoco_" + id);

		String _autoRecuperacao = (String) PlcTagUtil.getValue(this.autoRecuperacao, ctx);
		if (StringUtils.isBlank(_autoRecuperacao))
			_autoRecuperacao = "N";
		ValueExpression _exibeSe = PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);

		PlcOidAdapter.getInstance().adapter(bean, _autoRecuperacao, _exibeSe);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
