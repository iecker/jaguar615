/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputLabel;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;


public class PlcTitleAdapter {

	private static PlcTitleAdapter INSTANCE = new PlcTitleAdapter ();
	
	private PlcTitleAdapter () {
		
	}
	
	public static PlcTitleAdapter getInstance () {
		return INSTANCE;
	}
	
	/**
	 * Adapter entre o Handler e a tag
	 * 
	 * @param bean
	 * @param titulo
	 * @param colunas
	 * @param classeCSS
	 * @param tituloChave
	 * @param bundle
	 * @param alias
	 * @param propOrdenacao
	 * @param ordem
	 * @param riaUsa 
	 */
	public void adapter (FacesBean bean, ValueExpression titulo, String colunas, String classeCSS,	String tituloChave,	String bundle, 
			String alias, String propOrdenacao,	String ordem, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE); 
		
		bean.setProperty(bean.getType().findKey(PlcTagUtil.BUNDLE), PlcTagUtil.getDefaultBundle(bundle));
		
		if (StringUtils.isNotBlank( tituloChave)) {
			bean.setProperty(PlcTitle.TITULO_CHAVE_KEY,  tituloChave);
		}
		
		if (! componenteUtil.isValueDefined(bean, UIXValue.VALUE_KEY)) {
			if (titulo!=null ) {
				bean.setProperty(UIXValue.VALUE_KEY, titulo.getValue(FacesContext.getCurrentInstance().getELContext()));
			} 
			else{ 
				if (! StringUtils.isBlank( tituloChave)){ 
					String valorChave = componenteUtil.createLocalizedMessage(bean,  tituloChave);
					bean.setProperty(UIXValue.VALUE_KEY, valorChave);
					// Se não encontrou o tituloChave no bundle default e a key começa com jcompany então procura no bundle jCompanyResources.properties 
					if (valorChave ==null || (valorChave.startsWith("???") &&  tituloChave.startsWith("jcompany"))){
						String bundleDefault = (String)bean.getProperty(bean.getType().findKey(PlcTagUtil.BUNDLE));
						//Configurando o bundle para o componente
						bean.setProperty(bean.getType().findKey(PlcTagUtil.BUNDLE), "jCompanyResources");
						valorChave = componenteUtil.createLocalizedMessage(bean,  tituloChave);
						// Voltando com o bundleDefault
						bean.setProperty(bean.getType().findKey(PlcTagUtil.BUNDLE), bundleDefault);
					}
					bean.setProperty(UIXValue.VALUE_KEY, valorChave);
				}
			}
			
		}
		
		bean.setProperty(PlcTitle.RIA_USA, riaUsa);
		
		String propriedade = componenteUtil.getValueProperty(bean);
		if (StringUtils.isNotBlank(propriedade)) {
			bean.setProperty(PlcTitle.PROPRIEDADE_KEY, propriedade);
		}

		if (! StringUtils.isBlank( colunas)) 
			bean.setProperty(CoreInputText.COLUMNS_KEY, Integer.valueOf( colunas));
		if (! StringUtils.isBlank( classeCSS )) 
			bean.setProperty(CoreOutputLabel.STYLE_CLASS_KEY,  classeCSS);
		
		if(! StringUtils.isBlank( propOrdenacao))
			bean.setProperty(PlcTitle.PROP_ORDENACAO,  propOrdenacao);
		
		if(! StringUtils.isBlank( alias))
			bean.setProperty(PlcTitle.ALIAS,  alias);
		
		if(! StringUtils.isBlank( ordem))
			bean.setProperty(PlcTitle.ORDEM,  ordem);
		
	  
	}
	
}
