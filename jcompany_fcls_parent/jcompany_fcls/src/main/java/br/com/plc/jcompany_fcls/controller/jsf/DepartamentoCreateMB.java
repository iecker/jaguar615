package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.ConversationScoped;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.controller.jsf.PlcBaseCreateMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

//@QDepartamento
@QPlcSpecific(name="departamento")
@ConversationScoped
@PlcHandleException
public class DepartamentoCreateMB extends PlcBaseCreateMB{

}
