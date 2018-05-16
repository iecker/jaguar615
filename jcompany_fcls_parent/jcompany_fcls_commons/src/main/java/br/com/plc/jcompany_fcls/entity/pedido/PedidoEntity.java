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

import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PEDIDO")
@SequenceGenerator(name="SE_PEDIDO", sequenceName="SE_PEDIDO")
//@AccessType("field")
@Access (AccessType.FIELD)

@PlcPrimaryKey(classe=PedidoKeyVO.class, propriedades={"numero"})

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PedidoEntity.queryMan", query="from PedidoEntity"),
	@NamedQuery(name="PedidoEntity.querySel", query="select obj.idNatural as idNatural, obj.solicitante as solicitante, obj2.idNatural as tipoDocumento_idNatural, obj2.descricao as tipoDocumento_descricao from PedidoEntity obj left outer join obj.tipoDocumento obj2 order by obj.idNatural.numero asc"),
	@NamedQuery(name="PedidoEntity.querySelLookup", query="select idNatural as idNatural, solicitante as solicitante from PedidoEntity where idNatural.numero = ?  order by idNatural.numero asc")
})
public class PedidoEntity extends Pedido {
 	
    /*
     * Construtor padrÃ£o
     */
    public PedidoEntity() {
    }
	@Override
	public String toString() {
		return getSolicitante();
	}

}
