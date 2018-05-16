/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Table(name="DEPENDENTE")
@SequenceGenerator(name="SE_DEPENDENTE", sequenceName="SE_DEPENDENTE")
@Access (AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="DependenteEntity.querySelLookup", query="select id as id, nome as nome from DependenteEntity where id = ? order by id asc")
})
public class DependenteEntity extends Dependente {
 	
    /*
     * Construtor padrão
     */
    public DependenteEntity() {
    }

    @Override
	public String toString() {
		return getNome();
	}

}
