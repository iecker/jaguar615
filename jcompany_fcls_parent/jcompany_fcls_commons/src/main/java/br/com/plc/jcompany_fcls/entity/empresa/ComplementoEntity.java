/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;


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
@Table(name="COMPLEMENTO")
@SequenceGenerator(name="SE_COMPLEMENTO", sequenceName="SE_COMPLEMENTO")
@Access (AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ComplementoEntity.querySelLookup", query="select id as id, dadosComplementares as dadosComplementares from ComplementoEntity where id = ? order by id asc")
})
public class ComplementoEntity extends Complemento {
 	
    /*
     * Construtor padrÃ£o
     */
    public ComplementoEntity() {
    }

    @Override
	public String toString() {
		return getDadosComplementares();
	}

}
