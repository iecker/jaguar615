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
import com.powerlogic.jcompany.view.jsf.component.PlcDate;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcDateAdapter {

	private static PlcDateAdapter INSTANCE = new PlcDateAdapter ();

	private PlcDateAdapter () {
		
	}

	public static PlcDateAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean, String tamanho, String tamanhoMaximo, String ajudaChave, String ajuda, String classeCSS, String obrigatorio,		
	 String obrigatorioDestaque, String somenteLeitura, String chavePrimaria, String tituloChave, ValueExpression titulo, ValueExpression exibeSe,
	 String mascara, String bundle, String msgErro, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Se informou somente algum destes dois, assume o outro como default. Somente se forem diferentes é feita distincao
		if (StringUtils.isBlank(tamanho) && !StringUtils.isBlank(tamanhoMaximo))
			tamanho = tamanhoMaximo;
		else if (!StringUtils.isBlank(tamanho) && StringUtils.isBlank(tamanhoMaximo))
			tamanhoMaximo = tamanho;
		
	    // Passar estas propriedades para a função setPropertiesComuns	    
	    PlcGlobalTag globalTag = new PlcGlobalTag( componenteUtil.getValueProperty(bean),
				    								classeCSS, ajuda,
				    								ajudaChave, titulo, tituloChave,
				    								somenteLeitura, chavePrimaria, obrigatorio,
				    								tamanho, tamanhoMaximo,bundle, exibeSe);
	    
		if (!StringUtils.isBlank(msgErro))
			bean.setProperty(PlcDate.MSG_ERRO, msgErro);
		
		bean.setProperty(PlcDate.RIA_USA, riaUsa);

		// Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(), globalTag);
		
		
	}
	
}
