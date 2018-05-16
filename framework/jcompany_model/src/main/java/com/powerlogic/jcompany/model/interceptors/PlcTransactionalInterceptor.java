/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model.interceptors;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;
import com.powerlogic.jcompany.model.util.PlcDBFactoryUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;

@PlcTransactional
@Interceptor
public class PlcTransactionalInterceptor extends PlcBaseTransactionalInterceptor implements Serializable{

	private static final long serialVersionUID = 6689134558410189179L;

	@Override
	protected void interceptBefore(Object obj, Method method, Object[] args)  {

	}

	/**
	 * Encerra Transação Declarativa via Interceptor. Se tiver anotação em qualquer método de persistência ou 
	 * se for método do Façade padrão, descendente de PlcBaseFacadeImpl, entao executa o fechamento.
	 * @since jCompany 5.6
	 */
	@Override
	protected void interceptAfter(Object obj, Method method, Object[] args)  {

		PlcIocModelUtil iocModelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcIocModelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcAnnotationPersistenceUtil  anotacaoPersistenceUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcAnnotationPersistenceUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcDBFactoryUtil dbFactoryUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDBFactoryUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcTransactional transactional = anotacaoPersistenceUtil.getTransactional(method);
		
		String fabrica =  null;
		String[] fabricas = null;
		PlcBaseDAO dao = null;
		
		// Só vai fazer commit aqui se o container não estiver gerenciando
		PlcBaseContextVO context = null;
		if (ArrayUtils.isNotEmpty(args)) {
			context = (PlcBaseContextVO)args[0];
		}

		if (transactional!=null) {
			fabrica = transactional.fabrica();
		}
		
		if (StringUtils.isEmpty(fabrica) || fabrica.equals("default")){
			// Troca fábrica se o contexto não usa a padrão.
			fabrica = dbFactoryUtil.getDBFactoryName(fabrica, context!=null?context.getEntityClassPlc():null, context);
		}
		
		if (StringUtils.isNotEmpty(fabrica)) {

			// Importante: Se for descendente do Facade, faz commit de qualquer forma.
			try {

				dao = iocModelUtil.getPersistenceObject(context == null || context.getEntityClassPlc() == null || !metamodelUtil.isEntityClass(context.getEntityClassPlc()) ? null : context.getEntityClassPlc());

				boolean commit = transactional.commit();
				
				// Pode ter sido chamado de um método da versão 2.7.4 
				if (fabrica !=null) {
					fabricas = new String[]{fabrica};
					if (commit)
						commit(fabricas, dao);
					else
						rollback(fabricas, dao);
				}
				// Emite um logging de fechamento, o que permite a auditoria de unidade logica de transacao (ULT) corretamente.
				// if (logProfiling.isDebugEnabled() && helperProfiling.getNivel()>=1)
				// helperProfiling.registraFimTransacao("commit");

			}catch(PlcException ePlc){
				throw ePlc;	
			} catch (Exception e) {
				trataExcecaoHibernate(args);
				throw new PlcException("PlcTransactionalInterceptor", "interceptAfter", e, log, "");
			}

		}

	}

	/**
	 * Sobrescrito somente para funcionar no Openwebbeans
	 */
	@Override
	@AroundInvoke
	public Object intercept(InvocationContext ic) throws Exception {
		return super.intercept(ic);
	}

}	


