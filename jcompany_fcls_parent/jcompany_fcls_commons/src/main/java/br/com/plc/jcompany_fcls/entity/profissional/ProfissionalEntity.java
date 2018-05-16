/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.profissional;


import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.plc.jcompany_fcls.entity.colaborador.EstadoCivil;
import br.com.plc.jcompany_fcls.entity.departamento.DepartamentoEntity;
import br.com.plc.jcompany_fcls.entity.departamento.Endereco;
import br.com.plc.jcompany_fcls.entity.uf.UfEntity;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PROFISSIONAL")
@SequenceGenerator(name="SE_PROFISSIONAL", sequenceName="SE_PROFISSIONAL")
@Access(javax.persistence.AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ProfissionalEntity.queryMan", query="from ProfissionalEntity where sitHistoricoPlc='A'"),
	@NamedQuery(name="ProfissionalEntity.querySel", query="select new ProfissionalEntity(obj.id, obj.nome, obj.dataNascimento, obj.cpf, obj.estadoCivil, obj0.id , obj0.descricao, obj.endereco.rua, obj.endereco.uf.id, obj1.nome) from ProfissionalEntity obj left outer join obj.departamento as obj0 left outer join obj.endereco.uf as obj1 where obj.sitHistoricoPlc='A' order by obj.nome asc"),
	@NamedQuery(name="ProfissionalEntity.querySelLookup", query="select new ProfissionalEntity (id, nome) from ProfissionalEntity where id = ? order by id asc"),
	@NamedQuery(name="ProfissionalEntity.naoDeveExistirCPFDuplicado", query="select count(*) from ProfissionalEntity where cpf = :cpf")
})
public class ProfissionalEntity extends Profissional {

	@Transient
	private transient int hashCodePlc = 0;	

 	
    /*
     * Construtor padrÃ£o
     */
    public ProfissionalEntity() {
    }
	public ProfissionalEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}


	public ProfissionalEntity(Long id, String nome, java.util.Date dataNascimento, String cpf, EstadoCivil estadoCivil, Long departamentoId, String departamentoDescricao, String enderecoRua, Long enderecoUfId, String enderecoUfNome) {
		setId(id);
		setNome(nome);
		setDataNascimento(dataNascimento);
		setCpf(cpf);
		setEstadoCivil(estadoCivil);
		if (getDepartamento() == null){
			setDepartamento(new DepartamentoEntity());
		}
		getDepartamento().setId(departamentoId);
		getDepartamento().setDescricao(departamentoDescricao);
		if (getEndereco() == null){
			setEndereco(new Endereco());
		}
		getEndereco().setRua(enderecoRua);
		if (getEndereco().getUf() == null){
			getEndereco().setUf(new UfEntity());
		}
		getEndereco().getUf().setId(enderecoUfId);
		getEndereco().getUf().setNome(enderecoUfNome);
	}
}
