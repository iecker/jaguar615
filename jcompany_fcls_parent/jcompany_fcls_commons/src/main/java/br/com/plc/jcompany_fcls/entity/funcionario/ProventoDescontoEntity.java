/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;


import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PROVENTO_DESCONTO")
@SequenceGenerator(name="SE_PROVENTO_DESCONTO", sequenceName="SE_PROVENTO_DESCONTO")
@Access(javax.persistence.AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ProventoDescontoEntity.queryMan", query="from ProventoDescontoEntity"),
	@NamedQuery(name="ProventoDescontoEntity.querySelLookup", query="select id as id, descricao as descricao from ProventoDescontoEntity where id = ? order by id asc")
})
public class ProventoDescontoEntity extends ProventoDesconto {

    /*
     * Construtor padrÃ£o
     */
    public ProventoDescontoEntity() {
    }
	@Override
	public String toString() {
		return getDescricao();
	}

}
