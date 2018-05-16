/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information in your installation directory. 
*  Contact Powerlogic for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br  */ 
package ###NOME_PACOTE###.controller.jsf;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.empresa.controller.jsf.EmpBaseMB;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@Specializes
@PlcHandleException
@SPlcMB
public class AppMB extends EmpBaseMB {

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected transient Logger log;
	
}
