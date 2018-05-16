/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.suitecasoteste;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="CASO_TESTE")
@SequenceGenerator(name="SE_CASO_TESTE", sequenceName="SE_CASO_TESTE")
@Access (AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="CasoTesteEntity.querySel", query="select id as id, nome as nome from CasoTesteEntity order by id asc"),
	@NamedQuery(name="CasoTesteEntity.queryMan", query="from CasoTesteEntity obj"),
	@NamedQuery(name="CasoTesteEntity.querySelLookup", query="select new CasoTesteEntity (obj.id, obj.nome) from CasoTesteEntity obj where obj.id = ? order by obj.id asc")

})
public class CasoTesteEntity extends CasoTeste {
 	
    /*
     * Construtor padrÃ£o
     */
    public CasoTesteEntity() {
    }
	public CasoTesteEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}

}
