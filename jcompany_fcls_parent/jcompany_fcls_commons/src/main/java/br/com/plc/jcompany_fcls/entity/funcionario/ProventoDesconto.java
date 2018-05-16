/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.plc.jcompany_fcls.entity.AppBaseEntity;
import br.com.plc.jcompany_fcls.entity.enumerations.NaturezaProventoDesconto;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;




@MappedSuperclass

public abstract class ProventoDesconto extends AppBaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PROVENTO_DESCONTO")
	@Column (name = "ID_PROVENTO_DESCONTO")
	private Long id;

	@Transient
	private String indExcPlc = "N";	
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "ANO_MES_REFERENCIA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date anoMesReferencia;
	
	@NotNull(groups=PlcEntityList.class)
	@Digits(integer=12,fraction=2)
	@Column (name = "VALOR")
	private BigDecimal valor;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column (name = "DESCRICAO")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "NATUREZA_PROVENTO_DESCONTO")
	private NaturezaProventoDesconto naturezaProventoDesconto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Date getAnoMesReferencia() {
		return anoMesReferencia;
	}

	public void setAnoMesReferencia(Date anoMesReferencia) {
		this.anoMesReferencia=anoMesReferencia;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor=valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}

	public NaturezaProventoDesconto getNaturezaProventoDesconto() {
		return naturezaProventoDesconto;
	}

	public void setNaturezaProventoDesconto(NaturezaProventoDesconto naturezaProventoDesconto) {
		this.naturezaProventoDesconto=naturezaProventoDesconto;
	}
	
	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
