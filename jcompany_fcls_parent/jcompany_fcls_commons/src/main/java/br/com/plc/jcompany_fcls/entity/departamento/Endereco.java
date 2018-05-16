/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.departamento;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.plc.jcompany_fcls.entity.uf.Uf;
import br.com.plc.jcompany_fcls.entity.uf.UfEntity;

import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@Embeddable
@Access (AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select rua as rua from Endereco where id = ? order by rua asc")
})
public class Endereco  implements Serializable {
	
	@NotNull
	@Size(max=30)
	@Column (name = "RUA")
	private String rua;
	
	@NotNull
	@Size(max=10)
	@PlcValSimpleFormat(format=SimpleFormat.NUMBER)
	@Column (name = "NUMERO")
	private String numero;
	
	@NotNull
	@Size(max=30)
	@Column (name = "BAIRRO")
	private String bairro;
	
	@NotNull
	@Size(max=30)
	@Column (name = "CIDADE")
	private String cidade;
	
	@NotNull
	@Size(max=8)
	@Column (name = "CEP")
	private String cep;
	
	@NotNull
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "ID_UF")
	private Uf uf;
	
	@Transient
	private String indExcPlc = "N";		
	
	public Endereco() {
		
	}
	
	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua=rua;
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

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
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

	@Override
	public String toString() {
		return getRua();
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}
	public String getIndExcPlc() {
		return indExcPlc;
	}

}
