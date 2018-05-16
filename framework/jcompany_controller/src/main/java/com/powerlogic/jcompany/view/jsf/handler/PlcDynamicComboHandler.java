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
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;
import com.powerlogic.jcompany.view.jsf.adapter.PlcDynamicComboAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcDynamicCombo;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreSelectOneChoiceTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo em forma de lista, para escolha de dados. A lista é dinâmica, com base em uma coleção de registros.!
 * @Exemplo <plcf:comboDinamico id="endereco_uf" value="#{plcEntidade.endereco.uf}" dominio="UfEntity" exibeBranco="S"   />!
 * @Tag comboDinamico!
 */
public class PlcDynamicComboHandler extends TrinidadComponentHandler{
	
	
	protected TagAttribute tamanho;
	protected TagAttribute ajudaChave;
	protected TagAttribute ajuda;
	protected TagAttribute classeCSS;
	protected TagAttribute obrigatorio;
	protected TagAttribute obrigatorioDestaque;	
	protected TagAttribute somenteLeitura;
	protected TagAttribute chavePrimaria; 
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute dominio;
	protected TagAttribute exibeSe;
	protected TagAttribute exibeBranco;
	protected TagAttribute botaoAtualiza;
	protected TagAttribute propRotulo;
	protected TagAttribute bundle;
	protected TagAttribute colunas;
	protected TagAttribute numLinha;
	protected TagAttribute msgErro;
	protected TagAttribute navegacaoParaCampos;
	protected TagAttribute comboAninhado;
	protected TagAttribute riaUsa;
	
	public PlcDynamicComboHandler(ComponentConfig config) {
		super(config);

		tamanho =  getAttribute("tamanho");
		ajudaChave =  getAttribute("ajudaChave");
		ajuda =  getAttribute("ajuda");
		classeCSS =  getAttribute("classeCSS");
		obrigatorio =  getAttribute("obrigatorio");
		obrigatorioDestaque =  getAttribute("obrigatorioDestaque");	
		somenteLeitura =  getAttribute("somenteLeitura");
		chavePrimaria =  getAttribute("chavePrimaria"); 
		tituloChave =  getAttribute("tituloChave");
		titulo =  getAttribute("titulo");
		dominio =  getAttribute("dominio");
		exibeSe =  getAttribute("exibeSe");
		exibeBranco =  getAttribute("exibeBranco");
		botaoAtualiza =  getAttribute("botaoAtualiza");
		propRotulo =  getAttribute("propRotulo");
		bundle =  getAttribute("bundle");
		colunas =  getAttribute("colunas");
		numLinha =  getAttribute("numLinha");
		msgErro =  getAttribute("msgErro");
		navegacaoParaCampos =  getAttribute("navegacaoParaCampos");
		comboAninhado =  getAttribute("comboAninhado");
		riaUsa = getAttribute("riaUsa");

	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);

		String _tamanho = (String)PlcTagUtil.getValue(this.tamanho, ctx);
		String _ajudaChave= (String)PlcTagUtil.getValue(this.ajudaChave, ctx);
		String _ajuda= (String)PlcTagUtil.getValue(this.ajuda, ctx);
		String _classeCSS= (String)PlcTagUtil.getValue(this.classeCSS, ctx);
		String _obrigatorio= (String)PlcTagUtil.getValue(this.obrigatorio, ctx);
		String _obrigatorioDestaque= (String)PlcTagUtil.getValue(this.obrigatorioDestaque, ctx);	
		String _somenteLeitura= (String)PlcTagUtil.getValue(this.somenteLeitura, ctx);
		String _chavePrimaria= (String)PlcTagUtil.getValue(this.chavePrimaria, ctx); 
		String _tituloChave= (String)PlcTagUtil.getValue(this.tituloChave, ctx);
		ValueExpression _titulo= PlcTagUtil.getValueExpression(this.titulo, ctx, String.class);
		ValueExpression _dominio= PlcTagUtil.getValueExpression(this.dominio, ctx, Object.class);
		ValueExpression _exibeSe= PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);
		String _exibeBranco= (String)PlcTagUtil.getValue(this.exibeBranco, ctx);
		String _botaoAtualiza= (String)PlcTagUtil.getValue(this.botaoAtualiza, ctx);
		String _propRotulo= (String)PlcTagUtil.getValue(this.propRotulo, ctx);
		String _bundle= (String)PlcTagUtil.getValue(this.bundle, ctx);
		String _colunas= (String)PlcTagUtil.getValue(this.colunas, ctx);
		String _numLinha= (String)PlcTagUtil.getValue(this.numLinha, ctx);
		String _msgErro= (String)PlcTagUtil.getValue(this.msgErro, ctx);
		String _navegacaoParaCampos = (String)PlcTagUtil.getValue(this.navegacaoParaCampos, ctx);
		String _comboAninhado = (String)PlcTagUtil.getValue(this.comboAninhado, ctx);
		String _riaUsa = (String)PlcTagUtil.getValue(riaUsa, ctx);

		FacesBean bean = ((PlcDynamicCombo)instance).getFacesBean();

		PlcDynamicComboAdapter.getInstance().adapter(bean, _tamanho, _ajudaChave, _ajuda, _classeCSS, _obrigatorio, _obrigatorioDestaque, 
				_somenteLeitura, _chavePrimaria, _tituloChave, _titulo, _dominio, _exibeSe, _exibeBranco, _botaoAtualiza, _propRotulo, _bundle, 
				_colunas, _numLinha, _msgErro, _navegacaoParaCampos, _comboAninhado, _riaUsa);

	}

	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}	
	
}
