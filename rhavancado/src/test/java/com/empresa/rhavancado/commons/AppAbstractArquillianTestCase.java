package com.empresa.rhavancado.commons;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.empresa.rhavancado.facade.AppFacadeImpl;
import com.empresa.rhavancado.persistence.jpa.AppJpaDAO;
import com.empresa.rhavancado.persistence.jpa.funcionario.FuncionarioDAO;
import com.powerlogic.jcompanyqa.commons.PlcAbstractArquillianTestCase;

@Run(RunModeType.IN_CONTAINER)
public class AppAbstractArquillianTestCase extends
		PlcAbstractArquillianTestCase {

	@Deployment
	public static Archive<?> createTestArchive() {
		JavaArchive j = ShrinkWrap.create(JavaArchive.class, "app-test.jar");
		PlcAbstractArquillianTestCase.complementTestArchive(j);
		complementTestArchive(j);
		return j;
	}

	/**
	 * Complementa o archive com informações adicionais
	 * 
	 * @param j
	 */
	protected static void complementTestArchive(JavaArchive j) {
		j.addPackages(true, AppFacadeImpl.class.getPackage());
		j.addPackages(true, AppJpaDAO.class.getPackage());
		j.addPackages(true, FuncionarioEntity.class.getPackage());
		j.addPackages(true, FuncionarioDAO.class.getPackage());
	}
}
