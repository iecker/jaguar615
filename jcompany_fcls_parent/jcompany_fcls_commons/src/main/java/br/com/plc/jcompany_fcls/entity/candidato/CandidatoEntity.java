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
@Table(name="CANDIDATO")
@SequenceGenerator(name="SE_CANDIDATO", sequenceName="SE_CANDIDATO")
//@AccessType("field")
@Access(AccessType.FIELD)



@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="CandidatoEntity.queryMan", query="from CandidatoEntity"),
	@NamedQuery(name="CandidatoEntity.querySel", query="select id as id, nome as nome, dataNascimento as dataNascimento from CandidatoEntity order by nome asc"),
	@NamedQuery(name="CandidatoEntity.querySelLookup", query="select id as id, nome as nome from CandidatoEntity where id = ? order by id asc")
})
public class CandidatoEntity extends Candidato {
 	
    /*
     * Construtor padrÃ£o
     */
    public CandidatoEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}
}
