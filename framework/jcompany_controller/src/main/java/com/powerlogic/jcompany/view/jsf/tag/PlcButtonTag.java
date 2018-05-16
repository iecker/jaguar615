/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.nav.CoreCommandButtonTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.adapter.PlcButtonAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.renderer.PlcButtonRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcConstantMethodExpression;

/**
 * Especialização da tag base CoreCommandButtonTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um botão no padrão JSF/Trinidad! 
 * @Exemplo plcf:botaoAcao id="botaoAcaoAbrir" acao="abre" destino="abre" label="jcompany.evt.abrir" botaoArrayID="ABRIR" immediate="true" validaForm="false" limpaForm="false" rendered="#{requestScope.exibeAbrirPlc=='S' and requestScope.exibe_jcompany_evt_abrir!='N'}" hotKey="#{requestScope.abrirHotKey}"
 * @Tag botao!
 */
public class PlcButtonTag extends CoreCommandButtonTag {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * Propriedades Mapeadas
	 */
	/**
	 * Ação (método da classe de controle) que será executada ao clicar o botão.
	 * Tem precedência sobre "destino"
	 */
	private String acao;
	/**
	 * Destino (configurado no navigation-rules) para o qual será enviado ao clicar o botão.
	 * Só funciona se acao não for definido. Nesse caso, não executa uma ação, apenas redireciona. 
	 */
	private String destino;
	private String label;
	private String urlIcone;
	private String evento;
	private ValueExpression validaForm; //true ou false, indicando se deve ou não ser validado o formulario. Default:true
	private ValueExpression limpaForm; //true ou false, indicando se deve ou não limpar o formulario. Default:false
	private String botaoArrayID;

	private ValueExpression idPlc;
	private String alertaExcluir;
	private String alertaExcluirDetalhe;
	private String detalhe; // nome do detalhe
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String riaUsa;
	
	/*
	 * Propriedades nao mapeadas
	 */
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType()
	{
		return PlcButton.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType()
	{
		return PlcButtonRenderer.RENDERER_TYPE;
	}
	
	
	@Override
	public int doStartTag() throws JspException {
		
		if (idPlc != null){
			String idComplemento = (String)idPlc.getValue(FacesContext.getCurrentInstance().getELContext());
			idComplemento = getId() + idComplemento;
			setId(idComplemento);
		}
		
		return super.doStartTag();
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		
		
		//Esse código tem que ficar antes do super.setProperties(..) porque redefine a action, que será utilizada naquele metodo.
		if (StringUtils.isNotEmpty(this.acao)) {
			String acaoAction = componentUtil.transformProperty(PlcJsfConstantes.PLC_ACTION_KEY, this.acao, false);
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			setAction(elUtil.createMethodExpression(acaoAction, String.class, null));
		} else if (this.destino!=null) {
			setAction(new PlcConstantMethodExpression(this.destino));
		}

		// Genericos Trinidad
		super.setProperties(bean);
				
		PlcButtonAdapter.getInstance().adapter(bean, acao, destino, label, urlIcone, evento, validaForm, 
				limpaForm, botaoArrayID, idPlc, alertaExcluir, alertaExcluirDetalhe, detalhe, bundle, riaUsa);

	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public String  getAcao(){
		return this.acao;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel(){
		return this.label;
	}

	public void setUrlIcone(String urlIcone) {
		this.urlIcone = urlIcone;
	}

	public void setBotaoArrayID(String botaoArrayID) {
		this.botaoArrayID = botaoArrayID;
	}
	

	public void setValidaForm(ValueExpression validaForm) {
		this.validaForm = validaForm;
	}

	public void setLimpaForm(ValueExpression limpaForm) {
		this.limpaForm = limpaForm;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getAlertaExcluir() {
	    return alertaExcluir;
	}

	public void setAlertaExcluir(String alertaExcluir) {
	    this.alertaExcluir = alertaExcluir;
	}

	public String getAlertaExcluirDetalhe() {
	    return alertaExcluirDetalhe;
	}

	public void setAlertaExcluirDetalhe(String alertaExcluirDetalhe) {
	    this.alertaExcluirDetalhe = alertaExcluirDetalhe;
	}

	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}
	
	public ValueExpression getIdPlc() {
		return idPlc;
	}

	public void setIdPlc(ValueExpression idPlc) {
		this.idPlc = idPlc;
	}
	
}
