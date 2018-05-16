/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.usuario;


import java.io.Serializable;

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
@Table(name="USUARIO")
@SequenceGenerator(name="SE_USUARIO", sequenceName="SE_USUARIO")
@Access (AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="UsuarioEntity.queryMan", query="from UsuarioEntity"),
	@NamedQuery(name="UsuarioEntity.querySel", query="select id as id, nomeCompleto as nomeCompleto, idade as idade, numeroIdentidade as numeroIdentidade, login as login, cpf as cpf from UsuarioEntity"),
	@NamedQuery(name="UsuarioEntity.querySelLookup", query="select id as id, nomeCompleto as nomeCompleto from UsuarioEntity where id = ? order by id asc")
})

public class UsuarioEntity extends Usuario implements Serializable {
 	
    /*
     * Construtor padrÃ£o
     */
    public UsuarioEntity() {
    }
	@Override
	public String toString() {
		return getNomeCompleto();
	}
}
