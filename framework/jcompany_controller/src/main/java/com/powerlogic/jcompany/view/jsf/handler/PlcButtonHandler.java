/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import java.io.IOException;
import java.util.List;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.AttachedObjects;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.adapter.PlcButtonAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcConstantMethodExpression;

/**
 * Especialização da tag base CoreCommandButtonTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um botão no padrão JSF/Trinidad! 
 * @Exemplo plcf:botaoAcao id="botaoAcaoAbrir" acao="abre" destino="abre" label="jcompany.evt.abrir" botaoArrayID="ABRIR" immediate="true" validaForm="false" limpaForm="false" rendered="#{requestScope.exibeAbrirPlc=='S' and requestScope.exibe_jcompany_evt_abrir!='N'}" hotKey="#{requestScope.abrirHotKey}"
 * @Tag botao!
 */
public class PlcButtonHandler extends TrinidadComponentHandler {

	protected static final Logger	logVisao	= Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected TagAttribute			acao;

	private TagAttribute			destino;

	protected TagAttribute			label;

	private TagAttribute			urlIcone;

	private TagAttribute			evento;

	private TagAttribute			validaForm;																//true ou false, indicando se deve ou não ser validado o formulario. Default:true

	private TagAttribute			limpaForm;																	//true ou false, indicando se deve ou não limpar o formulario. Default:false

	private TagAttribute			botaoArrayID;

	private TagAttribute			idPlc;

	private TagAttribute			alertaExcluir;

	private TagAttribute			alertaExcluirDetalhe;

	private TagAttribute			detalhe;																	// nome do detalhe

	protected TagAttribute			bundle;

	protected TagAttribute			riaUsa;

	private FaceletContext			ctx;

	public PlcButtonHandler(ComponentConfig config) {

		super(config);

		acao = getAttribute("acao");

		destino = getAttribute("destino");
		label = getAttribute("label");
		urlIcone = getAttribute("urlIcone");
		evento = getAttribute("evento");
		validaForm = getAttribute("validaForm");
		limpaForm = getAttribute("limpaForm");
		botaoArrayID = getAttribute("botaoArrayID");

		idPlc = getAttribute("idPlc");
		alertaExcluir = getAttribute("alertaExcluir");
		alertaExcluirDetalhe = getAttribute("alertaExcluirDetalhe");
		detalhe = getAttribute("detalhe");
		bundle = getAttribute("bundle");
		riaUsa = getAttribute("riaUsa");

	}

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		PlcComponentUtil componentUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		this.ctx = ctx;

		super.setAttributes(ctx, instance);

		FacesBean bean = ((PlcButton) instance).getFacesBean();

		String _acao = (String) retornaValor(acao);
		String _destino = (String) retornaValor(destino);
		String _riaUsa = (String) retornaValor(riaUsa);

		if (StringUtils.isNotEmpty(_acao)) {
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			String acaoAction = componentUtil.transformProperty(PlcJsfConstantes.PLC_ACTION_KEY, _acao, false);
			bean.setProperty(PlcButton.ACTION_EXPRESSION_KEY, elUtil.createMethodExpression(acaoAction, String.class, null));
		} else if (StringUtils.isNotBlank(_destino)) {
			bean.setProperty(PlcButton.ACTION_EXPRESSION_KEY, new PlcConstantMethodExpression(_destino));
		}

		ValueExpression _idPlc = null;
		if (idPlc != null) {
			_idPlc = idPlc.getValueExpression(ctx, String.class);
			String idComplemento = "btn-" + getTagId() + idPlc.getValue(ctx);
			bean.setProperty(PlcButton.ID_KEY, idComplemento);
		}

		String _label = (String) retornaValor(label);
		String _urlIcone = (String) retornaValor(urlIcone);

		String _evento = (String) retornaValor(evento);

		ValueExpression _validaForm = null;
		if (validaForm != null)
			_validaForm = validaForm.getValueExpression(ctx, Boolean.class);

		ValueExpression _limpaForm = null;
		if (limpaForm != null)
			_limpaForm = limpaForm.getValueExpression(ctx, Boolean.class);

		String _botaoArrayID = (String) retornaValor(botaoArrayID);

		String _alertaExcluir = (String) retornaValor(alertaExcluir);
		String _alertaExcluirDetalhe = (String) retornaValor(alertaExcluirDetalhe);
		String _detalhe = (String) retornaValor(detalhe);
		String _bundle = (String) retornaValor(bundle);

		PlcButtonAdapter.getInstance().adapter(bean, _acao, _destino, _label, _urlIcone, _evento, _validaForm, _limpaForm, _botaoArrayID, _idPlc, _alertaExcluir, _alertaExcluirDetalhe, _detalhe, _bundle, _riaUsa);

	}

	private Object retornaValor(TagAttribute atributo) {

		if (atributo != null) {
			return atributo.getValue(ctx);
		}
		return null;

	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
}
