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
@Table(name="COMPRADOR")
@SequenceGenerator(name="SE_COMPRADOR", sequenceName="SE_COMPRADOR")
//@AccessType("field")
@Access (AccessType.FIELD)

@PlcPrimaryKey(classe=CompradorKeyVO.class, propriedades={"codigoComprador"})

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="CompradorEntity.querySelLookup", query="select idNatural as idNatural, nome as nome from CompradorEntity where idNatural.codigoComprador = ?  order by idNatural.codigoComprador asc")
})
public class CompradorEntity extends Comprador {
 	
    /*
     * Construtor padrÃ£o
     */
    public CompradorEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
