/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;

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
@Table(name = "EMPRESA")
@SequenceGenerator(name = "SE_EMPRESA", sequenceName = "SE_EMPRESA")
@Access(AccessType.FIELD)
@SuppressWarnings("serial")
@NamedQueries( {
		@NamedQuery(name = "EmpresaEntity.querySel2", query = "select id as id, nome as nome, capitalSocial as capitalSocial, cnpjBasico as cnpjBasico from EmpresaEntity order by nome asc"),
		@NamedQuery(name = "EmpresaEntity.queryMan", query = "from EmpresaEntity "),
		@NamedQuery(name = "EmpresaEntity.querySel", query = "select id as id, nome as nome, capitalSocial as capitalSocial from EmpresaEntity order by nome asc"),
		@NamedQuery(name = "EmpresaEntity.querySelLookup", query = "select id as id, nome as nome from EmpresaEntity where id = ? order by id asc") })
public class EmpresaEntity extends Empresa {

	/*
	 * Construtor padrÃ£o
	 */
	public EmpresaEntity() {
	}

	@Override
	public String toString() {
		return getNome();
	}
}
