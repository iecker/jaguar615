/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;


@Embeddable
@Access(javax.persistence.AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select new Endereco (logradouro) from Endereco where id = ? order by logradouro asc")
})
public class Address  implements Serializable {

	@Transient
	private transient int hashCodePlc = 0;	

	@Column (name = "LOGRADOURO", nullable=false, length=40)
	private String logradouro;
	
	@Column (name = "NUMERO", nullable=false, length=15)
	private String numero;
	
	@Column (name = "BAIRRO", nullable=false, length=30)
	private String bairro;
	
	@PlcValSimpleFormat(format=SimpleFormat.NUMBER)
	@PlcValExactSize(size=8)
	@Column (name = "CEP", length=8)
	private String cep;
	
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@JoinColumn (name = "ID_UF", nullable=false)
	private Uf uf;
	
	public Address() {
	}
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro=logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero=numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro=bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep=cep;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf=uf;
	}

	public Address(String logradouro) {
		this.setLogradouro(logradouro);
	}
	@Override
	public String toString() {
		return getLogradouro();
	}


}
