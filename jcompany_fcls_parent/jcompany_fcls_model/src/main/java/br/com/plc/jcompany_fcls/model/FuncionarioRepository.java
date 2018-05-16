/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.model;

import br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity;

import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@SPlcRepository
@PlcAggregationIoC(clazz=FuncionarioEntity.class)
public class FuncionarioRepository extends PlcBaseRepository {

	
//	@Inject
//	public void setBaseCRUDS(@QFuncionarioCRUDSRepository FuncionarioCRUDSRepository baseCRUDS) {
//		this.base = baseCRUDS;
//	}
	
	
}
