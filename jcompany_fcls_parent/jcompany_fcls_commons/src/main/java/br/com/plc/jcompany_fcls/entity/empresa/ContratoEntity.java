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
@Table(name="CONTRATO")
@SequenceGenerator(name="SE_CONTRATO", sequenceName="SE_CONTRATO")
@Access (AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ContratoEntity.querySelLookup", query="select id as id, escopoContrato as escopoContrato from ContratoEntity where id = ?  order by numeroContrato asc")
})
public class ContratoEntity extends Contrato {
 	
    /*
     * Construtor padrÃ£o
     */
    public ContratoEntity() {
    }
    
	@Override
	public String toString() {
		return getEscopoContrato();
	}

}
