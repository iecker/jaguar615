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
 * Value Object específico para a geração de esquema do banco. 
 * @author Powerlogic Consultoria e Sistema
 * @version 6.0
 */
@SPlcEntity
public class PlcSchema implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String tipo="U";
    private String gerarArquivo="N";
    private String arquivoGerado;
    private String delimitador=";";
    private boolean objTabela=true;
    private boolean objConstraint=true;
    private boolean objSequence=true;
    private boolean objIndice=true;
    private boolean objDados=true;
    private String esquema;
    private String owner;
   
    
    public String getEsquema() {
        return esquema;
    }
    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }
    public String getDelimitador() {
        return delimitador;
    }
    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }
    public String getGerarArquivo() {
        return gerarArquivo;
    }
    public void setGerarArquivo(String gerarArquivo) {
        this.gerarArquivo = gerarArquivo;
    }
    public boolean getObjConstraint() {
        return objConstraint;
    }
    public void setObjConstraint(boolean objConstraint) {
        this.objConstraint = objConstraint;
    }
    public boolean getObjIndice() {
        return objIndice;
    }
    public void setObjIndice(boolean objIndice) {
        this.objIndice = objIndice;
    }
    public boolean getObjDados() {
        return objDados;
    }
    public void setObjDados(boolean objDados) {
        this.objDados = objDados;
    }
    public boolean getObjSequence() {
        return objSequence;
    }
    public void setObjSequence(boolean objSequence) {
        this.objSequence = objSequence;
    }
    public boolean getObjTabela() {
        return objTabela;
    }
    public void setObjTabela(boolean objTabela) {
        this.objTabela = objTabela;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getArquivoGerado() {
        return arquivoGerado;
    }
    public void setArquivoGerado(String arquivoGerado) {
        this.arquivoGerado = arquivoGerado;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
	    

}
