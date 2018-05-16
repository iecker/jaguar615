/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.empresa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

@MappedSuperclass
public abstract class Complemento  implements Serializable {

	@Transient
	private String indExcPlc = "N";	

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_COMPLEMENTO")
	@Column (name = "ID_COMPLEMENTO")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull
	@Size(max=20)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@NotNull
	@ManyToOne (targetEntity = EnderecoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_COMPLEMENTO_ENDERECO")
	@JoinColumn (name = "ID_ENDERECO")
	private Endereco endereco;

	@NotNull
	@Size(max=40)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "DADOS_COMPLEMENTARES")
	private String dadosComplementares;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getDadosComplementares() {
		return dadosComplementares;
	}

	public void setDadosComplementares(String dadosComplementares) {
		this.dadosComplementares=dadosComplementares;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao=versao;
	}

	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}

	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao=dataUltAlteracao;
	}

	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao=usuarioUltAlteracao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco=endereco;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
