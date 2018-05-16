/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.ValueExpressionValueBinding;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneChoice;
import org.apache.myfaces.trinidadinternal.taglib.util.MethodExpressionMethodBinding;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcDynamicCombo;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcDynamicComboAdapter {

	private static PlcDynamicComboAdapter INSTANCE = new PlcDynamicComboAdapter ();
	
	private PlcDynamicComboAdapter () {
		
	}
	
	public static PlcDynamicComboAdapter getInstance () {
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
	 * @param riaUsa 
	 * @param alias
	 * @param propOrdenacao
	 * @param ordem
	 */
	public void adapter (FacesBean bean, String tamanho, String ajudaChave, String ajuda, String classeCSS, String obrigatorio, String obrigatorioDestaque,	
							 String somenteLeitura, String chavePrimaria, String tituloChave, ValueExpression titulo, ValueExpression dominio, ValueExpression exibeSe,
							 String exibeBranco, String botaoAtualiza, String propRotulo, String bundle, String colunas, String numLinha, String msgErro, String navegacaoParaCampos,
							 String comboAninhado, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Passar estas propriedades para a função setPropertiesComuns
		PlcGlobalTag globalTag = new PlcGlobalTag( componenteUtil.getValueProperty(bean), classeCSS, ajuda,
				  			         ajudaChave, titulo, tituloChave,
				   					 somenteLeitura, chavePrimaria, obrigatorio,
				   					 tamanho, null,bundle , exibeSe);
		
		// Propriedades especificas
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		if(dominio.isLiteralText()){
			//Se for uma string (ou seja, nao é uma expressao com #, cria a expressão baseado no valor passado.
			ValueExpression createValueExpression = elUtil.createValueExpression("#{plcDominios.dominios['"+dominio.getValue(FacesContext.getCurrentInstance().getELContext())+"']}", Object.class);
			bean.setValueExpression(PlcDynamicCombo.DOMINIO_KEY, createValueExpression);
			bean.setValueBinding(PlcDynamicCombo.DOMINIO_KEY, ValueExpressionValueBinding.getValueBinding(createValueExpression));
		}else{
			bean.setValueExpression(PlcDynamicCombo.DOMINIO_KEY, dominio);
			
		}
		
		if (! StringUtils.isBlank(propRotulo))
			bean.setProperty(PlcDynamicCombo.PROP_ROTULO_KEY, propRotulo);
		

		if ("S".equals(botaoAtualiza)) {
			bean.setProperty(PlcDynamicCombo.BOTAO_ATUALIZA_KEY, PlcDynamicCombo.BotaoAtualizaTipo.ATUALIZA);
		} else if ("SC".equals(botaoAtualiza) || "S-Cache".equals(botaoAtualiza)) {
			bean.setProperty(PlcDynamicCombo.BOTAO_ATUALIZA_KEY, PlcDynamicCombo.BotaoAtualizaTipo.ATUALIZA_CACHE);
		}

		if ("S".equals(exibeBranco))
			bean.setProperty(CoreSelectOneChoice.UNSELECTED_LABEL_KEY, "[Selecione]");
		
		bean.setProperty(PlcDynamicCombo.COMBO_ANINHADO_KEY, "S".equals(comboAninhado));

		if ( StringUtils.isNotBlank(navegacaoParaCampos)){
			bean.setProperty(CoreSelectOneChoice.AUTO_SUBMIT_KEY, Boolean.TRUE);
			bean.setProperty(PlcDynamicCombo.NAVEGACAO_PARA_CAMPOS_KEY, navegacaoParaCampos);

			MethodExpression me = elUtil.createMethodExpression("#{plcAction.findNavigationNestedCombo}", String.class, new Class[]{javax.faces.event.ValueChangeEvent.class});
			bean.setProperty(UIXEditableValue.VALUE_CHANGE_LISTENER_KEY, new MethodExpressionMethodBinding(me));
		}

		bean.setProperty(PlcDynamicCombo.RIA_USA, riaUsa);
		
		//	 Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(),globalTag);
		
	    
	}
	
}
