/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;


import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="UNIDADE_ORGANIZACIONAL")
@SequenceGenerator(name="SE_UNIDADE_ORGANIZACIONAL", sequenceName="SE_UNIDADE_ORGANIZACIONAL")
@Access(javax.persistence.AccessType.FIELD)
@PlcEntityTreeView(recursividadeNomeProp="unidadePai", recursividadeSomente=true, titulo="Hierarquia Organizacional",
		nomePropPadrao="unidadePai",urlManutencao="/unidadeorganizacionalman")
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="OrganizationalUnitEntity.queryMan", query="from OrganizationalUnitEntity"),
	@NamedQuery(name="OrganizationalUnitEntity.querySel", query="select new OrganizationalUnitEntity(id, nome, endereco.logradouro, endereco.numero) from OrganizationalUnitEntity order by nome asc"),
	@NamedQuery(name="OrganizationalUnitEntity.querySelLookup", 
			query="select new OrganizationalUnitEntity (id, nome) " +
					"from OrganizationalUnitEntity where id = ? order by id asc"),
	@NamedQuery(name="OrganizationalUnitEntity.queryTreeView", 
				query="select new OrganizationalUnitEntity (id, nome) " +
					"from OrganizationalUnitEntity where unidadePai is null order by nome asc"),				
	@NamedQuery(name="OrganizationalUnitEntity.naoDeveExistirNomeDuplicado", 
			query="select count(*) from OrganizationalUnitEntity where nome = :nome")				
})
public class OrganizationalUnitEntity extends OrganizationalUnit {

	@Transient
	private transient int hashCodePlc = 0;	

    /*
     * Construtor padrão
     */
    public OrganizationalUnitEntity() {
    }
	public OrganizationalUnitEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}


	public OrganizationalUnitEntity(Long id, String nome, String enderecoLogradouro, String enderecoNumero) {
		setId(id);
		setNome(nome);
		if (getEndereco() == null){
			setEndereco(new Address());
		}
		getEndereco().setLogradouro(enderecoLogradouro);
		getEndereco().setNumero(enderecoNumero);
	}
}
