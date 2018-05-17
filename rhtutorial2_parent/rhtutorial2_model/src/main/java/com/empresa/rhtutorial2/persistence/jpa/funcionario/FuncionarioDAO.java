package com.empresa.rhtutorial2.persistence.jpa.funcionario;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import com.empresa.rhtutorial2.entity.funcionario.EstadoCivil;
import com.empresa.rhtutorial2.entity.funcionario.Funcionario;
import com.empresa.rhtutorial2.entity.funcionario.FuncionarioEntity;
import com.empresa.rhtutorial2.entity.funcionario.pendencia.Pendencia;
import com.empresa.rhtutorial2.entity.funcionario.pendencia.PendenciaEntity;
import com.empresa.rhtutorial2.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
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

@PlcAggregationDAOIoC(FuncionarioEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class FuncionarioDAO extends AppJpaDAO {

	@PlcQuery("querySel")
	public native List<FuncionarioEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc,
			@PlcQueryLineAmount Integer numeroLinhasPlc,

			@PlcQueryParameter(name = "id", expression = "id = :id") Long id,
			@PlcQueryParameter(name = "nome", expression = "nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name = "estadoCivil", expression = "estadoCivil = :estadoCivil") EstadoCivil estadoCivil);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,

			@PlcQueryParameter(name = "id", expression = "id = :id") Long id,
			@PlcQueryParameter(name = "nome", expression = "nome like :nome || '%' ") String nome,
			@PlcQueryParameter(name = "estadoCivil", expression = "estadoCivil = :estadoCivil") EstadoCivil estadoCivil);

	public void criaPendencia(PlcBaseContextVO context,
			Funcionario funcionario, String descricao, BigDecimal valorPendencia) {

		Pendencia pendencia = new Pendencia();
		pendencia.setFuncionario(funcionario);
		pendencia.setDescricao(descricao);
		pendencia.setValor(valorPendencia);
		super.update(context, pendencia);
	}

	public void atualizaPendencia(PlcBaseContextVO context,
			Funcionario funcionario, String descricao, BigDecimal valorPendencia) {

		try {
			// Verifica se já existe
			EntityManager em = getEntityManager(context);
			List lista = em
					.createNamedQuery("PendenciaEntity.pendenciaPorFuncionario")
					.setParameter("funcionario", funcionario).getResultList();

			PendenciaEntity pendencia = null;
			if (lista != null && lista.size() > 0) {
				pendencia = (PendenciaEntity) lista.get(0);
			}

			if (pendencia != null) {
				pendencia.setDescricao(descricao);
				pendencia.setValor(valorPendencia);
				em.persist(pendencia);
			} else {
				pendencia = new PendenciaEntity();
				pendencia.setFuncionario(funcionario);
				pendencia.setDescricao(descricao);
				pendencia.setValor(valorPendencia);
				em.persist(pendencia);
			}
		} catch (Exception e) {
			throw new PlcException("FuncionarioDAO", "atualizaPendencia", e,
					log, "");
		}
	}

	public void excluiPendencia(PlcBaseContextVO context,
			Funcionario funcionario) {
		try {
			EntityManager em = getEntityManager(context);
			List lista = em
					.createNamedQuery("PendenciaEntity.pendenciaPorFuncionario")
					.setParameter("funcionario", funcionario).getResultList();

			PendenciaEntity pendencia = null;
			if (lista != null && lista.size() > 0) {
				pendencia = (PendenciaEntity) lista.get(0);
			}

			if (pendencia != null)
				em.remove(pendencia);
		} catch (Exception e) {
			throw new PlcException("FuncionarioDAO", "excluiPendencia", e, log,
					"");
		}
	}

}
