package com.powerlogic.jcompany.extension.manytomanymatrix.controller.service;

/**
 * Dto utilizado para montagem do json da entidade associativa
 * 
 * @author Mauren Ginaldo Souza
 * @since out/2010
 *
 */
public class PlcManyToManyMatrixDto {

	private Long id;
	
	private Object entidade1;

	private Object entidade2;

	private Object entidade3;

	private Boolean associa;

	public PlcManyToManyMatrixDto(Long id, Object entidade1, Object entidade2, Boolean associa) {
		super();
		this.id = id;
		this.entidade1 = entidade1;
		this.entidade2 = entidade2;
		this.associa = associa;
	}
	public PlcManyToManyMatrixDto(Long id, Object entidade1, Object entidade2, Object entidade3, Boolean associa) {
		super();
		this.id = id;
		this.entidade1 = entidade1;
		this.entidade2 = entidade2;
		this.entidade3 = entidade3;
		this.associa = associa;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Object getEntidade1() {
		return entidade1;
	}
	public void setEntidade1(Object entidade1) {
		this.entidade1 = entidade1;
	}
	public Object getEntidade2() {
		return entidade2;
	}
	public void setEntidade2(Object entidade2) {
		this.entidade2 = entidade2;
	}
	public Object getEntidade3() {
		return entidade3;
	}
	public void setEntidade3(Object entidade3) {
		this.entidade3 = entidade3;
	}

	public Boolean getAssocia() {
		return associa;
	}
	public void setAssocia(Boolean associa) {
		this.associa = associa;
	}
	
	@Override
	public String toString() {
		return null != entidade3 ? entidade1 + "_" + entidade2 + "_" + entidade3 : entidade1 + "_" + entidade2;
	}
}
