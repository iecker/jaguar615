/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreInputFile;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.render.CoreRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.InputFileRenderer;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcInputFile;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcMsgComponentsUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcRendererUtil;

/**
 * Especialização do renderer base InputFileRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcInputFileRenderer extends InputFileRenderer {

	// Definindo o tipo do renderer
	static public final String RENDERER_TYPE 	= "com.powerlogic.jsf.Arquivo";

	/**
	 * IoC do jcompany. Se o inputFile contém valor, então cria componentes HTML para mostrar a descrição do arquivo e o botão para fazer o download
	 */
	@Override
	protected void delegateRenderer(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean, CoreRenderer arg4) throws IOException {

		PlcFile arqEntity  = (PlcFile)bean.getProperty(PlcInputFile.VALUE_ARQUIVO_KEY);
		if (arqEntity != null && arqEntity.getId()!=null){


			// Cria o campo texto com o nome do arquivo anexo.
			ResponseWriter rw = context.getResponseWriter();

			rw.startElement("span",component);

			String estilos = (String)bean.getProperty(CoreInputFile.STYLE_CLASS_KEY);

			//estilos nao podem ser fixos
			if (!StringUtils.isBlank(estilos))
				rw.writeAttribute("class", 	estilos, null);
			else
				rw.writeAttribute("class", 	"af_inputText", null);

			estilos = (String)bean.getProperty(CoreInputFile.INLINE_STYLE_KEY);

			if (!StringUtils.isBlank(estilos))
				rw.writeAttribute("style", 	estilos, null);
			else
				rw.writeAttribute("style", 	"float: left;", null);

			rw.startElement("input", component);
			renderId(context, component);
			renderAllAttributes(context, arc, component, bean, false);
			rw.writeAttribute("type", "text", "type");
			rw.writeAttribute("value", arqEntity.getNome(), null);
			rw.writeAttribute("readonly", true, null);
			rw.writeAttribute("name", arqEntity, "name");
			rw.endElement("input");

			rw.endElement("span");
		}
		else {
			// Cria o campo texto com o nome do arquivo anexo.
			ResponseWriter rw = context.getResponseWriter();

			rw.startElement("span",component);

			String estilos = (String)bean.getProperty(CoreInputFile.STYLE_CLASS_KEY);

			//estilos nao podem ser fixos
			if (!StringUtils.isBlank(estilos))
				rw.writeAttribute("class", 	estilos, null);
			else
				rw.writeAttribute("class", 	"af_inputText", null);

			estilos = (String)bean.getProperty(CoreInputFile.INLINE_STYLE_KEY);

			if (!StringUtils.isBlank(estilos))
				rw.writeAttribute("style", 	estilos, null);
			else
				rw.writeAttribute("style", 	"float: left;", null);

			super.delegateRenderer(context, arc, component, bean,arg4);
			rw.endElement("span");


		}

		try{

			String defaultRiaParameters = getDefaultRiaParameters(context, component, bean);
			String defaultRia = getDefaultRia(context, component, bean);
			String customRia = getCustomRia(context, component, bean);
			PlcRendererUtil.includeRiaTemplates(component.getClientId(context), defaultRiaParameters, defaultRia, customRia, context.getResponseWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void encodeEnd(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {

		super.encodeEnd(context, arc, component, bean);

		PlcMsgComponentsUtil msgComponentsUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgComponentsUtil.class, QPlcDefaultLiteral.INSTANCE);

		msgComponentsUtil.printMessageError(component);

	}

	/**
	 * @return Parâmetros default de todos os templates ria.
	 */
	protected String getDefaultRiaParameters(FacesContext context, UIComponent component, FacesBean bean) {
		return "id='" + component.getClientId(context) + "'";
	}
	/**
	 * @return Templates RIA default do componente.
	 */
	protected String getDefaultRia(FacesContext context, UIComponent component, FacesBean bean) {
		return null;
	}
	/**
	 * @return Templates RIA customizado.
	 */
	protected String getCustomRia(FacesContext context, UIComponent component, FacesBean bean) {
		return (String) bean.getProperty(PlcInputFile.RIA_USA);
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {

		PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		try {
			return super.getConvertedValue(context, component, submittedValue);
		} catch (ConverterException e) {

			PlcConfigUtil configUtil =  PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
			PlcURLUtil urlUtil =  PlcCDIUtil.getInstance().getInstanceByType(PlcURLUtil.class, QPlcDefaultLiteral.INSTANCE);
			PlcContextUtil contextUtil =  PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

			String url;
			try {
				url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

				PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
				PlcConfigAggregationPOJO configDominio = configUtil.getConfigAggregation(url);
				
				Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(configDominio.entity(), PlcFileAttach.class);
				
				for (Field field : camposAnotados) {
					PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
					
					if (field.getName().equals(component.getAttributes().get("propriedade"))) {
						if (fileAttach.maximumLength() > 0) {
							msgUtil.msgError(PlcBeanMessages.JCOMPANY_ERROR_FILE_INVALID_MAX_LENGTH, new String[] {String.valueOf(fileAttach.maximumLength())});
						}
					}	
				}
				
				return null;
				
			} catch (PlcException e1) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FILE_INVALID_MAX_LENGTH, e1);
			}				
		}

	}	

}
