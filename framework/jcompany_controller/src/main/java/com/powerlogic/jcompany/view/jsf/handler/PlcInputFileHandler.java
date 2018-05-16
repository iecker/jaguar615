/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcInputFileAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcInputFile;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreInputFileTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo do tipo Html/file com a opção de fazer upload de arquivos.!
 * @Exemplo Anotar a propriedade PlcFileAttach na entidade. Ex: @PlcFileAtrach
 * @Tag arquivo!
 */
public class PlcInputFileHandler extends TrinidadComponentHandler {

	protected TagAttribute propriedade;
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute tamanho;			
	protected TagAttribute ajudaChave;		
	protected TagAttribute ajuda;
	protected TagAttribute classeCSS;	
	protected TagAttribute obrigatorio;
	protected TagAttribute colunas;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected TagAttribute bundle;
	protected TagAttribute exibeSe;

	public PlcInputFileHandler(ComponentConfig config) {
		
		super(config);
		propriedade = getAttribute("propriedade");
		tituloChave = getAttribute("tituloChave");
		titulo= getAttribute("titulo");
		tamanho= getAttribute("tamanho");			
		ajudaChave= getAttribute("ajudaChave");		
		ajuda= getAttribute("ajuda");
		classeCSS= getAttribute("classeCSS");	
		obrigatorio= getAttribute("obrigatorio");
		colunas= getAttribute("colunas");
		bundle= getAttribute("bundle");
		exibeSe= getAttribute("exibeSe");

	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);



	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);
		String _propriedade = (String)PlcTagUtil.getValue(this.propriedade, ctx);
		String _tituloChave = (String)PlcTagUtil.getValue(this.tituloChave, ctx); 
		ValueExpression _titulo = PlcTagUtil.getValueExpression(this.titulo, ctx, String.class); 
		String _tamanho= (String)PlcTagUtil.getValue(this.tamanho, ctx); 
		String _ajudaChave= (String)PlcTagUtil.getValue(this.ajudaChave, ctx); 
		String _ajuda= (String)PlcTagUtil.getValue(this.ajuda, ctx); 
		String _classeCSS= (String)PlcTagUtil.getValue(this.classeCSS, ctx);	
		String _obrigatorio= (String)PlcTagUtil.getValue(this.obrigatorio, ctx); 
		String _colunas= (String)PlcTagUtil.getValue(this.colunas, ctx); 
		String _bundle= (String)PlcTagUtil.getValue(this.bundle, ctx); 
		ValueExpression _exibeSe = PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);

		FacesBean bean = ((PlcInputFile)instance).getFacesBean();

		PlcInputFileAdapter.getInstance().adapter(bean, _propriedade, _tituloChave, _titulo, _tamanho, _ajudaChave, _ajuda, _classeCSS, _obrigatorio, _colunas, _bundle, _exibeSe);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
