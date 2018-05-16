/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.modelo.commons;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrap;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcMessageConveyorFactory;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.modelo.repository.UsuarioRepository;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.jpa.PlcBaseJpaDAO;
import com.powerlogic.jcompany.persistence.jpa.entity.attach.EmployeeEntity;
import com.powerlogic.jcompany.persistence.jpa.entity.basic.UserEntity;

/**
 * Classe abstrata de testes com arquillian
 * 
 * @author george
 * 
 */
@Run(RunModeType.IN_CONTAINER)
public abstract class AbstractArquillianTestCase {

	@Deployment
	public static Archive<?> createTestArchive() {
		JavaArchive j = ShrinkWrap.create(JavaArchive.class, "test.jar");
		complementTestArchive(j);
		return j;
	}

	/**
	 * Complementa o archive com informações adicionais
	 * 
	 * @param j
	 */
	private static void complementTestArchive(JavaArchive j) {

		j.addPackages(true, EmployeeEntity.class.getPackage());
		j.addPackages(true, UserEntity.class.getPackage());
		j.addPackages(true, UsuarioRepository.class.getPackage());
		j.addPackages(true, PlcIocModelUtil.class.getPackage());
		j.addPackages(true, PlcMetamodelUtilBootstrap.class.getPackage());
		j.addPackages(true, PlcMetamodelUtil.class.getPackage());
		j.addPackages(true, PlcBaseDAO.class.getPackage());
		j.addPackages(true, PlcBaseJpaDAO.class.getPackage());
		j.addPackages(true, PlcFacadeImpl.class.getPackage());
		j.addPackages(true, PlcBaseRepository.class.getPackage());
		j.addPackages(true, PlcEntityCommonsUtil.class.getPackage());
		j.addPackages(true, PlcEntity.class.getPackage());
		j.addPackages(true, PlcMessageConveyorFactory.class.getPackage());
		j.addPackages(true, PlcBeanMessages.class.getPackage());
		j.addPackages(true, QPlcDefault.class.getPackage());
		j.addPackages(true, SPlcEntity.class.getPackage());
		j.addPackages(true, IPlcFacade.class.getPackage());
		j
		.addManifestResource(
				"META-INF/services/javax.enterprise.inject.spi.Extension",
				ArchivePaths
						.create("META-INF/services/javax.enterprise.inject.spi.Extension"));

		j.addManifestResource("META-INF/persistence.xml", ArchivePaths.create("META-INF/persistence.xml"));
		j.addManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"));
		j.addManifestResource("log4j.properties", ArchivePaths.create("log4j.properties"));
		j.addManifestResource("jCompanyResources_pt_BR.properties", ArchivePaths.create("jCompanyResources_pt_BR.properties"));
	}

	/**
	 * Inicializa os contextos necessários
	 */
//	@Before
//	public void initContexts() {
//		ContextLifecycle lifecycle = Container.instance().services().get(ContextLifecycle.class);
//		{
//			ConversationContext cc = lifecycle.getConversationContext();
//			cc.activate()setActive(true);
//			cc.setBeanStore(new HashMapBeanStore());
//		}
//		{
//			RequestContext cc = lifecycle.getRequestContext();
//			cc.setActive(true);
//			cc.setBeanStore(new HashMapBeanStore());
//		}
//		{
//			SessionContext cc = lifecycle.getSessionContext();
//			cc.setActive(true);
//			cc.setBeanStore(new HashMapBeanStore());
//		}
//	}

}
