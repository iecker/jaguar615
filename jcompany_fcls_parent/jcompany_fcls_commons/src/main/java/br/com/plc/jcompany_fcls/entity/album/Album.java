/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.album;


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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass

public abstract class Album implements Serializable {

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = MusicaEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="album")
	@ForeignKey(name="FK_MUSICA_ALBUM")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	@OrderBy("id")
	private List<Musica> musica;

	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = EncarteEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@OrderBy("id")
	@ForeignKey(name="FK_ENCARTE_ALBUM")
	@PlcFileAttach(extension={"txt","doc","pdf","png"},  multiple=true)
	private List<EncarteEntity> encarte;
	
	public List<EncarteEntity> getEncarte() {
		return encarte;
	}

	public void setEncarte(List<EncarteEntity> encarte) {
		this.encarte = encarte;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_ALBUM")
	@Column (name = "ID_ALBUM")
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
	@Column (name = "USUARIO_ULT_ALTERACAO", nullable=false)
	private String usuarioUltAlteracao = "";
	
	@NotNull
	@Size(max=30)
	@Column (name = "NOME")
	private String nome;
	
	@NotNull
	@Size(max=30)
	@Column (name = "ARTISTA")
	private String artista;

	@Transient
	private String indExcPlc = "N";	
	
	@NotNull
	@Digits(integer=3,fraction=0)
	@Column (name = "QUANTIDADE_FAIXAS")
	private Integer quantidadeFaixas;
	
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

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista=artista;
	}

	public Integer getQuantidadeFaixas() {
		return quantidadeFaixas;
	}

	public void setQuantidadeFaixas(Integer quantidadeFaixas) {
		this.quantidadeFaixas=quantidadeFaixas;
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

	public List<Musica> getMusica() {
		return musica;
	}

	public void setMusica(List<Musica> musica) {
		this.musica=musica;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
