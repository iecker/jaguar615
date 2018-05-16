/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.OneToMany;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.bindingtype.PlcInsertAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcInsertBefore;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;

/**
 * Serviços de criação de agregações de entidades
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseCreateRepository extends PlcBaseParentRepository {

	@Inject @QPlcSpecific
	protected PlcBaseAuditingRepository baseAuditoria;
	
	/**
	 * Salva uma nova agregação de entidades utilizando-se do serviço de persistencia (DAO)
	 * 
	 * @return Retorna a Entidade Raiz com o object-Id gerado, se for automático
	 * 
	 */
	public Object insert(PlcBaseContextVO context, Object entidade) throws PlcException, Exception {

		PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidade.getClass());

		if (baseCRUDSRepository.persistenceBefore(context,dao,entidade, PlcOperationType.CREATE) && insertBefore(context,dao,entidade)) {

			//Verifica se existe alguma propriedade duplicada, com base na anotação 
			dao.noSimilarExecute(context, entidade, "I");
			
			retiraIdEntidades(entidade);
	
			baseCRUDSRepository.fileUploadAfter(context,dao,entidade, PlcOperationType.CREATE);
	
			insertBuildIdentifierApi(context,dao,entidade);
	
			// incluiOneToOne(entidade);
			baseAuditoria.registrySimpleAudit(context, entidade,PlcConstants.MODOS.MODO_INCLUSAO);
	
			if (context == null || (context.getPkClass() == null || (context != null
					&& context.getPkClass() != null && context.getPkClass().trim().equals("")))) {
				Long idGerado = dao.insert(context, entidade);
				PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);
				entidadeInstance.setId(idGerado);
			} else
				dao.insert(context, entidade);
			
			log.debug("Passou no insert padrao");
			
		}
	
		insertAfter(context,dao,entidade);

		baseCRUDSRepository.persistenceAfter(context,dao,entidade, PlcOperationType.CREATE);

		return entidade;
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After"
 	 * são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).<p>
	 * Método disparado antes da gravação de objetos em modo de inclusão. 
	 * Pode ser utilizado alternativamente ao método "persistenceBefore" para lógicas que devem ser disparadas somente na inclusão.
	 * @param entidade Entidade raiz da agregação a ser persistida
	 */
	@SuppressWarnings("serial")
	protected boolean insertBefore(PlcBaseContextVO context, Object dao, Object entidade) {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context,new AnnotationLiteral<PlcInsertBefore>() {});
		return context.isDefaultProcessFlow();
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" 
	 * são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).<p>
	 * 
	 *        Método disparado após a gravação de objetos em modo de inclusão.
	 *        Pode ser utilizado alternativamente ao método "persistenceBefore",
	 *        para lógicas que devem ser disparadas somente na inclusão.
	 */
	protected void insertAfter(PlcBaseContextVO context, Object dao, Object entidade) {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context,new AnnotationLiteral<PlcInsertAfter>(){});
	}

	/**
	 * Método para que o descendente monte chaves compostas. Se utilizado, o descendente deve instanciar a classe da
	 *  chave e montar os valores do Entidade nesta classe.
	 */
	protected void insertBuildIdentifierApi(PlcBaseContextVO context,Object dao, Object entidade) {
	}

	/**
	 * Retira id das entidades para quando houver erro de validação de banco
	 */
	protected void retiraIdEntidades(Object entidade) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);

		String idAux = entidadeInstance.getIdAux();

		if (StringUtils.isNotBlank(idAux)) {

			entidadeInstance.setIdAux(null);

			Field[] atributos = reflectionUtil.findAllFieldsHierarchically(entidade.getClass(), false, true);

			//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
			
			for (Field filho : atributos) {

				OneToMany oneToMany = filho.getAnnotation(OneToMany.class);

				if (oneToMany != null && propertyUtilsBean.isReadable(entidade, filho.getName())) {

					Object detalhe = propertyUtilsBean.getProperty(entidade,filho.getName());

					if (detalhe instanceof Collection) {

						Collection listaDetalhe = (Collection) detalhe;

						for (Object umDetalhe : listaDetalhe) {
							if (umDetalhe != null && metamodelUtil.isEntityClass(umDetalhe.getClass()))
								retiraIdEntidades(umDetalhe);
						}

					} else if (detalhe != null && metamodelUtil.isEntityClass(detalhe.getClass()))
						retiraIdEntidades(detalhe);

				}

			}

		}
	}

}
