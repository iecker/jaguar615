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


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValExactSize;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;
/**
 * Estados da FederaÃ§ao
 */
@MappedSuperclass
public abstract class Uf extends AppBaseEntity{

	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_UF")	
	private Long id;

	@NotNull(groups=PlcValGroupEntityList.class)
	@PlcValExactSize(size=2)
	@PlcValSimpleFormat(format=SimpleFormat.UPPER_CASE)
	@Column 
	@RequiredIf(valueOf="nome",is=RequiredIfType.not_empty)
	@Size(max = 2)
	private String sigla;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@Column 
	@PlcReference(testDuplicity=true)
	@Size(max = 40)
	private String nome;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla=sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

}