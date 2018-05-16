/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import java.util.TreeMap;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidadinternal.taglib.util.MethodExpressionMethodBinding;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcAggregate;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;


public class PlcAggregateAdapter {

	private static final Logger log = Logger.getLogger(PlcAggregateAdapter.class.getCanonicalName());
	
	private static PlcAggregateAdapter INSTANCE = new PlcAggregateAdapter ();
	
	private PlcAggregateAdapter () {
		
	}
	
	public static PlcAggregateAdapter getInstance () {
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
	 * @param modal 
	 * @param riaParametros 
	 * @param alias
	 * @param propOrdenacao
	 * @param ordem
	 */
	public void adapter (FacesBean bean, ValueExpression propSelPop, ValueExpression propsSelPop, String limpaPropsSelPop, String multiSel, String multiSelTitulo, String tituloBotaoSelPop,
	 String tituloChaveBotaoSelPop, String tituloBotaoLimpar, String tituloChaveBotaoLimpar, String lookupTamanho, String idTamanho, String classeCSS, String colunas, String idExibe,
	 String idSomenteLeitura, String obrigatorio, String obrigatorioDestaque, String autoRecuperacaoClasse, String autoRecuperacaoPropriedade, String ajaxIdUnico, String tamanho,			
	 String tamanhoMaximo, String chavePrimaria, String baseActionSel, ValueExpression actionSel, String evento, ValueExpression parametro, String alt, String larg, String posx, String posy,
	 String ajudaChave, String ajuda, String tituloChave, ValueExpression titulo, ValueExpression exibeSe, String bundle, String exibeBotaoLimpar, ValueExpression riaUsa, 
	 String somenteLeitura, String modal ,String delimitador, String transacaoUnica) {
		
		// Passar estas propriedades para a função setPropertiesComuns
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcGlobalTag globalTag = new PlcGlobalTag (componenteUtil.getValueProperty(bean),
													classeCSS, ajuda,
													ajudaChave, titulo, tituloChave,
													null, chavePrimaria, obrigatorio,
													tamanho, tamanhoMaximo ,bundle, exibeSe);
		
		if (propSelPop!=null) {
			bean.setProperty(PlcAggregate.PROPSEL_POP_KEY,propSelPop);
		} else {
			bean.setProperty(PlcAggregate.PROPSEL_POP_KEY,globalTag.getPropriedade());
		}
		bean.setProperty(PlcAggregate.PROPSSEL_POP_KEY,propsSelPop);
		
		bean.setProperty(PlcAggregate.LIMPA_PROPSSEL_POP_KEY, limpaPropsSelPop);

		componentPropertiesUtil.setPropertyAggregateButtonTitle(PlcAggregate.TITULO_BOTAO_LIMPAR_KEY, bean, tituloBotaoLimpar, tituloChaveBotaoLimpar);

		componentPropertiesUtil.setPropertyAggregateButtonTitle(PlcAggregate.TITULO_BOTAO_SEL_POP_KEY, bean, tituloBotaoSelPop, tituloChaveBotaoSelPop);

		bean.setProperty(PlcAggregate.OBRIGATORIO_KEY, obrigatorio);
		
		bean.setProperty(PlcAggregate.MODAL, modal);
		bean.setProperty(PlcAggregate.DELIMITADOR, delimitador);

		if (StringUtils.isNotBlank(multiSel))
			bean.setProperty(PlcAggregate.EXIBE_BOTAO_MULTISEL, multiSel.equals("S"));
		
		bean.setProperty(PlcAggregate.BOTAO_MULTISEL_TITULO, StringUtils.isNotBlank(multiSelTitulo)?multiSelTitulo:"Seleção Múltipla");

		// Propriedades Especificas desta Tag
		// Tamanhos de agregado não 'herdam' de modelo
		if (tamanho == null && tamanhoMaximo != null)
			tamanho = tamanhoMaximo;
		bean.setProperty(CoreInputText.COLUMNS_KEY, Integer.valueOf(tamanho));

		bean.setProperty(PlcAggregate.LOOKUP_TAMANHO_KEY, lookupTamanho);
		bean.setProperty(PlcAggregate.ID_TAMANHO_KEY, idTamanho);
		// Fim tamanhos
		
		if((StringUtils.isBlank(idSomenteLeitura) || idSomenteLeitura.equals("S")) && StringUtils.isBlank(autoRecuperacaoClasse)) {
			log.warn("########## Configuração inconsistente no Vinculado '" + tituloChave + "'.");
			log.warn("########## Vinculado marcado como 'idSomenteLeitura' mas a propriedade 'autoRecuperacaoClasse' desabilita a opção para funcionamento da Auto Recuperação. Ajuste seu caso de uso corretamente.");
		}
		
		if ((StringUtils.isBlank(idSomenteLeitura) || idSomenteLeitura.equals("S"))) {
			idSomenteLeitura = "S";
		} else {
			idSomenteLeitura = "N";
		}

		bean.setProperty(PlcAggregate.ID_EXIBE_KEY,"N".equalsIgnoreCase(idExibe) ? "N" : "S");
		bean.setProperty(PlcAggregate.ID_SOMENTE_LEITURA_KEY,idSomenteLeitura);

		bean.setProperty(PlcAggregate.AUTO_RECUPERACAO_CLASSE_KEY,autoRecuperacaoClasse);
		bean.setProperty(PlcAggregate.AUTO_RECUPERACAO_PROPRIEDADE_KEY,autoRecuperacaoPropriedade);
		if (StringUtils.isNotBlank(autoRecuperacaoClasse)){
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			MethodExpression me = elUtil.createMethodExpression("#{plcAction.autofindAggregate}", String.class, new Class[]{javax.faces.event.ValueChangeEvent.class});
			bean.setProperty(UIXEditableValue.VALUE_CHANGE_LISTENER_KEY, new MethodExpressionMethodBinding(me));
			bean.setProperty(PlcAggregate.AUTO_SUBMIT_KEY, true);
		}

		
		bean.setProperty(PlcAggregate.AJAX_ID_UNICO_KEY,ajaxIdUnico);

		bean.setProperty(PlcAggregate.BASE_ACTION_KEY,StringUtils.isNotBlank(baseActionSel) ? baseActionSel : "");
		bean.setProperty(PlcAggregate.ACTION_SEL_KEY,actionSel);
		bean.setProperty(PlcAggregate.PARAMETRO_KEY, parametro);

		bean.setProperty(PlcAggregate.ALT_KEY,StringUtils.isNotBlank(alt) 	? alt 	: "0");
		bean.setProperty(PlcAggregate.LARG_KEY,StringUtils.isNotBlank(larg) ? larg 	: "0");
		bean.setProperty(PlcAggregate.POSX_KEY,StringUtils.isNotBlank(posx) ? posx 	: "0");
		bean.setProperty(PlcAggregate.POSY_KEY,StringUtils.isNotBlank(posy) ? posy 	: "0");

		bean.setProperty(PlcAggregate.EVENTO_KEY,StringUtils.isNotBlank(evento) ? evento : "");

		// Propriedades Comuns
		PlcTagUtil.setCommonProperties(bean, bean.getType(), globalTag);
		
		TreeMap<String, Object> propsChaveNatural 	= (TreeMap<String, Object>)bean.getProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC);
		
		if (propsChaveNatural 	== null || propsChaveNatural.isEmpty()){
			
			String entity = (String)bean.getProperty(PlcAggregate.AUTO_RECUPERACAO_CLASSE_KEY);
			
			if (entity != null && StringUtils.isNotBlank(entity.toString()))
				try {
					propsChaveNatural = (TreeMap<String, Object>)PlcTagUtil.createNaturalKeyMap(entity);
					bean.setProperty(PlcAggregate.PROPS_CHAVE_NATURAL_PLC, propsChaveNatural);
				} catch (ClassNotFoundException e) {
					log.info( "A classe de autorecuperação não foi informado ou está incorreta. Classe informada: " + entity,	e);
					chavePrimaria 	= null;
				}
		}

		if (StringUtils.isNotBlank(exibeBotaoLimpar) || "S".equals(exibeBotaoLimpar))
			bean.setProperty(PlcAggregate.EXIBE_BOTAO_LIMPAR_KEY,true);
		else
			bean.setProperty(PlcAggregate.EXIBE_BOTAO_LIMPAR_KEY,false);
			  
		bean.setProperty(PlcAggregate.RIA_USA, riaUsa);
	
		bean.setProperty(PlcAggregate.SOMENTE_LEITURA, somenteLeitura);
		
		bean.setProperty(PlcAggregate.TRANSACAO_UNICA, transacaoUnica);
	}
	
}
