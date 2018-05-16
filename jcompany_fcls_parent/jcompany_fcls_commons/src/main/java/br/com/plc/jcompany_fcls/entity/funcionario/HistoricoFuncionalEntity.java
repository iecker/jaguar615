/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.funcionario;


import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Table(name="HISTORICO_FUNCIONAL")
@SequenceGenerator(name="SE_HISTORICO_FUNCIONAL", sequenceName="SE_HISTORICO_FUNCIONAL")
//@AccessType("field")
@Access (AccessType.FIELD)



@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="HistoricoFuncionalEntity.querySelLookup", query="select id as id, descricao as descricao from HistoricoFuncionalEntity where id = ? order by id asc")
})
public class HistoricoFuncionalEntity extends HistoricoFuncional {
 	
    /*
     * Construtor padrÃ£o
     */
    public HistoricoFuncionalEntity() {
    }
	@Override
	public String toString() {
		return getDescricao();
	}

}
