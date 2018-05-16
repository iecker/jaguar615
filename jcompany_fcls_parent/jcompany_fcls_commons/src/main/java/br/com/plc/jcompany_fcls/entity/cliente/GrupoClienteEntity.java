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

import org.apache.commons.lang.ObjectUtils;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="GRUPO_CLIENTE")
@SequenceGenerator(name="SE_GRUPO_CLIENTE", sequenceName="SE_GRUPO_CLIENTE")
@Access(javax.persistence.AccessType.FIELD)



@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="GrupoClienteEntity.querySelLookup", query="select id as id, grupo as grupo from GrupoClienteEntity where id = ? order by id asc")
})
public class GrupoClienteEntity extends GrupoCliente {

	private transient String grupoAuxLookup;


	@Transient
	@SuppressWarnings("unused")
	private transient int hashCodePlc = 0;

	/*
	 * Construtor padrÃ£o
	 */
	public GrupoClienteEntity() {
	}

	@Override
	public String toString() {
		return ObjectUtils.toString(getGrupo());
	}

	@Transient
	private transient String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}


	public void setGrupoAuxLookup(String grupoAuxLookup) {
		this.grupoAuxLookup=grupoAuxLookup;
	}

}
