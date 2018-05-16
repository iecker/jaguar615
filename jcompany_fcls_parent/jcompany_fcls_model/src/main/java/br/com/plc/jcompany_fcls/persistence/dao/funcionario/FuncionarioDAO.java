/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.persistence.dao.funcionario;

import java.util.Date;
import java.util.List;

import br.com.plc.jcompany_fcls.entity.departamento.Departamento;
import br.com.plc.jcompany_fcls.entity.funcionario.FuncionarioEntity;
import br.com.plc.jcompany_fcls.entity.funcionario.Sexo;
import br.com.plc.jcompany_fcls.entity.uf.Uf;
import br.com.plc.jcompany_fcls.persistence.dao.AppJpaDAO;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

@PlcAggregationDAOIoC(FuncionarioEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class FuncionarioDAO extends AppJpaDAO {

	@PlcQuery("querySel")
   	public native List<FuncionarioEntity> findList(
   			PlcBaseContextVO context,
		   @PlcQueryParameter(name = "id", expression=" obj.id = :id") Long id,
		   @PlcQueryParameter(name = "nome", expression=" obj.nome = :nome") String nome,
		   @PlcQueryParameter(name = "dataNascimento", expression=" obj.dataNascimento = :dataNascimento") Date dataNascimento,
		   @PlcQueryParameter(name = "departamento", expression=" obj.departamento = :departamento") Departamento departamento,
		   @PlcQueryParameter(name = "temCursoSuperior", expression=" obj.temCursoSuperior = :temCursoSuperior") PlcYesNo temCursoSuperior,
		   @PlcQueryParameter(name = "sexo", expression=" obj.sexo = :sexo") Sexo sexo, 
		   @PlcQueryParameter(name = "endereco.uf", expression=" obj.endereco.uf = :endereco_uf ") Uf uf,
		   @PlcQueryOrderBy String dynamicOrderByPlc,
		   @PlcQueryFirstLine Integer primeiraLinhaPlc, 
		   @PlcQueryLineAmount Integer numeroLinhasPlc		   
		  );
	
	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
	   @PlcQueryParameter(name = "id", expression=" obj.id = :id") Long id,
	   @PlcQueryParameter(name = "nome", expression=" obj.nome = :nome") String nome,
	   @PlcQueryParameter(name = "dataNascimento", expression=" obj.dataNascimento = :dataNascimento") Date dataNascimento,
	   @PlcQueryParameter(name = "departamento", expression=" obj.departamento = :departamento") Departamento departamento,
	   @PlcQueryParameter(name = "temCursoSuperior", expression=" obj.temCursoSuperior = :temCursoSuperior") PlcYesNo temCursoSuperior,
	   @PlcQueryParameter(name = "sexo", expression=" obj.sexo = :sexo") Sexo sexo,
	   @PlcQueryParameter(name = "endereco.uf", expression=" obj.endereco.uf = :endereco_uf ") Uf uf
	  );	
	
}
