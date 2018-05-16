package com.powerlogic.jcompany.model.init;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

public class PlcRemoteInvocationJCompanyInitializerInterceptor {

	@Inject @QPlcDefault
	PlcMetamodelInitializer plcMetamodelInitializer;
	
	
	@AroundInvoke
	public Object interceptAllRemoteInvocations (InvocationContext ctx) throws Exception {
		plcMetamodelInitializer.init(null);
		return ctx.proceed();
	}
}
