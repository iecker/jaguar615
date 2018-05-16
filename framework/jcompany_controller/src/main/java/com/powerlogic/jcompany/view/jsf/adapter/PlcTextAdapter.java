/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.ValueExpression;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.tagfile.PlcTagFileUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcTextAdapter {

	private static PlcTextAdapter INSTANCE = new PlcTextAdapter ();

	private PlcTextAdapter () {
		
	}

	public static PlcTextAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, String tamanho, String tamanhoMaximo, String ajudaChave, String ajuda, String classeCSS, 
			String obrigatorio, String obrigatorioDestaque, String somenteLeitura, String formato, String chavePrimaria, String tabIndex, 
			String tituloChave, ValueExpression titulo, ValueExpression exibeSe, String bundle, String colunas, String numLinha, 
			String numDet, String msgErro, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcTagFileUtil tagFileUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcTagFileUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		String propriedadeDeValue = componenteUtil.getValueProperty(bean);
		
		// Se informou somente algum destes dois, assume o outro como default. Somente se forem diferentes é feita distincao
		if (StringUtils.isBlank(tamanho) && !StringUtils.isBlank(tamanhoMaximo)) {
			 tamanho =  tamanhoMaximo;
		}
		
		// Passar estas propriedades para a função setPropertiesComuns
		PlcGlobalTag globalTag = new PlcGlobalTag(propriedadeDeValue, classeCSS, ajuda, ajudaChave, titulo, tituloChave, somenteLeitura, chavePrimaria, obrigatorio, tamanho, tamanhoMaximo,bundle, exibeSe);
		
		try{
			
			PlcConfigAggregationPOJO grupoAgregado = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));

			// Propriedades Especificas desta Tag
			if (StringUtils.isBlank(formato)){

				if (grupoAgregado!=null){
					String formatoSimples = tagFileUtil.simpleFormat(grupoAgregado.entity(), null, globalTag.getPropriedade(),false);
					if (formatoSimples!=null)
						 formato=formatoSimples;
				}

			}

			if (!StringUtils.isBlank(formato)) {
				bean.setProperty(PlcText.FORMATO_KEY, formato);
			}
			
			bean.setProperty(PlcText.RIA_USA, riaUsa);
			
			bean.setProperty(PlcText.TAB_INDEX, tabIndex);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(), globalTag);
		
		
	}
	
}
