/*
Jaguar-jCompany Developer Suite.
Powerlogic 2010-2014.
Please read licensing information in your installation directory.Contact Powerlogic for more
information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br
*/
package com.empresa.controller.jsf;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.empresa.commons.facade.IEmpFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.PlcCustomizeSkinMB;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;

@PlcConfigForm(
formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls-plc/plc")
)
                  
/**
* Controlador para personalização de pele
*/
@PlcUriIoC("personalizarpeleplc")
@PlcHandleException
@ConversationScoped
@SPlcMB
@Specializes
public class EmpCustomizeSkinMB extends PlcCustomizeSkinMB {

private static final long serialVersionUID = 8092349060616474037L;

@Inject
protected transient Logger log;

@Inject
protected IEmpFacade facade;

@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
protected PlcConversationControl plcControleConversacao;	

@Inject @QPlcDefault
protected PlcCreateContextUtil contextMontaUtil;	

@Override
protected String trocaPele(String skin) throws Exception {

PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

//facade.atualizaPreferenciaAplicacao(context, skin, null);

return super.trocaPele(skin);
}

}