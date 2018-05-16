/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.modelo.repository;


import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.modelo.commons.AbstractArquillianTestCase;
import com.powerlogic.jcompany.persistence.jpa.entity.basic.UserEntity;



@RunWith(Arquillian.class)
public class PlcRepositoryTestCase extends AbstractArquillianTestCase {
	
	@Inject BeanManager beanManager;

	@Inject @QPlcDefault PlcIocModelUtil iocModelUtil;

	@Inject @QPlcDefault IPlcFacade iFacadePlc = null;
	
	@Test
	public void inserirUsuario() throws Exception
	{
		PlcCDIUtil.getInstance().setBeanManager(beanManager);
				
		UserEntity usuario = new UserEntity();
		usuario.setName("Baldini");
		
		PlcBaseContextVO context = new PlcBaseContextVO();
		
		usuario = (UserEntity)iFacadePlc.saveObject(context, usuario);
		
		Assert.assertTrue(usuario.getId()==1);
		
	}

	
}


