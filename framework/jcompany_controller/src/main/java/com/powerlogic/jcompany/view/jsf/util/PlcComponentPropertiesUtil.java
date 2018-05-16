/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import javax.el.ValueExpression;
import javax.faces.el.ValueBinding;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Digits;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.tagfile.PlcTagFileUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcNumeric;
import com.powerlogic.jcompany.view.jsf.component.PlcText;

/**
 * @since jCompany 5.0 Classe para auxiliar na montagem dinamica de valores comuns dos Componentes JSF do JCompany 
 * @author Paulo Alvim
 */
@SPlcUtil
@QPlcDefault
public class PlcComponentPropertiesUtil {
	
	private static final Logger	log	= Logger.getLogger(PlcComponentPropertiesUtil.class.getCanonicalName());
	
	/**
	 * Serviï¿½o injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;

	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcTagFileUtil tagFileUtil;

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	@Inject @QPlcDefault 
	protected PlcComponentPropertiesUtil componentPropertiesUtil;
	
	
 	public PlcComponentPropertiesUtil() { 
 		
 	}

 	/**
 	 * Insere estilo inline mais especifico, incluindo estilo para permitir que campo fique abaixo do rotulo.
 	 */
	public void setPropertyInlineStyle(PropertyKey pk,FacesBean bean) {
		String inlineStyle = (String)bean.getProperty(pk);
		Boolean nup = ((Boolean)bean.getProperty(bean.getType().findKey(PlcTagUtil.SIMPLE)));
		if (StringUtils.isBlank(inlineStyle) && nup)
			bean.setProperty(pk, "float: left;");
	}
	
	/**
	 * Trata obrigatoriedade, com heranca
	 * @param propriedade Propriedade da tag para investigar heranï¿½a
	 * @param obrigatorio indicador de obrigatoriedade declarado
	 */
	public void setPropertyRequired(PropertyKey pk, FacesBean bean, String obrigatorio, String propriedade,String detalhe) {

//		Somente se nao informou valor trinidad direto
		if (!componentUtil.isValueDefined(bean, pk)) { 
			if (!StringUtils.isBlank(obrigatorio)){ 
				if ("S".equals(obrigatorio))
					bean.setProperty(pk, true);
				else
					bean.setProperty( pk, false);
			} else{
				try {
					PlcConfigAggregationPOJO configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
					boolean isRequerid 		 = tagFileUtil.required(configAcao.entity(), detalhe, propriedade);
					if (isRequerid)
						bean.setProperty( pk, true);
					else
						bean.setProperty( pk, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * Trata campo protegido, considerando chave primaria
	 * @param somenteLeitura S ou N
	 * @param chavePrimaria Indicador se ï¿½ ou nao parte da chave primaria (S ou N)
	 */
	public void setPropertyDisabled(PropertyKey pk, FacesBean bean, String somenteLeitura, String chavePrimaria) {
		
		// Somente se nao informou valor trinidad direto
		if (bean.getProperty(pk)==null || StringUtils.isBlank(bean.getProperty(pk).toString())) { 
			
			if ("S".equalsIgnoreCase(chavePrimaria))
				bean.setProperty( pk, true);
			else if ("S".equalsIgnoreCase(somenteLeitura )) 
				bean.setProperty( pk, true);
			else
				bean.setProperty( pk, false);

		}
		
	}
	
	/**
	 * Insere labels segundo convenï¿½ï¿½es do jCompany
	 * @param titulo Titulo sem I18n
	 * @param tituloChave Titulo com I18n
	 */
	public void setPropertyLabel(PropertyKey pk, FacesBean bean, ValueExpression titulo, String tituloChave,String propriedade) {

		// Se nao informou label assume atravï¿½s de opcoes do jCompany
		if (StringUtils.isBlank((String)bean.getProperty(pk))) {
			String bundle = componentPropertiesUtil.getPropertyBundle(bean);

			if (titulo!=null )
				if (titulo.isLiteralText()) {	
					bean.setProperty(pk, titulo.getValue(null));
				} else {
					bean.setValueExpression(pk, titulo);				
				}

			HttpServletRequest request = contextUtil.getRequest();
			PlcI18nUtil i18nUtilI = componentUtil.getUtilI18n();
			
			if (! StringUtils.isBlank(tituloChave)){
				String valorChave = i18nUtilI.mountLocalizedMessageAnyBundle(request, bundle, tituloChave, new String[]{});
				bean.setProperty( pk, valorChave);
			}
			// Se nï¿½o foi definido titulo coloca o padrï¿½o jcompany label.componente_propriedade
			// Alvim: Ajustado para argumentos reusarem os mesmos rotulos e para uso de "_" em lugar de "."
			if (StringUtils.isBlank(tituloChave)  && titulo==null ){

				String rotuloPropriedade = propriedade;

				if (propriedade != null && propriedade.startsWith("argumentos") && propriedade.endsWith("valor"))
					rotuloPropriedade = propriedade.substring(propriedade.indexOf("argumentos")+11,propriedade.length()-6);
				
				String labelChave = i18nUtilI.mountLocalizedMessageAnyBundle(request, bundle, "label." + rotuloPropriedade, new String[]{});
				
				bean.setProperty(pk, labelChave);
			}

		}

	}
	
	/**
	 * registra o tamanho para o componente baseado nos valores informados ou de heranï¿½a do modelo 
	 */
	public void setPropertyColumnsMaxLength(PropertyKey pkColumnsKey, PropertyKey pkPropertyKey, FacesBean bean, String tamanho, String tamanhoMaximo, String propriedade, String detalhe) {
		
		try {

			Integer tamanhoMaximoModelo = 5;
			Integer fractionModel		= null;
			// Se nao informou tamanho maximo para digitacao assume atravï¿½s de opcoes do jCompany
			if (pkPropertyKey != null && StringUtils.isBlank((String)bean.getProperty(pkPropertyKey))) {

				if (!StringUtils.isBlank(tamanhoMaximo)) {
					bean.setProperty(pkPropertyKey, Integer.valueOf(tamanhoMaximo));
					if (StringUtils.isBlank(tamanho)) {
						tamanho = String.valueOf(tamanhoMaximoModelo);
					}
				} else {
					// Se nenhum foi informado verifica heranca do modelo
					PlcConfigAggregationPOJO configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
					/*
					 * Se for String, Short, Integer ou Long - tamanhoMaximoModelo = tamanho maximo total
					 * Se for BigDecimal, Float ou Double - tamanhoMaximoModelo = Parte Interia e fractionModel = parte facionï¿½ria
					 * Se a fractionModel for igual a 0, ï¿½ porque nï¿½o tem fraï¿½ï¿½o
					 * 
					 * Se fractionView for preenchido e fractionModel = 0, utilizar fractionView
					 * Se fractionView e fractionModel forem preenchidos, lanï¿½ar WARN informando que serï¿½ utilizada a propriedade fractionModel
					 * 		Nesse caso, informar ao desenvolvedor para remover a propriedade do xhtml.
					 */
					
					tamanhoMaximoModelo 	= tagFileUtil.getMaximumLength(configAcao.entity(), detalhe, propriedade);
					fractionModel			= tagFileUtil.getMaximumLengthFraction(configAcao.entity(), detalhe, propriedade);
					String fractionView 	= (String)bean.getProperty(PlcNumeric.NUM_CASAS_KEY);
					
					if(StringUtils.isBlank(fractionView)) {
						bean.setProperty(PlcNumeric.NUM_CASAS_KEY, fractionModel);
					} else {
						if(fractionModel != null) {
							log.warn("Numero de casas informado na camada de modelo e na camada de visao (xhtml). Utilizando por padrï¿½o a propriedade fraction da anotacao @Digits, mapeada na entidade.", new Throwable("PlcThrowable")); 							
						}
					}
						
						
					if (tamanhoMaximoModelo == null) {
						tamanhoMaximoModelo = 5;
					}
					
					
					// assume tambem para o tamanho fisico do campo, neste caso
					if (StringUtils.isBlank(tamanho)) {
						tamanho = String.valueOf(tamanhoMaximoModelo);
					}else{
						tamanhoMaximoModelo = Integer.valueOf(tamanho);						
					}
					
					bean.setProperty(pkPropertyKey, tamanhoMaximoModelo);
					
				}

			}

			// Se nao informou tamanho fisico do campo assume atravï¿½s de opcoes do jCompany
			if (bean.getProperty(pkColumnsKey)==null || (pkPropertyKey != null && StringUtils.isBlank(bean.getProperty(pkPropertyKey).toString()))) {

				// Se tamanho for informado (ou se nao for e o maximo for), inclui em columns
				if (StringUtils.isNotBlank(tamanho)) {
					bean.setProperty( pkColumnsKey, Integer.valueOf(tamanho));
				} else { 
					bean.setProperty(pkColumnsKey, 5);
				}
			}

		} catch (Exception e) {
			log.error( e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Insere estilo ShortDesc. No jCompany, ï¿½ obrigatï¿½rio e no padrï¿½o "ajuda.[propriedade]". Para evitar
	 * o padrao e nï¿½o utilizar 'title' nos campos, deve-se declarar ajuda="#" ou ajudaChave="#"
	 */
	public void setPropertyShortDesc(PropertyKey pk, FacesBean bean,String ajuda,String ajudaChave,String propriedade) {
		
		if ((bean.getProperty(pk)==null || StringUtils.isBlank((String)bean.getProperty(pk))) &&
				!("#".equals(ajuda) || "#".equals(ajudaChave))) {
			
			// O padrao ï¿½ ajuda com convencao
			if (StringUtils.isBlank(ajudaChave) && StringUtils.isBlank(ajuda)){
				String rotuloPropriedade = propriedade;
				
				if (propriedade != null && propriedade.startsWith("argumentos") && propriedade.endsWith("valor")){
					rotuloPropriedade = propriedade.substring(propriedade.indexOf("argumentos")+11,propriedade.length()-6);
					rotuloPropriedade = rotuloPropriedade.contains("_")?rotuloPropriedade.replace("_", "."):rotuloPropriedade;
				}
				
				ajudaChave = "ajuda."+rotuloPropriedade;
			}
				
			
			if (!StringUtils.isBlank(ajudaChave )) {
				PlcI18nUtil i18nUtilI = componentUtil.getUtilI18n();
				String valorChave 	= i18nUtilI.mountLocalizedMessageAnyBundle(contextUtil.getRequest(), ajudaChave, null);
				bean.setProperty(pk, valorChave);
			} else if (!StringUtils.isBlank(ajuda))
				bean.setProperty(pk, ajuda);
		}
		
	}
	
	/**
	 * Modo de renderizaï¿½ï¿½o padrao do jCompany ï¿½ true
	 */
	public void setPropertySimple(PropertyKey pk, FacesBean bean) {
		
		if (bean.getProperty(pk)==null) {
			bean.setProperty(pk, true);
		}
		
	}
	
	/**
	 * registrando o Styleclass
	 */
	public void setPropertyStyleClass(PropertyKey pk, FacesBean bean, String classeCSS) {
		
		if (bean.getProperty(pk)==null || StringUtils.isBlank((String)bean.getProperty(pk))) {
		
			bean.setProperty(pk,classeCSS);
		
		}
		
	}
	
	/**
	 * Aplica rendered conforme exibeSe
	 * @param exibeSe regra de exibiï¿½ï¿½o
	 */
	public void setPropertyRendered(PropertyKey pk, FacesBean bean, ValueExpression exibeSe) {
		
		if (exibeSe != null){
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			Boolean valorExibeSe = elUtil.evaluateExpressionGet(exibeSe.getExpressionString(), Boolean.class);
			bean.setProperty(PlcText.RENDERED_KEY, valorExibeSe != null ? valorExibeSe.booleanValue() : true);
		}
	
	}
	
	/**
	 * Registra propriedade
	 * @param propriedade Propriedade da tag
	 */
	public void setPropertyProperty(PropertyKey pk, FacesBean bean, String propriedade) {
		
		if (! StringUtils.isBlank(propriedade))
			bean.setProperty(pk, propriedade);
		
	}
	
	/**
	 * Mï¿½todo para inserir um JavaScript em parï¿½metros, adicionando a algum cï¿½digo que jï¿½ exista na propriedade.
	 * Isso evita a substituiï¿½ï¿½o de cï¿½digos, ao forï¿½ar algum cï¿½digo em atributo (p.ex. onClick, onSubmit, etc.).
	 * Ou seja, se jï¿½ existir algum cï¿½digo, concatena o cï¿½digo indicado apï¿½s um ponto-e-virgula. 
	 * @param pk chave do atributo
	 * @param bean componente
	 * @param script cï¿½digo a ser adicionado ao atributo.
	 */
	public void setPropertyNestedJavaScript(PropertyKey pk, FacesBean bean, String script) {
		if (StringUtils.isBlank(script)) {
			return;
		}
		Object valor = bean.getProperty(pk);
		if (valor==null || valor.toString().trim().length()==0) {
			bean.setProperty(pk, script);
		} else {
			String _valor = valor.toString().trim();
			if (!_valor.endsWith(";")) {
				_valor = _valor + ";";
			}
			bean.setProperty(pk, _valor+script);
		}
	}
	
	/**
	 * Inclui registro de 'componente' corrente para cada campo
	 * @param detalhe Nome do componente de detalhe ou null se for mestre
	 */
	public void setPropertyOnFocus(PropertyKey pk, FacesBean bean, String detalhe) {
		
		String javascriptOnFocus = "";
		
		ValueExpression value = bean.getValueExpression(bean.getType().findKey("value"));
		
		boolean isArgumento = value != null && value.getExpressionString().startsWith("#{plcControleDetalhePaginado.mapaControles");
		
		if (detalhe !=null && !detalhe.contains("[") && !detalhe.contains("]"))
			javascriptOnFocus="insereValorCampo('detCorrPlc', '" + detalhe+ "');";
		else if (StringUtils.isBlank(detalhe))
			javascriptOnFocus="insereValorCampo('detCorrPlc', '');";
				
		if (componentUtil.isValueDefined(bean, pk)) {
			Object property = bean.getLocalProperty(pk);
			if (property!=null) {
				String expressao;
				if (property instanceof ValueExpression) {
					expressao=((ValueExpression)property).getExpressionString();
				} else if (property instanceof ValueBinding) {
					expressao=((ValueBinding)property).getExpressionString();
				} else {
					expressao=property.toString();
				}

				if (StringUtils.isNotBlank(expressao) && !isArgumento) {
					javascriptOnFocus = expressao+';'+javascriptOnFocus;
				}
			}
		}
		if (StringUtils.isNotBlank(javascriptOnFocus) && !isArgumento){
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			ValueExpression ve = elUtil.createValueExpression(javascriptOnFocus, String.class);
			if (ve.isLiteralText()) {
				bean.setProperty(pk, ve.getValue(null));
			} else {
				bean.setValueExpression(pk, ve);
			}
		}
		
	}
	/**
	 * Registra a linha do componente em uma lï¿½gica Tabular ou Detalhe
	 * @param keyLinha, chave do atributo
	 * @param numLinha
	 */
	public void setPropertyLineOnCollection(FacesBean bean,PropertyKey keyLinha, Object numLinha) {
		bean.setProperty(keyLinha, numLinha);
		
	}
	
	/**
	 * Registra o arquivo de resources especï¿½fico para o componente.
	 */
	public void setPropertyBundle(PropertyKey key, FacesBean bean, String bundle){
		bean.setProperty(key, bundle);
	}
	
	/**
	 * Retorna o arquivo de resources.
	 * Se nï¿½o foi informado retorna o padrï¿½o.
	 */
	public String getPropertyBundle(FacesBean bean){
		if (bean!=null) {
			PropertyKey key = bean.getType().findKey(PlcTagUtil.BUNDLE);
			if (key != null){
				Object bundle  	= bean.getProperty(key);
				if ( bundle != null)
					return (String)bundle;
			}
		}
		return PlcJsfConstantes.BUNDLE_PADRAO_TAGS;
	}
	
	public void setPropertyOnBlur(PropertyKey key, FacesBean bean, String propriedadeDeValue) {
		try{
			PlcConfigAggregationPOJO grupoAgregado = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			Class classe = grupoAgregado.entity();
			PlcPrimaryKey chavePrimaria = (PlcPrimaryKey)classe.getAnnotation(PlcPrimaryKey.class);
			
			String onBlur = (String)bean.getProperty(key);
						
			if (chavePrimaria != null && chavePrimaria.autoRecuperacao()){
				String [] campos = chavePrimaria.propriedades();
				for (int i = 0; i< campos.length; i++){
					if (propriedadeDeValue.contains(campos[i])){
						String autoRecuperacao = "autoRecuperacaoChaveNatural(this, '" + campos[i]  + "'," + campos.length + ");";
						bean.setProperty(key, StringUtils.isNotBlank(onBlur)?onBlur + autoRecuperacao:autoRecuperacao);
					}
				}
			}
			
		}catch(PlcException e){
			e.printStackTrace(); 
		}
	}
	
	
	/**
	 * Insere tï¿½tulo para botï¿½o do vinculado segundo convenï¿½ï¿½es do jCompany
	 * 
	 * @param chave  Titulo com I18n
	 * @autor Geraldo Matos
	 */
	public void setPropertyAggregateButtonTitle(PropertyKey pk, FacesBean bean,
			String titulo, String chave) {
		// Se nao informou label assume atravï¿½s de opcoes do jCompany
		if (StringUtils.isBlank((String) bean.getProperty(pk))) {
			String bundle = componentPropertiesUtil.getPropertyBundle(bean);

			if (StringUtils.isNotBlank(chave)) {
				String valorChave = componentUtil.createLocalizedMessage(bean, chave, new Object [] {});
				bean.setProperty(pk, valorChave);
			} else {
				if (StringUtils.isNotBlank(titulo)) {
					bean.setProperty(pk, titulo);
				} else {
					String botao = pk.getName().contains("SelPop") ? PlcJsfConstantes.COMPONENTES.PLC_VINCULADO_TITULO_BOTAO_SEL_POP: PlcJsfConstantes.COMPONENTES.PLC_VINCULADO_TITULO_BOTAO_LIMPAR;
					// Se nï¿½o foi definido titulo coloca o padrï¿½o jcompany
					// jcompany.componente.vinculado.label.popup
					String valorChave = componentUtil.createLocalizedMessage(bean, botao, new Object [] {});
					if (valorChave.contains("???")) {
						valorChave = componentUtil.getUtilI18n().mountLocalizedMessage("jCompanyResources", botao, new String[] {});
					}
					bean.setProperty(pk, valorChave);
				}
			}
		}
	}

}
