package com.powerlogic.rhjboss.facade;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacadeEjb;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;

@SPlcFacadeEjb
@Stateless
@Remote(IAppFacade.class)
public class AppFacadeRemoteImpl extends PlcFacadeImpl implements IAppFacade{

}
