/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

@MappedSuperclass
public abstract class Departamento implements Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_DEPARTAMENTO")
	@Column (name = "ID_DEPARTAMENTO", nullable=false, length=5)
	private Long id;

	@NotNull 
	@Size(max = 30) 
	@Column
	private String descricao;
	
	@ManyToOne (targetEntity = DepartamentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_DEPARTAMENTO_DEPARTAMENTOPAI")
	@JoinColumn (name = "ID_DEPARTAMENTO_PAI")
	private Departamento departamentoPai;

	@Embedded
	private Endereco endereco;
	
	@Transient
	private transient String indExcPlc = "N";	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public Departamento getDepartamentoPai() {
		return departamentoPai;
	}

	public void setDepartamentoPai(Departamento departamentoPai) {
		this.departamentoPai=departamentoPai;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco=endereco;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
