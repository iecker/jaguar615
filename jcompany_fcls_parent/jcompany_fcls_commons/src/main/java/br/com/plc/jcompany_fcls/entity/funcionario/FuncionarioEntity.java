/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;


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
@Table(name="FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="SE_FUNCIONARIO")
//@AccessType("field")
@Access (AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="FuncionarioEntity.querySel2", query="select obj.id as id, obj.nome as nome, obj.dataNascimento as dataNascimento, obj2.id as departamento_id, obj2.descricao as departamento_descricao, obj.sexo as sexo, obj3.id as endereco_uf_id,obj3.nome as endereco_uf_nome from FuncionarioEntity obj left outer join obj.departamento obj2 left outer join obj.endereco.uf obj3 order by obj.nome asc"),
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity"),
	@NamedQuery(name="FuncionarioEntity.querySel", query="select obj.id as id, obj.nome as nome, obj.dataNascimento as dataNascimento, obj2.id as departamento_id, obj2.descricao as departamento_descricao, obj.sexo as sexo, obj.temCursoSuperior as temCursoSuperior, obj3.id as endereco_uf_id, obj3.nome as endereco_uf_nome from FuncionarioEntity obj left outer join obj.departamento obj2 left outer join obj.endereco.uf obj3 order by obj.nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome from FuncionarioEntity where id = ? order by id asc"),
	@NamedQuery(name="FuncionarioEntity.naoDeveExistirNomeDuplicado", query="select count(*) from FuncionarioEntity where nome = :nome")
})
public class FuncionarioEntity extends Funcionario {
 	
    /*
     * Construtor padrÃ£o
     */
    public FuncionarioEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
