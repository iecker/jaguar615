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

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="DEPENDENTE")
@SequenceGenerator(name="SE_DEPENDENTE", sequenceName="SE_DEPENDENTE")
@Access(javax.persistence.AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="DependentEntity.querySelLookup", query="select new DependentEntity (id, nome) from DependentEntity where id = ? order by id asc")
})
public class DependentEntity extends Dependent {

	@Transient
	private transient int hashCodePlc = 0;	

 	
    /*
     * Construtor padrão
     */
    public DependentEntity() {
    }
	public DependentEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private transient String indExcPlc = "N";	

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
