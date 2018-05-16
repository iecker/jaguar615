/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.tipodocumento;

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

import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.categoria.Categoria;
import br.com.plc.jcompany_fcls.entity.categoria.CategoriaEntity;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;




@MappedSuperclass

public abstract class TipoDocumento  implements Serializable {

	
	@EmbeddedId
	private TipoDocumentoKeyVO idNatural;
	
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

	@Transient
	private String indExcPlc = "N";	
			
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "DESCRICAO")
	private String descricao;
	
	@SuppressWarnings("unchecked")
	@NotNull(groups=PlcEntityList.class)
	@ManyToOne (targetEntity = CategoriaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_TIPODOCUMENTO_CATEGORIA")
	@JoinColumns({@JoinColumn( name = "SIGLA_CAT", referencedColumnName="SIGLA")})
	private Categoria categoria;


    /*
     * Construtor sem parÃ¢metros.
     */
    public TipoDocumento() {
    }

    public void setIdNatural(TipoDocumentoKeyVO idNatural){
        this.idNatural = idNatural;
    }
    
	public TipoDocumentoKeyVO getIdNatural() {
		return idNatural;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria=categoria;
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

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
