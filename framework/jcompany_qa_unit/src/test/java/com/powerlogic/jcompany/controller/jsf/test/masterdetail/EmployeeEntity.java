/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;


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
@Table(name="FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="SE_FUNCIONARIO")
@Access(javax.persistence.AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="EmployeeEntity.queryMan", query="from EmployeeEntity where sitHistoricoPlc='A'"),
	@NamedQuery(name="EmployeeEntity.querySel", 
			query="select new EmployeeEntity(obj.id, obj.nome, obj.dataNascimento, obj.cpf, obj.estadoCivil, " +
					"obj0.id , obj0.nome, obj.enderecoResidencial.logradouro) " +
					"from EmployeeEntity obj left outer join obj.unidadeOrganizacional as obj0 " +
					"where obj.sitHistoricoPlc='A' order by obj.nome asc"),
	@NamedQuery(name="EmployeeEntity.querySelLookup", 
			query="select new EmployeeEntity (id, nome) " +
					"from EmployeeEntity where id = ? order by id asc"),
	@NamedQuery(name="EmployeeEntity.naoDeveExistirCPFDuplicado", 
			query="select count(*) from EmployeeEntity where cpf = :cpf")
				
})
public class EmployeeEntity extends Employee {

	@Transient
	private transient int hashCodePlc = 0;	

 	
    /*
     * Construtor padrão
     */
    public EmployeeEntity() {
    }
	public EmployeeEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}


	public EmployeeEntity(Long id, String nome, java.util.Date dataNascimento, String cpf, MaritalStatus estadoCivil, Long unidadeOrganizacionalId, String unidadeOrganizacionalNome, String enderecoResidencialLogradouro) {
		setId(id);
		setNome(nome);
		setDataNascimento(dataNascimento);
		setCpf(cpf);
		setEstadoCivil(estadoCivil);
		if (getUnidadeOrganizacional() == null){
			setUnidadeOrganizacional(new OrganizationalUnitEntity());
		}
		getUnidadeOrganizacional().setId(unidadeOrganizacionalId);
		getUnidadeOrganizacional().setNome(unidadeOrganizacionalNome);
		if (getEnderecoResidencial() == null){
			setEnderecoResidencial(new Address());
		}
		getEnderecoResidencial().setLogradouro(enderecoResidencialLogradouro);
	}
}
