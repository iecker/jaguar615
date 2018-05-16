/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.preferencia;


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
@Table(name="PREFERENCIA_APLICACAO")
@SequenceGenerator(name="SE_PREFERENCIA_APLICACAO", sequenceName="SE_PREFERENCIA_APLICACAO")
//@AccessType("field")
@Access (AccessType.FIELD)



@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PreferenciaAplicacaoEntity.querySelLookup", query="select new PreferenciaAplicacaoEntity (id, mensagemBoasVindas) from PreferenciaAplicacaoEntity where id = ? order by id asc")
})
public class PreferenciaAplicacaoEntity extends PreferenciaAplicacao {
 	
    /*
     * Construtor padrÃ£o
     */
    public PreferenciaAplicacaoEntity() {
    }
	public PreferenciaAplicacaoEntity(Long id, String mensagemBoasVindas) {
		this.setId(id);
		this.setMensagemBoasVindas(mensagemBoasVindas);
	}
	@Override
	public String toString() {
		return getMensagemBoasVindas();
	}

}
