/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.set.ListOrderedSet;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.model.bindingtype.PlcDeleteAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcDeleteBefore;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;

/**
 * Operações de exclusão sobre agregação de entidade
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseDeleteRepository extends PlcBaseParentRepository {

	@Inject @QPlcSpecific
	protected PlcBaseEditRepository baseRetrieve;	
	
	@Inject @QPlcSpecific
	protected PlcBaseAuditingRepository baseAuditoria;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Exclui uma Entidade raiz e sua agregação definida
	 */
	public void delete(PlcBaseContextVO context, Object entidade)  {

		try {

			PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);
			
			PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidade.getClass());
			
			if (!entidadeInstance.isIdentificado()) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_DELETE_KEY_NULL, new Object[]{entidade.toString()}, true);
			}
			
			if (baseCRUDSRepository.persistenceBefore(context,dao,entidade, PlcOperationType.DELETE) && deleteBefore(context,dao,entidade)) {

				PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(entidade.getClass());

				// Recupera a entidade novamente com todo o grafo de objetos
				if (entidadeInstance.getIdNaturalDinamico() != null) {
					entidade = baseDAO.findById(context, entidade.getClass(), entidadeInstance.getIdNaturalDinamico());
				} else if (entidadeInstance.getId() != null) {
					entidade = baseDAO.findById(context, entidade.getClass(), entidadeInstance.getId());
				} else {
					entidade = baseDAO.findById(context, entidade.getClass(), entidadeInstance.getRowId());
				}
				
				baseAuditoria.registrySimpleAudit(context, entidade, PlcConstants.MODOS.MODO_EXCLUSAO);

				baseDAO.delete(context, entidade);

			}

			deleteAfter(dao,context,entidade);

			baseCRUDSRepository.persistenceAfter(context,dao,entidade, PlcOperationType.DELETE);

		} catch (PlcException plcE) { 
			throw plcE;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE, new Object[] {"delete", e},e,log);
		}

	}

	/**
	 * Método disparado antes da exclusão de objeto. Pode ser 
	 * utilizado alternativamente ao método "persistenceBefore", para lógicas que devem ser disparadas somente na exclusão
	 * @param entidade Objeto a ser excluido
	 */
	protected boolean deleteBefore(PlcBaseContextVO context,Object dao, Object entidade)  {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcDeleteBefore>(){});
		return context.isDefaultProcessFlow();
	}

	/**
	 * Método disparado após a exclusão de objeto. Pode ser
	 * utilizado alternativamente ao método "persistenceAfter", para lógicas que devem ser
	 * disparadas somente na exclusão
	 * @param dao referencia ao DAO
	 * @param entidade entidade excluida
	 * @param context estado da requisição
	 */
	protected void deleteAfter(Object dao, PlcBaseContextVO context, Object entidade)  {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcDeleteAfter>(){});
	}


	/**
	 * Exclui múltiplos arquivos anexados.
	 * @param entidade Entidade raiz contendo os arquivos
	 */
	protected void deleteMultipleFiles(PlcBaseContextVO context, Object entidade)  {

		try { 
			String nomeAtributo = null;
			boolean isMultiplo = false;

			Field[] fields = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entidade.getClass(), PlcFileAttach.class);
			for (Field field : fields) {
				
				nomeAtributo = field.getName();
				isMultiplo = field.getAnnotation(PlcFileAttach.class).multiple();

				PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entidade.getClass());
				List<PlcFile> colecaoArquivos = null;

				if (propertyUtilsBean.isReadable(entidade, nomeAtributo)) {
					if (isMultiplo) {
						colecaoArquivos = (List<PlcFile>)propertyUtilsBean.getProperty(entidade, nomeAtributo);
					} else {
						PlcFile plcFile = (PlcFile)propertyUtilsBean.getProperty(entidade, nomeAtributo);
						if (plcFile!=null && plcFile.getIndExcPlc().equals("S")){
							propertyUtilsBean.setProperty(entidade, nomeAtributo, null);
						}
					}
				}

				if (deleteMultipleFilesBefore(dao, context,entidade,colecaoArquivos)) {

					if (colecaoArquivos != null && !colecaoArquivos.isEmpty()){
						PlcFile[] plcFiles = colecaoArquivos.toArray(new PlcFile[]{});
						for (int i = 0; i < plcFiles.length; i++) {							
							PlcFile plcFile = plcFiles[i];
							if (plcFile.getIndExcPlc().equals("S")){
								colecaoArquivos.remove(plcFile);
							}
						}
					}

				}

				deleteMultipleFilesAfter(context,dao,entidade,colecaoArquivos);

			}
		}catch(PlcException ePlc){
			throw ePlc;
		}catch (Exception e) {
			throw  new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_UPDATE,new Object[] {"excluiMultiplosArquivosMarcados", e},e,log);
		}

	}	

	/**
	 * Método disparado antes da exclusão arquivos anexados. 
	 * @param entidade Objeto que contém arquivos a serem excluidos
	 * @param colecaoArquivos Coleção de arquivos
	 */
	protected boolean deleteMultipleFilesBefore(Object dao, PlcBaseContextVO context,Object entidade, List colecaoArquivos)  {
		context.setDefaultProcessFlow(true);
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcDeleteBefore>(){});
		return context.isDefaultProcessFlow();
	}

	/**
	 * Método disparado depois da exclusão arquivos anexados. 
	 * @param entidade Objeto que contém arquivos excluidos
	 * @param colecaoArquivos Coleção de arquivos restantes (após remoção)
	 */
	protected void deleteMultipleFilesAfter(PlcBaseContextVO context,Object dao, Object entidade, List colecaoArquivos)  {
		context.setEntityForExtension(entidade);
		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcDeleteAfter>(){});
	}

	/**
	 * Garante que todos os detalhes por demanda foram recuperados antes da exclusão, para evitar erro de integridade
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveDetailOnDemandBeforeDelete(PlcBaseContextVO context, Object entidade,  Map<String, Class> detalhesPorDemanda){

		if (detalhesPorDemanda != null){
			for (String detalhe : detalhesPorDemanda.keySet()){
				Collection detalhePaginado = baseRetrieve.editList(context, detalhesPorDemanda.get(detalhe), "", 0, Integer.MAX_VALUE);
				try {
					Class fieldDetail = reflectionUtil.findFieldHierarchically(entidade.getClass(), detalhe).getType();
					if(fieldDetail.isAssignableFrom(List.class)) {
						propertyUtilsBean.setProperty(entidade, detalhe, detalhePaginado);
					} else {
						ListOrderedSet detalhePaginadoOrdenado = new ListOrderedSet();
						detalhePaginadoOrdenado.addAll(detalhePaginado);
						propertyUtilsBean.setProperty(entidade, detalhe, detalhePaginadoOrdenado);
					}
				}catch(PlcException ePlc){
					throw ePlc;	
				} catch (Exception e) {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_DELETE, new Object[] {"retrieveDetailOnDemandBeforeDelete", e},e,log);
				}						
			}
		}
	}	
}
