package com.empresa.rhtutorial2.controller.jsf.preferenciausuario;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.empresa.rhtutorial2.commons.AppUserProfileVO;
import com.empresa.rhtutorial2.controller.jsf.AppMB;
import com.empresa.rhtutorial2.entity.PreferenciaUsuarioEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(entity = com.empresa.rhtutorial2.entity.PreferenciaUsuarioEntity.class)
@PlcConfigForm(

formPattern = FormPattern.Usu, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls")

)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("preferenciausuario")
@PlcHandleException
public class PreferenciaUsuarioMB extends AppMB {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("preferenciausuario")
	public PreferenciaUsuarioEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new PreferenciaUsuarioEntity();
			this.newEntity();
		}
		return (PreferenciaUsuarioEntity) this.entityPlc;
	}

	@Override
	public String save() {

		super.save();
		//AppUserProfileVO userProfileVO = PlcCDIUtil.getInstance().getInstanceByType(AppUserProfileVO.class, QPlcDefaultLiteral.INSTANCE);
		AppUserProfileVO userProfileVO =  new AppUserProfileVO();
		userProfileVO.setPreferenciaUsuario((PreferenciaUsuarioEntity) this.entityPlc);
		return null;
	}

}
