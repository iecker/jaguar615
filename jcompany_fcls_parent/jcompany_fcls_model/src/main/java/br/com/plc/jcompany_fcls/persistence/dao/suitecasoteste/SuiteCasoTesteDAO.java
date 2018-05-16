/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.persistence.dao.suitecasoteste;

import java.util.List;

import br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity;
import br.com.plc.jcompany_fcls.entity.suitecasoteste.CasoTeste;
import br.com.plc.jcompany_fcls.entity.suitecasoteste.Suite;
import br.com.plc.jcompany_fcls.entity.suitecasoteste.SuiteCasoTesteEntity;
import br.com.plc.jcompany_fcls.persistence.dao.AppJpaDAO;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

@PlcAggregationDAOIoC(SuiteCasoTesteEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class SuiteCasoTesteDAO extends AppJpaDAO {

	@PlcQuery("querySel")
   	public native List<FuncionarioEntity> findList(
   			PlcBaseContextVO context,
		   @PlcQueryParameter(name = "id", expression=" obj.id = :id") Long id,
		   @PlcQueryParameter(name = "suite", expression=" obj.suite = :suite") Suite suite,
		   @PlcQueryParameter(name = "casoTeste", expression=" obj.casoTeste = :casoTeste") CasoTeste casoTeste,
		   @PlcQueryOrderBy String dynamicOrderByPlc,
		   @PlcQueryFirstLine Integer primeiraLinhaPlc, 
		   @PlcQueryLineAmount Integer numeroLinhasPlc		   
		  );
	
	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
		   @PlcQueryParameter(name = "id", expression=" obj.id = :id") Long id,
		   @PlcQueryParameter(name = "suite", expression=" obj.suite = :suite") Suite suite,
		   @PlcQueryParameter(name = "casoTeste", expression=" obj.casoTeste = :casoTeste") CasoTeste casoTeste
	  );	
	

	@PlcQuery("querySelDesmarcados")
   	public native List<FuncionarioEntity> findList(
   			PlcBaseContextVO context,
		   @PlcQueryParameter(name = "suite", expression=" obj = :suite") Suite suite,
		   @PlcQueryParameter(name = "casoTeste", expression=" obj1 = :casoTeste") CasoTeste casoTeste,
		   @PlcQueryOrderBy String dynamicOrderByPlc,
		   @PlcQueryFirstLine Integer primeiraLinhaPlc, 
		   @PlcQueryLineAmount Integer numeroLinhasPlc		   
		  );
	
}
