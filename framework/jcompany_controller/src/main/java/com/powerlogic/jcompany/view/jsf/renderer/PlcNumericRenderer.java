/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.FormInputRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.SimpleInputTextRenderer;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;
import com.powerlogic.jcompany.view.jsf.component.PlcNumeric;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;

/**
 * Especialização do renderer base PlcTextoRenderer para permitir IoC e DI nos renderes JSF/Trinidad. 
 * 
 */
public class PlcNumericRenderer extends PlcTextRenderer {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	
	// Definindo o tipo do renderer
	static public final String RENDERER_TYPE 	= "com.powerlogic.jsf.Numerico";
	
	/**
	 * IoC do jcompany. Função reimplementada para acrescentar o código javascript existente na tag numerico.tag
	 * Acrescenta código nos atributos onchange, onkeydown, onkeyup.
	 */
	@Override
	protected void encodeAll(FacesContext context, RenderingContext arc, UIComponent component, FacesBean bean) throws IOException {
		
		PlcComponentPropertiesUtil componentPropertiesUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentPropertiesUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		Integer tamanhoMaximo = (Integer)bean.getProperty(CoreInputText.MAXIMUM_LENGTH_KEY);
		String numCasas = (bean.getProperty(PlcNumeric.NUM_CASAS_KEY) != null ? bean.getProperty(PlcNumeric.NUM_CASAS_KEY).toString() : "");

		if (StringUtils.isBlank(numCasas)) {
			String formato 		= (String)bean.getProperty(PlcText.FORMATO_KEY);
			if(formato != null && formato.equals(SimpleFormat.NUMBER.toString())) {
				numCasas = "0";
			}/* else {
				numCasas = "2";
			}*/
		}		

		StringBuilder sb = new StringBuilder();

		//Essas propriedades são definidas no Renderer porque dependem do clientID, e só é possível recuperá-lo na fase de renderização. 
		
		sb.append("FormataValor(this,").append(tamanhoMaximo!=null?tamanhoMaximo:"''").append(",'',").append(numCasas).append(");");
		String onchange = (String)bean.getProperty(CoreInputText.ONCHANGE_KEY);
		if (onchange == null || !onchange.contains(sb.toString()))
			componentPropertiesUtil.setPropertyNestedJavaScript(CoreInputText.ONCHANGE_KEY, bean, sb.toString());

		sb.setLength(0); //reaproveita o objeto

		sb.append("FormataValor(this,").append(tamanhoMaximo!=null?tamanhoMaximo:"''").append(",event,").append(numCasas).append(");").append("return CheckNumerico(this,").append(tamanhoMaximo!=null?tamanhoMaximo:"''").append(",event);");
		String onkeypress = (String)bean.getProperty(CoreInputText.ONKEYPRESS_KEY);
		if (onkeypress == null || !onkeypress.contains(sb.toString()))
			componentPropertiesUtil.setPropertyNestedJavaScript(CoreInputText.ONKEYPRESS_KEY, bean, sb.toString());

		sb.setLength(0); //reaproveita o objeto

		sb.append("FormataValor(this,").append(tamanhoMaximo!=null?tamanhoMaximo:"''").append(",event,").append(numCasas).append("); return CheckNumerico(this,").append(tamanhoMaximo!=null?tamanhoMaximo:"''").append(",event);");
		String onkeyup = (String)bean.getProperty(CoreInputText.ONKEYUP_KEY);
		if (onkeyup == null || !onkeyup.contains(sb.toString()))
			componentPropertiesUtil.setPropertyNestedJavaScript(CoreInputText.ONKEYUP_KEY, bean, sb.toString());
		
		super.encodeAll(context, arc, component, bean);
		
	}
	
	/**
	 * Definindo formatação padrão para o campo numérico
	 */
	@Override
	protected FormInputRenderer getFormInputRenderer()
	{
	    return new SimpleInputTextRenderer() {
	    	
	    	/**
	     * Especialização para retornar o BigDecimal convertido de acordo com o Locale padrão. 
	     */
		@Override
		protected Object getValue(UIComponent component, FacesBean bean) {
			Object _value = bean.getProperty(bean.getType().findKey("value"));

			boolean ehBigDecimal = true; 

			if (_value!=null) {
				// Verifica e passa para BigDecimal
				try {
					_value = new BigDecimal (_value.toString());
				}catch (Exception e) {
					ehBigDecimal = false;
				}

				if (ehBigDecimal){
					String numCasas = bean.getProperty(PlcNumeric.NUM_CASAS_KEY)!=null?bean.getProperty(PlcNumeric.NUM_CASAS_KEY).toString():"";
					int _numCasas;
					if (StringUtils.isNotBlank(numCasas) && StringUtils.isNumeric(numCasas)) {
						_numCasas = Integer.parseInt(numCasas);
					} else {
						_numCasas = 2; // O default é ter duas casas decimais.
					}
					
					NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
					nf.setMinimumFractionDigits(_numCasas);
					nf.setMaximumFractionDigits(_numCasas);

					return nf.format(_value);
				}
			}
			return _value;
		}
		
	    /**
	     * Especialização para retornar o submitted value convertido de acordo com o Locale padrão. 
	     */
		@Override
		public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
			if (submittedValue!=null) {
				try {
					NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
					submittedValue = nf.parse((String)submittedValue).toString();
				} catch (ParseException e) {
					//ignora
				}
			}
			return super.getConvertedValue(context, component, submittedValue);
		}
		
		@Override
		protected void renderAllAttributes(
		    FacesContext        context,
		    RenderingContext arc,
		    UIComponent component,
		    FacesBean           bean,
		    boolean             renderStyleAttrs) throws IOException
		  {
		      super.renderAllAttributes(context, arc, component, bean, renderStyleAttrs);
		      ResponseWriter rw = context.getResponseWriter();
		      rw.writeAttribute("style", "text-align:right;", "style");		  
		  }
	    };
	}  
}