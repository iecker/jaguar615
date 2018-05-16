/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.adapter;

import java.util.Locale;

import javax.el.ValueExpression;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.ValueExpressionValueBinding;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcELUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

public class PlcButtonAdapter {

	private static PlcButtonAdapter INSTANCE = new PlcButtonAdapter ();

	private PlcButtonAdapter () {
		
	}

	public static PlcButtonAdapter getInstance () {
		return INSTANCE;
	}

	public void adapter (FacesBean bean,  String acao, String destino, String label, 
			String urlIcone, String evento, ValueExpression validaForm, ValueExpression limpaForm, 
			String botaoArrayID, ValueExpression idPlc, String alertaExcluir, 
			String alertaExcluirDetalhe, String detalhe, String bundle, String riaUsa){
		
		PlcComponentUtil componenteUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcComponentUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Especificos de botoes de Novo Para Detalhes
		setPropertiesDetalhe(bean, detalhe);

		bean.setProperty(bean.getType().findKey(PlcTagUtil.BUNDLE), PlcTagUtil.getDefaultBundle(bundle));
		
		Locale local = contextUtil.getRequest().getLocale();
		if (!"pt_BR".equals(local.toString()) && (contextUtil.getRequest().getParameter(PlcConstants.QA.MODO_TESTE)!=null || ((HttpServletRequest)contextUtil.getRequest()).getSession().getAttribute(PlcConstants.QA.MODO_TESTE)!=null ) && 
				!contextUtil.getRequest().getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO).equalsIgnoreCase("P"))
   		   local = new Locale("pt","BR");
		
		String _label 	= componenteUtil.createLocalizedMessage(bean, label, new Object[]{});
		String _desc 	= componenteUtil.createLocalizedMessage(bean, label+".hotkey", new Object[]{});
		
		bean.setProperty(PlcButton.ID_PLC, idPlc);
		if (StringUtils.isNotBlank(_label)){
			bean.setProperty(CoreCommandButton.TEXT_KEY, _label);
			if(_desc != null && !_desc.startsWith("???") && (contextUtil.getRequest().getParameter(PlcConstants.QA.MODO_TESTE)==null && ((HttpServletRequest)contextUtil.getRequest()).getSession().getAttribute(PlcConstants.QA.MODO_TESTE)==null )) {
				bean.setProperty(CoreCommandButton.SHORT_DESC_KEY, _desc);
			} else {
				bean.setProperty(CoreCommandButton.SHORT_DESC_KEY, _label);
			}
			
		}
		
		if (alertaExcluir!=null){
			String msgAlertaExcluir = componenteUtil.createLocalizedMessage(bean, alertaExcluir, new Object[]{});
			bean.setProperty(PlcButton.ALERTA_EXCLUIR_KEY, msgAlertaExcluir);
		}
		
		if (alertaExcluirDetalhe!=null){
			String msgAlertaExcluirDetalhe = componenteUtil.createLocalizedMessage(bean, alertaExcluirDetalhe, new Object []{});
			bean.setProperty(PlcButton.ALERTA_EXCLUIR_DETALHE_KEY,msgAlertaExcluirDetalhe);
		} 

		bean.setProperty(PlcButton.VALIDA_FORM, validaForm);
		
		if (limpaForm!=null) {
			bean.setProperty(PlcButton.LIMPA_FORM, limpaForm);
		} else {//false é o default
			bean.setProperty(PlcButton.LIMPA_FORM, "false");
		}
		
		if (StringUtils.isNotBlank(evento)) {
			bean.setProperty(PlcButton.EVENTO_KEY, evento);
		} else if (StringUtils.isNotBlank(label)){
			bean.setProperty(PlcButton.EVENTO_KEY, label);
		}
		
		// Se informou icone especifico, substitui
		if (! componenteUtil.isValueDefined(bean, CoreCommandButton.ICON_KEY)){
			if (StringUtils.isNotBlank(urlIcone)) {
				bean.setProperty(PlcButton.URL_ICONE_KEY, urlIcone);
				bean.setProperty(CoreCommandButton.ICON_KEY, contextUtil.getRequest().getContextPath()+urlIcone);
			} 
		}
		
		bean.setProperty(PlcButton.RIA_USA, riaUsa);
		
		if (!componenteUtil.isValueDefined(bean, CoreCommandButton.STYLE_CLASS_KEY)) {
			bean.setProperty(CoreCommandButton.STYLE_CLASS_KEY, "botao_menu ui-state-default plc-botao");
		}
		if (!componenteUtil.isValueDefined(bean, CoreCommandButton.ONMOUSEOVER_KEY)) {
			bean.setProperty(CoreCommandButton.ONMOUSEOVER_KEY, "this.className='botao_menu2 ui-state-hover plc-botao'");
		}
		if (!componenteUtil.isValueDefined(bean, CoreCommandButton.ONMOUSEOUT_KEY)) {
			bean.setProperty(CoreCommandButton.ONMOUSEOUT_KEY, "this.className='botao_menu ui-state-default plc-botao'");
		}
		
	}
	
	protected void setPropertiesDetalhe(FacesBean bean, String detalhe) {
		
		if (StringUtils.isNotBlank(detalhe) && !"#".equals(detalhe)) {
			
			PlcELUtil elUtil = (PlcELUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcELUtil.class, QPlcDefaultLiteral.INSTANCE);
			
			String _expr = "insereValorCampo('detCorrPlc', '#{plcItensStatus==null ? \""+detalhe+"\" : plcItensStatus.colecaoComplementa_"+detalhe+"}');";
			ValueExpression valueExpression = elUtil.createValueExpression(_expr, String.class);
			bean.setValueExpression(CoreCommandButton.ONCLICK_KEY,valueExpression);
			bean.setValueBinding(CoreCommandButton.ONCLICK_KEY, ValueExpressionValueBinding.getValueBinding(valueExpression));
		}
	}
	
	
}
