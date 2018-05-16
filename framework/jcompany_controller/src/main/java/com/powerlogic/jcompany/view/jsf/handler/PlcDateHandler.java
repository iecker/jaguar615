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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcDateAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcDate;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreInputDateTag para permitir IoC e DI nos componentes JSF/Trinidad.
 *
 * @Descricao Renderiza um campo com uma linha para entrada de dados no formato data e um botão que abre um calendário para escolha de uma data.!
 * @Exemplo <plcf:data id="dataNascimento" value="#{plcEntidade.dataNascimento}" ajudaChave="ajuda.dataNascimento" />!
 * @Tag data!
 */
public class PlcDateHandler extends TrinidadComponentHandler{


	private TagAttribute tamanho;		
	private TagAttribute tamanhoMaximo;
	private TagAttribute ajudaChave;		
	private TagAttribute ajuda;
	private TagAttribute classeCSS;		
	private TagAttribute obrigatorio;		
	protected TagAttribute obrigatorioDestaque;	
	private TagAttribute somenteLeitura;	
	private TagAttribute chavePrimaria;  
	private TagAttribute tituloChave;
	private TagAttribute titulo;
	protected TagAttribute exibeSe;
	private TagAttribute mascara;
	protected TagAttribute bundle;

	private TagAttribute msgErro;
	private TagAttribute riaUsa;

	public PlcDateHandler(ComponentConfig config) {
		super(config);

		tamanho = getAttribute("tamanho");		
		tamanhoMaximo= getAttribute("tamanhoMaximo");
		ajudaChave= getAttribute("ajudaChave");		
		ajuda= getAttribute("ajuda");
		classeCSS= getAttribute("classeCSS");		
		obrigatorio= getAttribute("obrigatorio");		
		obrigatorioDestaque= getAttribute("obrigatorioDestaque");	
		somenteLeitura= getAttribute("somenteLeitura");	
		chavePrimaria= getAttribute("chavePrimaria");  
		tituloChave= getAttribute("tituloChave");
		titulo= getAttribute("titulo");
		exibeSe= getAttribute("exibeSe");
		mascara= getAttribute("mascara");
		bundle= getAttribute("bundle");
		riaUsa= getAttribute("riaUsa");

	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Propriedades já mapeadas
	 */
	/*private String tamanho;		
	private String tamanhoMaximo;
	private String ajudaChave;		
	private String ajuda;
	private String classeCSS;		
	private String obrigatorio;		
	protected String obrigatorioDestaque;	
	private String somenteLeitura;	
	private String chavePrimaria;  
	private String tituloChave;
	private ValueExpression titulo;
	protected ValueExpression exibeSe;
	private String mascara="dd/MM/yyyy";
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;

	private String msgErro;*/

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);

		String _tamanho = (String)PlcTagUtil.getValue(this.tamanho, ctx);		
		String _tamanhoMaximo= (String)PlcTagUtil.getValue(this.tamanhoMaximo, ctx);
		String _ajudaChave= (String)PlcTagUtil.getValue(this.ajudaChave, ctx);		
		String _ajuda= (String)PlcTagUtil.getValue(this.ajuda, ctx);
		String _classeCSS= (String)PlcTagUtil.getValue(this.classeCSS, ctx);		
		String _obrigatorio= (String)PlcTagUtil.getValue(this.obrigatorio, ctx);		
		String _obrigatorioDestaque= (String)PlcTagUtil.getValue(this.obrigatorioDestaque, ctx);	
		String _somenteLeitura= (String)PlcTagUtil.getValue(this.somenteLeitura, ctx);	
		String _chavePrimaria= (String)PlcTagUtil.getValue(this.chavePrimaria, ctx);  
		String _tituloChave= (String)PlcTagUtil.getValue(this.tituloChave, ctx);
		String _riaUsa= (String)PlcTagUtil.getValue(this.riaUsa, ctx);
		ValueExpression _titulo= PlcTagUtil.getValueExpression(this.titulo, ctx, String.class);
		ValueExpression _exibeSe= PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);

		String _mascara = (String)PlcTagUtil.getValue(this.mascara, ctx);
		if (StringUtils.isBlank(_mascara))
			_mascara = "dd/MM/yyyy";

		// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
		String _bundle= (String)PlcTagUtil.getValue(this.bundle, ctx);

		String _msgErro= (String)PlcTagUtil.getValue(this.msgErro, ctx);

		FacesBean bean = ((PlcDate)instance).getFacesBean();

		PlcDateAdapter.getInstance().adapter(bean, _tamanho, _tamanhoMaximo, _ajudaChave, _ajuda, _classeCSS, _obrigatorio,
				_obrigatorioDestaque, _somenteLeitura, _chavePrimaria, _tituloChave, _titulo, _exibeSe, _mascara, _bundle, _msgErro, _riaUsa);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
