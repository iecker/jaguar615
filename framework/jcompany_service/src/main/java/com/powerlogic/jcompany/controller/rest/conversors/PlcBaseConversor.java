/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.conversors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

public abstract class PlcBaseConversor<C> implements IPlcConversor<C> {

	@Inject
	protected transient Logger log;
	
	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	private IPlcRestRendererUtil restRendererUtil;
	
	@Override
	public void readParameters(C controller) {
		notImplemented();
	}

	@Override
	public void readEntityCollection(C controller, InputStream inputStream) {
		notImplemented();
	}

	@Override
	public void readEntity(C controller, InputStream inputStream) {
		notImplemented();
	}

	@Override
	public void writeEntityCollection(C controller, OutputStream outputStream) {
		notImplemented();
	}

	@Override
	public void writeEntity(C controller, OutputStream outputStream) {
		notImplemented();
	}

	@Override
	public void writeException(C controle, OutputStream outputStream, PlcMessage plcMessage) {
		if (log != null) {
			log.error(plcMessage.getMensagem());
		}
	}
	
	@Override
	public void writeException(C controle, OutputStream outputStream, Throwable t) {
		if (log != null) {
			log.error(t);
		}
	}

	
	public void writeExceptions(IPlcController<Object, Object> controller, OutputStream outputStream, List<PlcMessage> msgs){
		if(log!=null){
			for (PlcMessage plcMessage : msgs) {
				log.error(plcMessage.getMensagem());
				
			}
		}
	}

	
	private void notImplemented() {
		if (log != null && log.isDebugEnabled()) {
			log.debug("Não implementado.");
		}
	}
	
	
	public Map<String, List<String>> getMessages() {
		
		PlcMsgUtil msgUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMsgUtil.class, QPlcDefaultLiteral.INSTANCE);
		Map<String, List<PlcMessage>> mensagens = msgUtil.getMensagens();
		Map<String, List<String>> mensagensTratadas = new HashMap<String, List<String>>();
		List<String> listaMsgsString;
		for(String chave:mensagens.keySet()){
			listaMsgsString=new ArrayList<String>();
			for(PlcMessage msg : mensagens.get(chave)){
				listaMsgsString.add(msg.getMensagem());
			}
			mensagensTratadas.put(Cor.getTipo(chave), listaMsgsString);
		}
		return mensagensTratadas;
	}
	
	public void writeMessages(OutputStream outputStream) {
		try {
			IOUtils.write(restRendererUtil.createObject(Collections.singletonMap("messages", getMessages())), outputStream);
			outputStream.flush();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (IOException e) {
			throw new PlcException(e);
		}
	}

	public IPlcRestRendererUtil getRestRendererUtil() {
		return restRendererUtil;
	}

	public void setRestRendererUtil(IPlcRestRendererUtil restRendererUtil) {
		this.restRendererUtil = restRendererUtil;
	}
}
