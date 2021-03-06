package com.powerlogic.rhjboss.facade;

import javax.ejb.Stateless;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacadeEjb;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;

@QPlcDefault
@SPlcFacadeEjb
@Stateless
public class AppFacadeImpl extends PlcFacadeImpl implements IAppFacade{

}
