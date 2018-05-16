/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf;


import java.io.Serializable;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.bindingtype.PlcHandleButtonsAccordingUseCaseAfter;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDomainLookupUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcViewJsfUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;

/**
 * Backing Bean JSF para controle das colaborações. Server como um "front-controller" único para simplificar extensões, mas
 * delega as operações em si para manter a coesão em cada operacão.
 * @author Rogério Baldini, Paulo Alvim, Igor Guimarães
 */
@QPlcDefault
@SPlcMB
public class PlcBaseMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Object entityPlc;	
	
	protected PlcEntityInstance<?> plcEntityInstance;
	
	protected PlcEntityList entityListPlc;
	
	@Inject @QPlcDefault
	protected PlcViewJsfUtil visaoJsfUtil;
	
	@Inject @QPlcSpecific
	protected PlcBaseCreateMB baseCreateMB; 
	
	@Inject @QPlcSpecific
	protected PlcBasePortletMB basePortletMB; 	

	@Inject @QPlcSpecific
	protected PlcBaseSaveMB baseSaveMB;
	
	@Inject @QPlcSpecific
	protected PlcBaseEditMB baseEditMB; 		

	@Inject @QPlcSpecific
	protected PlcBaseDeleteMB baseDeleteMB; 		
	
	@Inject @QPlcSpecific
	protected PlcBaseSearchMB baseSearchMB; 	
	
	@Inject @QPlcSpecific
	protected PlcBaseFileMB baseFileMB; 	
	
	@Inject @QPlcSpecific
	protected PlcBaseLogoutMB baseLogoutMB; 	
	
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcDomainLookupUtil dominioLookupUtil;

    @Inject @QPlcDefault 
    protected PlcContextUtil contextUtil;

    @Inject @QPlcDefault 
    protected PlcMsgUtil msgUtil;
	/**
	 * Parâmetro de request padrão para OID, gerenciado pelo CDI.
	 */
	@Inject	@HttpParam("id")
	protected String id;
	
	/**
	 * Auxiliar para controle de exclusão de ítens (linhas) em padrão Tabular ou Mestre/Detalhe e variações.
	 */
	protected String formAuxPlc;

	public String getFormAuxPlc() {
		return formAuxPlc;
	}

	public void setFormAuxPlc(String formAuxPlc) {
		this.formAuxPlc = formAuxPlc;
	}
	
	/**
	 * Método responsável pela criação de Entidades ou sua recuperação da
	 * persistencia caso o OID seja passado na URL.
	 */
	public void newEntity()  {
		this.entityPlc = baseCreateMB.newEntity(entityPlc);
	}

	/**
	 * Método responsável pela criação do objeto de controle {@link PlcEntityList}.
	 * No caso de tabular, crud tabular ou seleção/consulta, será criado uma instância da subclasse {@link PlcBaseLogicaArgumento},
	 * para comportar também os argumentos para a lista.
	 */
	public void newObjectList()  {
		baseCreateMB.newItems(entityListPlc);
	}


	/**
	 * @since jCompany 5.0 Limpa Entidade da conversação para permitir que uma
	 *        nova seja criada no fluxo que se segue.
	 */
	public String create()  {
		if (entityPlc!=null) {
			if (entityListPlc!=null)
				return baseCreateMB.create(entityPlc, entityListPlc);
			else
				return baseCreateMB.create(entityPlc);
		}	
		else
			return baseCreateMB.create(entityListPlc);
	}


	/**
	 * @since jCompany 5.0 Maximiza seções de formulário
	 */
	public String portletMax()  {
		return basePortletMB.portletMax(entityPlc, entityListPlc);
	}


	/**
	 * @since jCompany 5.0 Minimiza portlets
	 */
	public String portletMin()  {
		return basePortletMB.portletMin(entityPlc, entityListPlc);
	}

	
	/**
	 * @since jCompany 5.0 Submete modificações para camada modelo.
	 */
	public String save()  {
		if (entityPlc!=null) {
			if (entityListPlc!=null)
				return baseSaveMB.save(entityPlc, entityListPlc);
			else
				return baseSaveMB.save(entityPlc);
		}	
		else
			return baseSaveMB.save(entityListPlc);
	}

	/**
	 * Recupera um grafo da entidade principal e disponibiliza para o formulario
	 * de visão
	 */
	public String edit()  {
		String result = baseEditMB.edit(entityPlc);
		this.entityPlc = baseEditMB.getEntity();
		return result;
	}
	

	/**
	 * Comuta modo de exibição de formulário com uso ou nao de tab-folder de
	 * layout. Nao tem template method de Antes por ser muito simples. Pode-se
	 * sobrepor e utilizar super.
	 */
	public String editDocumentView()  {
		return baseEditMB.editDocumentView();
	}

	/**
	 * Exclui grafo do Value Object principal (ie. Mestre, seus Detalhes e
	 * Sub-Detalhe eventuais).
	 */
	public String delete()  {
		return baseDeleteMB.delete(entityPlc);
	}
	
	/**
	 * Exclui o item de um componente de formulário, podendo este ser transiente (sem objetctId informado) ou persistente (com id informado).
	 * Obs.: somente funciona para Object-Id nesta versão.
	 */
	public String deleteItem()  {
		return baseDeleteMB.deleteItem(formAuxPlc);
	}

	public String search()  {
		baseSearchMB.setEntity(entityPlc);
		return baseSearchMB.search(entityListPlc);
	}

	/**
	 * jCompany. Método que implementa o evento "clonar", disparado pelo usuario
	 * quando ele deseja fazer uma cópia de um registro existente para inclui
	 * outro parecido
	 * <p>
	 * Este método também altera o modo para "inclusao" e dispara a operação
	 * "clonaAntes", para possiveis complementações no descendente.
	 * 
	 */
	public String cloneEntity()  {
		return baseCreateMB.cloneEntity(entityPlc);
	}

	/**
	 * Recupera a navegação para Combo Aninhado no onChange do componente
	 * 
	 * @param Evento disparado pelo Change do componente selectOneChoice.
	 * @return Página que será renderizada.
	 */
	public String findNavigationNestedCombo(ValueChangeEvent value)  {
		return baseSearchMB.findNavigationNestedCombo(value);
	}

	/**
	 * @since jCompany 5.0 Monta PlcArquivoVO baseado no arquivo informado pelo usuário no componente de inputFile, (upload) e coloca este arquivo no Arquivo Anexado da Entidade corrente.
	 */
	public void uploadFile(ValueChangeEvent event) {
		baseFileMB.uploadFile(event, entityPlc);
	}
	
	/**
	 * @since jCompany 5.0 Evento responsável por efetuar o download do arquivo. 
	 * 
	 */
	public void downloadFile() {
		baseFileMB.downloadFile(entityPlc);
	}
	

	/**
	 * Limpa todos os argumentos da pesquisa.
	 */
	public String clearArgs()  {
		return baseSearchMB.clearArgs(entityPlc);
	}

	/**
	 * Método para desconexão do usuário.
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {
		return baseLogoutMB.logout();
	}

	
	/**
	 * Habilita para exibir arquivo a ser anexado. Necessário para evitar forms
	 * do tipo multipart sem necessidade.
	 */
	public String modeFileAttach()  {
		return baseFileMB.modeFileAttach();
	}

	/**
	 * Método (evento) para atualizar classes lookup a partir do banco. Força a
	 * atualização a partir do banco.
	 * 
	 * 
	 */
	public void loadLookupClasses()  {
		dominioLookupUtil.loadLookupClasses();
	}

	/**
	 * 
	 * @since jCompany 5.0 Método que será sempre executado, antes da fase de
	 *        renderização, e antes de qualquer operação, independente da
	 *        operação que será executada.
	 * 
	 */
	public void executeBefore()  {}

	/**
	 * Efetua a auto recuperação para o id ou Chave Naturais Informados no componente vinculado.
	 * A Propriedade autoRecuperacaoClasse deve ser informada para que o valueChangeListener do componete seje configurado automaticamente pelo jCompany.
	 */
	public String autofindAggregate(ValueChangeEvent valueChangeEvent) {
		return baseSearchMB.autofindAggregate(valueChangeEvent);
	}
	
	/**
	 * @since jCompany 5.1 Gera página de saída do relatório .
	 */
	public String generateReport()  {
		return baseSearchMB.generateReport();
	}
	
	/**
	 * Recupera detalhes "por demanda"
	 */
	public String findDetailOnDemand()  {
		return baseSearchMB.findDetailOnDemand(entityPlc);
	}
	
	/**
	 * Método navegação de detalhe, para avanço de páginas
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String  navigationDetailNext () {
		return baseSearchMB.navigationDetailNext(entityPlc);
	}

	/**
	 * Método navegação de detalhe, para regreção de páginas
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailPrevious () {
		return baseSearchMB.navigationDetailPrevious(entityPlc);
	}
	
	/**
	 * Método navegação de detalhe, vai para a primeira página
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailFirst () {
		return baseSearchMB.navigationDetailFirst(entityPlc);
	}
	
	/**
	 * Método navegação de detalhe, vai para ultima página
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationDetailLast () {
		return baseSearchMB.navigationDetailLast(entityPlc);		
	}

	public String navigationToPage (ValueChangeEvent event) {
		return baseSearchMB.navigationToPage(event, entityPlc);		
	}
	
	/**
	 * Acionado pelo comboBox na página de navegação.
	 * Baseia a navegação no detalhe corrente registrado no campo detCorrPlcPaginado
	 */
	public String navigationToPage () {
		return baseSearchMB.navigationToPage(entityPlc);		
	}
	
	public void handleButtonsAccordingFormPattern() {
		visaoJsfUtil.handleButtonsAccordingUseCase();
		PlcCDIUtil.getInstance().fireEvent(this, new AnnotationLiteral<PlcHandleButtonsAccordingUseCaseAfter>(){});
	}
	

	public PlcEntityInstance<?> getPlcEntityInstance() {
		return metamodelUtil.createEntityInstance(entityPlc);
	}

	public String getKeyPlc() {
		return id;
	}

	public void setKeyPlc(String id) {
		this.id = id;
	}

	/**
	 * Retorna o Action (MB) responsável por criar a entidade
	 * @return Action (MB) responsável por criar a entidade
	 */
	public PlcBaseCreateMB getBaseCreateMB() {
		return baseCreateMB;
	}

	/**
	 * Retorna o Action (MB) responsável por manipular portlet
	 * @return Action (MB) responsável por manipular portlet
	 */
	public PlcBasePortletMB getBasePortletMB() {
		return basePortletMB;
	}

	/**
	 * Retorna o Action (MB) responsável por salvar a entidade
	 * @return Action (MB) responsável por salvar a entidade
	 */
	public PlcBaseSaveMB getBaseSaveMB() {
		return baseSaveMB;
	}
	
	/**
	 * Retorna o Action (MB) responsável por editar a entidade
	 * @return Action (MB) responsável por editar a entidade
	 */
	public PlcBaseEditMB getBaseEditMB() {
		return baseEditMB;
	}

	/**
	 * Retorna o Action (MB) responsável por apagar a entidade
	 * @return Action (MB) responsável por apagar a entidade
	 */
	public PlcBaseDeleteMB getBaseDeleteMB() {
		return baseDeleteMB;
	}

	/**
	 * Retorna o Action (MB) responsável por buscar a entidade
	 * @return Action (MB) responsável por buscar a entidade
	 */	
	public PlcBaseSearchMB getBaseSearchMB() {
		return baseSearchMB;
	}

	/**
	 * Retorna o Action (MB) responsável por manipular arquivos
	 * @return Action (MB) responsável por manipular arquivos
	 */	
	public PlcBaseFileMB getBaseFileMB() {
		return baseFileMB;
	}

	/**
	 * Retorna o Action (MB) responsável por operações de logout
	 * @return Action (MB) responsável por operações de logout
	 */		
	public PlcBaseLogoutMB getBaseLogoutAction() {
		return baseLogoutMB;
	}

	/**
	 * @return POJO que encapsula coleções de entidade para padrão Tabular
	 */
	public PlcEntityList getEntityListPlc() {
		return entityListPlc;
	}

	public void setEntityListPlc(PlcEntityList entityListPlc) {
		this.entityListPlc = entityListPlc;
	}

	public Object getEntityPlc() {
		return entityPlc;
	}

	public void setEntityPlc(Object entityPlc) {
		this.entityPlc = entityPlc;
	}
	
}
