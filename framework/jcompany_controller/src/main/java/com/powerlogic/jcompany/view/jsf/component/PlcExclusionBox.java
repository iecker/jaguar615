/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXValue;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.view.jsf.renderer.PlcSelectionBoxRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização do componente base PlcCaixaMarcacao para permitir IoC e DI nos componentes JSF/Trinidad. 
 * 
 */
public class PlcExclusionBox extends PlcSelectionBox {

	/*
	 * Define o Tipo a Família para o componente 
	 */
	static public final String			COMPONENT_FAMILY	= "com.powerlogic.jsf.componente.entrada";

	static public final String			COMPONENT_TYPE		= "com.powerlogic.jsf.componente.PlcCaixaExclusao";

	static public final FacesBean.Type	TYPE				= new FacesBean.Type(PlcSelectionBox.TYPE);

	/*
	 * Registra novas propriedades para o componente 
	 */
	static public final PropertyKey		VALORCHAVE_KEY		= TYPE.registerKey("valorChave", String.class);

	static {
		TYPE.lockAndRegister(COMPONENT_TYPE, PlcSelectionBoxRenderer.RENDERER_TYPE);//usa o renderer do CaixaMarcacao
	}

	@Override
	protected FacesBean.Type getBeanType() {

		return TYPE;
	}

	@Override
	public String getFamily() {

		return COMPONENT_FAMILY;
	}

	/**
	 * Evita a renderização se a chave estiver nula.
	 * TODO - Trocar essa implementação por rendered com uma expressão que retorne se id for nulo. Se o rendered já existir, incluir na expressão essa condição.
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (getValorChave() != null && getExibeCaixaExclusao()) {
			Object limpa = contextUtil.getRequestAttribute(PlcJsfConstantes.PLC_LIMPA_CAIXA_EXCLUSAO);
			if (Boolean.TRUE.equals(limpa)) {
				setValue(false);
			}
			super.encodeBegin(context);
		}

	}
	
	/**
	 * Verificar a exibicao ou nao da caixa de exclusao
	 * @return exibeCaixaExclusao
	 */
	protected Boolean getExibeCaixaExclusao() {
		String propriedade = ((ValueExpression) getFacesBean().getProperty(PlcExclusionBox.VALORCHAVE_KEY)).getExpressionString();
		
		Boolean exibeCaixaExclusao = true;
		
		if (propriedade != null && propriedade.contains("idNatural.")) {
			if (getValorChave()!= null) {
				exibeCaixaExclusao = PlcTagUtil.checkShowComponent(true);
			}	
			else {
				exibeCaixaExclusao = PlcTagUtil.checkShowComponent(false);
			}
		}
		return exibeCaixaExclusao;
	}

	/**
	 * Evita a renderização se a chave estiver nula.
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {

		if (getValorChave() != null && getExibeCaixaExclusao()) {
			super.encodeEnd(context);
		}
	}

	/**
	 * Faz uma conversão do valor para String "S" ou "N".
	 * Isso foi feito porque a camada modelo só trata String,
	 * e não outros tipos, para o campo indExcPlc.
	 * TODO - Verificar como fazer para tratar outros tipos: Usando valueBinding.getType(...) ou um Converter JSF.
	 */
	@Override
	public Object getLocalValue() {

		Object valor = getProperty(UIXValue.VALUE_KEY);

		if (valor == null) {
			return "N";
		}

		if (valor instanceof Boolean) {
			if (((Boolean) valor).booleanValue()) {
				return "S";
			} else {
				return "N";
			}
		}

		if (valor.toString().equals("S")) {
			return "S";
		} else {
			return "N";
		}

	}

	/**
	 * Retorna o valor da chave do elemento atual, baseado na propriedade definida como valor chave.
	 * @see PlcExclusionBox#VALORCHAVE_KEY
	 * @return valor da chave atual, ou nulo se não tiver sido definido.
	 */
	protected Object getValorChave() {

		Long valorChave = null;

		Object _valorChave = getFacesBean().getProperty(PlcExclusionBox.VALORCHAVE_KEY);

		if (_valorChave instanceof ValueExpression) {
			_valorChave = ((ValueExpression) _valorChave).getValue(getFacesContext().getELContext());
		}

		if (_valorChave != null) {
			if (_valorChave instanceof Long) {
				valorChave = (Long) _valorChave;
			} else if (_valorChave instanceof Number) {
				valorChave = ((Number) _valorChave).longValue();
			} else if (NumberUtils.isNumber(_valorChave.toString())) {
				valorChave = Long.valueOf(_valorChave.toString());
			}
		}

		if (valorChave != null)
			return valorChave;
		else
			return _valorChave;
	}

}
