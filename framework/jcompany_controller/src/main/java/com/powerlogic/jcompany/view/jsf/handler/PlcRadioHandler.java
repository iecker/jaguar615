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
import com.powerlogic.jcompany.view.jsf.adapter.PlcRadioAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcRadio;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreSelectOneRadioTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza uma área com campos de marcação, escolha de itens, para entrada de dados.!
 * @Exemplo <plcf:radio id="sexo" value="#{plcLogicaItens.argumentos.sexo.valor}" dominio="Sexo" />!
 * @Tag radio!
 */
public class PlcRadioHandler extends TrinidadComponentHandler{

	protected TagAttribute dominio;
	private TagAttribute obrigatorio;
	protected TagAttribute obrigatorioDestaque;	
	private TagAttribute somenteLeitura;
	private TagAttribute classeCSS;
	private TagAttribute ajudaChave;
	private TagAttribute ajuda;
	protected TagAttribute exibeSe;
	protected TagAttribute bundle;
	private TagAttribute chavePrimaria;
	private TagAttribute colunas;
	private TagAttribute titulo;
	private TagAttribute tituloChave;
	protected TagAttribute riaUsa;
	
	public PlcRadioHandler(ComponentConfig config) {
		super(config);
		
		  dominio = getAttribute("dominio");
		  obrigatorio = getAttribute("obrigatorio");
		  obrigatorioDestaque = getAttribute("obrigatorioDestaque");	
		  somenteLeitura = getAttribute("somenteLeitura");
		  classeCSS = getAttribute("classeCSS");
		  ajudaChave = getAttribute("ajudaChave");
		  ajuda = getAttribute("ajuda");
		  exibeSe = getAttribute("exibeSe");
		  bundle = getAttribute("bundle");
		  chavePrimaria = getAttribute("chavePrimaria");
		  colunas = getAttribute("colunas");
		  titulo = getAttribute("titulo");
		  tituloChave = getAttribute("tituloChave");
		  riaUsa = getAttribute("riaUsa");
		
	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		super.setAttributes(ctx, instance);

		ValueExpression _dominio = PlcTagUtil.getValueExpression(this.dominio, ctx, Object.class);
		String _obrigatorio = (String)PlcTagUtil.getValue(this.obrigatorio, ctx);
		String _obrigatorioDestaque= (String)PlcTagUtil.getValue(this.obrigatorioDestaque, ctx);	
		String _somenteLeitura= (String)PlcTagUtil.getValue(this.somenteLeitura, ctx);
		String _classeCSS= (String)PlcTagUtil.getValue(this.classeCSS, ctx);
		String _ajudaChave= (String)PlcTagUtil.getValue(this.ajudaChave, ctx);
		String _ajuda= (String)PlcTagUtil.getValue(this.ajuda, ctx);
		ValueExpression _exibeSe= PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);
		String _bundle= (String)PlcTagUtil.getValue(this.bundle, ctx);
		String _chavePrimaria= (String)PlcTagUtil.getValue(this.chavePrimaria, ctx);
		String _colunas= (String)PlcTagUtil.getValue(this.colunas, ctx);
		ValueExpression _titulo= PlcTagUtil.getValueExpression(this.titulo, ctx, Boolean.class);
		String _tituloChave= (String)PlcTagUtil.getValue(this.tituloChave, ctx);
		String _riaUsa= (String)PlcTagUtil.getValue(this.riaUsa, ctx);

		FacesBean bean = ((PlcRadio)instance).getFacesBean();

		PlcRadioAdapter.getInstance().adapter(bean, _dominio, _obrigatorio, _obrigatorioDestaque, _somenteLeitura,
				_classeCSS, _ajudaChave, _ajuda, _exibeSe, _bundle, _chavePrimaria, _colunas, _titulo, _tituloChave, _riaUsa);

//		bean.setProperty(PlcRadio.INLINE_STYLE_KEY, "");

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
	
}
