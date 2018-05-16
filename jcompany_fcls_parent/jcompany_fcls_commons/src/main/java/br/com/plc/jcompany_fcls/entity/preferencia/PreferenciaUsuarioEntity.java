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
@Table(name="PREFERENCIA_USUARIO")
@SequenceGenerator(name="SE_PREFERENCIA_USUARIO", sequenceName="SE_PREFERENCIA_USUARIO")
//@AccessType("field")
@Access (AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PreferenciaUsuarioEntity.querySelLookup", query="select new PreferenciaUsuarioEntity (id, caminhoRaiz) from PreferenciaUsuarioEntity where id = ? order by id asc")
})
public class PreferenciaUsuarioEntity extends PreferenciaUsuario {
 	
    /*
     * Construtor padrÃ£o
     */
    public PreferenciaUsuarioEntity() {
    }
	public PreferenciaUsuarioEntity(Long id, String caminhoRaiz) {
		this.setId(id);
		this.setCaminhoRaiz(caminhoRaiz);
	}
	@Override
	public String toString() {
		return getCaminhoRaiz();
	}

}
