/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.extension.manytomanymatrix.metadata.PlcConfigManyToManyMatrix;


@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity.class
)


@PlcConfigManyToManyMatrix(classeAssociativa = br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity.class, 
		classeEntidade1 = br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteEntity.class, 
		classeEntidade2 = br.com.plc.jcompany_fcls.entity.suitecasoteste.CasoTesteEntity.class, 
		propriedadeEntidade1 = "suite", 
		propriedadeEntidade2 = "casoTeste"
)


@SPlcMB
@PlcUriIoC("suitecasotestematrix")
@PlcHandleException
public class SuiteCasoTesteMatrixMB extends AppMB {

	private static final long serialVersionUID = 1L;
	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("suitecasotestematrix") 
	public SuiteCasoTesteEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new SuiteCasoTesteEntity();
			this.newEntity();
		}
		return (SuiteCasoTesteEntity)this.entityPlc;
	}

}
