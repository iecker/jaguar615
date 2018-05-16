/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.pedido;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;


@MappedSuperclass

public abstract class Produtos  implements Serializable {

	@Transient
	private String indExcPlc = "N";	
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = CompradorEntity.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="produtos")
	@ForeignKey(name="FK_COMPRADOR_PRODUTOS")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	@OrderBy("nome")
	private List<Comprador> comprador;

	@EmbeddedId
	private ProdutosKeyVO idNatural;
	
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
	@Column (name = "USUARIO_ULT_ALTERACAO")
	private String usuarioUltAlteracao = "";
	
	@NotNull(groups=PlcEntityList.class)
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = PedidoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PRODUTOS_PEDIDO")
	@JoinColumns({@JoinColumn( name = "NUMERO")})
	private Pedido pedido;
	
	@NotNull(groups=PlcValGroupEntityList.class)
	@Size(max=40)
	@RequiredIf(valueOf="#{produto[index].idNatural.numeroProduto}",is=RequiredIfType.not_empty)
	@Column (name = "DESCRICAO")
	private String descricao;
	
	@NotNull(groups=PlcEntityList.class)
	@Max(40)
	@RequiredIf(valueOf="descricao",is=RequiredIfType.not_empty)
	@Column (name = "QUANTIDADE")
	private Integer quantidade;

    /*
     * Construtor sem parÃ¢metros.
     */
    public Produtos() {
    }

    public void setIdNatural(ProdutosKeyVO idNatural){
        this.idNatural = idNatural;
    }
    
	public ProdutosKeyVO getIdNatural() {
		return idNatural;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade=quantidade;
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

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido=pedido;
	}

	public List<Comprador> getComprador() {
		return comprador;
	}

	public void setComprador(List<Comprador> comprador) {
		this.comprador=comprador;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
