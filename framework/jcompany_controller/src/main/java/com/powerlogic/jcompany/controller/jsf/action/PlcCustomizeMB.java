/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.action;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes.NAVEGACAO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.entity.PlcForm;
import com.powerlogic.jcompany.commons.entity.PlcLayout;
import com.powerlogic.jcompany.commons.entity.PlcSkin;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcCookieUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;

/**
 * jCompany. Controlador para a personalização da aplicação (pele, layout, etc).
 */
@PlcHandleException
public class PlcCustomizeMB extends PlcBaseMB {

	private static final long serialVersionUID = 9191609301241068638L;

	@Inject
	protected transient Logger log;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	protected Object entityPlc;	
	
	@Inject @QPlcDefault
	protected PlcCookieUtil cookieUtil;
	
	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	/**
	 * jCompany. Sobrepõe o botão de gravar para fazer registro em cookie da
	 * nova pele ou layout.
	 */
	@Override
	public String save()  {

		try {
			log.debug( "########## Entrou em save");
			if (this.entityPlc instanceof PlcSkin) {

				log.debug( "Entrou no evento para Trocar Pele");
				trocaPele(((PlcSkin) this.entityPlc).getPele());
			} else if (this.entityPlc instanceof PlcLayout) {

				log.debug( "Entrou no evento para Trocar Layout");
				trocaLayout(((PlcLayout) this.entityPlc).getLayout());
			} else if (this.entityPlc instanceof PlcForm) {

				log.debug( "Entrou no evento para Trocar Form");
				trocaForm(((PlcForm) this.entityPlc));
			}
			
			contextUtil.getRequest().setAttribute(PlcConstants.PlcJsfConstantes.PLC_ENTIDADE, entityPlc);
			msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[] {});

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcCustomizeMB", "save", e, log, "");
		}

		return null;
	}

	/**
	 * jCompany. Método para trocar pele da aplicação para o usuário
	 */
	protected String trocaPele(String pele) throws Exception {

		log.debug( "##################Entrou para trocar pele");
		HttpServletRequest request = contextUtil.getRequest();
		HttpServletResponse response = contextUtil.getResponse();

		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);

		plcSessao.setPele(pele);

		request.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY, plcSessao);

		if (log.isDebugEnabled())
			log.debug( "Trocou pele para =" + pele);

		cookieUtil.saveCookie(request, response, PlcConstants.COOKIE.COOKIE_PELE, pele);

		return null;
	}

	/**
	 * JCompany. Método para trocar layout do usuário
	 */
	protected String trocaLayout(String layout) throws Exception {

		log.debug( "##################Entrou para trocar layout");
		HttpServletRequest request = contextUtil.getRequest();
		HttpServletResponse response = contextUtil.getResponse();

		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);

		// Se for reduzido, guarda informação no atributo separado, pois vale
		// para todos os layouts com topo e rodapé
		
		int pos = layout.indexOf("_reduzido");
		String layoutBase = layout;
		if (pos > -1) {
			layoutBase = layout.substring(0, pos);
			plcSessao.setIndLayoutReduzido("S");
		} else
			plcSessao.setIndLayoutReduzido("N");

		if (layoutBase.contains("_dinamico")){
			layout = layout.replace("_dinamico", "ex");
			layoutBase = layoutBase.replace("_dinamico", "ex");
		}
		plcSessao.setLayout(layoutBase);

		request.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY, plcSessao);

		if (log.isDebugEnabled())
			log.debug( "Trocou layout para =" + layout + " na sessao=" + plcSessao.getLayout());

		cookieUtil.saveCookie(request, response, PlcConstants.COOKIE.COOKIE_LAYOUT, layout);

		return null;

	}

	/**
	 * JCompany. Método para trocar layout do usuário
	 */
	protected String trocaForm(PlcForm  form) throws Exception {

		String acoesExibeTexto = form.getFormAcaoExibeTexto();  
		String formAlertaAlteracao = form.isFormAlertaAlteracao()?"S":"N";
		String formAlertaExclusaoDetalhe = form.isFormAlertaExclusaoDetalhe()?"S":"N";
		String pesquisaRestful = form.isPesquisaRestful()?"S":"N";

		log.debug( "##################Entrou para trocar layout");
		HttpServletRequest request = contextUtil.getRequest();
		HttpServletResponse response = contextUtil.getResponse();

		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);

		if (formAlertaAlteracao != null) {
			if (formAlertaAlteracao.equals(""))
				formAlertaAlteracao = "N";
			plcSessao.setFormAlertaAlteracao(formAlertaAlteracao);
		}
		
		if (formAlertaExclusaoDetalhe != null) {
			if (formAlertaExclusaoDetalhe.equals(""))
				formAlertaExclusaoDetalhe = "N";
			plcSessao.setFormAlertaExclusaoDetalhe(formAlertaExclusaoDetalhe);
		}
		
		if (pesquisaRestful != null) {
			if (pesquisaRestful.equals(""))
				pesquisaRestful = "N";
			plcSessao.setPesquisaRestful(pesquisaRestful);
		}		
		
		// Se for reduzido, guarda informação no atributo separado, pois vale
		// para todos
		// os layouts com topo e rodapé
		plcSessao.setFormAcaoExibeTexto(acoesExibeTexto);

		cookieUtil.saveCookie(request, response, PlcConstants.COOKIE.COOKIE_FORM, plcSessao.getFormToCookie());

		return null;

	}

	/**
	 * Método responsável pela criação de Entidades ou sua recuperação da
	 * persistencia caso o OID seja passado na URL.
	 */
	@Override
	public void newEntity()  {
		//FIXME não esta sendo mais utilizado as alterações estão sendo feitos nos metodos especificos de
		//cada personalização de leiaute, formulário e pele nas actions especificas. 
//		try {
//			HttpServletRequest request = contextUtil.getRequest();
//
//			PlcCacheSessaoVO plcSessao = (PlcCacheSessaoVO) request.getSession().getAttribute(PlcConstantes.SESSION_CACHE_KEY);
//
//			if (request.getSession().getAttribute(PlcConstantes.SESSION_CACHE_KEY) != null) {
//				request.setAttribute(PlcConstantes.GUI.PELE.IND_REQ_PELE, plcSessao.getPele());
//				request.setAttribute(PlcConstantes.GUI.GERAL.IND_REQ_LAYOUT, plcSessao.getLayout());
//			} else {
//				request.setAttribute(PlcConstantes.GUI.PELE.IND_REQ_PELE, configUtil.getConfigAplicacao().aparencia().pele());
//				request.setAttribute(PlcConstantes.GUI.GERAL.IND_REQ_LAYOUT, configUtil.getConfigAplicacao().aparencia().layout());
//			}
//
//			if (this.entityPlc instanceof PlcPele)
//				((PlcPele) this.entityPlc).setPele((String) request.getAttribute(PlcConstantes.GUI.PELE.IND_REQ_PELE));
//			else if (this.entityPlc instanceof PlcLayout)
//				((PlcLayout) this.entityPlc).setLayout((String) request.getAttribute(PlcConstantes.GUI.GERAL.IND_REQ_LAYOUT));
//			else if (this.entityPlc instanceof PlcForm) {
//
//				PlcForm form = ((PlcForm) this.entityPlc);
//				form.setFormAcaoExibeTexto(plcSessao.getFormAcaoExibeTexto()==null?"A":plcSessao.getFormAcaoExibeTexto());
//				form.setFormAlertaAlteracao(plcSessao.getFormAlertaAlteracao().equals("S"));
//				form.setFormAlertaExclusaoDetalhe(plcSessao.getFormAlertaExclusaoDetalhe().equals("S"));
//				form.setPesquisaRestful(plcSessao.getPesquisaRestful().equals("S"));
//			}
//
//			if (!configUtil.getConfigAplicacao().aparencia().aparenciaFormulario().barraAcoes())
//				request.setAttribute(PlcConstantes.PERSONALIZA.ACOES, "N");
//			if (!configUtil.getConfigAplicacao().aparencia().aparenciaFormulario().alertaAlteracao())
//				request.setAttribute(PlcConstantes.PERSONALIZA.ALERTA_ALTERACOES, "N");
//			if (!configUtil.getConfigAplicacao().aparencia().aparenciaFormulario().alertaExclusaoDetalhe())
//				request.setAttribute(PlcConstantes.PERSONALIZA.ALERTA_EXCLUSAO_DETALHE, "N");
//
//		} catch (Exception e) {
//			throw new PlcException(PlcBeanMessages.JCOMPANY_ERRO_GENERICO, new Object[] { "criaOuRecuperaEntidade", e }, e, log);
//		}		
	}
}
