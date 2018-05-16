package com.empresa.rhtutorial.entity.funcionario;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="FUNCIONARIO")
@SequenceGenerator(name="SE_FUNCIONARIO", sequenceName="SE_FUNCIONARIO")
@Access(AccessType.FIELD)

@NamedQueries({
	@NamedQuery(name="FuncionarioEntity.queryMan", query="from FuncionarioEntity where sitHistoricoPlc='A'"),
	@NamedQuery(name="FuncionarioEntity.querySel", query="select obj.id as id, obj.nome as nome, obj.dataNascimento as dataNascimento, obj.cpf as cpf, obj.estadoCivil as estadoCivil," +
			" obj1.id as unidadeOrganizacional_id , obj1.nome as unidadeOrganizacional_nome, " +
			"obj.enderecoResidencial.logradouro as enderecoResidencial_logradouro " +
			"from FuncionarioEntity obj left outer join obj.unidadeOrganizacional as obj1" +
			" where obj.sitHistoricoPlc='A' order by obj.nome asc"),
	@NamedQuery(name="FuncionarioEntity.querySelLookup", query="select id as id, nome as nome, temCursoSuperior as temCursoSuperior from FuncionarioEntity where id = ? order by id asc"),
	@NamedQuery(name="FuncionarioEntity.naoDeveExistirCpfDuplicado", query="select count(*) from FuncionarioEntity where cpf = :cpf")
})
public class FuncionarioEntity extends Funcionario {

	@Transient
	private int idadeMin;

	@Transient
	private int idadeMax;
	
	@Transient
	private Date dataNascimentoMin;
	
	@Transient
	private Date dataNascimentoMax;
	
	public void setIdadeMin(int idadeMin) {
		this.idadeMin = idadeMin;
		if (idadeMin >0)
		  this.dataNascimentoMin = dateUtil.dateAccodingToYears(new Date(), idadeMin, true);
	}
	public void setIdadeMax(int idadeMax) {
		this.idadeMax = idadeMax;
		if (idadeMax >0)
		    this.dataNascimentoMax = dateUtil.dateAccodingToYears(new Date(), idadeMax, true);
	}

	public Date getDataNascimentoMin() {
		return dataNascimentoMin;
	}
	public void setDataNascimentoMin(Date dataNascimentoMin) {
		this.dataNascimentoMin = dataNascimentoMin;
	}
	public Date getDataNascimentoMax() {
		return dataNascimentoMax;
	}
	public void setDataNascimentoMax(Date dataNascimentoMax) {
		this.dataNascimentoMax = dataNascimentoMax;
	}

	
	public int getIdadeMin() {
		return idadeMin;
	}

	public int getIdadeMax() {
		return idadeMax;
	}
	

	private static final long serialVersionUID = 1L;
 	
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
