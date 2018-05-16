/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;


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
@Table(name="DEPARTAMENTO")
@SequenceGenerator(name="SE_DEPARTAMENTO", sequenceName="SE_DEPARTAMENTO")
@Access (AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="DepartamentoEntity.querySel2", query="select obj.id as id, obj.descricao as descricao, obj2.id as departamentoPai_id, obj2.descricao as departamentoPai_descricao, obj3.id as endereco_uf_id, obj3.nome as endereco_uf_nome from DepartamentoEntity obj left outer join obj.departamentoPai obj2 left outer join obj.endereco.uf obj3 order by obj.descricao asc"),
	@NamedQuery(name="DepartamentoEntity.queryMan", query="from DepartamentoEntity"),
	@NamedQuery(name="DepartamentoEntity.querySel", query="select obj.id as id, obj.descricao as descricao, obj2.id as departamentoPai_id, obj2.descricao as departamentoPai_descricao from DepartamentoEntity obj left outer join obj.departamentoPai obj2 order by obj.descricao asc"),
	@NamedQuery(name="DepartamentoEntity.querySelLookup", query="select id as id, descricao as descricao from DepartamentoEntity where id = ? order by id asc")
})
public class DepartamentoEntity extends Departamento implements Serializable {
 	
    /*
     * Construtor padrï¿½o
     */
    public DepartamentoEntity() {
    }

    @Override
	public String toString() {
		return getDescricao();
	}


}
