/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import org.apache.commons.beanutils.PropertyUtils;

import br.com.plc.jcompany_fcls.commons.AppUsuarioPerfilVO;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSaveMB;


public class UsuarioSaveMB extends PlcBaseSaveMB {

	@Override
	protected boolean saveBefore(FormPattern pattern, Object entityPlc, PlcBaseContextVO context) {

		AppUsuarioPerfilVO usuario = (AppUsuarioPerfilVO) contextUtil.getRequest().getSession().getAttribute("USER_INFO");
		
		AppUsuarioPerfilVO profile = PlcCDIUtil.getInstance().getInstanceByType(AppUsuarioPerfilVO.class, QPlcDefaultLiteral.INSTANCE);
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		
		FormPattern formPattern = configUtil.getConfigAggregation(url).pattern().formPattern();
		
		try {
			PropertyUtils.setProperty(entityPlc, "nomeResponsavel", usuario.getLogin());
			PropertyUtils.setProperty(entityPlc, "casoDeUso", formPattern.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.saveBefore(pattern, entityPlc, context);
	}
	
}
