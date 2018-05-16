/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.candidato;


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
@Table(name="REFERENCIA_GERAIS")
@SequenceGenerator(name="SE_REFERENCIA_GERAIS", sequenceName="SE_REFERENCIA_GERAIS")
//@AccessType("field")
@Access(AccessType.FIELD)




@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ReferenciaGeraisEntity.querySelLookup", query="select id as id, indicacaoPartidaria as indicacaoPartidaria from ReferenciaGeraisEntity where id = ? order by id asc")
})
public class ReferenciaGeraisEntity extends ReferenciaGerais {
 	
    /*
     * Construtor padrÃ£o
     */
    public ReferenciaGeraisEntity() {
    }
	@Override
	public String toString() {
		return getIndicacaoPartidaria();
	}

}
