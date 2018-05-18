package com.empresa.rhavancado.persistence.jpa.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.empresa.rhavancado.entity.EstadoCivil;
import com.empresa.rhavancado.entity.Funcionario;
import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.empresa.rhavancado.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(Funcionario.class)
@SPlcDataAccessObject
@PlcQueryService
public class FuncionarioDAO extends AppJpaDAO {

	@PlcQuery("querySel")
	public List<FuncionarioEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc,
			@PlcQueryLineAmount Integer numeroLinhasPlc,
			@PlcQueryParameter(name = "nome", expression = "nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name = "estadoCivil", expression = "estadoCivil = :estadoCivil") EstadoCivil estadoCivil,
			@PlcQueryParameter(name = "dataNascimentoMin", expression = "dataNascimento <= :dataNascimentoMin ") Date dataNascimentoMin,
			@PlcQueryParameter(name = "dataNascimentoMax", expression = "dataNascimento >= :dataNascimentoMax ") Date dataNascimentoMax) {

		String querySQL = "from com.powerlogic.rhavancado.entity.funcionario.FuncionarioEntity f "
				+ "where "
				+ "f.nome like :nome and "
				+ "f.dataNascimento<= :dataNascimentoMin and "
				+ "f.dataNascimento>= :dataNascimentoMax "
				+ "order by nome asc";

		Query query = getEntityManager(context).createQuery(querySQL);
		query.setParameter("nome", (nome != null ? nome : "") + "%");
		query.setParameter("dataNascimentoMin",
				(dataNascimentoMin != null ? dataNascimentoMin : new Date()));
		query.setParameter("dataNascimentoMax",
				(dataNascimentoMax != null ? dataNascimentoMax : new Date(0L)));

		return query.getResultList();
	}

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			@PlcQueryParameter(name = "dataNascimentoMin", expression = "dataNascimento <= :dataNascimentoMin ") Date dataNascimentoMin,
			@PlcQueryParameter(name = "dataNascimentoMax", expression = " dataNascimento >= :dataNascimentoMax ") Date dataNascimentoMax,
			@PlcQueryParameter(name = "id", expression = "id = :id") Long id);

}
