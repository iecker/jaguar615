/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.facade;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileDTO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcSecurityFacade;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.repository.IPlcJsecurityBO;

public class PlcSecurityFacadeImpl implements IPlcSecurityFacade {

 	@Inject @QPlcDefault 
 	protected PlcIocModelUtil iocModelUtil;

	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
	/**
	 * Placeholder para a classe PlcJSecurityBO. 
	 * Se a aplicação estiver configurada para utilizar o JSecurity, será armazenada a classe nesssa propriedade para evitar chamadas repetidas ao Class.forName().
	 * 
	 * @since jCompany 3.0
	 */
	private static Class classePlcJSecurityBO;

	
	@PlcTransactional(commit=false)
	@Override
	public List<String> recuperaFilterDefs(PlcBaseContextVO context, Class entity)  {
		
		PlcBaseRepository repository = iocModelUtil.getRepository(entity);
		List<String> filterParam = repository.findFilterDefs(context, entity);
		return filterParam;
	}
	

    /**
	 * Se a aplicação estiver configurada para utilizar o jSecurity, carrega o
	 * profile do usuário.
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param context
	 * @return Perfil do usuário configurado.
	 * 
	 */
	@PlcTransactional(commit=false)
	@Override
	public PlcBaseUserProfileVO carregaProfileJSecurity(PlcBaseContextVO context)  {

		Object repository = iocModelUtil.getRepository(getClassePlcJSecurityBO());

		Object userProfileDTO = reflectionUtil.executeMethod(repository, "carregaProfileUsuario", new Object[] { context }, new Class[] { PlcBaseContextVO.class });
		
		PlcBaseUserProfileVO perfil = new PlcBaseUserProfileVO();
		
		try {
			PropertyUtils.copyProperties(perfil, (PlcBaseUserProfileDTO) userProfileDTO);
		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException(e);
		}
		return perfil;
	}

	/**
	 * Se a aplicação estiver configurada para utilizar o jSecurity, recupera o
	 * cadastro básico do usuário.
	 * 
	 * @since jCompany 3.x
	 * @param context
	 * @return
	 * 
	 */
	@PlcTransactional(commit=false)
	@Override
	public Serializable recuperaUsuarioJSecurity(PlcBaseContextVO context)  {

		Class classeBO = getClassePlcJSecurityBO();
		Object jsBO = iocModelUtil.getRepository(classeBO);

		Serializable usuario = (Serializable)  reflectionUtil.executeMethod(jsBO, "recuperaUsuario", new Object[] { context }, new Class[] { PlcBaseContextVO.class });	

	    return usuario;
	}	

	
	/**
	 * Se a aplicação estiver configurada para utilizar o jSecurity, recupera o cadastro básico do usuário.
	 * 
	 * @since jCompany 3.x
	 * @param context
	 * @return
	 * 
	 */
	@PlcTransactional(commit=false)
	@Override
	public Serializable recuperaUsuarioCompletoJSecurity(PlcBaseContextVO context, String login, String[] siglaAplicacoes)  {

		Class classeBO = getClassePlcJSecurityBO();
		Object jsBO = iocModelUtil.getRepository(classeBO);

		Serializable usuario = (Serializable)  reflectionUtil.executeMethod(jsBO, "recuperaUsuarioCompleto", new Object[] { context, login, siglaAplicacoes }, new Class[] { PlcBaseContextVO.class, String.class, java.lang.String[].class });	

	    return usuario;
	}	

	
	/**
	 * Recupera a classe BO do jSecurity, caso esteja configurado.
	 * 
	 * @return
	 * 
	 */
	private static Class getClassePlcJSecurityBO()  {
    	if (classePlcJSecurityBO == null) {
			classePlcJSecurityBO = PlcCDIUtil.getInstance().getBean(IPlcJsecurityBO.class).getBeanClass();
		}
    	return classePlcJSecurityBO;
	}

}
