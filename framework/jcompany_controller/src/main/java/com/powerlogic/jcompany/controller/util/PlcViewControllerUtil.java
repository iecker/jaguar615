/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.util;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail.DynamicType;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;


/**
 * jCompany 3.0 Serviço de registro para exibições condicionais da camada VIEW.<p>
 * DP Singleton. Modificado para 'protected' para permitir especializações (descendentes) que possam
 * substituí-lo
 * @since jCompany 3.0
 */
@SPlcUtil
@QPlcDefault
public class PlcViewControllerUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	protected static final Logger logProfiling = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_QA_PROFILING);
	/**
	 * @since jcompany5 Utilitários para a manipulação de metadados via Anotações
	 */

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;

	public PlcViewControllerUtil() {
	}

	public enum ExhibitionMode {
		/**
		 * Exibe para leitura e gravação
		 */
		NORMAL,
		/**
		 * Exibe protegido contra gravanção (read-only)
		 */
		PROTECTED,
		/**
		 * Não exibe
		 */
		INVISIBLE
	}


	/**
	 * jCompany 3.0 Registra objetos para não serem exibidos, incluindo seus correlatos, sendo estes:<p>
	 * - label.[nomeObjeto]<br>
	 * - [nomeObjeto]_Arg
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, bem como labels e campos auxiliarres correlatos.
	 * @param nomeColecao Nome da colecão (para tabulares ou consultas, usar itensPlc). Para detalhes e subDetalhes, o nome da propriedade
	 *  de coleção. Ex: 'historicoCurso'
	 */
	@SuppressWarnings("unchecked")
	public void hideWithLabel(HttpServletRequest request, String nomeObjeto,String nomeColecao)  {
		
		log.debug( "########## Entrou em naoExibirComCorrelatos");

		String nomeColecaoAux = "";
		
		if (nomeColecao != null) {
			nomeColecaoAux = nomeColecao + ".";
		} else {
			
		}
		
		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		
		if (m == null){ 
			m = new HashMap<String,Boolean>();
		}

		// Objeto - sempre com um ponto na frente, para simplificação das lógicas de coleçoes
		m.put(nomeColecaoAux + nomeObjeto,false);

		// Correlatos
		m.put("label." + nomeColecaoAux + nomeObjeto,false);
		// Nome de mestre sempre com '.' na frente, para simplificar logicas em tag-files
		m.put("." + nomeColecaoAux + nomeObjeto + "_Arg",false);

		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);
	}

	/**
	 * jCompany 3.0 Registra objetos para não serem exibidos, incluindo seus correlatos, sendo estes:<p>
	 * - label.[nomeObjeto]<br>
	 * - [nomeObjeto]_Arg
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, bem como labels e campos auxiliarres correlatos.
	 */
	public void hideWithLabel(HttpServletRequest request, String nomeObjeto)  {

		hideWithLabel(request,nomeObjeto,null);
	}

	/**
	 * jCompany 3.0 Registra objetos para serem exibidos, incluindo seus correlatos, sendo estes:<p>
	 * - label.[nomeObjeto]<br>
	 * - [nomeObjeto]_Arg<p>
	 * Importante: Exibir é o padrão (default), portanto este método somente deveria estar sendo chamado em situações
	 * onde seja importante se desfazer um registro previamente realizado (em lógicas genéricas ou do jCompany Security, por exemplo)
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, bem como labels e campos auxiliarres correlatos.
	 */
	@SuppressWarnings("unchecked")
	public void showWithLabel(HttpServletRequest request, String nomeObjeto)    {
		log.debug( "########## Entrou em exibirComCorrelatos");

		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		if (m == null) return;

		try {
			// Objeto			
			m.remove(nomeObjeto);

			// Correlatos
			m.remove("label."+nomeObjeto);
			m.remove(nomeObjeto+"_Arg");

		} catch (Exception e) {
			log.warn( "Atencao: Erro tentando fazer exibir para "+nomeObjeto+". Erro: "+e);
		}

		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);		

	}

	/**
	 * jCompany 3.0 Registra uma posição, de 0 a N, de aba de tab-folder para não ser exibido. 
	 * @param tituloAba Título da aba (ex: minha.aba) que não deve ser exibido.
	 */
	@SuppressWarnings("unchecked")
	public void hideTabFolderOfIndex(HttpServletRequest request, int pos)  {
		log.debug( "########## Entrou em naoExibirAbaTabFolderPos");

		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		if (m == null) m = new HashMap<String,Boolean>();

		String [] camposFoco = (String [])request.getAttribute(PlcConstants.GUI.TABFOLDER.CAMPOS_FOCO);
		String stringCamposFoco = (String)request.getAttribute(PlcConstants.GUI.TABFOLDER.STRING_CAMPOS_FOCO);

		if (camposFoco != null && StringUtils.isNotBlank(stringCamposFoco)){

			if (pos < camposFoco.length && stringCamposFoco.contains(camposFoco[pos])) {
				stringCamposFoco = stringCamposFoco.replace(camposFoco[pos] + ",", "");
				request.setAttribute(PlcConstants.GUI.TABFOLDER.STRING_CAMPOS_FOCO, stringCamposFoco);
			}
		}
		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);

	}

	/**
	 * jCompany 3.0 Registra uma posição, de 0 a N, de aba de tab-folder para ser exibido. 
	 * @param tituloAba Título da aba (ex: minha.aba) que deve ser exibido.
	 */
	@SuppressWarnings("unchecked")
	public void showTabFolderOfIndex(HttpServletRequest request, int pos)  {
		log.debug( "########## Entrou em naoExibirAbaTabFolderPos");

		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		if (m == null) m = new HashMap<String,Boolean>();

		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);

	}

	/**
	 * jCompany 3.0 Registra um objeto para não ser exibido. Objetos podem ser nomes de propriedades ou chaves internacionalizadas de título.
	 * Repare que até a versão 3.0 estas lógicas somente funcionam em rótulos internacionalizados (recomendados mesmo que não se tenha
	 * intenção de usar I18n em produçao)
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, ou de chave de título
	 * @param nomeColecao Nome da colecão (para tabulares ou consultas, usar itensPlc). Para detalhes e subDetalhes, o nome da propriedade
	 *  de coleção. Ex: 'historicoCurso'
	 */
	@SuppressWarnings("unchecked")
	public void hide(HttpServletRequest request, String nomeObjeto,String nomeColecao)  {
		log.debug( "########## Entrou em naoExibir");

		String nomeColecaoAux = "";
		if (nomeColecao != null)
			nomeColecaoAux = nomeColecao;

		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		if (m == null) m = new HashMap<String,Boolean>();

		// Objeto
		m.put(nomeColecaoAux+"."+nomeObjeto,false);

		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);

	}

	/**
	 * jCompany 3.0 Registra um objeto para não ser exibido. Objetos podem ser nomes de propriedades ou chaves internacionalizadas de título.
	 * Repare que até a versão 3.0 estas lógicas somente funcionam em rótulos internacionalizados (recomendados mesmo que não se tenha
	 * intenção de usar I18n em produçao)
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, ou de chave de título
	 */
	public void hide(HttpServletRequest request, String nomeObjeto)  {
		hide(request,nomeObjeto,null);
	}

	/**
	 * jCompany 3.0 Registra objeto para ser exibido
	 * Importante: Exibir é o padrão (default), portanto este método somente deveria estar sendo chamado em situações
	 * onde seja importante se desfazer um registro previamente realizado (em lógicas genéricas ou do jCompany Security, por exemplo)
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, ou de chave de título
	 * @param nomeColecao Nome da colecão (para tabulares ou consultas, usar itensPlc). Para detalhes e subDetalhes, o nome da propriedade
	 *  de coleção. Ex: 'historicoCurso'
	 */
	@SuppressWarnings("unchecked")
	public void show(HttpServletRequest request, String nomeObjeto, String nomeColecao)    {
		log.debug( "########## Entrou em exibir");
		Map<String,Boolean> m = (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		if (m == null) return;

		try {
			// Objeto			
			m.remove(nomeObjeto);

		} catch (Exception e) {
			log.warn( "Atencao: Erro tentando fazer exibir para "+nomeObjeto+". Erro: "+e);
		}

		request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);		

	}

	/**
	 * jCompany 3.0 Registra objeto para ser exibido
	 * Importante: Exibir é o padrão (default), portanto este método somente deveria estar sendo chamado em situações
	 * onde seja importante se desfazer um registro previamente realizado (em lógicas genéricas ou do jCompany Security, por exemplo)
	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, ou de chave de título
	 */
	public void show(HttpServletRequest request, String nomeObjeto)    {
		show(request,nomeObjeto,null);
	}

	/**
	 * Registra um objeto de ação que <b>deve</b> ser exibido (botão, link, etc.) O
	 * objeto de ação é identificado por um rótulo (I18N).
	 * <p>
	 * Para uso nas verficações na camada view no rótulo informado substitui-se
	 * os '.' por '_' e inclui-se o prefixo 'exibe_'. A chave gerada é gravada
	 * no request com valor "S". Exemplo:
	 * </p>
	 * 
	 * <pre><code>
	 *   exibirAcao('jcompany.evt.incluir'); // O botão &quot;Incluir&quot; será exibido
	 * </code></pre>
	 * 
	 * Dentro de uma jsp o teste é feito da seguinte forma:
	 * 
	 * <pre><code>
	 *   &lt;c:if test=&quot;${requestScope.exibe_jcompany_evt_incluir!='N'}&quot;&gt;
	 *   ...mostra o botão
	 *   &lt;/c:if&gt;
	 * </code></pre>
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param request
	 * @param nomeObjetoAcao
	 *            Rótulo do botão ou link que deve ser exibido, utilizando
	 *            internacionalização (I18N)
	 * 
	 */
	public void showAction(HttpServletRequest request,String nomeObjetoAcao)  {
		if (!StringUtils.isBlank(nomeObjetoAcao)) {
			request.setAttribute("exibe_"+nomeObjetoAcao.replace('.', '_'), "S");
		}
	}

	/**
	 * Registra um objeto de ação que não deve ser exibido (botão, link, etc.) O
	 * objeto de ação é identificado por um rótulo (I18N).
	 * <p>
	 * Para uso nas verficações na camada view no rótulo informado substitui-se
	 * os '.' por '_' e inclui-se o prefixo 'exibe_'. A chave gerada é gravada
	 * no request com valor "N". Exemplo:
	 * </p>
	 * 
	 * <pre><code>
	 *   naoExibirAcao('jcompany.evt.incluir'); // O botão &quot;Incluir&quot; não será exibido
	 * </code></pre>
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param request
	 * @param nomeObjetoAcao
	 *            Rótulo do botão ou link que não deve ser exibido, utilizando
	 *            internacionalização (I18N)
	 * 
	 */
	public void hideAction(HttpServletRequest request, String nomeObjetoAcao)
	{
		if (!StringUtils.isBlank(nomeObjetoAcao)) {
			//Mantendo funcionalidade com o legado
			request.setAttribute("exibe_"+nomeObjetoAcao.replace('.', '_'), "N");
			// Funciona com as constantes Ex: visaoJsfUtil.hideAction(PlcConstants.ACAO.EVT_GRAVAR); 
			request.setAttribute(nomeObjetoAcao, "N");
		}
	}

	public void handleGenericParameters(HttpServletRequest request, Integer mestreTotalJsps,  
			String navegadorNumPorPagina, String dinamicoTipo, String topoEstilo, String topoPosicao,
			String alertaAlteracaUsa, String impressaoInteligente, String modalidade) {

		request.setAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.MESTRE_TOTAL_JSPS,mestreTotalJsps);

		request.setAttribute(PlcConstants.UNIVERSAL_COM_NAVEGADOR,navegadorNumPorPagina);
		if (navegadorNumPorPagina!=null) {
			request.setAttribute(PlcConstants.GUI.NAVEGADOR.TOPO_ESTILO,topoEstilo);
			request.setAttribute(PlcConstants.GUI.NAVEGADOR.TOPO_POSICAO,topoPosicao);

			// Navegador
			if((DynamicType.DYNAMIC_REGNUM.equals(dinamicoTipo))||(DynamicType.DYNAMIC_BOTH.equals(dinamicoTipo))){
				request.setAttribute(PlcConstants.GUI.NAVEGADOR.NUM_REG_DINAMICO, "S");			
			}
		}

		request.setAttribute(PlcConstants.CONTEXTPARAM.INI_ALERTA_ALTERACAO_USA,""+alertaAlteracaUsa);

		request.setAttribute(PlcConstants.LOGICAPADRAO.GERAL.IMPRESSAO_INTELIGENTE,""+impressaoInteligente);
		request.setAttribute(PlcConstants.LOGICAPADRAO.GERAL.MODALIDADE,modalidade);
	}
	
	public void handleGenericParameters(HttpServletRequest request, Integer mestreTotalJsps,  
			String navegadorNumPorPagina, String dinamicoTipo, String topoEstilo, String topoPosicao,
			String alertaAlteracaUsa, String alertaExclusaoDetalhe,
			String impressaoInteligente, String modalidade,String dirBaseFcls) {

		request.setAttribute(PlcConstants.CONTEXTPARAM.INI_ALERTA_EXCLUSAO_DETALHE_USA,""+alertaExclusaoDetalhe);
		request.setAttribute(PlcConstants.LOGICAPADRAO.GERAL.DIR_BASE_FCLS,dirBaseFcls);

		handleGenericParameters(request, mestreTotalJsps, navegadorNumPorPagina, dinamicoTipo, topoEstilo, topoPosicao, alertaAlteracaUsa, impressaoInteligente, modalidade);

	}

	/**
	 * Inicia e encerra visao do explorador de dados
	 * @since jCompany 5. Compatibilização com JSF
	 * @param explorerUsa Se logica tem meta-dados para usar explorador
	 * @param formPattern Se for logica tabular
	 * @param id Object-id correntemente informado, se houver, ou String Vazio se nao tiver
	 */
	public void handleTreeViewPanel(HttpServletRequest request,boolean explorerUsa,FormPattern formPattern, Long id,String urlBase,PlcEntityTreeView entidade)  {

		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
		
		if(plcSessao != null) {
			
			if (explorerUsa) {
				
				request.setAttribute(PlcConstants.GUI.EXPLORER.USA_PLC, "S");
				String acao = request.getParameter("tree_acao");
				
				if (StringUtils.isNotEmpty(acao)){
					if(acao.equals("tree_fim")) {
						plcSessao.setExplorerStatus(false);
					} else {
						if(acao.equals("tree_ini")) {
							plcSessao.setExplorerStatus(true);
						}
					}
				} 
				
				if (plcSessao.isExplorerStatus()) {
					if (isTreeViewEnabled(formPattern, id, entidade)) {
						plcSessao.setExplorerAtivo(true);
					} else {
						plcSessao.setExplorerAtivo(false);
					}
				} else {
					plcSessao.setExplorerAtivo(false);
				}
				
				String urlLocation = request.getContextPath() + urlBase + (StringUtils.contains(urlBase, '?') ? "&": "?");
				String linkPlc = "";
				if(id != null) {
					linkPlc = urlLocation + "id=" + id.toString() +"&tree_acao" + "=";
				} else {
					linkPlc = urlLocation +"&tree_acao" + "=";
				}
				
				if (plcSessao.isExplorerAtivo()) {
					request.setAttribute(PlcConstants.GUI.EXPLORER.LINK_PLC, linkPlc + "tree_fim");
				} else {
					request.setAttribute(PlcConstants.GUI.EXPLORER.LINK_PLC, linkPlc + "tree_ini");
				}
				
			} else {
				request.setAttribute(PlcConstants.GUI.EXPLORER.USA_PLC, "N");
				plcSessao.setExplorerAtivo(false);
			}
		}
	}


	protected boolean isPopupWindow(HttpServletRequest request, String modoJanela, String path) {

		return (modoJanela.equals(PlcConstants.LOGICAPADRAO.GERAL.MODO_JANELA_KEY_VALORES.MODO_JANELA_POPUP) ||
				modoJanela.equals(PlcConstants.LOGICAPADRAO.GERAL.MODO_JANELA_KEY_VALORES.MODO_JANELA_POPUP_EDICAO)) ||
				path.toLowerCase().endsWith(PlcConstants.GUI.UNIVERSAL.IND_POPUP) ||
				popupOpened(request,path);
	}


	/**
	 * jCompany. Ajusta botões conforme LÓGICA PADRÂO e MODO.
	 * @param relSubdiretorio 
	 * @param relHoraExecucao 
	 * @param relFormato 
	 */
	public void handleButtonsAccordingFormPattern(
			HttpServletRequest request, FormPattern formPattern, String logicaImpressao,
			String modoJanela, String path, String detModoNovo, int totalDetalhes, String multiSel, 
			String modalidade, String urlAjuda, boolean exibeBotaoRss, boolean exibeBotaoPesquisaRestful)  {

		log.debug( "########## Vai ajustar botoes conforme o padrao do formulario");

		boolean popUp = isPopupWindow(request, modoJanela, path);

		request.setAttribute( PlcConstants.IMPRESSAO.IND_LOGICA_IMPRESSAO_KEY, logicaImpressao);

		// Diz para os layouts se é popup - para inserir CSS e Javascript
		if (popUp) {
			request.setAttribute(PlcConstants.ACAO.EXIBE_BT_CANCELAR_POPUP, PlcConstants.SIM);
			request.setAttribute( PlcConstants.POPUP.SELECAO_POPUP,PlcConstants.SIM);
			log.debug( "Marcou que o padrao esta utilizando janela popup");
		} else {
			request.setAttribute( PlcConstants.POPUP.SELECAO_POPUP,PlcConstants.NAO);
			log.debug( "Marcou que o padrao nao esta usando janela popup");
		}


		// Indicador para modo de botão de novo de detalhe e sub-det
		if (formPattern.equals(FormPattern.Mdt) ||
				formPattern.equals(FormPattern.Mds) ||
				formPattern.equals(FormPattern.Mad) ||
				formPattern.equals(FormPattern.Mas)) {
			request.setAttribute( "detModoNovo",detModoNovo);
			// Para layout universal
			request.setAttribute( "totalDetalhes",totalDetalhes);
		}

		cleanButtons(request);

		String modo = PlcCDIUtil.getInstance().getInstanceByType(PlcConversationControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)).getModoPlc();		

		// Se for MODO DE IMPRESSÂO não exibe nenhum botão
		if (!PlcConstants.MODOS.MODO_IMPRESSAO.equals(modo)) {

			if (formPattern.equals(FormPattern.Rel)) {

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GERAR_RELATORIO, PlcConstants.EXIBIR);

			} else if (formPattern.equals(FormPattern.Sel) ||
					formPattern.equals(FormPattern.Smd)) {

				if (!formPattern.equals(FormPattern.Smd) &&
						!modoJanela.equals(PlcConstants.LOGICAPADRAO.GERAL.MODO_JANELA_KEY_VALORES.MODO_JANELA_POPUP))
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.EXIBIR);

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR, PlcConstants.EXIBIR);
				//setAtributo(request, PlcConstantes.ACAO.EXIBE_BT_PESQUISAR_RSS, PlcConstantes.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_IMPRIMIR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_LIMPAR, PlcConstants.EXIBIR);

				//se for seleção de multipla, exibir botão de devolver todos

				if(request.getParameter("indMultiSelPlc") != null && request.getParameter("indMultiSelPlc").equals("S"))
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_RETORNAR_MULTI_SEL, PlcConstants.EXIBIR);

			} else if (formPattern.equals(FormPattern.Tab) ||
					formPattern.equals(FormPattern.Ctb)) {

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_IMPRIMIR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);

				if (formPattern.equals(FormPattern.Tab)){
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR, PlcConstants.EXIBIR);
					request.setAttribute( "pesquisaComEnter", "N");	
				}

				if (formPattern.equals(FormPattern.Ctb)) {
					if (modalidade.equals("B"))
						request.setAttribute( PlcConstants.ACAO.EXIBE_BT_ABRIR, PlcConstants.EXIBIR);
					else {
						request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR, PlcConstants.EXIBIR);
						request.setAttribute( "pesquisaComEnter", "N");
						request.setAttribute( PlcConstants.ACAO.EXIBE_BT_LIMPAR, PlcConstants.EXIBIR);
					}

				}

			} else if (formPattern.equals(FormPattern.Con) ||
					formPattern.equals(FormPattern.Rel)) {

				if (formPattern.equals(FormPattern.Rel))
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GERAR_RELATORIO, PlcConstants.EXIBIR);
				else
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_IMPRIMIR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_LIMPAR, PlcConstants.EXIBIR);

			} else if (formPattern.equals(FormPattern.Man) ||
					formPattern.equals(FormPattern.Ctb) ||
					formPattern.equals(FormPattern.Apl) ||
					formPattern.equals(FormPattern.Mdt) ||
					formPattern.equals(FormPattern.Mds) ||
					formPattern.equals(FormPattern.Mad) ||
					formPattern.equals(FormPattern.Mas)) {

				if (!formPattern.equals(FormPattern.Mad) &&
					!formPattern.equals(FormPattern.Mas) &&	
					!formPattern.equals(FormPattern.Apl) )
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.EXIBIR);

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);

				if (!formPattern.equals(FormPattern.Apl) )
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_ABRIR, PlcConstants.EXIBIR);

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_IMPRIMIR, PlcConstants.EXIBIR);

				if (PlcConstants.MODOS.MODO_EDICAO.equals(modo) &&
						!formPattern.equals(FormPattern.Mad) &&
						!formPattern.equals(FormPattern.Mas) &&
						!formPattern.equals(FormPattern.Apl) )  {
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.EXIBIR);
					request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GERAR_DOCUMENTO_OFFICE, PlcConstants.EXIBIR);

					if (!formPattern.equals(FormPattern.Apl) ) {
						request.setAttribute( PlcConstants.ACAO.EXIBE_BT_CLONAR, PlcConstants.EXIBIR);
						request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR_DETALHE, PlcConstants.EXIBIR);
					}
				}
			} else if (formPattern.equals(FormPattern.Usu)) {

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_IMPRIMIR, PlcConstants.EXIBIR);

			} else if (formPattern.equals(FormPattern.Ctl)) {

				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);

			}

			if (formPattern.equals(FormPattern.Sel) && exibeBotaoRss){
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_PESQUISAR_RSS, PlcConstants.EXIBIR);
			}

			// Se é para apresentar botao de visualização/edicao de formulario (Exibe por default para todas as logicas de controle
			// que exibem campos auxiliares como marcador de exclusão para detalhes/itens ou tab-folders.
			if ((FormPattern.Mdt.equals(formPattern) || FormPattern.Tab.equals(formPattern) || FormPattern.Man.equals(formPattern) ||
					FormPattern.Ctb.equals(formPattern) || FormPattern.Mds.equals(formPattern) ||
					FormPattern.Mad.equals(formPattern) || FormPattern.Mas.equals(formPattern))

					&& !("v".equals(request.getSession().getAttribute("mfPlc"))) && PlcConstants.MODOS.MODO_EDICAO.equals(modo))
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO, "S");
			else
				request.setAttribute( PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO,"N");

			// Se houver configuração de Pesquisa com RESTful, exibirá o botão apropriado
			if (exibeBotaoPesquisaRestful)
				request.setAttribute( PlcConstants.PESQUISA_RESTFUL_USA, "S");
			else
				request.setAttribute( PlcConstants.PESQUISA_RESTFUL_USA, "N");


			// Faz ajuste final se estiver em modo de consulta dinamico (mcPlc=t ou mcPlc=d)
			if ("t".equals(request.getParameter("mcPlc")) || "d".equals(request.getParameter("mcPlc")) || "p".equals(request.getParameter("mcPlc"))) {
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_INCLUIR);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_CLONAR);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GRAVAR);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ASSISTENTE_INICIALIZA);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EXCLUIR);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO);
				removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EDITA_DOCUMENTO);
			}


			// Faz ajuste final para o parâmetro informado pelo jsecurity
			if(request.getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO) != null){
				if(request.getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO).equals("texto") || 
						request.getAttribute(PlcConstants.LOGICAPADRAO.MANUTENCAO.ESTILO_APRESENTACAO).equals("protegido")) {
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_INCLUIR);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_CLONAR);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GRAVAR);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ASSISTENTE_INICIALIZA);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EXCLUIR);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO);
					removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EDITA_DOCUMENTO);
				}	
			}

		}


	}



	protected void cleanButtons(HttpServletRequest request) {
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ABRIR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_AJUDA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_APROVA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_APROVA_PESQUISA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_APROVA_TODOS);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ASSISTENTE_ANTERIOR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ASSISTENTE_INICIALIZA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ASSISTENTE_PROXIMO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_CANCELAR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_CANCELAR_POPUP);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_CLONAR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_PESQUISAR_DETALHE);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EDITA_DOCUMENTO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_EXCLUIR);
		///removeAtributo(request, PlcConstantes.ACAO.EXIBE_BT_EXPORTA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GERAR_ESQUEMA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GERAR_RELATORIO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GRAVA_VERSAO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GRAVAR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_ICONE_APROVACAO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_IMPRIMIR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_INCLUIR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_LIMPAR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_PESQUISAR);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_PESQUISAR_RSS);
		removeAttribute(request, "pesquisaComEnter");
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_PUBLICA_VERSAO);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_REPROVA);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_RETORNAR_MULTI_SEL);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_UPLOAD);
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_VISUALIZA_DOCUMENTO);	 
		removeAttribute(request, PlcConstants.ACAO.EXIBE_BT_GERAR_DOCUMENTO_OFFICE);
	}

	/**
	 * Remover atributo do request. No caso do JSF, verifica se o atributo foi setado pelo usuário.
	 * 
	 * @param request
	 * @param atributo Atributo a ser removido
	 */
	protected void removeAttribute (HttpServletRequest request, String atributo){

		request.removeAttribute(atributo);

	}
	
	/**
	 * Modificações dinamicas via URL na sessão, de elementos como indicadores de modo de teste,
	 * layouts corrente, etc.
	 * @since jCompany 3.0
	 */
	public void saveOnSession(HttpServletRequest request) {

		//Se estiver em modo de teste automatizado, marca na sessão
		//Se mudar pele pela URL, troca na sessão
		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);

		if (request.getParameter(PlcConstants.QA.MODO_TESTE)!=null &&
				request.getParameter(PlcConstants.QA.MODO_TESTE).equalsIgnoreCase("S") && 
				request.getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO) != null &&
				!request.getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO).equalsIgnoreCase("P")) {
			request.getSession().setAttribute(PlcConstants.QA.MODO_TESTE,"S");
			//plcSessao.setFormAcaoAjax("N");
		}

		// Se expirou sessão e passa por aqui, procura recuperar a pele
		if (plcSessao == null) plcSessao = new PlcCacheSessionVO();
		if (request.getParameter(PlcConstants.GUI.PELE.PELE_PLC) != null 
				&& !("".equals(request.getParameter(PlcConstants.GUI.PELE.PELE_PLC)))) {
			plcSessao.setPele(request.getParameter(PlcConstants.GUI.PELE.PELE_PLC));
		}

		//	Verifica layout reduzido
		if ("s".equals(request.getParameter(PlcConstants.GUI.GERAL.LAYOUT_SOMENTE_CORPO))) {
			plcSessao.setIndLayoutReduzido("S");
			plcSessao.setIndLayoutExibeMenu("N");
		} else if ("n".equals(request.getParameter(PlcConstants.GUI.GERAL.LAYOUT_SOMENTE_CORPO))) {
			plcSessao.setIndLayoutReduzido("N");
			plcSessao.setIndLayoutExibeMenu("S");
		}

		//		 Se estiver indicando radiografia (spy) de layouts, em tres niveis
		if ("N".equalsIgnoreCase(request.getParameter(PlcConstants.GUI.LAY_SPY_PLC)) ||
				"0".equalsIgnoreCase(request.getParameter(PlcConstants.GUI.LAY_SPY_PLC)))
			request.getSession().removeAttribute(PlcConstants.GUI.LAY_SPY_PLC);
		else if (("1".equalsIgnoreCase(request.getParameter(PlcConstants.GUI.LAY_SPY_PLC)) ||
				"2".equalsIgnoreCase(request.getParameter(PlcConstants.GUI.LAY_SPY_PLC)) ||
				"3".equalsIgnoreCase(request.getParameter(PlcConstants.GUI.LAY_SPY_PLC))) &&
				request.isUserInRole("AreaTecnica"))
			request.getSession().setAttribute(PlcConstants.GUI.LAY_SPY_PLC,request.getParameter(PlcConstants.GUI.LAY_SPY_PLC));

	}

	/**
	 * Se a janela for aberta como popup, verifica e mantém estado
	 */
	protected boolean popupOpened(HttpServletRequest request, String path) {
		log.debug( "########## Entrou em abriuPopup");

		return PlcConstants.GUI.UNIVERSAL.IND_POPUP.equals((String)request.getParameter(PlcConstants.POPUP.URL_MODO_POPUP));
	}


	/**
	 * Disponibiliza chave para layouts declararem apropriadamente.
	 * @param key Chave do google maps válida para url conforme declarada nos meta-dados
	 */
	public void handleGoogleServices(HttpServletRequest request, String key) {

		if (key != null && !"".equals(key))
			request.setAttribute(PlcConstants.GOOGLE_MAP_KEY, key);

	}

	public Map<String, Boolean> getSecurityMap() {
		return null;
	}
	
	protected boolean isTreeViewEnabled( FormPattern formPattern, Long id, PlcEntityTreeView entidade) {
		return (((formPattern != null && formPattern.equals(FormPattern.Tab))
				|| id != null || (entidade != null && entidade.recursividadeSomente()))) 
				&& (formPattern != null && !formPattern.equals(FormPattern.Con) 
						&& !formPattern.equals(FormPattern.Sel));
	}
}
