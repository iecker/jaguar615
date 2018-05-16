/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.cliente;


import javax.persistence.Access;
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
@Table(name="CLIENTE")
@SequenceGenerator(name="SE_CLIENTE", sequenceName="SE_CLIENTE")
@Access(javax.persistence.AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ClienteEntity.queryMan", query="from ClienteEntity obj"),
	@NamedQuery(name="ClienteEntity.querySel", query="select id as id, loginCliente as loginCliente, dataUltAlteracao as dataUltAlteracao from ClienteEntity obj order by loginCliente asc"),
	@NamedQuery(name="ClienteEntity.querySelLookup", query="select id as id, loginCliente as loginCliente from ClienteEntity where id = ? order by id asc")
})
public class ClienteEntity extends Cliente {

    /*
     * Construtor padrÃ£o
     */
    public ClienteEntity() {
    }

    @Override
	public String toString() {
		return getLoginCliente();
	}

}
