/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.jaxb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jboss.jaxb.intros.IntroductionsAnnotationReader;
import org.jboss.jaxb.intros.IntroductionsConfigParser;
import org.jboss.jaxb.intros.configmodel.JaxbIntros;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;
import com.sun.xml.bind.api.JAXBRIContext;

public abstract class PlcJAXBBaseConversor<C> extends PlcBaseConversor<C> {

	protected JAXBContext jaxbContext;
	
	@Override
	public void writeEntity(C _container, OutputStream outputStream) {
		
		IPlcController container = (IPlcController) _container; 
		
		Object entidade = container.getEntity();
		
		if (entidade != null) {
			try {			
				Marshaller marshaller = escreveEntidadeObtemMarshaller(entidade);
				marshaller.marshal(entidade, outputStream);
			} catch(PlcException plcE){
				throw plcE;				
			} catch (JAXBException e) {
				throw new PlcException(e);
			}				
		}
		else {
			throw new PlcException("Nenhuma entidade foi configurada para conversão em texto!");
		}
			
	}

	@Override
	public void readEntity(C container, InputStream inputStream) {
				
	}
	
	// -- Template Methods
	
	protected Marshaller escreveEntidadeObtemMarshaller(Object entidade) {
		
		try {			
			JAXBContext jaxb = getJAXBContext(entidade);
			Marshaller marshaller = jaxb.createMarshaller();
			writeEnityAndConfigureMarshaller(marshaller);
			return marshaller;		
		} catch(PlcException plcE){
			throw plcE;			
		} catch (JAXBException e) {
			throw new PlcException(e);
		}	
		
	}

	protected void writeEnityAndConfigureMarshaller(Marshaller marshaller) {
		
	}
	
	protected JAXBContext getJAXBContext(Object entidade) {	
		if (jaxbContext != null) {
			return jaxbContext;
		}
		else {
			return getNewJAXBContext(entidade);
		}
	}

	protected JAXBContext getNewJAXBContext(Object entidade) {
		try {
			String configString = getJAXBCongiIntro(entidade);
			
			if (configString != null) {
				JaxbIntros config = IntroductionsConfigParser.parseConfig(new ByteArrayInputStream(configString.getBytes()));
				IntroductionsAnnotationReader reader = new IntroductionsAnnotationReader(config);

				Map<String, Object> jaxbConfig = new HashMap<String, Object>();
				jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);		
				
				jaxbContext = JAXBContext.newInstance(new Class[]{entidade.getClass()}, jaxbConfig);				
			} else {
				jaxbContext = JAXBContext.newInstance(new Class[]{entidade.getClass()});
			}

			return getNewJAXBContext(entidade);
		} catch(PlcException plcE){
			throw plcE;			
		} catch (JAXBException e) {
			throw new PlcException(e);
		}
	}

	protected String getJAXBCongiIntro(Object entidade) {
		String configString = 
			"<?xml version = \"1.0\" encoding = \"UTF-8\"?>" +
			"<jaxb-intros xmlns=\"http://www.jboss.org/xsd/jaxb/intros\">" +
			"<Class name=\""+entidade.getClass().getName()+"\">" +
			"    <XmlRootElement name=\""+entidade.getClass().getName()+"\" ></XmlRootElement>" +
			"</Class>" +
			"</jaxb-intros>";
		return configString;
	}

	@Override
	public void writeEntityCollection(C container, OutputStream outputStream) {

	}

	@Override
	public void readEntityCollection(C container, InputStream inputStream) {

	}

}
