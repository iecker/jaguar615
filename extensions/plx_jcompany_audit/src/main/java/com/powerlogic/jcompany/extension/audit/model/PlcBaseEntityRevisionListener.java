package com.powerlogic.jcompany.extension.audit.model;

import org.hibernate.envers.RevisionListener;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.extension.audit.entity.PlcBaseEntityRevision;

/**
 * Listener que insere informações na entidade de revisão nas operações de persistência
 * 
 * @author Mauren Ginaldo Souza
 *
 */
public class PlcBaseEntityRevisionListener implements RevisionListener {
 
	public void newRevision(Object revisionEntity) {
 
        PlcBaseEntityRevision plcBaseEntityRevision = (PlcBaseEntityRevision) revisionEntity;
        
        //PlcBaseContextVO context = PlcContextManager.getContextVO();
        
		PlcBaseUserProfileVO baseUsuarioPerfilVO = null;
		//if (context != null) {
		//	baseUsuarioPerfilVO = context.getUserProfile();
		//}

		if (baseUsuarioPerfilVO == null || baseUsuarioPerfilVO.getLogin() == null){
			if (baseUsuarioPerfilVO == null)
				baseUsuarioPerfilVO = new PlcBaseUserProfileVO();
			baseUsuarioPerfilVO.setLogin("Anônimo");
		}
        
        plcBaseEntityRevision.setUsuarioUltAlteracao(baseUsuarioPerfilVO.getLogin());
 
	}
 
}