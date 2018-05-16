/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa.entity.attach;


import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="TEST_FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="TEST_SE_FUNCIONARIO")
@Access(javax.persistence.AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="EmployeeEntity.queryMan", 		query="from EmployeeEntity"),
	@NamedQuery(name="EmployeeEntity.querySel", 		query="select new EmployeeEntity(id, nome) from EmployeeEntity order by nome asc"),
	@NamedQuery(name="EmployeeEntity.querySelLookup", 	query="select new EmployeeEntity (id, nome) from EmployeeEntity obj where id = ? order by id asc")	
})
public class EmployeeEntity extends Employee {

	private static final long serialVersionUID = 1L;
	
	/*
     * Construtor padrão
     */
    public EmployeeEntity() {
    }
	public EmployeeEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}


}
