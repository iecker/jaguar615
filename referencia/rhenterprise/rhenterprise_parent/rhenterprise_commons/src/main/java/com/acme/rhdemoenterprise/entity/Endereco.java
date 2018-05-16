/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa Ã© licenciado com todos os seus cÃ³digos fontes. VocÃª pode modificÃ¡-los e							
	    utilizÃ¡-los livremente, inclusive distribuÃ­-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de cÃ³digo aberto da Powerlogic, definidos			
	    na licenÃ§a 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste cÃ³digo, para os clientes licenciados, 	
	    desde que todos os cÃ³digos do programa sejam mantidos intactos, sem modificaÃ§Ãµes por parte do licenciado. 		
	 																													
	    VocÃª deve ter recebido uma cÃ³pia da licenÃ§a POSL 2.0 juntamente com este programa.								
	    Se nÃ£o recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
package com.acme.rhdemoenterprise.entity;

import java.io.Serializable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import org.hibernate.annotations.ForeignKey;
import javax.persistence.Access;
import javax.persistence.Embeddable;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * EndereÃ§o
 */
@Embeddable
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="Endereco.querySelLookup", query="select logradouro as logradouro from Endereco where id = ? order by logradouro asc")
})
public class Endereco  implements Serializable {
	
	@Column 
	@Size(max = 50)
	@NotNull
	private String logradouro;
	
	@Column 
	@Size(max = 6)
	@NotNull
	private String numero;
	
	@Column 
	@Size(max = 40)
	@NotNull
	private String bairro;
	
	@Column 
	@Size(max = 30)
	@NotNull
	private String cidade;
	
	@PlcValSimpleFormat(format=SimpleFormat.NUMBER)
	@PlcValExactSize(size=8)
	@Column 
	@Size(max = 11)
	@NotNull
	private String cep;
	
	@ManyToOne (targetEntity = UfEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_ENDERECO_UF")
	@JoinColumn (nullable=false)
	@NotNull
	private Uf uf;

	public Endereco() {
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
		return getLogradouro();
	}

}