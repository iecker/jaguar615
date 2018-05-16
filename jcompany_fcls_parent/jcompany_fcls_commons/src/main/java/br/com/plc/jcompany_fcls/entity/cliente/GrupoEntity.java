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
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="GRUPO")
@SequenceGenerator(name="SE_GRUPO", sequenceName="SE_GRUPO")
@Access(javax.persistence.AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="GrupoEntity.queryMan", query="from GrupoEntity"),
	@NamedQuery(name="GrupoEntity.querySel", query="select id as id, nome as nome, inativo as inativo from GrupoEntity order by nome asc"),
	@NamedQuery(name="GrupoEntity.querySelLookup", query="select id as id, nome as nome from GrupoEntity where id = ? order by id asc")
})
public class GrupoEntity extends Grupo {

	@Transient
	@SuppressWarnings("unused")
	private transient int hashCodePlc = 0;

	/*
	 * Construtor padrÃ£o
	 */
	public GrupoEntity() {
	}

	@Override
	public String toString() {
		return getNome();
	}
}
