/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.uf;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="UF")
@SequenceGenerator(name="SE_UF", sequenceName="SE_UF")
@Access (AccessType.FIELD)
@PlcEntityTreeView(classeLookup=true)
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="UfEntity.queryMan", query="from UfEntity order by id asc"),
	@NamedQuery(name="UfEntity.naoDeveExistirSiglaDuplicado", query="select count(*) from UfEntity where sigla = :sigla"),	
	@NamedQuery(name="UfEntity.querySelLookup", query="select id as id, nome as nome from UfEntity where id = ? order by id asc")
})
public class UfEntity extends Uf implements Serializable {
 	
    /*
     * Construtor padrÃ£o
     */
    public UfEntity() {
    }

    @Override
	public String toString() {
		return getNome();
	}

}
