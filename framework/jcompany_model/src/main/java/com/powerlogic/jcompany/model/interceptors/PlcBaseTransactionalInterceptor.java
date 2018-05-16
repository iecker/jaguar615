/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model.interceptors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;
import com.powerlogic.jcompany.model.util.PlcDBFactoryUtil;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;


public abstract class PlcBaseTransactionalInterceptor {

	private static final long serialVersionUID = -8191155452009691972L;

	protected static final Logger log = Logger.getLogger(PlcBaseTransactionalInterceptor.class.getCanonicalName());

	protected static final Logger logProfiling = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_QA_PROFILING);
	
	protected static final Logger logModelo = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_MODELO);
	
	public PlcBaseTransactionalInterceptor(){
		
	}
	
	@AroundInvoke
	public Object intercept(InvocationContext ic) throws Exception {


		Object retValFromSuper = null;

		try {
			interceptBefore(ic.getTarget(), ic.getMethod(), ic.getParameters());

			retValFromSuper = ic.proceed();

			interceptAfter(ic.getTarget(), ic.getMethod(), ic.getParameters());

		} catch (PlcException ePlc) {
			
			interceptException(ic.getTarget(), ic.getMethod(), ic.getParameters());
			 
			throw ePlc;
			
		} catch (Throwable e) {
			if (logProfiling.isDebugEnabled()) {
				logProfiling.debug( "++++ Excecao Externa Disparada: "+e.toString());
			}
			interceptException(ic.getTarget(), ic.getMethod(), ic.getParameters());
			throw new PlcException("PlcBaseTransactionalInterceptor", "intercept", e, log, "");
		}

		return retValFromSuper;
	}


	protected abstract void interceptBefore(Object obj, Method method, Object[] args)  ;


	/**
	 * Encerra Transação Declarativa via AOP. Se tiver anotação em qualquer método de persistência ou 
	 * se for método do Façade padrão, descendente de PlcBaseFacadeImpl, entao executa o fechamento.
	 * @since jCompany 3.0
	 */

	protected abstract void interceptAfter(Object obj, Method method, Object[] args) ;


	/**
	 * @since jCompany 3.0 
	 * Template Method para interceptadores AOP. Em caso de excecao no método principal
	 */
	protected void interceptException(Object obj, Method method, Object[] args)  {

		PlcIocModelUtil iocModelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcIocModelUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcAnnotationPersistenceUtil  anotacaoPersistenceUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcAnnotationPersistenceUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcDBFactoryUtil dbFactoryUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDBFactoryUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcTransactional transactional = anotacaoPersistenceUtil.getTransactional(method);

		PlcBaseContextVO context = (PlcBaseContextVO)args[0];

		String fabrica =  null;
		if (transactional!=null) {
			fabrica = transactional.fabrica();
		}
		
		if (StringUtils.isEmpty(fabrica) || fabrica.equals("default")){
			// Troca fábrica se o contexto não usa a padrão.
			fabrica = dbFactoryUtil.getDBFactoryName(fabrica, context!=null?context.getEntityClassPlc():null, context);
		}
		
		if (StringUtils.isNotEmpty(fabrica)) {
			try {
				// Só vai fazer rollback aqui se o container não estiver gerenciando

				PlcBaseDAO dao = iocModelUtil.getPersistenceObject(null);
				String[] fabricas = new String[]{fabrica};

				rollback(fabricas, dao);

				// Emite um logging de fechamento, o que permite a auditoria de unidade logica de transacao (ULT) corretamente.
				// if (logProfiling.isDebugEnabled() && helperProfiling.getNivel()>=1)
				// helperProfiling.registraFimTransacao("rollback");

			} catch (Exception e) {
				log.fatal( "Erro ao tentar fazer rollback " + e, e);
			}
		}
	}


	protected void rollback(String[] fabricas, PlcBaseDAO dao)  {

		for (String fabrica : fabricas) {
			dao.rollback(fabrica);
		}
	}

	protected void commit(String[] fabricas, PlcBaseDAO dao)  {

		for (String fabrica : fabricas) {
			dao.commit(fabrica);			
		}

	}

	/**
	 * Método necessario para corrigir um 'bug' do Hibernate.
	 * É para necessário voltar a versao (de todo o grafo de objetos) do campo que 
	 * contenha a annotation @version manualmente, pois a hibernate faz isso, caso 
	 * ocorra um erro na validação.
	 * @since jCompany 5.5
	 * @param args contendo entidadeAtual e entidadeAnterior a persistencia
	 * 
	 */
	protected void trataExcecaoHibernate(Object[] args) {

		if(args.length ==2 ){

			Object entidadeAtual 		= args[1];
			if(entidadeAtual != null){
				atualizaVersao(entidadeAtual);
			}

		}

	}

	/**
	 * Método necessario para corrigir um 'bug' do Hibernate.
	 * É para necessário voltar a versao (de todo o grafo de objetos) do campo que 
	 * contenha a annotation @version manualmente, pois a hibernate faz isso, caso 
	 * ocorra um erro na validação.
	 * @since jCompany 5.5
	 * @param entidadeAtual
	 * @param entidadeAnterior
	 */
	private void atualizaVersao(Object entidadeAtual){

		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);

		Field[] camposEntidadeAtual =  reflectionUtil.findAllFieldsHierarchically(entidadeAtual.getClass());

		for(Field campo : camposEntidadeAtual){
			if(campo.isAnnotationPresent(Version.class)){
				Long numeroVersao;
				try {
					if (reflectionUtil.getEntityPropertyPlc(entidadeAtual, campo.getName()).getClass().isAssignableFrom(Integer.class))
						numeroVersao = Long.valueOf(((Integer) reflectionUtil.getEntityPropertyPlc(entidadeAtual, campo.getName())).toString());
					else 
						numeroVersao = (Long) reflectionUtil.getEntityPropertyPlc(entidadeAtual, campo.getName());
					numeroVersao--;
					reflectionUtil.setEntityPropertyPlc(entidadeAtual, campo.getName(), numeroVersao.intValue());
				} catch (PlcException e) {
					log.error( e.getLocalizedMessage());
				}
			} else {
				if(campo.isAnnotationPresent(OneToMany.class)){
					try {
						Collection colecao 			= (Collection) reflectionUtil.getEntityPropertyPlc(entidadeAtual, campo.getName());
						if(colecao != null) {
							for (Object novaEntidadeAtual : colecao) {
								atualizaVersao(novaEntidadeAtual);
							}
						}
					} catch (PlcException e) {
						log.error( e.getLocalizedMessage());
					}
				}
			}
		}
	}


}	


