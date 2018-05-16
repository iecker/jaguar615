/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.model;

import javax.enterprise.context.ApplicationScoped;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.model.PlcBaseCRUDSRepository;
import com.powerlogic.jcompany.model.PlcOperationType;

@ApplicationScoped
@QPlcSpecific(name="br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity")
public class FuncionarioCRUDSRepository extends PlcBaseCRUDSRepository{

	@Override
	protected void persistenceAfter(PlcBaseContextVO context, Object dao, Object entidade, PlcOperationType operationType) {
		super.persistenceAfter(context, dao, entidade, operationType);
		System.out.println(">>>>>>>>>>>>>>>>###");
	}
	
	
}
