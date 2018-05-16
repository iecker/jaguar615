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

import com.powerlogic.jcompany.view.jsf.adapter.PlcAggregateAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcAggregate;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreInputTextTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
* @Descricao Renderiza um campo e botão para seleção popup de classes agregadas à classe principal! 
* A seleção popup pode ser um outro action de seleção da mesma aplicação ou mesmo de outra aplicação (outro WAR). Além disso, um mesmo action utilizado como seleção convencional pode ser reutilizado para seleção popup.!
* @Exemplo <plcf:vinculado id="departamentoPai" value="#{plcEntidade.departamentoPai}" propSelPop="departamentoPai" idExibe="S" idSomenteLeitura="N" autoRecuperacaoClasse="com.powerlogic.teste.entidade.DepartamentoEntity" actionSel="departamentosel" ajudaChave="ajuda.departamentoPai"  onkeydown="selecionaPorTecla(event,this);"/>!	
* @Tag vinculado!
*/
public class PlcAggregateHandler extends TrinidadComponentHandler {
	
	private static final Logger log = Logger.getLogger(PlcAggregateHandler.class.getCanonicalName());
	
	protected TagAttribute propSelPop;
	protected TagAttribute propsSelPop;
	protected TagAttribute limpaPropsSelPop;
	protected TagAttribute multiSel;
	protected TagAttribute multiSelTitulo;
	protected TagAttribute tituloBotaoSelPop;
	protected TagAttribute tituloChaveBotaoSelPop;
	protected TagAttribute tituloBotaoLimpar;
	protected TagAttribute tituloChaveBotaoLimpar;
	protected TagAttribute lookupTamanho;
	protected TagAttribute idTamanho;
	protected TagAttribute classeCSS;
	protected TagAttribute colunas;
	protected TagAttribute idExibe;
	protected TagAttribute idSomenteLeitura;
	protected TagAttribute obrigatorio;
	protected TagAttribute obrigatorioDestaque;	
	protected TagAttribute autoRecuperacaoClasse;
	protected TagAttribute autoRecuperacaoPropriedade;
	protected TagAttribute ajaxIdUnico;
	protected TagAttribute tamanho;			
	protected TagAttribute tamanhoMaximo;
	protected TagAttribute chavePrimaria;
	protected TagAttribute baseActionSel;
	protected TagAttribute actionSel;
	protected TagAttribute evento;
	protected TagAttribute parametro;
	protected TagAttribute alt;
	protected TagAttribute larg;
	protected TagAttribute posx;
	protected TagAttribute posy;
	protected TagAttribute ajudaChave;
	protected TagAttribute ajuda;
	
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected TagAttribute bundle;
	
	protected TagAttribute exibeBotaoLimpar;
	protected TagAttribute riaUsa;
	protected TagAttribute somenteLeitura;
	protected TagAttribute modal;
	protected TagAttribute delimitador;
	protected TagAttribute transacaoUnica;

	public PlcAggregateHandler(ComponentConfig config) {
		super(config);

		propSelPop = getAttribute("propSelPop");
		propsSelPop = getAttribute("propsSelPop");
		limpaPropsSelPop = getAttribute("limpaPropsSelPop");
		multiSel = getAttribute("multiSel");
		multiSelTitulo = getAttribute("multiSelTitulo");
		tituloBotaoSelPop = getAttribute("tituloBotaoSelPop");
		tituloChaveBotaoSelPop = getAttribute("tituloChaveBotaoSelPop");
		tituloBotaoLimpar = getAttribute("tituloBotaoLimpar");
		tituloChaveBotaoLimpar = getAttribute("tituloChaveBotaoLimpar");
		lookupTamanho = getAttribute("lookupTamanho");
		idTamanho =  getAttribute("idTamanho");
		classeCSS = getAttribute("classeCSS");
		colunas = getAttribute("colunas");
		idExibe = getAttribute("idExibe");
		idSomenteLeitura = getAttribute("idSomenteLeitura");
		obrigatorio = getAttribute("obrigatorio");
		obrigatorioDestaque = getAttribute("obrigatorioDestaque");
		autoRecuperacaoClasse = getAttribute("autoRecuperacaoClasse");
		autoRecuperacaoPropriedade = getAttribute("autoRecuperacaoPropriedade");
		ajaxIdUnico = getAttribute("ajaxIdUnico");
		tamanho = getAttribute("tamanho");			
		tamanhoMaximo = getAttribute("tamanhoMaximo");
		chavePrimaria = getAttribute("chavePrimaria");
		baseActionSel = getAttribute("baseActionSel");
		actionSel = getAttribute("actionSel");
		evento = getAttribute("evento");
		parametro = getAttribute("parametro");
		alt = getAttribute("alt");
		larg = getAttribute("larg");
		posx = getAttribute("posx");
		posy = getAttribute("posy");
		ajudaChave = getAttribute("ajudaChave");
		ajuda = getAttribute("ajuda");

		tituloChave = getAttribute("tituloChave");
		titulo = getAttribute("titulo");
		exibeSe = getAttribute("exibeSe");
		// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
		bundle = getAttribute("bundle");

		exibeBotaoLimpar = getAttribute("exibeBotaoLimpar");		
		riaUsa = getAttribute("riaUsa");		
		modal = getAttribute("modal");		
		delimitador = getAttribute("delimitador");		
		transacaoUnica = getAttribute("transacaoUnica");

	}

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
	
		super.setAttributes(ctx, instance);
		
		ValueExpression _propSelPop = PlcTagUtil.getValueExpression(propSelPop, ctx, String.class);
		ValueExpression _propsSelPop= PlcTagUtil.getValueExpression(propsSelPop, ctx, String.class);
		 String _limpaPropsSelPop= (String)PlcTagUtil.getValue(limpaPropsSelPop, ctx);
		 String _multiSel= (String)PlcTagUtil.getValue(multiSel, ctx);
		 String _multiSelTitulo= (String)PlcTagUtil.getValue(multiSelTitulo, ctx);
		 String _tituloBotaoSelPop= (String)PlcTagUtil.getValue(tituloBotaoSelPop, ctx);
		 String _tituloChaveBotaoSelPop= (String)PlcTagUtil.getValue(tituloChaveBotaoSelPop, ctx);
		 String _tituloBotaoLimpar= (String)PlcTagUtil.getValue(tituloBotaoLimpar, ctx);
		 String _tituloChaveBotaoLimpar= (String)PlcTagUtil.getValue(tituloChaveBotaoLimpar, ctx);
		 
		 String _lookupTamanho= "20";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(lookupTamanho, ctx)))
			 _lookupTamanho = (String)PlcTagUtil.getValue(lookupTamanho, ctx);
		 
		 String _idTamanho = "5";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(idTamanho, ctx)))
			 _idTamanho= (String)PlcTagUtil.getValue(idTamanho, ctx);
		 
		 String _classeCSS= (String)PlcTagUtil.getValue(classeCSS, ctx);
		 String _colunas= (String)PlcTagUtil.getValue(colunas, ctx);
		 
		 String _idExibe = "S";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(idExibe, ctx)))
			 _idExibe= (String)PlcTagUtil.getValue(idExibe, ctx);
		 
		 String _idSomenteLeitura= (String)PlcTagUtil.getValue(idSomenteLeitura, ctx);
		 String _obrigatorio= (String)PlcTagUtil.getValue(obrigatorio, ctx);
		 String _obrigatorioDestaque = (String)PlcTagUtil.getValue(obrigatorioDestaque, ctx);	
		 String _autoRecuperacaoClasse= (String)PlcTagUtil.getValue(autoRecuperacaoClasse, ctx);
		 String _autoRecuperacaoPropriedade= (String)PlcTagUtil.getValue(autoRecuperacaoPropriedade, ctx);
		 String _ajaxIdUnico= (String)PlcTagUtil.getValue(ajaxIdUnico, ctx);
		 String _tamanho = (String)PlcTagUtil.getValue(tamanho, ctx);
		 
		 String _tamanhoMaximo = "5";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(tamanhoMaximo, ctx)))
		 _tamanhoMaximo= (String)PlcTagUtil.getValue(tamanhoMaximo, ctx);
		 
		 String _chavePrimaria= (String)PlcTagUtil.getValue(chavePrimaria, ctx);
		 String _baseActionSel= (String)PlcTagUtil.getValue(baseActionSel, ctx);
		 ValueExpression _actionSel= PlcTagUtil.getValueExpression(actionSel, ctx, String.class);
		 String _evento= (String)PlcTagUtil.getValue(evento, ctx);
		 ValueExpression _parametro= PlcTagUtil.getValueExpression(parametro, ctx, String.class);
		 
		 String _alt = "500";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(alt, ctx)))
			 _alt = (String)PlcTagUtil.getValue(alt, ctx);
			 
		 String _larg = "600";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(larg, ctx)))
		 _larg = (String)PlcTagUtil.getValue(larg, ctx);
		 
		 String _posx = "10";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(posx, ctx)))
			 _posx = (String)PlcTagUtil.getValue(posx, ctx);
		 
		 String _posy = "10";
		 if (StringUtils.isNotBlank((String)PlcTagUtil.getValue(posy, ctx)))
			 _posy = (String)PlcTagUtil.getValue(posy, ctx);
		 
		 String _ajudaChave= (String)PlcTagUtil.getValue(ajudaChave, ctx);
		 String _ajuda= (String)PlcTagUtil.getValue(ajuda, ctx);
		 String _tituloChave= (String)PlcTagUtil.getValue(tituloChave, ctx);
		 
		 ValueExpression _titulo= PlcTagUtil.getValueExpression(titulo, ctx, String.class);
		 ValueExpression _exibeSe= PlcTagUtil.getValueExpression(exibeSe, ctx, Boolean.class);
		 ValueExpression _riaUsa = PlcTagUtil.getValueExpression(riaUsa, ctx, String.class);
		 
		 String _bundle= (String)PlcTagUtil.getValue(bundle, ctx);
		 String _exibeBotaoLimpar = (String)PlcTagUtil.getValue(exibeBotaoLimpar, ctx);
		 String _somenteLeitura	 = (String)PlcTagUtil.getValue(somenteLeitura, ctx);
		 String _modal = (String)PlcTagUtil.getValue(modal, ctx);
		 String _delimitador = (String)PlcTagUtil.getValue(delimitador, ctx);
		 String _transacaoUnica = (String)PlcTagUtil.getValue(transacaoUnica, ctx);
		 if (StringUtils.isBlank(_transacaoUnica)) {
			 _transacaoUnica = "N";
		 }
		 	 
		 FacesBean bean = ((PlcAggregate)instance).getFacesBean();
		 
		 PlcAggregateAdapter.getInstance().adapter(bean, _propSelPop, _propsSelPop, _limpaPropsSelPop, _multiSel, _multiSelTitulo, _tituloBotaoSelPop, 
				 _tituloChaveBotaoSelPop, _tituloBotaoLimpar, _tituloChaveBotaoLimpar, _lookupTamanho, _idTamanho, _classeCSS, _colunas, 
				 _idExibe, _idSomenteLeitura, _obrigatorio, _obrigatorioDestaque, _autoRecuperacaoClasse, _autoRecuperacaoPropriedade, 
				 _ajaxIdUnico, _tamanho, _tamanhoMaximo, _chavePrimaria, _baseActionSel, _actionSel, _evento, _parametro, _alt, _larg, _posx, _posy, 
				 _ajudaChave, _ajuda, _tituloChave, _titulo, _exibeSe, _bundle, _exibeBotaoLimpar, _riaUsa, _somenteLeitura, _modal, _delimitador, _transacaoUnica);

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
	
}
