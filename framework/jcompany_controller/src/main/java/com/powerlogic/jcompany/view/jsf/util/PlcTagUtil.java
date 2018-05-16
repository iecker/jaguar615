/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.el.ValueExpression;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXValue;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcValCnpj;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.view.jsf.component.PlcAggregate;
import com.powerlogic.jcompany.view.jsf.component.PlcText;

/**
 * Helper de apoio as Tags, principalmente para registrar valor para propriedades específicas
 *
 */

@QPlcDefault
public class PlcTagUtil implements Serializable {

	private static final long serialVersionUID = -8747929531115016503L;

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Propriedades usadas na maioria das tags
	 */
	static public final String DISABLED 		= "disabled";
	static public final String SHORT_DESC 		= "shortDesc";
	static public final String LABEL 			= "label";
	static public final String SIMPLE 			= "simple";
	static public final String ONFOCUS 			= "onfocus";
	static public final String STYLE_CLASS 		= "styleClass";
	static public final String INLINE_STYLE 	= "inlineStyle";
	static public final String COLUMNS 			= "columns";
	static public final String MAXIMUM_LENGTH 	= "maximumLength";
	static public final String REQUIRED 		= "required";
	static public final String PROPRIEDADE 		= "propriedade";
	static public final String BUNDLE 			= "bundle";
	static public final String ONBLUR 			= "onblur";
	static public final String RENDERED			= "rendered";
	static public final String RIA_USA			= "riaUsa";
	
	/**
	 * Insere propriedades comuns a várias tags. As propriedades são recebidas através do PlcGlobalTag.
	 *	
	 * @param bean
	 * @param TYPE
	 * @param listaAtributos
	 */
	public static void setCommonProperties(FacesBean bean, FacesBean.Type TYPE, PlcGlobalTag globalTag) {	

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcURLUtil urlUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (logVisao.isDebugEnabled()) {
    		logVisao.debug("(Trinidad)"+PlcTagUtil.class.getSimpleName()+":setPropertiesComuns");
		}
		
		PropertyKey key = null;
		
		//Deve ser a primeira propriedade a ser registrada, pois algumas dependem desta.
		/* 
		 * Arquivo de Resources para i18n.
		 * Se não foi informado o default será ApplicationResources
		 */
		key				= TYPE.findKey(PlcTagUtil.BUNDLE);
		String bundle 	= getDefaultBundle(globalTag.getBundle());
		componentPropertiesUtil.setPropertyBundle(key, bean, bundle);		
		
		// Propriedade
		key = TYPE.findKey(PlcTagUtil.PROPRIEDADE);
		if (key != null) {
			componentPropertiesUtil.setPropertyProperty(key,bean,globalTag.getPropriedade());
		}
		
		// Classe padrao - Paramentros: classeCSS
		key = TYPE.findKey(PlcTagUtil.STYLE_CLASS);
		componentPropertiesUtil.setPropertyStyleClass(key,bean,globalTag.getClasseCSS());
				
		// Renderizacao padrao
		key = TYPE.findKey(PlcTagUtil.SIMPLE);
		componentPropertiesUtil.setPropertySimple(key,bean);
		
		// Estilo padrao
		key = TYPE.findKey(PlcTagUtil.INLINE_STYLE);
		componentPropertiesUtil.setPropertyInlineStyle(key,bean);

		// Ajuda - Parametros: ajuda, ajudaChave, propriedade
		key = TYPE.findKey(PlcTagUtil.SHORT_DESC);
		componentPropertiesUtil.setPropertyShortDesc(key,bean,globalTag.getAjuda(),globalTag.getAjudaChave(),globalTag.getPropriedade());

		// Lógicas de rotulos para mensagem de erro - Parametros: titulo,tituloChave,propriedade
		
		Boolean nup = ((Boolean)bean.getProperty(TYPE.findKey(PlcTagUtil.SIMPLE)));
		if(!nup){
			key = TYPE.findKey(PlcTagUtil.LABEL);
			componentPropertiesUtil.setPropertyLabel(key,bean,globalTag.getTitulo(),globalTag.getTituloChave(),globalTag.getPropriedade());
		}
		
		// Lógicas de desabilitação - Parametros: somenteLeitura,chavePrimaria
		key = TYPE.findKey(PlcTagUtil.DISABLED);
		componentPropertiesUtil.setPropertyDisabled(key,bean,globalTag.getSomenteLeitura(), globalTag.getChavePrimaria());
		
		// Registra nome detalhe se estiver neste contexto
		String detalhe = componentUtil.getCurrentDetail();
		String subdetalhe = componentUtil.getCurrentSubDetail();
		
		// Lógicas de obrigatoriedade - Parametros: obrigatorio,propriedade
		key = TYPE.findKey(PlcTagUtil.REQUIRED);
		componentPropertiesUtil.setPropertyRequired(key,bean,globalTag.getObrigatorio(),globalTag.getPropriedade(),detalhe);
		
		// Lógicas de rotulos para mensagem de erro - Parametros: tamanho,tamanhoMaximo,propriedade
		key = TYPE.findKey(PlcTagUtil.COLUMNS);
		if (TYPE.findKey(PlcTagUtil.MAXIMUM_LENGTH) != null) {
			if(detalhe != null && subdetalhe != null && !subdetalhe.equals("null")) {
				detalhe = detalhe + "." + subdetalhe; 
			}
			componentPropertiesUtil.setPropertyColumnsMaxLength(key, TYPE.findKey(PlcTagUtil.MAXIMUM_LENGTH),bean, globalTag.getTamanho(),globalTag.getTamanhoMaximo(),globalTag.getPropriedade(),detalhe);
		}
		
		key = TYPE.findKey(PlcTagUtil.ONBLUR);
		if (TYPE.findKey(PlcTagUtil.ONBLUR) != null){
			componentPropertiesUtil.setPropertyOnBlur(key, bean, globalTag.getPropriedade());
		}
		
		key = TYPE.findKey(PlcTagUtil.RENDERED);
		if (TYPE.findKey(PlcTagUtil.RENDERED) != null){
			componentPropertiesUtil.setPropertyRendered(key, bean, globalTag.getExibeSe());
		}
		
		// Lógicas de componente sensível ao contexto, para logicas de Mestre-Detalhe e variantes.
		try {
			FormPattern logicaCorrente = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest())).pattern().formPattern();
			if (logicaCorrente.equals(FormPattern.Mdt) || logicaCorrente.equals(FormPattern.Mad) || logicaCorrente.equals(FormPattern.Mds) || logicaCorrente.equals(FormPattern.Mas)) {	
				key = TYPE.findKey(PlcTagUtil.ONFOCUS);
				componentPropertiesUtil.setPropertyOnFocus(key,bean,detalhe);
			}
		} catch (Exception e) {
			// Assume que nao é logica jCompany se saiu
		}
		
	}
	
	
	/**
	 * Ajusta Cpf e Cnpj com zeros a esquerda quando o valores sao do tipo Long e iniciam com 0.
	 */
	public static Object fitCpfCnpjWithZeros(FacesBean bean, Object valor, String propriedade) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		//Recuperando a entidade corrente, não importa a Lógica {entityPlc ou itensPlc}
		Object entity = componentUtil.getManagedBean(bean);
		
		if ( entity != null ){
			try {
				Class<?> classe 	= entity.getClass();
				Field field 	= null;
				if (metamodelUtil.isEntityClass(classe)){ 
					// Se for componente
					if (propriedade.contains(".")){
						//propriedade = propriedade.substring(propriedade.indexOf(".") + 1);
						field 		= reflectionUtil.findFieldHierarchically(classe,propriedade);
					}
					else{
						//não é componente
						field 		= reflectionUtil.findFieldHierarchically(classe,propriedade);
					}
					PlcValCpf formatoCpf = field.getAnnotation(PlcValCpf.class);
					PlcValCnpj formatoCnpj = field.getAnnotation(PlcValCnpj.class);
					if (formatoCpf!=null || formatoCnpj!=null){
						if (valor!=null && valor instanceof Long){
							String _valor = valor.toString(); 
							if (formatoCpf!=null && StringUtils.isNotBlank(_valor)){
								return StringUtils.leftPad(String.valueOf(_valor), 11,"0");
							}
							if (formatoCnpj!=null && StringUtils.isNotBlank(_valor)){
								return StringUtils.leftPad(String.valueOf(_valor), 14,"0");
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Busca formato simples da classe abstrata para formatação do campo.
	 */
	public static String getAnnotationSimpleFormat(FacesBean bean) {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);

		String formato 		= (String)bean.getProperty(PlcText.FORMATO_KEY);
		String propriedade 	= (String)bean.getProperty(PlcText.PROPRIEDADE_KEY);
		//Recuperando a entidade corrente, não importa a Lógica {entityPlc ou itensPlc}
		Object entity		= componentUtil.getManagedBean(bean);
		if ( entity != null ){
			try {
				Class<?> classe 	= entity.getClass();
				Field field 	= null;
				if (metamodelUtil.isEntityClass(classe)){ 
					// Se for componente
					if (propriedade.contains(".")){
						//propriedade = propriedade.substring(propriedade.indexOf(".") + 1);
						field 		= reflectionUtil.findFieldHierarchically(classe,propriedade);
					}
					else{
						//não é componente
						field 		= reflectionUtil.findFieldHierarchically(classe,propriedade);
					}
					PlcValSimpleFormat formatoSimples = field.getAnnotation(PlcValSimpleFormat.class);

					if (formatoSimples != null && StringUtils.isBlank(formato))
						formato = formatoSimples.format().toString();
				}

			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return formato;

	}
	
	/**
	 * Usado nas tags que são utilizadas em propriedades chaves.
	 * @param bean
	 * @param propriedade
	 * @return true se for chave e estiver preenchido
	 */
	public static boolean checkNaturalKeyFilled(FacesBean bean, String propriedade) {
		// Verivica se é propriedade da chave natural
		if (propriedade != null){
			if (propriedade.startsWith("idNatural.")){
				// Se for propriedade da chave e estiver preenchido, deve ficar disabled
				Object valor = bean.getProperty(UIXValue.VALUE_KEY);
				if (valor instanceof String){
					if (StringUtils.isNotBlank((String)valor)){
						return true;
					} else {
						return false;
					}
				}else if (valor != null){
					return true;
				} else {
					return false;
				}
			} else {
				PropertyKey key = bean.getType().findKey(DISABLED);
				if (key != null){
					Boolean disabled = (Boolean)bean.getProperty(key);
					if (disabled != null){
						return disabled.booleanValue();
					}
				}

				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Usado para verificar se um componente devera ser exibido ou estar habilitado para o usuario ou nao
	 * @param chaveNaturalPreenchido
	 * @return disabled
	 */
	public static Boolean checkShowComponent(Boolean chaveNaturalPreenchido) {
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);

		Boolean disabled = false;
		
		List<?> itensPlcAnterior = elUtil.evaluateExpressionGet("#{plcLogicaItensAnterior.itensPlc}", List.class);
		
		if (itensPlcAnterior != null && !itensPlcAnterior.isEmpty()) {
			Integer count = elUtil.evaluateExpressionGet("#{"+PlcJsfConstantes.PLC_ITENS_STATUS+".count}", Integer.class);				
			
			if ((count <= itensPlcAnterior.size()) && chaveNaturalPreenchido) {
				disabled = true;
			}	
		}
		else {
			Object plcEntidadeAnterior = elUtil.evaluateExpressionGet("#{plcEntidadeAnterior}", Object.class);
			if (plcEntidadeAnterior != null) {
				try {
					if ((chaveNaturalPreenchido) && (reflectionUtil.callGetter(plcEntidadeAnterior,  "idNatural") != null)) {
						disabled = true;
					}
				} catch (PlcException e) {
					disabled = true;
				}
			}
			else {
				if ((chaveNaturalPreenchido) /**&& (!ehInclusao)**/) {
					disabled = true;
				}
			}
		}
		return disabled;
	}
	
	public static Map<String, Object> createNaturalKeyMap(String entity)
			throws ClassNotFoundException {
		PlcPrimaryKey chavePrimaria 	= (PlcPrimaryKey)Class.forName(entity).getAnnotation(PlcPrimaryKey.class);
		Map<String, Object> propsChaveNatural = new TreeMap<String, Object>(new PlcTreeMapComparator());
		
		if (chavePrimaria != null)
			for (String propriedadeCN : chavePrimaria.propriedades()) 
				propsChaveNatural.put(propriedadeCN, null);
		return propsChaveNatural;
	}
	

	
	/**
	 * Retorna o bundle padrão para a Lógica Corrente.
	 * Se a Lógica for de Módulo retorna o módulo padrão do Módulo,
	 * Se não retorna o módulo padrão da aplicação.
	 * 
	 * Se o bundle for informado para o componente não vai descobrir o módulo padrão pois já existe
	 * 
	 * @param bundle , bundle declarado no componente. 
	 * @return o bundle padrão para o componente
	 */
	public static String getDefaultBundle(String bundle ){
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Se foi informado o bundle não precisa descobrir o bundle default
		if (StringUtils.isNotBlank(bundle)) {
			return bundle;
		} else{
			String modulo = (String) contextUtil.getRequest().getAttribute(PlcJsfConstantes.PLC_MODULO_CORRENTE);
			if (  modulo != null) {
				return modulo+ "Resources";
			}
		}
		
		return null;
	}
	
	/**
	 * Método para a recuperação da classe do vinculado
	 * 
	 * @param bean
	 * @return a classe do atributo vinculado
	 * @throws NoSuchFieldException
	 */
	public static String getClassByField(FacesBean bean) throws NoSuchFieldException {
		
		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		String nameClass = null;
		
		ValueExpression valueExpression = bean.getValueExpression(PlcAggregate.VALUE_KEY);
		String expressao = valueExpression.getExpressionString();
		expressao = expressao.replace("#{", "").replace("}","");
		String atributo = expressao.substring(expressao.indexOf(".")+1);
		String managedBean = expressao.substring(0, expressao.indexOf("."));
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		Object objeto = elUtil.evaluateExpressionGet("#{" + managedBean + "}", Object.class);
		
		Field field = reflectionUtil.findFieldHierarchically(objeto.getClass(), atributo);

		ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
		if (manyToOne != null){
			nameClass = manyToOne.targetEntity().getName();
		}

		return nameClass;
	}
	
	public static Object getValue(TagAttribute atributo, FaceletContext ctx) {
		if (atributo != null)
			return atributo.getValue(ctx);

		return null;

	}
	
	public static ValueExpression getValueExpression(TagAttribute atributo, FaceletContext ctx, Class<?> type) {
		if (atributo != null)
			return atributo.getValueExpression(ctx, type);

		return null;

	}
	
}
