/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.architecture;

import com.seventytwomiles.architecturerules.AbstractArchitectureRulesConfigurationTest;
import com.seventytwomiles.architecturerules.configuration.ConfigurationFactory;

public class PlcArchitectureTest extends AbstractArchitectureRulesConfigurationTest {

	public PlcArchitectureTest() {

		final ConfigurationFactory configurationFactory;

		configurationFactory= new PlcDigesterConfigurationFactory(ConfigurationFactory.DEFAULT_CONFIGURATION_FILE_NAME);

		getConfiguration().getRules().addAll(configurationFactory.getRules());

		getConfiguration().getSources().addAll(configurationFactory.getSources());

		getConfiguration().setDoCyclicDependencyTest(configurationFactory.doCyclicDependencyTest());

	}

	
	@Override
	protected String getConfigurationFileName() {
		// Para anular o processamento do construtor do ancestral dessa classe.
		return null;
	}

	/** 
	 * @see AbstractArchitectureRulesConfigurationTesttestArchitecture 
	 */  
	public void testArchitecture() {  

		/** 
		 * Run the test via doTest(). If any rules are broken, 
		 * or if the configuration can not be loaded properly, 
		 * then the appropriate Exception will be thrown. 
		 */  
		assertTrue(doTests());  
	}  

	//	protected void setUp() throws Exception {  
	//
	//		super.setUp();  
	//
	//		/* get the configuration reference */  
	//		final Configuration configuration = getConfiguration();  
	//
	//		/* add sources */  
	////		configuration.addSource(  
	////				new SourceDirectory("target\\test-classes", true));  
	//
	//		/* set options */  
	////		configuration.setDoCyclicDependencyTest(false);  
	////		configuration.setThrowExceptionWhenNoPackages(true);  
	//
	//		/* add Rules */  
	//		final Rule daoRule = new Rule("dao");  
	//		daoRule.setComment("Testando.");  
	//		daoRule.addPackage("*.pedido.*");  
	//		daoRule.addViolation(".*.funcionario.*");  
	//
	//		configuration.addRule(daoRule);  
	//	} 

}
