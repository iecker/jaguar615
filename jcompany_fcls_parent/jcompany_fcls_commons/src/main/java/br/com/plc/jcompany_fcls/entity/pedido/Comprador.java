/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.pedido;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;



@MappedSuperclass

public abstract class Comprador  implements Serializable {

	
	@EmbeddedId
	private CompradorKeyVO idNatural;
	
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
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = ProdutosEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_COMPRADOR_PRODUTOS")
	@JoinColumns({@JoinColumn( name = "NUMERO_PRODUTO")})
	private Produtos produtos;

	@Transient
	private String indExcPlc = "N";	
		
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@RequiredIf(valueOf="id",is=RequiredIfType.not_empty)
	@Column (name = "NOME")
	private String nome;


    /*
     * Construtor sem parÃ¢metros.
     */
    public Comprador() {
    }

    public void setIdNatural(CompradorKeyVO idNatural){
        this.idNatural = idNatural;
    }
    
	public CompradorKeyVO getIdNatural() {
		return idNatural;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
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

	public Produtos getProdutos() {
		return produtos;
	}

	public void setProdutos(Produtos produtos) {
		this.produtos=produtos;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
