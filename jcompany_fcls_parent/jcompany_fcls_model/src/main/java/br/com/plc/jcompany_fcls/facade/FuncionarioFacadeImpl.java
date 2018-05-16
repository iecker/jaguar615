/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.facade;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;


@SPlcFacade
@PlcUriIoC("funcionario")
public class FuncionarioFacadeImpl extends PlcFacadeImpl {

	@Override
	@PlcTransactional
	public Object saveObject(PlcBaseContextVO context, Object entidade) {
		System.out.println(">>>>>>>>>>>>>>>>");
		return super.saveObject(context, entidade);
	}
	
	
}
