/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.suitecasoteste;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="SUITE")
@SequenceGenerator(name="SE_SUITE", sequenceName="SE_SUITE")
@Access (AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="SuiteEntity.queryMan", query="from SuiteEntity obj"),
	@NamedQuery(name="SuiteEntity.querySel", query="select new SuiteEntity (obj.id, obj.nome) from SuiteEntity obj order by obj.id asc"),
	@NamedQuery(name="SuiteEntity.querySelLookup", query="select new SuiteEntity (obj.id, obj.nome) from SuiteEntity obj where obj.id = ? order by obj.id asc")
})


public class SuiteEntity extends Suite {
 	
    /*
     * Construtor padrÃ£o
     */
    public SuiteEntity() {
    }
	public SuiteEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	

}
