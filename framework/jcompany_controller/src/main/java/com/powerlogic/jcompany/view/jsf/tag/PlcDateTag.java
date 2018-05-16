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
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreInputDateTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcDateAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcDate;
import com.powerlogic.jcompany.view.jsf.renderer.PlcDateRenderer;

/**
 * Especialização da tag base CoreInputDateTag para permitir IoC e DI nos componentes JSF/Trinidad.
 *
 * @Descricao Renderiza um campo com uma linha para entrada de dados no formato data e um botão que abre um calendário para escolha de uma data.!
 * @Exemplo <plcf:data id="dataNascimento" value="#{plcEntidade.dataNascimento}" ajudaChave="ajuda.dataNascimento" />!
 * @Tag data!
 */
public class PlcDateTag extends CoreInputDateTag{
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	* Propriedades já mapeadas
	*/
	private String tamanho;		
	private String tamanhoMaximo;
	private String ajudaChave;		
	private String ajuda;
	private String classeCSS;		
	private String obrigatorio;		
	protected String obrigatorioDestaque;	
	private String somenteLeitura;	
	private String chavePrimaria;  
	private String tituloChave;
	private String riaUsa;
	private ValueExpression titulo;
	protected ValueExpression exibeSe;
	private String mascara="dd/MM/yyyy";
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	
	private String msgErro;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcDate.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcDateRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		
		

		// Ancestrais do Trinidad
	    super.setProperties(bean);
		
		PlcDateAdapter.getInstance().adapter(bean, tamanho, tamanhoMaximo, ajudaChave, ajuda, classeCSS, obrigatorio, 
				obrigatorioDestaque, somenteLeitura, chavePrimaria, tituloChave, titulo, exibeSe, mascara, bundle, msgErro, riaUsa);
		
	}
	
	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public void setObjetoIndexado(String objetoIndexado) {
//		Método está aqui para compatibilidade anterior.
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}


	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public void setTamanhoMaximo(String tamanhoMaximo) {
		this.tamanhoMaximo = tamanhoMaximo;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public void setRiaUsa(String riaUsa) {
		this.riaUsa = riaUsa;
	}
}
