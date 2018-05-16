package com.powerlogic.jcompany.extension.audit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.powerlogic.jcompany.extension.audit.model.PlcBaseEntityRevisionListener;

/**
 * Entidade de revisão
 * 
 * @author Mauren Ginaldo Souza
 *
 */
@Entity
@RevisionEntity(PlcBaseEntityRevisionListener.class)
@Table(name="REVISION")
public class PlcBaseEntityRevision implements Serializable  {
 
	private static final long serialVersionUID = 1L;
 
	@Id
    @GeneratedValue
    @RevisionNumber
    private Integer rev;

	@RevisionTimestamp  
	private Date dataUltAlteracao;
	
	private String usuarioUltAlteracao;
	
	public int getRev() {
		return rev;
	}
	public void setRev(int rev) {
		this.rev = rev;
	}
	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}
	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao = dataUltAlteracao;
	}
	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}
	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao = usuarioUltAlteracao;
	}
 
 
 

}
