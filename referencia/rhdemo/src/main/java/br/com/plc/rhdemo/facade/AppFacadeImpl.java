package br.com.plc.rhdemo.facade;

import javax.ejb.Stateless;
import javax.enterprise.inject.Specializes;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;

/**
 * jcompany_fcls . Classe de implementaÃ§Ã£o da interface de persistÃ©ncia
 */
@QPlcDefault
@SPlcFacade
//@PersistenceUnit(name="persistence/default", unitName="default")
//@PersistenceContext(name="persistence/context/default", unitName="default")
public class AppFacadeImpl extends PlcFacadeImpl  {
	

	
	
	
}