/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXCommand;
import org.apache.myfaces.trinidad.component.UIXEditableValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputFile;
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreInputFileTag;
import org.apache.myfaces.trinidadinternal.taglib.util.MethodExpressionMethodBinding;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcInputFile;
import com.powerlogic.jcompany.view.jsf.renderer.PlcInputFileRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcGlobalTag;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreInputFileTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo do tipo Html/file com a opção de fazer upload de arquivos.!
 * @Exemplo Anotar a propriedade PlcFileAtacch.
 * @Tag arquivo!
 */
public class PlcInputFileTag extends CoreInputFileTag {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected String propriedade;
	protected String tituloChave;
	protected ValueExpression titulo;
	protected String tamanho;			
	protected String ajudaChave;		
	protected String ajuda;
	protected String classeCSS;	
	protected String obrigatorio;
	protected String colunas;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected ValueExpression exibeSe;
	protected String riaUsa;

	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;
	
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */	
	@Override
	public String getComponentType() {
		return PlcInputFile.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcInputFileRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		
		super.setProperties(bean);
		
		// Passar estas propriedades para a função setPropertiesComuns
		PlcGlobalTag globalTag = new PlcGlobalTag (propriedade, classeCSS, ajuda, ajudaChave, titulo, tituloChave, null, null, obrigatorio, tamanho, null ,bundle, exibeSe);
		
		PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
		MethodExpression uploadMethod = elUtil.createMethodExpression("#{plcAction.uploadFile}", null, new Class[]{javax.faces.event.ValueChangeEvent.class});
		bean.setProperty(UIXEditableValue.VALUE_CHANGE_LISTENER_KEY, new MethodExpressionMethodBinding(uploadMethod));
		
		setProperty(bean, PlcInputFile.VALUE_ARQUIVO_KEY, elUtil.createValueExpression("#{plcEntidade.arquivoAnexado}", Object.class));

		String downloadAction = componentUtil.transformProperty(PlcJsfConstantes.PLC_ACTION_KEY, "downloadFile", false);
		MethodExpression downloadMethod = elUtil.createMethodExpression(downloadAction, null, null);
		bean.setProperty(UIXCommand.ACTION_EXPRESSION_KEY, downloadMethod);
	
		//setProperty(bean, CoreInputFile.STYLE_CLASS_KEY, "botao_menu");
		bean.setProperty(CoreInputFile.ONMOUSEOVER_KEY, "try{animarBotaoMenu(event , \'2\')}catch(e){}");
		bean.setProperty(CoreInputFile.ONMOUSEOUT_KEY, "try{animarBotaoMenu(event, \'\')}catch(e){}");
		
		bean.setProperty(PlcInputFile.RIA_USA, riaUsa);
		
		PlcTagUtil.setCommonProperties(bean, bean.getType(), globalTag);
		
	}

	
	public String getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}

	public String getAjuda() {
		return ajuda;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public String getClasseCSS() {
		return classeCSS;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public String getColunas() {
		return colunas;
	}

	public void setColunas(String colunas) {
		this.colunas = colunas;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public ValueExpression getTitulo() {
		return titulo;
	}

	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public String getTituloChave() {
		return tituloChave;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}
	

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

}
