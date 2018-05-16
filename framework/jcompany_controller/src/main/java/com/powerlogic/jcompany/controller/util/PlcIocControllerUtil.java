/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.PlcBaseParentMB;

@SPlcUtil
@QPlcDefault
public class PlcIocControllerUtil {

	
	@Inject 
	@QPlcDefault
	protected PlcMetamodelUtil metamodelUtil;

	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;
	
	@Inject
	@Any
	protected Instance<PlcBaseMB> todasBaseMB;
	
	public Class resolveBaseMBClass(String nomeColaboracao)  {

		Bean bean = metamodelUtil.getUriIocMB(nomeColaboracao);
		
		return bean!=null?bean.getBeanClass():PlcBaseMB.class;
	}

	protected PlcBaseMB resolveBaseMBInstanceDefault() {
		
		Instance<PlcBaseMB> actionInstance = todasBaseMB.select(PlcBaseMB.class, QPlcDefaultLiteral.INSTANCE);
		PlcBaseMB objetoAction=null;
		
		if (!actionInstance.isUnsatisfied()) {
			if (!actionInstance.isAmbiguous()) {
				objetoAction = actionInstance.get();
			} else if (actionInstance.iterator() != null) {
				Iterator<PlcBaseMB> i = actionInstance.iterator();
				while(i.hasNext() && objetoAction==null) {
					objetoAction = i.next();
					if (!objetoAction.getClass().getSuperclass().equals(PlcBaseMB.class))
						objetoAction = null;
				}
			}
		} 
		return objetoAction;
	}

	public Object resolveBaseMBInstance(String nomeColaboracao)  {

		Bean bean = metamodelUtil.getUriIocMB(nomeColaboracao);

		return bean!=null ? PlcCDIUtil.getInstance().getReference(bean) : resolveBaseMBInstanceDefault();
	}

	
	public <T> T resolveBaseDelegatedMBClass(Class<? extends PlcBaseParentMB>  clazz, String nomeColaboracao) {
				
		Annotation annotation = new DynamicAnnotation(nomeColaboracao);

		PlcBaseParentMB plcBaseParentMB = null;
		
		try {
			plcBaseParentMB = PlcCDIUtil.getInstance().getInstanceByType(clazz, annotation);
		} catch (Exception e) {
			plcBaseParentMB = null;
		}
		
		if (plcBaseParentMB == null) {
			plcBaseParentMB = PlcCDIUtil.getInstance().getInstanceByType(clazz, QPlcDefaultLiteral.INSTANCE);
		}
		return (T) plcBaseParentMB;
	}	
	
	private static class DynamicAnnotation extends AnnotationLiteral<QPlcSpecific> implements QPlcSpecific {

		private static final long serialVersionUID = 1L;
		
		private String nomeColaboracao;

		public DynamicAnnotation(String nomeColaboracao) {
			this.nomeColaboracao = nomeColaboracao;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return QPlcSpecific.class;
		}

		@Override
		public String name() {
			return nomeColaboracao;
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
