/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.adm.PlcUserOnlineUtil;
import com.powerlogic.jcompany.controller.adm.PlcUserOnlineVO;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;


/**
 * jCompany. Controlador para a exibição de usuárion on line.
 * 
 * @since jCompany 5.5
 * @author igor.guimaraes
 *
 */
@SPlcMB
@PlcUriIoC("usuarioOnLinePlc")
@PlcHandleException
public class PlcOnlineUserMB extends PlcBaseMB {

	
	@Override
	public void executeBefore()  {
		
		PlcCacheSessionVO plcSessao = (PlcCacheSessionVO) contextUtil.getRequest().getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
		
		PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);
		 
		PlcUserOnlineUtil userOnlineUtil = (PlcUserOnlineUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcUserOnlineUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		
		Map sessoes = userOnlineUtil.getUsuariosOnLine();
		
		Set s = sessoes.keySet();
		Iterator i = s.iterator();
		List listaUsuarios = new ArrayList();
		
		while (i.hasNext()) {
			String idSessao = (String) i.next();
			PlcUserOnlineVO usuario = (PlcUserOnlineVO) sessoes.get(idSessao);
			listaUsuarios.add(usuario);
		}
		
		contextUtil.getRequest().setAttribute("listaUsuarioOnLine", listaUsuarios);
		
		super.executeBefore();
		
	}

}
