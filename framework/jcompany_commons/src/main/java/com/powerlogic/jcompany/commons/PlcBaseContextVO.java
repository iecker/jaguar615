/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;

/**
 * DP Context Param. Classe que agrupa informações de contexto (estado) disponíveis na camada de aplicação/controle 
 * para uso na camada modelo, que é stateless (Application Scoped). Desta forma otimiza performance e possível distribuição 
 * (ex.: EJB distribuido ou Web-Services).
*/
@QPlcDefault
public class PlcBaseContextVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Mode {
		INCLUSAO,
		ALTERACAO,
		EXCLUSAO,
		PESQUISA
	}	
	
	protected Mode mode = Mode.INCLUSAO;
	/**
	 * Sigla da aplicação.
	 */
	protected String appAcronym;
	/**
	 * Armazena o nome da propriedade de utilizado na lógica de preferência de usuário para recuperação. 
	 */
	protected String argPreference;
	/**
	 * jCompany 3.0 Lógica sem complexida, em minúscula. Ex: tabular, crudtabular, etc.
	 */
	protected String formPattern;
	/**
	 * Classe principal do grafo
	 */
	protected Class entityClassPlc;
	/**
	 * Referencia a entidade raiz para Extensions. Obs.: não deve ser utilizada em Template Methods.
	 */
	protected Object entityForExtension;
	
	/**
	 * Utilizado para controlar o processo de execução em métodos de camada Modelo (Repository), quando utilizando Extensions.
	 */
	private boolean defaultProcessFlow;
	
	/* Informações de contexto */
	protected String dbFactory = "default";
	protected PlcBaseUserProfileVO userProfile;
	protected List<String> detailNames;
	protected List<String> detailNamesProps;
	protected Map<String,Class> detailOnDemand;
	protected String verticalFilter;
	protected String executionMode = "P";
	protected String url;
	protected String deleteModeAux = "F"; // Pode ser "L"-Lógico ou "F"-Físico
	protected String pkClass;
	protected String apiQuerySel = null;
	protected String subDetailPropNameCollection= null;
	protected String subDetailClass= null;
	protected String subDetailParent= null;
	protected String namedQueryReport = null;
	protected Map<String, Class<?>> logicalExclusionDetails;
	protected Map<String, Map<String,Class<?>>> logicalExclusionSubDetails;

	protected Map detalheComparator = new HashMap();
	/**
	 * Para detalhe paginado
	 */
	protected Map <String, PlcConfigDetail> pagedDetails = null;

	// TODO - Analisar a retirada dessa propriedade
	// Ação (botão) clicado originalmente, exceto em unespecified, onde a ação default é disponibilizada.
	protected String originalAction = ""; 
	
    
    public PlcBaseContextVO() {
	
    }
    
	public boolean isDefaultProcessFlow() {
		return defaultProcessFlow;
	}


	public void setDefaultProcessFlow(boolean defaultProcessFlow) {
		this.defaultProcessFlow = defaultProcessFlow;
	}
	
    /**
     * @return Retorna o classePrimaryKey.
     */
    public String getPkClass() {
        return this.pkClass;
    }
    /**
     * @param classePrimaryKey O classePrimaryKey a ser definido.
     */
    public void setPkClass(String classePrimaryKey) {
        this.pkClass = classePrimaryKey;
    }
    /**
     * @return Retorna o acaoOriginal.
     *
     */
    public String getOriginalAction() {
        return this.originalAction;
    }
    /**
     * @param acaoOriginal O acaoOriginal a ser definido.
     *
     */
    public void setOriginalAction(String acaoOriginal) {
        this.originalAction = acaoOriginal;
    }
    /**
     * @return Retorna o excluirModoAux.
     *
     */
    public String getDeleteModeAux() {
        return this.deleteModeAux;
    }
    /**
     * @param excluirModoAux O excluirModoAux a ser definido.
     *
     */
    public void setDeleteModeAux(String excluirModoAux) {
        this.deleteModeAux = excluirModoAux;
    }
    /**
     * @return Retorna o fabricaPlc.
     *
     */
    public String getDbFactory() {
        return this.dbFactory;
    }
    /**
     * @param fabricaPlc O fabricaPlc a ser definido.
     *
     */
    public void setDbFactory(String fabricaPlc) {
        this.dbFactory = fabricaPlc;
    }
    /**
     * @return Retorna o filtroVertical.
     *
     */
    public String getVerticalFilter() {
        return this.verticalFilter;
    }
    /**
     * @param filtroVertical O filtroVertical a ser definido.
     *
     */
    public void setVerticalFilter(String filtroVertical) {
        this.verticalFilter = filtroVertical;
    }
    /**
     * @return Retorna o formPattern.
     *
     */
    public String getFormPattern() {
        return this.formPattern;
    }
    /**
     * @param formPattern O padrão a ser definido.
     *
     */
    public void setFormPattern(String formPattern) {
        this.formPattern = formPattern;
    }
    /**
     * @return Retorna o url.
     *
     */
    public String getUrl() {
        return this.url;
    }
    /**
     * @param url O url a ser definido.
     *
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return Retorna uma coleçao contendo os nomes das classes com package de todos os detalhes.
     *
     */
    public List<String> getDetailNames() {
        return this.detailNames;
    }
    /**
     * @param nomeDetalhes O nomeDetalhes a ser definido.
     *
     */
    public void setDetailNames(List<String> nomeDetalhes) {
        this.detailNames = nomeDetalhes;
    }
    /**
     * @return Retorna uma coleção contendo os nomes das propriedades de coleçoes de todos os detalhes
     *
     */
    public List<String> getDetailNamesProps() {
        return this.detailNamesProps;
    }
    /**
     * @param nomeDetalhesPlc O nomeDetalhesPlc a ser definido.
     *
     */
    public void setDetailNamesProps(List<String> nomeDetalhesProps) {
        this.detailNamesProps = nomeDetalhesProps;
    }
    /**
     * @return Retorna o perfilUsu.
     *
     */
    public PlcBaseUserProfileVO getUserProfile() {
        return this.userProfile;
    }
    /**
     * @param perfilUsu O perfilUsu a ser definido.
     *
     */
    public void setUserProfile(PlcBaseUserProfileVO perfilUsu) {
        this.userProfile = perfilUsu;
    }
	/**
	 * @return Returns the apiQuerySel.
	 * 
	 */
	public String getApiQuerySel() {
		return apiQuerySel;
	}
	/**
	 * @param apiQuerySel The apiQuerySel to set.
	 * 
	 */
	public void setApiQuerySel(String apiQuerySel) {
		this.apiQuerySel = apiQuerySel;
	}
	/**
	 * 
	 */
	public String getNamedQueryReport() {
		return namedQueryReport;
	}
	/**
	 * 
	 */
	public void setNamedQueryReport(String namedQueryRel) {
		this.namedQueryReport = namedQueryRel;
	}
	/**
	 * @return Returns the subDetalhePropNomeColecao.
	 * 
	 */
	public String getSubDetailPropNameCollection() {
		return subDetailPropNameCollection;
	}
	/**
	 * @param subDetalhePropNomeColecao The subDetalhePropNomeColecao to set.
	 * 
	 */
	public void setSubDetailPropNameCollection(String subDetalhePropNomeColecao) {
		this.subDetailPropNameCollection = subDetalhePropNomeColecao;
	}
	
	public String getAppAcronym() {
		return appAcronym;
	}
	
	public void setAppAcronym(String siglaAplicacao) {
		this.appAcronym = siglaAplicacao;
	}
	/**
	 * @return Retorna subDetalheClasse.
	 */
	public String getSubDetailClass() {
		return subDetailClass;
	}
	/**
	 * @param subDetalheClasse Registra subDetalheClasse
	 */
	public void setSubDetailClass(String subDetalheClasse) {
		this.subDetailClass = subDetalheClasse;
	}
	/**
	 * @return Retorna detalhePorDemanda.
	 */
	public Map<String, Class> getDetailOnDemand() {
		return detailOnDemand;
	}
	/**
	 * @param detalhePorDemanda Registra detalhePorDemanda
	 */
	public void setDetailOnDemand(Map<String, Class> detalhePorDemanda) {
		this.detailOnDemand = detalhePorDemanda;
	}
	/**
	 * Recebe o nome de uma propriedade de detalhe e testa se é por demanda
	 * @param nomeColDet nome da coluna de detalhe.
	 * @return true se for por demanda ou false (default)
	 */
	public boolean isOnDemand(String nomeColDet) {
		return detailOnDemand!=null && detailOnDemand.containsKey(nomeColDet);
	}
	/**
	 * @return Retorna classePrincipal.
	 */
	public Class getEntityClassPlc() {
		return entityClassPlc;
	}
	/**
	 * @param classePrincipal Registra classePrincipal
	 */
	public void setEntityClassPlc(Class classePrincipal) {
		this.entityClassPlc = classePrincipal;
	}
	public String getArgPreference() {
		return argPreference;
	}
	public void setArgPreference(String argPreferencia) {
		this.argPreference = argPreferencia;
	}
	public String getSubDetailParent() {
		return subDetailParent;
	}
	public void setSubDetailParent(String subDetalhePai) {
		this.subDetailParent = subDetalhePai;
	}
	public Map<String, PlcConfigDetail> getPagedDetails() {
		return pagedDetails;
	}
	public void setPagedDetails(Map<String, PlcConfigDetail> mapaDetalhesPaginados) {
		this.pagedDetails = mapaDetalhesPaginados;
	}
	public void setLogicalExclusionDetails(Map<String, Class<?>> detalhesExclusaoLogica) {
		this.logicalExclusionDetails = detalhesExclusaoLogica;
	}
	public Map<String, Class<?>> getLogicalExclusionDetails() {
		return logicalExclusionDetails;
	}
	public boolean isLogicalExclusionDetail(String nomeColDet) {
		return logicalExclusionDetails!=null && logicalExclusionDetails.containsKey(nomeColDet);
	}
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode modo) {
		this.mode = modo;
	}
	public String getExecutionMode() {
		return executionMode;
	}
	public void setExecutionMode(String modoExecucao) {
		this.executionMode = modoExecucao;
	}
	public Map getDetalheComparator() {
		return detalheComparator;
	}
	public void setDetalheComparator(Map detalheComparator) {
		this.detalheComparator = detalheComparator;
	}
	
	public Object getEntityForExtension() {
		return entityForExtension;
	}

	public void setEntityForExtension(Object entityForExtension) {
		this.entityForExtension = entityForExtension;
	}
	
	public Map<String, Map<String,Class<?>>> getLogicalExclusionSubDetails() {
		return logicalExclusionSubDetails;
	}

	public void setLogicalExclusionSubDetails(
			Map<String, Map<String,Class<?>>> logicalExclusionSubDetails) {
		this.logicalExclusionSubDetails = logicalExclusionSubDetails;
	}
	
}