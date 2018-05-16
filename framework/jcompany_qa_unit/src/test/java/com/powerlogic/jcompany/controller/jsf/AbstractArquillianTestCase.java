/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf;

import javax.enterprise.context.Conversation;
import javax.interceptor.Interceptor;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrap;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.facade.AppFacadeImpl;
import com.powerlogic.jcompany.controller.facade.AppJpaDAO;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcPagedDetailControl;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcRequestControl;
import com.powerlogic.jcompany.controller.jsf.mock.HttpServletRequestMock;
import com.powerlogic.jcompany.controller.jsf.producer.PlcComponentProducer;
import com.powerlogic.jcompany.controller.jsf.util.PlcDeclarativeValidationUtil;
import com.powerlogic.jcompany.controller.rest.extensions.PlcServiceManager;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.faces.context.mock.ScopesExtensionMock;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;

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
		j.addPackages(true, PlcMetamodelUtilBootstrap.class.getPackage());
		j.addPackages(true, PlcMetamodelUtil.class.getPackage());
		j.addPackages(true, javax.ejb.Singleton.class.getPackage());
		try {
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.facade.PlcFacadeImpl").getPackage());
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.model.util.PlcIocModelUtil").getPackage());
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.model.PlcBaseRepository").getPackage());
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.persistence.PlcBaseDAO").getPackage());
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.model.interceptors.PlcTransactionalInterceptor").getPackage());
			j.addPackages(true, Class.forName("com.powerlogic.jcompany.model.annotation.PlcTransactional").getPackage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		j.addPackages(true, IPlcFacade.class.getPackage());
		j.addPackages(true, PlcEntityCommonsUtil.class.getPackage());
		j.addPackages(true, PlcClassLookupUtil.class.getPackage());
		j.addPackages(true, PlcEntity.class.getPackage());
		j.addPackages(true, PlcPagedDetailControl.class.getPackage());
		j.addPackages(true, Conversation.class.getPackage());
		j.addPackages(true, PlcConfigForm.class.getPackage());
		j.addPackages(true, PlcComponentProducer.class.getPackage());
		j.addPackages(true, ServletInputStream.class.getPackage());
		j.addPackages(true, PlcBaseMB.class.getPackage());
		j.addPackages(true, HttpServletRequest.class.getPackage());
		j.addPackages(true, HttpServletRequestMock.class.getPackage());
		j.addPackages(true, QPlcDefault.class.getPackage());
		j.addPackages(true, SPlcEntity.class.getPackage());
		j.addPackages(true, IPlcFile.class.getPackage());
		j.addPackages(true, PlcConstants.class.getPackage());
		j.addPackages(true, PlcDeclarativeValidationUtil.class.getPackage());
		j.addPackages(true, PlcIocModelUtil.class.getPackage());
		j.addPackages(true, HttpParam.class.getPackage());
		j.addPackages(true, Interceptor.class.getPackage());
		j.addPackages(true, PlcServiceManager.class.getPackage());
		

		j.addManifestResource("ApplicationResources_pt_BR.properties", ArchivePaths.create("ApplicationResources_pt_BR.properties"));
		j.addManifestResource("jCompanyResources_pt_BR.properties", ArchivePaths.create("jCompanyResources_pt_BR.properties"));
		j.addManifestResource("log4j.properties", ArchivePaths.create("log4j.properties"));
		j.addManifestResource("META-INF/persistence.xml", ArchivePaths.create("META-INF/persistence.xml"));
		j.addManifestResource("META-INF/beans.xml", ArchivePaths.create("META-INF/beans.xml"));
		j.addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension",ArchivePaths.create("META-INF/services/javax.enterprise.inject.spi.Extension"));
		j.addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
		j.addClass(ScopesExtensionMock.class);
		j.addClass(AppFacadeImpl.class);
		j.addClass(AppJpaDAO.class);
		

	}

//	/**
//	 * Inicializa os contextos necessários
//	 */
//	@Before
//	public void initContexts() {
//		ContextLifecycle lifecycle = Container.instance().services().get(
//				ContextLifecycle.class);
//		{
//			ConversationContext cc = lifecycle.getConversationContext();
//			cc.setActive(true);
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
	
	protected PlcRequestControl getPlcControleRequisicao() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcRequestControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_REQUISICAO));
	}
	protected PlcConversationControl getPlcControleConversacao() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcConversationControl.class, new PlcNamedLiteral(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO));
	}
}
