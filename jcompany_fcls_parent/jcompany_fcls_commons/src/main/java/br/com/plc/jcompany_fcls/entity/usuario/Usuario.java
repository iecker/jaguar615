/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.usuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.PlcBaseMapEntity;
import com.powerlogic.jcompany.domain.validation.PlcValCpf;


@MappedSuperclass

public abstract class Usuario extends PlcBaseMapEntity  implements Serializable {

	@NotNull(groups=PlcEntityList.class)
	@Size(max=1)
	@Column (name = "SIT_HISTORICO_PLC")
	private String sitHistoricoPlc="A";

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_USUARIO")
	@Column (name = "ID_USUARIO")
	private Long id;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=60)
	@Column (name = "NOME_COMPLETO")
	private String nomeCompleto;
	
	@Size(max=11)
	@Column (name = "CPF", nullable=true, length=11)
	@PlcValCpf
	private String cpf;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=3,fraction=0)
	@Column (name = "IDADE", nullable=false, length=3)
	private Integer idade;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=12,fraction=0)
	@Column (name = "NUMERO_IDENTIDADE")
	private Integer numeroIdentidade;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=8,fraction=2)
	@Column (name = "CONTRIBUICAO_INICIAL")
	private BigDecimal contribuicaoInicial;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=30)
	@Column (name = "LOGIN")
	private String login;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_CRIACAO_PERFIL_USUARIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacaoPerfilUsuario;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=250)
	@Column (name = "OBSERVACAO", nullable=false, length=250)
	private String observacao;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto=nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade=idade;
	}

	public Integer getNumeroIdentidade() {
		return numeroIdentidade;
	}

	public void setNumeroIdentidade(Integer numeroIdentidade) {
		this.numeroIdentidade=numeroIdentidade;
	}

	public BigDecimal getContribuicaoInicial() {
		return contribuicaoInicial;
	}

	public void setContribuicaoInicial(BigDecimal contribuicaoInicial) {
		this.contribuicaoInicial=contribuicaoInicial;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login=login;
	}

	public Date getDataCriacaoPerfilUsuario() {
		return dataCriacaoPerfilUsuario;
	}

	public void setDataCriacaoPerfilUsuario(Date dataCriacaoPerfilUsuario) {
		this.dataCriacaoPerfilUsuario=dataCriacaoPerfilUsuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao=observacao;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc=sitHistoricoPlc;
	}	
	
}
