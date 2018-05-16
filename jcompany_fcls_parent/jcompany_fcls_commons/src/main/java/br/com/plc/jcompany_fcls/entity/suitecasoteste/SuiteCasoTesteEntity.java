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

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="SUITE_CASO_TESTE")
@SequenceGenerator(name="SE_SUITE_CASO_TESTE", sequenceName="SE_SUITE_CASO_TESTE")
@Access (AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="SuiteCasoTesteEntity.queryMan", query="from SuiteCasoTesteEntity"),
	@NamedQuery(name="SuiteCasoTesteEntity.querySel", query="select obj.id as id, obj0.id as suite_id, obj0.nome as suite_nome, obj1.id as casoTeste_id, obj1.nome as casoTeste_nome from SuiteCasoTesteEntity obj left outer join obj.suite as obj0 left outer join obj.casoTeste as obj1 order by obj.id asc"),
	@NamedQuery(name="SuiteCasoTesteEntity.querySelDesmarcados", 
							query=" select obj as suite, obj1 as casoTeste " + 
								  " from SuiteEntity obj, CasoTesteEntity obj1 " +
								  " where obj1.id not in ( " +
								  " 	select obj2.casoTeste.id " +
								  " 	from SuiteCasoTesteEntity obj2 " +
								  " 	where obj2.suite.id = obj.id) " )
	
})

public class SuiteCasoTesteEntity extends SuiteCasoTeste {
 	
	public SuiteCasoTesteEntity() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public String toString() {
		return getSuite() + "-" + getCasoTeste();
	}
    

	
}
