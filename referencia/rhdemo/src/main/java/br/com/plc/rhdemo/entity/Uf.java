/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.validator.constraints.Length;

import com.powerlogic.jcompany.config.domain.PlcReference;

import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@MappedSuperclass
public abstract class Uf extends AppBaseEntity {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")
	@Column (name = "ID_UF", nullable=false, length=5)
	private Long id;

	@NotNull(groups=PlcValGroupEntityList.class)
	@Size(max=30)
	@Column
	@PlcReference(testDuplicity=true)	
	private String nome;
	
	@PlcValSimpleFormat(format=SimpleFormat.UPPER_CASE)
	@PlcValExactSize(size = 2)
    @RequiredIf(valueOf="nome",is=RequiredIfType.not_empty) 
	@NotNull(groups=PlcValGroupEntityList.class) 
	@Size(max = 2)
	@Column 	
	private String sigla;

	@Transient
	private String indExcPlc = "N";	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla=sigla;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
