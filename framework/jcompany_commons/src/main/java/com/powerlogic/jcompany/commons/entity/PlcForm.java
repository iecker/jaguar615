/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.entity;

import java.io.Serializable;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Value Object específico para configuração de formulário. 
 */

@SPlcEntity
public class PlcForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String formAcaoExibeTexto;

    private boolean formAlertaAlteracao;
    
    private boolean formAlertaExclusaoDetalhe;

    private boolean pesquisaRestful;

    public PlcForm() {
    	
    }

    public String getFormAcaoExibeTexto() {
    	return formAcaoExibeTexto;
    }

    public void setFormAcaoExibeTexto(String formAcaoExibeTexto) {
    	this.formAcaoExibeTexto = formAcaoExibeTexto;
    }

    public boolean isFormAlertaAlteracao() {
    	return formAlertaAlteracao;
    }

    public void setFormAlertaAlteracao(boolean formAlertaAlteracao) {
    	this.formAlertaAlteracao = formAlertaAlteracao;
    }
    
    public boolean isFormAlertaExclusaoDetalhe() {
    	return formAlertaExclusaoDetalhe;
    }

    public void setFormAlertaExclusaoDetalhe(boolean formAlertaExclusaoDetalhe) {
    	this.formAlertaExclusaoDetalhe = formAlertaExclusaoDetalhe;
    }

	public boolean isPesquisaRestful() {
		return pesquisaRestful;
	}

	public void setPesquisaRestful(boolean pesquisaRestful) {
		this.pesquisaRestful = pesquisaRestful;
	}
	
	@Override
	public String toString() {
		return getFormAcaoExibeTexto();
	}
	
}
