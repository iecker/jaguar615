/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.candidato;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass
public class Candidato implements Serializable {
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = ReferenciaGeraisEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="candidato")
	@ForeignKey(name="FK_REFERENCIAGERAIS_CANDIDATO")
	@PlcValDuplicity(property="indicacaoPartidaria")
	@PlcValMultiplicity(referenceProperty="indicacaoPartidaria")
	//TODO JPA / Validator: org.hibernate.validator.Valid
	private List<ReferenciaGerais> referenciaGerais;
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = PartidoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="candidato")
	@ForeignKey(name="FK_PARTIDO_CANDIDATO")
	@OrderBy("id")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	private List<Partido> partido;

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = HistoricoPoliticoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="candidato")
	@ForeignKey(name="FK_HISTORICOPOLITICO_CANDIDATO")
	@OrderBy("id")
	@PlcValDuplicity(property="cargoAnterior")
	@PlcValMultiplicity(min=1, referenceProperty="cargoAnterior")
	private List<HistoricoPolitico> historicoPolitico;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_CANDIDATO")
	@Column (name = "ID_CANDIDATO")
	private Long id;
	
	@Version
	@NotNull
	@Column (name = "VERSAO")
	@Digits(integer=5, fraction=0)
	private Integer versao = new Integer(0);
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_ULT_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltAlteracao = new Date();
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "NOME")
	private String nome;
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "DATA_NASCIMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=50)
	@Column (name = "IDEOLOGIA")
	private String ideologia;

	@Transient
	private String indExcPlc = "N";
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento=dataNascimento;
	}

	public String getIdeologia() {
		return ideologia;
	}

	public void setIdeologia(String ideologia) {
		this.ideologia=ideologia;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
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

	public List<HistoricoPolitico> getHistoricoPolitico() {
		return historicoPolitico;
	}

	public void setHistoricoPolitico(List<HistoricoPolitico> historicoPolitico) {
		this.historicoPolitico=historicoPolitico;
	}

	public List<Partido> getPartido() {
		return partido;
	}

	public void setPartido(List<Partido> partido) {
		this.partido=partido;
	}

	public List<ReferenciaGerais> getReferenciaGerais() {
		return referenciaGerais;
	}

	public void setReferenciaGerais(List<ReferenciaGerais> referenciaGerais) {
		this.referenciaGerais=referenciaGerais;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
