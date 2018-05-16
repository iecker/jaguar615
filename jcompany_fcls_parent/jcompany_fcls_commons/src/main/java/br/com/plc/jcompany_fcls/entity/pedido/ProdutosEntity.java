/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.pedido;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PRODUTOS")
@SequenceGenerator(name="SE_PRODUTOS", sequenceName="SE_PRODUTOS")
//@AccessType("field")
@Access (AccessType.FIELD)

@PlcPrimaryKey(classe=ProdutosKeyVO.class, propriedades={"numeroProduto"})


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ProdutosEntity.querySelLookup", query="select idNatural as idNatural, descricao as descricao from ProdutosEntity where idNatural.numeroProduto = ?  order by idNatural.numeroProduto asc")
})
public class ProdutosEntity extends Produtos {

	@Transient
	private String estadoSubDetalhePlc="E"; // E-expandido, R-retraido

 	
    /*
     * Construtor padrÃ£o
     */
    public ProdutosEntity() {
    }
	public ProdutosEntity(ProdutosKeyVO idNatural, String descricao) {
		this.setIdNatural(idNatural);
		this.setDescricao(descricao);
	}
	@Override
	public String toString() {
		ProdutosKeyVO idNatural = getIdNatural();
		return getDescricao();
	}

	public String getEstadoSubDetalhePlc() {
		return estadoSubDetalhePlc;
	}

	public void setEstadoSubDetalhePlc(String estadoSubDetalhePlc) {
		this.estadoSubDetalhePlc=estadoSubDetalhePlc;
	}

}
