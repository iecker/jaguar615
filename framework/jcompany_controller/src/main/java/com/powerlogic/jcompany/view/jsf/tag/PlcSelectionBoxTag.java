/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreSelectBooleanCheckboxTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcSelectionBoxAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectionBox;
import com.powerlogic.jcompany.view.jsf.renderer.PlcSelectionBoxRenderer;

/**
 * Especialização da tag base CoreSelectBooleanCheckboxTag para permitir IoC e DI nos componentes JSF/Trinidad.

 * @Descricao Renderiza um campo com opção de marcar e desmarcar para entrada de dados.!
 * @Exemplo <plcf:caixaMarcacao id="temCursoSuperior" value="#{plcEntidade.temCursoSuperior}"  ajudaChave="ajuda.temCursoSuperior"/>!
 * @Tag caixaMarcacao!
 */
public class PlcSelectionBoxTag extends CoreSelectBooleanCheckboxTag {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*Propriedades mapeadas*/
	private ValueExpression texto;
	private String classeCSS;
	private String chavePrimaria;
	private String ajuda;
	private String ajudaChave;
	private String obrigatorio;
	protected String obrigatorioDestaque;	
	private String somenteLeitura;
	private String textoChave;
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	private String valorMarcado;
	private String valorDesmarcado;
	private String colunas;
	protected String riaUsa;
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcSelectionBox.COMPONENT_TYPE;
	}
	

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcSelectionBoxRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		

		super.setProperties(bean);

		PlcSelectionBoxAdapter.getInstance().adapter(bean, texto, classeCSS, chavePrimaria, ajuda, 
				ajudaChave, obrigatorio, obrigatorioDestaque, somenteLeitura, textoChave, 
				tituloChave, titulo, exibeSe, bundle, valorMarcado, valorDesmarcado, colunas, riaUsa);

	}

	public void setTexto(ValueExpression texto) {
		this.texto = texto;
	}

	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public String getChavePrimaria() {
		return chavePrimaria;
	}

	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public String getClasseCSS() {
		return classeCSS;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public String getColunas() {
		return colunas;
	}

	public void setColunas(String colunas) {
		this.colunas = colunas;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public void setObjetoIndexado(String objetoIndexado) {
		//		Método está aqui para compatibilidade anterior.
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public String getSomenteLeitura() {
		return somenteLeitura;
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public String getTextoChave() {
		return textoChave;
	}

	public void setTextoChave(String textoChave) {
		this.textoChave = textoChave;
	}

	public String getValorDesmarcado() {
		return valorDesmarcado;
	}

	public void setValorDesmarcado(String valorDesmarcado) {
		this.valorDesmarcado = valorDesmarcado;
	}

	public String getValorMarcado() {
		return valorMarcado;
	}

	public void setValorMarcado(String valorMarcado) {
		this.valorMarcado = valorMarcado;
	}

	public ValueExpression getTitulo() {
		return titulo;
	}

	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public String getTituloChave() {
		return tituloChave;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}

	public String getAjuda() {
		return ajuda;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}


	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getRiaUsa() {
		return riaUsa;
	}

	public void setRiaUsa(String riaUsa) {
		this.riaUsa = riaUsa;
	}
	
}
