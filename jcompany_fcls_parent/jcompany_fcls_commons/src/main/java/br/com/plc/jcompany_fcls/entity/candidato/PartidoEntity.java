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
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PARTIDO")
@SequenceGenerator(name="SE_PARTIDO", sequenceName="SE_PARTIDO")
//@AccessType("field")
@Access(AccessType.FIELD)




@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PartidoEntity.querySelLookup", query="select id as id, nome as nome from PartidoEntity where id = ? order by id asc")
})
public class PartidoEntity extends Partido {

	@Transient
	private String estadoSubDetalhePlc="E"; // E-expandido, R-retraido

 	
    /*
     * Construtor padrÃ£o
     */
    public PartidoEntity() {
    }

    @Override
	public String toString() {
		return getNome();
	}

	public String getEstadoSubDetalhePlc() {
		return estadoSubDetalhePlc;
	}

	public void setEstadoSubDetalhePlc(String estadoSubDetalhePlc) {
		this.estadoSubDetalhePlc=estadoSubDetalhePlc;
	}

}
