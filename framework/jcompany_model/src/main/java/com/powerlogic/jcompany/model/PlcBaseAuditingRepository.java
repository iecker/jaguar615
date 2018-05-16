/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.OneToMany;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.collection.internal.AbstractPersistentCollection;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.model.bindingtype.PlcRegistrySimpleAuditBefore;

/**
 * Registra auditoria pauta mínima (simples) em agregações de entidades
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseAuditingRepository extends PlcBaseParentRepository {
	
	/**
	 * Registra Auditoria Simples ( Usuário e Data de Alteração/Criação) para o Entidade Raiz, Detalhes e Subdetalhes de uma agregação
	 * @param entidade Classe para a qual será registrada a auditoria
	 */
	protected void registrySimpleAudit(PlcBaseContextVO context, Object entidade, String modoAuditoria)   {

		if (log.isDebugEnabled())
			log.debug( "########## Entrou em registraAuditoriaInclusao para:  " + entidade.getClass().getSimpleName());

		try {

			if (registrySimpleAuditBefore(context,entidade, modoAuditoria) && 
					entidade != null && metamodelUtil.isEntityClass(entidade.getClass())){

				//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
				PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
				
				Date dataAtual = new Date();

				PlcBaseUserProfileVO baseUsuarioPerfilVO = null;
				if (context != null) {
					baseUsuarioPerfilVO = context.getUserProfile();
				}

				if (baseUsuarioPerfilVO == null || baseUsuarioPerfilVO.getLogin() == null){
					if (baseUsuarioPerfilVO == null)
						baseUsuarioPerfilVO = new PlcBaseUserProfileVO();
					baseUsuarioPerfilVO.setLogin("Anônimo");
				}

				// Registra Auditoria para o VO Principal
				if (propertyUtilsBean.isReadable(entidade, PlcConstants.VO.DATA_ULT_ALTERACAO))
					propertyUtilsBean.setProperty(entidade,PlcConstants.VO.DATA_ULT_ALTERACAO,dataAtual);

				if (propertyUtilsBean.isReadable(entidade, PlcConstants.VO.USUARIO_ULT_ALTERACAO))
					propertyUtilsBean.setProperty(entidade,PlcConstants.VO.USUARIO_ULT_ALTERACAO,baseUsuarioPerfilVO.getLogin());

				//Se for inclusão registra usuário e data de criação
				if (PlcConstants.MODOS.MODO_INCLUSAO.equals(modoAuditoria)){

					if (propertyUtilsBean.isReadable(entidade, PlcConstants.VO.DATA_CRIACAO))
						propertyUtilsBean.setProperty(entidade,PlcConstants.VO.DATA_CRIACAO,dataAtual);

					if (propertyUtilsBean.isReadable(entidade, PlcConstants.VO.USUARIO_CRIACAO))
						propertyUtilsBean.setProperty(entidade,PlcConstants.VO.USUARIO_CRIACAO,baseUsuarioPerfilVO.getLogin());
				}

				// se for Mestre detalhe ou Mantem Detalhe, registra para os Detalhes e ou subdetalhes
				if ( context != null 
						&& (FormPattern.Mdt.name().equals(context.getFormPattern()) ||
								FormPattern.Mad.name().equals(context.getFormPattern()) || 
								FormPattern.Mds.name().equals(context.getFormPattern()))){

					//  Recuperando os fiels, se a entidade tiver MappedSuperClass recupera da superClasse
					Field[] fields = reflectionUtil.findAllFieldsHierarchically(entidade.getClass(), false, false);

					// Para os fields OneToMany ( Detalhes e SubDetalhes) chama o registraAuditoriaSimples Novamente.
					for (Field f : fields) {
						if (f.isAnnotationPresent(OneToMany.class)) {
							try {
								Collection detalhes = (Collection) propertyUtilsBean.getProperty(entidade, f.getName());
								if (detalhes != null) {
									if (!(detalhes instanceof AbstractPersistentCollection) || ((AbstractPersistentCollection) detalhes).wasInitialized()) {
										for(Object detalhe : (Collection<Object>)detalhes){
											
											// Registra a auditoria para o detalhe.
											if(propertyUtilsBean.getProperty(detalhe, "id") == null){												
												registrySimpleAudit(context, detalhe, PlcConstants.MODOS.MODO_INCLUSAO);
											}else{
												registrySimpleAudit(context, detalhe, PlcConstants.MODOS.MODO_EDICAO);
											}
										}
									}
								}
							} catch (Exception e) {
								log.warn( "registraAuditoriaSimples - Não é possível ler proriedade " +
										"'"+f.getName()+"' da entidade '"+entidade.getClass().getName()+"': "+e.getMessage());
							}

						}    
					}

				}
			}
			
			//Templated Method Apos
			entidade = registrySimpleAuditAfter(context,entidade, modoAuditoria);

			if (log.isDebugEnabled())
				log.debug( "########## Registrou auditoria para:  " + entidade.getClass().getSimpleName());

		}catch(PlcException ePlc){
			throw ePlc;
		} catch (Exception e) {
			throw new PlcException("PlcBaseAuditingRepository", "registrySimpleAudit", e, log, "");
		}
	}

	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */	
	protected boolean registrySimpleAuditBefore(PlcBaseContextVO context,Object entidade, String modoAuditoria)   {
		context.setDefaultProcessFlow(true);
		PlcCDIUtil.getInstance().fireEvent(entidade,new AnnotationLiteral<PlcRegistrySimpleAuditBefore>() {});
		return context.isDefaultProcessFlow(); 
	}
	/**
	 * Design Pattern: Template Method ou Observer. Os métodos com sufixo "Before" ou "After" são destinados a especializações nos
	 * descendentes via Template Methods ou em Extensions via Eventos CDI (Observer).
	 */	
	protected Object registrySimpleAuditAfter( PlcBaseContextVO context, Object entidade, String modoAuditoria)   {
		return entidade;
	}	

	
}
