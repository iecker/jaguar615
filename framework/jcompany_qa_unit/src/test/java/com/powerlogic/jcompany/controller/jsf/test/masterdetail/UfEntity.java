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
@Table(name="UF")
@SequenceGenerator(name="SE_UF", sequenceName="SE_UF")
@Access(javax.persistence.AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="UfEntity.queryMan", query="from UfEntity"),
	@NamedQuery(name="UfEntity.querySelLookup", 
			query="select new UfEntity (id, nome, sigla) " +
					"from UfEntity where id = ? order by id asc")
})
public class UfEntity extends Uf {

	@Transient
	private transient int hashCodePlc = 0;	
 	
    /*
     * Construtor padrão
     */
    public UfEntity() {  }
	public UfEntity(Long id, String nome, String sigla) {
		this.setId(id);
		this.setNome(nome);
		this.setSigla(sigla);
	}
	@Override
	public String toString() {
		return getNome()+"-"+getSigla();
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
