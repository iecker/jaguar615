/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.model.bindingtype.PlcUpdateAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcUpdateBefore;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;

/**
 * Atualiza (Altera) uma agregação de entidades, podendo englobar exclusão, alteração ou inclusão de itens (detalhes) da agregação.
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseUpdateRepository extends PlcBaseParentRepository {

	@Inject @QPlcSpecific
	protected PlcBaseDeleteRepository baseDelete;
	
	@Inject @QPlcSpecific
	protected PlcBaseAuditingRepository baseAuditoria;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * Altera a agregação de entidades.<p>
	 *
	 * Segue o seguinte roteiro:<p>
	 * - trata upload/exclusao de arquivo: se Entidade é de arquivo anexado, então faz upload de arquivo apropriadamente; ou o exclui se estiver marcado
	 *  Importante: Este método chamado pode chamar o método altera novamente, em execução recursiva, para alteração do Arquivo Anexado<p>
	 * - testaNaoDeveExistir(vo,"A"): Envia a relação de queries de integridade naoDeveExistir (declaradas em anotações), antes de efetuar a alteração,<p>
	 * - atualizaArquivo(voArq,idArquivoAux,true): Atualiza arquivo anexado (pode gerar inclusão de arquivo ou exclusão e nova inclusão, já que arquivos anexados não são alterados verdadeiramente)<p>
	 *
	 * @param Entity Objeto a ser alterado
	 */
	public Object update(PlcBaseContextVO context, Object entity)  {

		Map detalhesAux = null;
		
		try {
			
			if (entity instanceof IPlcFile) {

				if (log.isDebugEnabled()){
					log.debug( "Entidade "+entity.toString()+" eh instancia de IPlcFile");
				}

				updateUploadFile(context, entity, (IPlcFile) entity);

			} else {

				if(context != null) {
					
					PlcBaseDAO dao = iocModelUtil.getPersistenceObject(entity.getClass());
					
					if (baseCRUDSRepository.persistenceBefore(context,dao,entity, PlcOperationType.UPDATE) && updateBefore(dao,context,entity)) {
					
						if (context.getDeleteModeAux().equals(PlcConstants.EXCLUSAO.MODO_LOGICA) && "I".equals(propertyUtilsBean.getProperty(entity,VO.SIT_HISTORICO_PLC)) ) {					
							updateForLogicalExclusionBefore(entity);
						}
						
						// Não executa o naoDeveExistir para a Exclusão Lógica, onde alteração é na verdade uma "exclusão". 
						if (context.getDeleteModeAux().toUpperCase().startsWith("F") || (context.getDeleteModeAux().toUpperCase().startsWith("L") && "A".equals(propertyUtilsBean.getProperty(entity, VO.SIT_HISTORICO_PLC)))) {
							dao.noSimilarExecute(context, entity,"A");
						}
						
						verifyUpdateFiles(context, entity);
						
						baseAuditoria.registrySimpleAudit(context, entity, PlcConstants.MODOS.MODO_EDICAO); 
						
						if (context.getDetailNames() != null) {
							detalhesAux = rememberDetailsTemp(context, entity); 
							updateDeleteMarkedDetails(context, entity);
						}
						
						baseDelete.deleteMultipleFiles (context, entity);
						
						log.debug( "Antes de chamar antesPersistenciaAposArquivoAnexado!");
						
						baseCRUDSRepository.fileUploadAfter(context,dao,entity, PlcOperationType.UPDATE);
						
						dao.update(context, entity);
					}
					
					updateAfter(context,dao,entity);
					
					baseCRUDSRepository.persistenceAfter( context,dao,entity, PlcOperationType.UPDATE);
					
					if (context.getDeleteModeAux().equals(PlcConstants.EXCLUSAO.MODO_LOGICA) && 
							"I".equals(propertyUtilsBean.getProperty(entity,VO.SIT_HISTORICO_PLC))) {
						updateLogicalDeleteAfter(context,dao,entity);
					}
					
				}
				
			}

			return entity;

		} catch (PlcException plcE) { 
			retrieveDetailsTemp(context, entity, detalhesAux);
			throw plcE;
		} catch (Exception e) { 
			throw  new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_UPDATE, new Object[] {"update", e},e,log);
		}
	}

	private void verifyUpdateFiles(PlcBaseContextVO context, Object entity) {
		
		Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entity.getClass(), PlcFileAttach.class);
		
		for (Field field : camposAnotados) {
			
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
			
			try {
				
				if (fileAttach.multiple()) {
					List<IPlcFile> lista = (List) propertyUtilsBean.getProperty(entity, field.getName());
					if (lista != null) {
						for (IPlcFile file : lista) {
							if(file.getBinaryContent() != null && file.getIndExcPlc() != null) {
								verifyUpdateFile(context, entity, file, field);
							}
						}
					}
				}  else {
					IPlcFile file = (IPlcFile) propertyUtilsBean.getProperty(entity, field.getName());
					if(file != null) {
						if(file.getBinaryContent() != null && file.getIndExcPlc() != null) {
							verifyUpdateFile(context, entity, file, field);
						}
					}
				}
			} catch (Exception e) {
				new PlcException(e);
			}
			
		}
	}

	/**
	 * Verifica operação sobre arquivo anexado, disparando exclusão, alteração e inclusão
	 * @param entidade Entidade raiz
	 * @param plcArq Referencia ao arquivo
	 */
	protected void updateUploadFile( PlcBaseContextVO context, Object entidade, IPlcFile plcArq)  {

		PlcEntityInstance entidadeAtualInstance = metamodelUtil.createEntityInstance(entidade);
		
		if (plcArq.getIndExcPlc() != null &&(  entidadeAtualInstance.getIndExcPlc().equals("S") || 
				entidadeAtualInstance.getIndExcPlc().equals("S") ) ) {
			log.debug( "Vai excluir");
			plcArq = updateFile(context, plcArq,plcArq.getId(),true);
		} else {
			plcArq = updateFile(context, plcArq,plcArq.getId(),false);
		}

	}
	
	/**
	 * Método disparado antes da gravação de objetos em modo de alteração. Pode ser
	 * utilizado alternativamente ao método "antesPersistencia", para lógicas que devem ser
	 * disparadas somente na alteração.
     * @param dao Referência ao DAO
	 * @param entity Entidade raiz da agregação
	 * @param context Objeto que encapsula dados de estado para uso na camada modelo, que é Stateless
     * @param entidadeAnt Value Object antes das alterações
     */
    protected boolean updateBefore(Object dao, PlcBaseContextVO context, Object entity)  {  
    	
    	//PlcCDIUtil.getInstance().fireEvent(entidadeAtual, new AnnotationLiteral<PlcUpdateBefore>(){});
    	//PlcCDIUtil.getInstance().fireEvent(this, entidadeAtual);
    	// TODO Homologar. O context encapsula o estado das requisições na camada modelo, uma vez que as classes são stateless 
    	// (escopo de aplicação). Ele contém inclusive referência à entidade
    	context.setDefaultProcessFlow(true);
   		context.setEntityForExtension(entity);
    	PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcUpdateBefore>(){});
    	return context.isDefaultProcessFlow();
    }
   
	/**
	 * Método disparado após a gravação de objetos em modo de alteração. Pode ser
	 * utilizado alternativamente ao método "persistenceAfter", para lógicas que devem ser
	 * disparadas somente na alteração.
	 * @param entity Entidade com alterações
	 * @param context Parametros da requisicao (estado) 
	 * @param dao Classe de DAO corrente
	 */
   	protected void updateAfter(PlcBaseContextVO context, PlcBaseDAO dao, Object entity)  {
   		
   		// TODO Homologar e remover
   		//	PlcCDIUtil.getInstance().fireEvent(entidadeAtual, new AnnotationLiteral<PlcUpdateAfter>(){});
   		//	PlcCDIUtil.getInstance().fireEvent(this, entidadeAtual);
   		context.setEntityForExtension(entity);
   		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcUpdateAfter>(){});
   		
   	}

	/**
	 * Método para lógicas antes da alteração para exclusão lógica.
	 * @param entidadeAtual  com sitHistoricoPlc='I'
	 * @param entidadeAnt com sitHistoricoPlc='A'
	 */
	protected void updateForLogicalExclusionBefore( Object entidadeAtual)  {	}

	/**
	 * Altera arquivo em upload em meio a lógica de alteração
	 * @param entidade Value Object Atual
	 * @param entidadeArq Value Object de arquivo.
	 */
	protected void verifyUpdateFile(PlcBaseContextVO context, Object entidade, IPlcFile file, Field field)  {

		try {

			PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);
			
			String nomeArquivo = field.getName();
			
			if (file != null) {
				
				if ((entidadeInstance.getIndExcPlc() != null && entidadeInstance.getIndExcPlc().equals("S")) || (file.getIndExcPlc().equals("S") || file.getIndExcPlc().equals("true")) ){

					if (log.isDebugEnabled())
						log.debug( "Vai excluir arquivo com id "+ file.getId());
					Long idArquivoAux = file.getId();

					if (List.class.isAssignableFrom(field.getType()))  {
						List lista  = (List) propertyUtilsBean.getProperty(entidade, field.getName());
						lista.remove(file);
					} else  {
						propertyUtilsBean.setProperty(entidade, nomeArquivo, null);
					}
					iocModelUtil.getPersistenceObject(entidade.getClass()).update(context, entidade);
					file = updateFile(context,file,idArquivoAux,true);
						

				} else if ( (file.getLength() != null) && (!file.getIndExcPlc().equals("S") && !file.getIndExcPlc().equals("true"))){

					if ((file.getUrl()==null) || (file.getUrl()!=null && file.getUrl().equals(""))) {
						file.setUrl(file.getNome());
					}
					
					file = updateFile(context,file,file.getId(),false);
					
					if (file != null) {
						// transiente
						propertyUtilsBean.setProperty(entidade, nomeArquivo, file);
					}
				}
			}
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseUpdateRepository", "alteraTrataArquivo", e, log, "");
		}

	}

	/**
	 * Método auxiliar que exclui efetivamente registros de detalhes.<p>
	 * Este método também faz alteração em cascata para arquivos anexados
	 * @param entidade Referência à entidade raiz (mestre) da agregação, cujas composiçoes oneToMany (detalhes) serão excluídas.
	 */
	protected void updateDeleteMarkedDetails(PlcBaseContextVO context, Object entidade)  {

		Object mestreEntidade = entidade;
		
		PlcEntityInstance mestreEntidadeInstance = metamodelUtil.createEntityInstance(mestreEntidade);

		try {

			if (context.getEntityClassPlc().isAssignableFrom(entidade.getClass())){
			
				List l = context.getDetailNames();
				List listaDetalhesPlc = context.getDetailNamesProps();
				List lDet = null;

				
				int cont = 0;

				// Cada interacao varre um tipo de detalhe
				while (cont<l.size()) {

					String nomeDetalhePlc = (String)listaDetalhesPlc.get(cont);					

					cont++;
					Set sDet = null;
				
					if (Set.class.isAssignableFrom(propertyUtilsBean.getPropertyType(mestreEntidade,nomeDetalhePlc)))	{
						sDet = (Set) propertyUtilsBean.getProperty(mestreEntidade,nomeDetalhePlc);					

					} else {
						lDet = (List)propertyUtilsBean.getProperty(mestreEntidade,nomeDetalhePlc);				
					}

					Iterator j = null;
					List lAux = new ArrayList();;

					if (sDet != null) {
						j = sDet.iterator();
					} else if (lDet != null) {
						j = lDet.iterator();
					}

					if (j==null)
						continue;

					String nomePropSubdetalhe = context.getSubDetailPropNameCollection();

					while (j.hasNext()) {

						Object detEntidade = j.next();

						PlcEntityInstance detEntidadeInstance = metamodelUtil.createEntityInstance(detEntidade);

						if (detEntidadeInstance.getIndExcPlc() != null && (detEntidadeInstance.getIndExcPlc().equals("S") || detEntidadeInstance.getIndExcPlc().equals("true"))) {

							lAux.add(detEntidade);

						} else if (nomePropSubdetalhe != null && !nomePropSubdetalhe.equals("") &&
								propertyUtilsBean.isReadable(detEntidade,nomePropSubdetalhe) &&
								propertyUtilsBean.getProperty(detEntidade,nomePropSubdetalhe) != null &&
								((Collection)propertyUtilsBean.getProperty(detEntidade,nomePropSubdetalhe)).size() > 0) {
							// Verifica sub-detalhe
							updateMarkedSubDetail(context, (Collection)propertyUtilsBean.getProperty(detEntidade,nomePropSubdetalhe));

						}

					}

					// Exclui fora do loop, para evitar problema de concorrencia
					Iterator z = lAux.iterator();
					while (z.hasNext()) {
						Object detEntidadeExc = z.next();
						PlcEntityInstance detEntidadeExcInstance = metamodelUtil.createEntityInstance(detEntidadeExc);
						if (sDet != null) {
							Object entidadeDet=null;

							if (log.isDebugEnabled()) {
								log.debug( "hashCode entidade "+detEntidadeExcInstance.hashCode());
								Iterator kk = sDet.iterator();
								while (kk.hasNext()) {
									entidadeDet = kk.next();
									PlcEntityInstance entidadeDetInstance = metamodelUtil.createEntityInstance(entidadeDet);
									log.debug( "id="+entidadeDetInstance.getId()+"equals: "+entidadeDetInstance.equals(detEntidadeExc)+" hashCode deste="+entidadeDetInstance.hashCode());
								}
							}

							Iterator iteratorSDet = sDet.iterator();
							while(iteratorSDet.hasNext()){
								Object entityDet = iteratorSDet.next();
								PlcEntityInstance entityDetInstance = metamodelUtil.createEntityInstance(entityDet);
								if (detEntidadeExcInstance.getIdNaturalDinamico()!= null){
									if (detEntidadeExcInstance.getIdNaturalDinamico().equals(entityDetInstance.getIdNaturalDinamico()))
										iteratorSDet.remove();
								}else{
									if (detEntidadeExcInstance.getId().equals(entityDetInstance.getId()))
										iteratorSDet.remove();
								}
							}


						} else
							lDet.remove(detEntidadeExc);

						// Se nao for manyToMany, entao detalhes marcados devem ser explicitamente excluidos
						// Assume uma coleção no detalhe apontando para o Mestre como referencia ManyTo-Many
						if (!updateMarkedDetailsForManyToMany(detEntidadeExc,mestreEntidadeInstance.getNomePropriedadePlc())) {
							PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(detEntidadeExc.getClass());
							if (context.getDeleteModeAux().equals("LOGICAL") && propertyUtilsBean.getProperty(detEntidadeExc, VO.SIT_HISTORICO_PLC).equals(VO.SIT_INATIVO)) {
								baseDAO.update(context,detEntidadeExc);
							} else {
								baseDAO.delete(context,detEntidadeExc, false);
							}	

							
						}
					}
				}
			}

		} catch (PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_HIBERNATE_UPDATE, new Object[] {"alteraExcluiDetalhesMarcados", e},e,log);
		}

	}


	
	/**
	 * Método para lógicas de programação após a alteração para exclusão lógica.
	 * @param entidade com sitHistoricoPlc='I'
	 */
	protected void updateLogicalDeleteAfter(PlcBaseContextVO context, PlcBaseDAO dao, Object entity)  {	
		context.setEntityForExtension(entity);
   		PlcCDIUtil.getInstance().fireEvent(context, new AnnotationLiteral<PlcUpdateAfter>(){});
	}

	/**
	 * Atualiza um arquivo, podendo incluí-lo, alterá-lo ou excluí-lo, em
	 * conformidade com o Value Object IPlcArquivoVO. Esta entidade é agregada à entidade principal,
	 * descendente de PlcEntityInstance, no momento da gravação, mas pode indicar operações
	 * diferentes da Entidade Mestre. Por exemplo: Ao alterar o mestre, o usuário pode excluir o
	 * arquivo (detalhe), ou mesmo incluí-lo ou alterá-lo. Portanto, a cada alteração
	 * no mestre, todas as operações devem ser verificadas por este método.
	 * @param arquivoNovo Value Object contendo informações de operações para o arquivo
	 *  anexado.
	 * @param idArquivoAnterior Chave do Arquivo Anterior, para facilitar lógicas de alteração
	 *        e exclusão.
	 * @param excluir Se for para excluir o arquivo, este flag já vem marcado. Este é o
	 *  caso onde o usuário clicou no checkbox de exclusão.
	 *
	 * @return Retorna o POJO de arquivo com as atualizações
	 */
	protected IPlcFile updateFile(PlcBaseContextVO context, IPlcFile arquivoNovo,Long idArquivoAnterior, boolean excluir)  {

		String oper = "";

		try {

			if (excluir) {
				oper="E";
			} else if (idArquivoAnterior == null) {
				oper="I";
			} else {
				oper="A";
			}

			if (oper.equals("I")) {

				PlcBaseRepository plcBO = iocModelUtil.getRepository(arquivoNovo.getClass());
				arquivoNovo = (IPlcFile) plcBO.insert(context, arquivoNovo);
				log.debug( "Incluiu arquivo");
				return ((IPlcFile) arquivoNovo);

			} else if (oper.equals("A")) {
				return null;
			} else if (oper.equals("E")) {
				log.debug( "Entrou para excluir");
				PlcBaseRepository plcBO = iocModelUtil.getRepository(arquivoNovo.getClass());

				IPlcFile arquivoAnterior = (IPlcFile) (plcBO.edit(context, arquivoNovo.getClass(),idArquivoAnterior)[0]);
				PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(arquivoNovo.getClass());
				baseDAO.delete(context, arquivoAnterior);
				log.debug( "Excluiu arquivo de id="+idArquivoAnterior);
				return ((IPlcFile) arquivoAnterior);

			}

		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_UPDATE, new Object[] {"alteraAtualizaArquivo", e},e,log);
		}

		return arquivoNovo;

	}

	/**
	 * Exclui objetos de sub-detalhe marcados para exclusão
	 * @param subDetailList Lista de objetos de sub-detalhes a ser analisada
	 */
	private void updateMarkedSubDetail(PlcBaseContextVO context, Collection subDetailList) {
		
		try {

			Iterator i = subDetailList.iterator();
			while (i.hasNext()) {
				Object entidadeSub = i.next();
				
				PlcEntityInstance entidadeSubInstance = metamodelUtil.createEntityInstance(entidadeSub);

				if (entidadeSubInstance.getIndExcPlc() != null && entidadeSubInstance.getIndExcPlc().equals("S")) {

					i.remove();
					PlcBaseDAO baseDAO = iocModelUtil.getPersistenceObject(null);
					if(context.getDeleteModeAux().equals("LOGICAL")){
						if (propertyUtilsBean.getProperty(entidadeSub, VO.SIT_HISTORICO_PLC).equals(VO.SIT_INATIVO)) {
							baseDAO.update(context,entidadeSub);
						} else {
							baseDAO.delete(context,entidadeSub, false);
						}
					}else{
						baseDAO.delete(context,entidadeSub, false);
					}

				}
			}
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBaseUpdateRepository", "alteraExcluiSubDetalhesMarcadosHelper", e, log, "");			
		}
	}
	
	/**
	 * Assume uma coleçao na classe de detalhe apontando para a classe Mestre com nome de propriedade
	 * padrão como relacionamento ManyToMany. 
	 * @param Classe de Detalhe
	 * @param nomePropriedadePlc nome da propriedade candidata a ManyToMany
	 * @return true se a propriedade for um List ou Set
	 */
	protected boolean updateMarkedDetailsForManyToMany(Object entidadeDet, String nomePropriedadePlc)  {

		try {

			return Collection.class.isAssignableFrom(propertyUtilsBean.getPropertyType(entidadeDet,nomePropriedadePlc));

		} catch (Exception e) {
			return false;
		}

	}


	/**
	 * Restaura detalhes e subdetalhes que foram salvos temporariamente.
	 * @param entidade
	 * @param detalhesAux
	 */
	public void retrieveDetailsTemp(PlcBaseContextVO context, Object entidade, Map detalhesAux) {
		try{
			List nomeDets = context.getDetailNamesProps(); 
			if (nomeDets != null && nomeDets.size() > 0){
				Iterator i = nomeDets.iterator();
				String nomeDet = "";
				while(i.hasNext()){
					nomeDet = (String) i.next();
					propertyUtilsBean.setProperty(entidade, nomeDet, detalhesAux.get(nomeDet));
				}
			}

		}catch (Exception eDet) {}
	}	
	

	/**
	 * Guarda os detalhes temporariamente para serem recuperados caso ocorra erro de gravação
	 * @param entidade
	 * @return
	 * 
	 */
	public Map rememberDetailsTemp(PlcBaseContextVO context, Object entidade)  {

		Collection novoSetDetalhe = null;
		String nomeProp = "";

		List nomeDets 			 = new ArrayList();
		List listaDetalhes 		 = new ArrayList();
		List detalhesTemp		 = new ArrayList();
		HashMap mapaDetalhes	 = new HashMap<String, Object>();

		if (context.getEntityClassPlc().isAssignableFrom(entidade.getClass())){
			listaDetalhes 		= context.getDetailNames();
			nomeDets 			= context.getDetailNamesProps();

			int cont = 0;
			// Começa iteração dentro dos nomes de detalhes
			Iterator i = nomeDets.iterator();
			while (i.hasNext()) {
				nomeProp = (String) i.next();
				novoSetDetalhe = null; 
				Object novoVODetalhe = null;

				// Monta nome do método apropriado para detalhes
				int pos = nomeProp.indexOf("_Det");
				if (pos >= 0)
					nomeProp = StringUtils.substring(nomeProp, 0, pos);
				try {
					Class tipoPropriedade = propertyUtilsBean.getPropertyType(entidade, nomeProp);
					if(Set.class.isAssignableFrom(tipoPropriedade)) {
						novoSetDetalhe = new HashSet();
					}
					else {
						novoSetDetalhe = new ArrayList();
					}

					// Recupera um conjunto de detalhes específico do voAtual
					Collection detalhes = (Collection) propertyUtilsBean.getSimpleProperty(entidade, nomeProp);
					// Começa iteração dentro dos detalhes recuperados
					if (detalhes != null) {

						Iterator j = detalhes.iterator();
						while (j.hasNext()) {
							novoVODetalhe = new Object();

							Object voDetalhe = j.next();

							novoVODetalhe =  BeanUtils.cloneBean(voDetalhe);

							// Recupera sub detalhes e cria novas instancias para serem guardados
							rememberSubDetailSession(context, novoVODetalhe);

							novoSetDetalhe.add(novoVODetalhe);
						}

					}

				} catch(PlcException plcE){
					throw plcE;
				} catch (Exception e) {
					throw new PlcException("PlcBaseUpdateRepository", "rememberDetailsTemp", e, log, "");
				}

				mapaDetalhes.put(nomeDets.get(cont), novoSetDetalhe );
				cont++;
			}

			return mapaDetalhes;

		}

		return null;
	}

	/**
	 * Recupera sub detalhes e cria novas instancias para serem guardados
	 * @param novoDetalhe Novo objeto de detalhe
	 */
	public void rememberSubDetailSession(PlcBaseContextVO context, Object novoDetalhe) throws Exception {

		if (context.getSubDetailPropNameCollection() != null && propertyUtilsBean.isReadable(novoDetalhe, context.getSubDetailPropNameCollection())){

			Collection novoListSubDetalhe = null;
			Class tipoPropriedade = propertyUtilsBean.getPropertyType(novoDetalhe, context.getSubDetailPropNameCollection());
			if(Set.class.isAssignableFrom(tipoPropriedade)) {
				novoListSubDetalhe = new HashSet();
			}
			else {
				novoListSubDetalhe = new ArrayList();
			}
			Object novoVOSubDetalhe = null;
			Object voSubDetalhe = null;

			// Começa iteração dentro dos subDetalhes
			Collection subDetalhes = (Collection) propertyUtilsBean.getProperty(novoDetalhe,context.getSubDetailPropNameCollection());			

			if(subDetalhes != null && subDetalhes.size() > 0) {
				
				// Começa iteração dentro dos nomes de detalhes
				Iterator k = subDetalhes.iterator();
				while (k.hasNext()) {
					voSubDetalhe = k.next();
					novoVOSubDetalhe = new Object();
					
					// Cria novo vo para sub-detalhe
					novoVOSubDetalhe = BeanUtils.cloneBean(voSubDetalhe);
					novoListSubDetalhe.add(novoVOSubDetalhe);
					
				}
				// Guarda o novo conjunto de sub-detalhes no detalhe atual
				propertyUtilsBean.setProperty(novoDetalhe, context.getSubDetailPropNameCollection(), novoListSubDetalhe);
			}
		}
	}
	
}
