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

import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.tipodocumento.TipoDocumento;
import br.com.plc.jcompany_fcls.entity.tipodocumento.TipoDocumentoEntity;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;




@MappedSuperclass

public abstract class Pedido  implements Serializable {

	@Transient
	private String indExcPlc = "N";	
	
	@SuppressWarnings("unchecked")
	@OneToMany (targetEntity = ProdutosEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="pedido")
	@ForeignKey(name="FK_PRODUTOS_PEDIDO")
	@PlcValDuplicity(property="descricao")
	@PlcValMultiplicity(referenceProperty="descricao")
	@OrderBy("descricao")
	private List<Produtos> produtos;

	@EmbeddedId
	private PedidoKeyVO idNatural;
	
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

	@NotNull
	@Size(max=40)
	@Column (name = "SOLICITANTE")
	private String solicitante;
	
	@NotNull
	@SuppressWarnings("unchecked")
	@ManyToOne (targetEntity = TipoDocumentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_PEDIDO_TIPODOCUMENTO")
	@JoinColumns({@JoinColumn( name = "CODIGO"),@JoinColumn( name = "SIGLA")})
	private TipoDocumento tipoDocumento;
	


    /*
     * Construtor sem parÃ¢metros.
     */
    public Pedido() {
    }

    public void setIdNatural(PedidoKeyVO idNatural){
        this.idNatural = idNatural;
    }
    
	public PedidoKeyVO getIdNatural() {
		return idNatural;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante=solicitante;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento=tipoDocumento;
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

	public List<Produtos> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produtos> produtos) {
		this.produtos=produtos;
	}

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
