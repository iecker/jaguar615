/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name="TEST_USUARIO")
public class UserEntity {

	private static final long serialVersionUID = -6942806509186467054L;
	
	@SequenceGenerator(name="SE_USUARIO", sequenceName="TEST_SE_USUARIO")	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_USUARIO")
	@Column (name = "ID_USUARIO", nullable=false, length=5) 
    private Long id;
	
	@Column (name = "NOME", nullable=false, length=50)
    private String name;
    
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

}