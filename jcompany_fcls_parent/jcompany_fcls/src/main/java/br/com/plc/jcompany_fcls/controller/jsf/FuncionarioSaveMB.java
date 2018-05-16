package br.com.plc.jcompany_fcls.controller.jsf;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;

public class FuncionarioSaveMB extends PlcBaseSaveMB {
	
	@Override
	protected String saveAfter(FormPattern pattern, Object entityPlc, PlcBaseContextVO context) {

		
		//baseCreateMB.clearEntity(entityPlc);
		
		return null;
		//return "selecaofuncionario";		
		
	}

}
