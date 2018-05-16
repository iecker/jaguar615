/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;


import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValue;
import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValueExpression;

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
import com.powerlogic.jcompany.view.jsf.adapter.PlcTextAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcText;

/**
 * 
 * Especialização da tag base CoreInputTextTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para entrada de dados no formato texto "string".!
 * @Exemplo <plcf:texto id="nome" value="#{plcEntidade.nome}" ajudaChave="ajuda.nome" />!
 * @Tag texto!
 */
public class PlcTextHandler extends TrinidadComponentHandler {

	/*
	 * Propriedades já mapeadas
	 */
	/*protected String tamanho;		
	protected String tamanhoMaximo;	
	protected String ajudaChave;	
	protected String ajuda;
	protected String classeCSS;		
	protected String obrigatorio;	
	protected String obrigatorioDestaque;	
	protected String somenteLeitura;	
	protected String formato;			
	protected String chavePrimaria;  
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String colunas;
	protected String numLinha;
	protected String numDet;
	protected String msgErro;*/

	protected TagAttribute tamanho;		
	protected TagAttribute tamanhoMaximo;	
	protected TagAttribute ajudaChave;	
	protected TagAttribute ajuda;
	protected TagAttribute classeCSS;		
	protected TagAttribute obrigatorio;	
	protected TagAttribute obrigatorioDestaque;	
	protected TagAttribute somenteLeitura;	
	protected TagAttribute formato;			
	protected TagAttribute chavePrimaria;
	protected TagAttribute tabIndex;
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected TagAttribute bundle;
	protected TagAttribute colunas;
	protected TagAttribute numLinha;
	protected TagAttribute numDet;
	protected TagAttribute msgErro;
	protected TagAttribute riaUsa;

	private FaceletContext ctx;

	public PlcTextHandler(ComponentConfig config) {

		super(config);

		tamanho = getAttribute("tamanho");		
		tamanhoMaximo = getAttribute("tamanhoMaximo");	
		ajudaChave = getAttribute("ajudaChave");	
		ajuda = getAttribute("ajuda");
		classeCSS = getAttribute("classeCSS");		
		obrigatorio = getAttribute("obrigatorio");	
		obrigatorioDestaque = getAttribute("obrigatorioDestaque");	
		somenteLeitura = getAttribute("somenteLeitura");	
		formato = getAttribute("formato");			
		chavePrimaria = getAttribute("chavePrimaria");  
		tabIndex = getAttribute("tabIndex");
		tituloChave = getAttribute("tituloChave");
		titulo = getAttribute("titulo");
		exibeSe = getAttribute("exibeSe");
		// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
		bundle = getAttribute("bundle");
		colunas = getAttribute("colunas");
		numLinha = getAttribute("numLinha");
		numDet = getAttribute("numDet");
		msgErro = getAttribute("msgErro");
		riaUsa = getAttribute("riaUsa");

	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		this.ctx = ctx;

		super.setAttributes(ctx, instance);

		String _tamanho = (String)getValue(this.tamanho, ctx);		
		String _tamanhoMaximo = (String)getValue(this.tamanhoMaximo, ctx);	
		String _ajudaChave= (String)getValue(this.ajudaChave, ctx);	
		String _ajuda = (String)getValue(this.ajuda, ctx);
		String _classeCSS= (String)getValue(this.classeCSS, ctx);		
		String _obrigatorio= (String)getValue(this.obrigatorio, ctx);	
		String _obrigatorioDestaque= (String)getValue(this.obrigatorioDestaque, ctx);	
		String _somenteLeitura= (String)getValue(this.somenteLeitura, ctx);	
		String _formato= (String)getValue(this.formato, ctx);			
		String _chavePrimaria= (String)getValue(this.chavePrimaria, ctx);
		String _tabIndex= (String)getValue(this.tabIndex, ctx);
		String _tituloChave= (String)getValue(this.tituloChave, ctx);

		
		ValueExpression _titulo = getValueExpression(titulo, ctx, String.class);
		ValueExpression _exibeSe = getValueExpression(exibeSe, ctx, Boolean.class);

		String _bundle= (String)getValue(this.bundle, ctx);
		String _colunas= (String)getValue(this.colunas, ctx);
		String _numLinha= (String)getValue(this.numLinha, ctx);
		String _numDet= (String)getValue(this.numDet, ctx);
		String _msgErro= (String)getValue(this.msgErro, ctx);
		String _riaUsa= (String)getValue(this.riaUsa, ctx);

		FacesBean bean = ((PlcText)instance).getFacesBean();
		
		PlcTextAdapter.getInstance().adapter(bean, _tamanho, _tamanhoMaximo, _ajudaChave, _ajuda, _classeCSS, _obrigatorio, 
				_obrigatorioDestaque, _somenteLeitura, _formato, _chavePrimaria, _tabIndex, _tituloChave, _titulo, _exibeSe, _bundle, _colunas,
				_numLinha, _numDet, _msgErro, _riaUsa);
		

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
