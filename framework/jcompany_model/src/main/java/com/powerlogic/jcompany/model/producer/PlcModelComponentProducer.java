/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/


package com.powerlogic.jcompany.model.producer;
import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.PlcBaseAuditingRepository;
import com.powerlogic.jcompany.model.PlcBaseCRUDSRepository;
import com.powerlogic.jcompany.model.PlcBaseCreateRepository;
import com.powerlogic.jcompany.model.PlcBaseDeleteRepository;
import com.powerlogic.jcompany.model.PlcBaseEditRepository;
import com.powerlogic.jcompany.model.PlcBaseFilterDefRepository;
import com.powerlogic.jcompany.model.PlcBaseParentRepository;
import com.powerlogic.jcompany.model.PlcBaseUpdateRepository;

/**
 * Classe responsável para criação de componentes para weld
 * 
 * @author Moisés Paula, Rogério Baldini
 */
@Named("plcModelComponentProducer")
@ApplicationScoped
public class PlcModelComponentProducer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Produces @QPlcSpecific
	public PlcBaseCreateRepository criaPlcBaseCreateRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseCreateRepository.class);
	}

	
	@Produces @QPlcSpecific
	public PlcBaseDeleteRepository criaPlcBaseDeleteRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseDeleteRepository.class);
	}
	
	@Produces @QPlcSpecific
	public PlcBaseEditRepository criaPlcBaseEditRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseEditRepository.class);
	}
	
	@Produces @QPlcSpecific
	public PlcBaseAuditingRepository criaPlcBaseAuditingRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseAuditingRepository.class);
	}

	@Produces @QPlcSpecific
	public PlcBaseFilterDefRepository criaPlcBaseFilterDefRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseFilterDefRepository.class);
	}

	@Produces @QPlcSpecific
	public PlcBaseUpdateRepository criaPlcBaseUpdateRepository( InjectionPoint injectionPoint  ){
		return criaBaseParentRepository(injectionPoint.getBean().getBeanClass(), PlcBaseUpdateRepository.class);
	}

	
	protected <T> T criaBaseParentRepository(Class beanClass, Class<? extends PlcBaseParentRepository>  clazz ) {
		
		PlcBaseParentRepository plcBaseParentRepository = null; 
		
		// Origem do BaseRepositorio
		PlcAggregationIoC aggregationIoc = (PlcAggregationIoC)beanClass.getAnnotation(PlcAggregationIoC.class);
		if (aggregationIoc!=null){
			try {
				plcBaseParentRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, new DynamicAnnotation(aggregationIoc.clazz().getCanonicalName()));
			} catch (Exception e){
				plcBaseParentRepository = null;
			}	
		}

		// Origem do BaseParentRepositorio
		if (plcBaseParentRepository==null) {
			QPlcSpecific plcSpecific = (QPlcSpecific)beanClass.getAnnotation(QPlcSpecific.class);
			if (plcSpecific!=null){
				try {
					plcBaseParentRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, new DynamicAnnotation(plcSpecific.name()));
				} catch (Exception e){
					plcBaseParentRepository = null;
				}	
			}
		}		
		if (plcBaseParentRepository==null)
			plcBaseParentRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, QPlcDefaultLiteral.INSTANCE);
		
		return (T)plcBaseParentRepository;
		
	}	


	@Produces @QPlcSpecific
	public PlcBaseCRUDSRepository criaPlcBaseCRUDSRepository( InjectionPoint injectionPoint  ){
		return criaBaseCRUDSRepository( injectionPoint.getBean().getBeanClass() , PlcBaseCRUDSRepository.class);
	}
		
	protected <T> T criaBaseCRUDSRepository(Class beanClass,  Class<? extends PlcBaseCRUDSRepository>  clazz ) {
		
		PlcBaseCRUDSRepository plcBaseCRUDSRepository = null; 
		
		// Origem do BaseRepositorio
		PlcAggregationIoC aggregationIoc = (PlcAggregationIoC)beanClass.getAnnotation(PlcAggregationIoC.class);
		if (aggregationIoc!=null){
			try {
				plcBaseCRUDSRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, new DynamicAnnotation(aggregationIoc.clazz().getCanonicalName()));
			} catch (Exception e){
				plcBaseCRUDSRepository = null;
			}	
		}

		// Origem do BaseParentRepositorio
		if (plcBaseCRUDSRepository==null) {
			QPlcSpecific plcSpecific = (QPlcSpecific)beanClass.getAnnotation(QPlcSpecific.class);
			if (plcSpecific!=null){
				try {
					plcBaseCRUDSRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, new DynamicAnnotation(plcSpecific.name()));
				} catch (Exception e){
					plcBaseCRUDSRepository = null;
				}	
			}
		}		
		
		if (plcBaseCRUDSRepository==null)
			plcBaseCRUDSRepository = PlcCDIUtil.getInstance().getInstanceByType(clazz, QPlcDefaultLiteral.INSTANCE);
		
		return (T)plcBaseCRUDSRepository;
		
	}		
	
	private static class DynamicAnnotation extends AnnotationLiteral<QPlcSpecific> implements QPlcSpecific {
		
		private String nomeEntidade;
		
		public DynamicAnnotation(String nomeEntidade) {
			this.nomeEntidade = nomeEntidade;
		}
		
		@Override
		public Class<? extends Annotation> annotationType() {
			return QPlcSpecific.class;
		}

		@Override
		public String name() {
			return nomeEntidade;
		}
		
		@Override
		public boolean equals(Object o) {
			if ((o instanceof DynamicAnnotation) && ((DynamicAnnotation) o).name().equals(this.name())) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			int hashCode = super.hashCode();
			if (StringUtils.isNotBlank(this.name())) {
				hashCode = hashCode + this.name().hashCode();
			}
			return hashCode;
		}	
	}	
	
}
