/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcEntityId;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

/**
 * Conversor do rest para requisições com MediaType Json
 * 
 * @author Adolfo dos Santos Júnior
 * @author Mauren Ginaldo Souza
 * @since dez/2010
 */
@SPlcConversor
@QPlcConversorMediaType( { "application/json", "*/*" })
@SuppressWarnings("unchecked")
public class PlcJsonConversor<C> extends PlcBaseConversor<C> {
	
	@Inject
	protected HttpServletRequest request;

	@Inject
	@QPlcEntityId
	private String id;
	
	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	private IPlcRestRendererUtil restRendererUtil;

	@Inject
	@QPlcDefault
	private PlcBeanPopulateUtil beanPopulateUtil;
	
	@Inject
	@QPlcDefault
	private PlcConfigUtil configUtil;
	
	@Inject
	@QueryParam("jsonp_callback")
	private String jsonpCallbackParam;

	public final static String JSONP_CALLBACK_ARGUMENT="$JSON_param";
	
	private String ucName;

	@Inject
	public void setUcName(@QPlcControllerName String controleNome, @QPlcControllerQualifier String controleQualificador) {
		this.ucName = StringUtils.defaultIfEmpty(controleQualificador, controleNome);
	}

	public String getUcName() {
		return ucName;
	}

	@Override
	public void readEntity(C _container, InputStream inputStream) {

		try {
			IPlcController container = (IPlcController)_container;
			
			if (inputStream != null) {
				
				String stringJSON = URLDecoder.decode(IOUtils.toString(inputStream), "UTF-8");
				
				JSONObject objectJSON = JSONObject.fromObject(stringJSON);
				
				// Popula a entidade com os dados do request!
				Object jsonObject = objectJSON.get(this.ucName);
				
				if (jsonObject instanceof Map) {
					// Instancia o tipo da entidade!
					Object beanInstance = container.getEntityType().newInstance();
					
					// preenche com o mapa.
					beanPopulateUtil.transferMapToBean((Map<String, Object>) jsonObject, beanInstance, getDetailsInfo());
					// Injeta a entidade no Controller!
					container.setEntity(beanInstance);
				} else if (jsonObject instanceof List) {
					List<Object> tab = new ArrayList<Object>();
					for (Object object : (List<?>) jsonObject) {
						// Isntancia o tipo da entidade!
						Object beanInstance = container.getEntityType().newInstance();
						// preenche com o mapa.
						beanPopulateUtil.transferMapToBean((Map<String, Object>) object, beanInstance);
						// Injeta a entidade no Controller!
						tab.add(beanInstance);
					}
					container.setEntityCollection(tab);
				}
			}
			else if(container.getIdentifier()!=null){
				Object id = container.getIdentifier();
				if(id instanceof Long){
					Object beanInstance = container.getEntityType().newInstance();
					PropertyUtils.setProperty(beanInstance, "id", id);
					container.setEntity(beanInstance);
				}
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcJsonConversor", "readEntity", e, null, "");
		}
	}

	@Override
	public void readEntityCollection(C _container, InputStream inputStream) {

		IPlcController container = (IPlcController)_container;

		Map<String, String> parameterMap = request.getParameterMap();

		if (parameterMap.isEmpty()) {
			container.setEntity(null);
		} else {
			try {
				container.setEntity(container.getEntityType().newInstance());
				String action = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1) + "_";
				Set<String> mapaKeys = parameterMap.keySet();
				Map<String, Object> mapa = new HashMap<String, Object>();
				for (String nome : mapaKeys) {
					mapa.put(nome.replace(action, ""), parameterMap.get(nome));
				}
				readPaginationCollection(_container, mapa);
				beanPopulateUtil.transferMapToBean(mapa, container.getEntity());
			} catch (Exception e) {
				log.fatal("Erro ao criar entidade.");
			}
		}
	}
	
	protected void readPaginationCollection(C controller, Map<String, Object> mapa) {
	}

	@Override
	public void writeEntity(C _container, OutputStream outputStream) {
		try {
			IPlcController container = (IPlcController)_container;

			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("id", id);
			response.put(getUcName(), container.getEntity());
			response.put("messages", getMessages());
			Object plcSelProps = request.getParameterMap().get("plcSelProps");
			response.put("plcSelProps", (plcSelProps != null ? ((String)Array.get(plcSelProps,0)).split(",") : plcSelProps));
			writeResponse(outputStream, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void writeResponse(OutputStream outputStream, Map<?, ?> response) throws IOException {
		
		/*se possui o jsonpParam quer dizer que é o servidor esta externo. Então, tem que chamar a funcao
		 * de callback especificada por esse parametro
		 */
		if(jsonpCallbackParam!=null){
			jsonpCallbackParam=jsonpCallbackParam.replace(JSONP_CALLBACK_ARGUMENT, restRendererUtil.createObject(response));
			IOUtils.write(jsonpCallbackParam, outputStream);
		}
		else{
			IOUtils.write(restRendererUtil.createObject(response), outputStream);
		}
		
	}

	@Override
	public void writeEntityCollection(C _container, OutputStream outputStream) {
		try {
			IPlcController container = (IPlcController)_container;

			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put(getUcName(), container.getEntityCollection());
			response.put("messages", getMessages());
			Object plcSelProps = request.getParameterMap().get("plcSelProps");
			response.put("plcSelProps", (plcSelProps != null ? ((String)Array.get(plcSelProps,0)).split(",") : plcSelProps));
			writeEntityCollectionPagination(_container, response);
			writeResponse(outputStream,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void writeException(C controle, OutputStream outputStream, PlcMessage plcMessage) {
		if (log != null) {
			log.error(plcMessage.getMensagem());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlcMessage.Cor.getTipo(Cor.msgVermelhoPlc.toString()), plcMessage.getMensagem());
		try {
			writeResponse(outputStream,Collections.singletonMap("messages", map));
			outputStream.flush();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (IOException e) {
			throw new PlcException("PlcJsonConversor", "writeException", e, log, "");
		}
	}
	
	
	protected void writeEntityCollectionPagination(C _container, Map<String, Object> mapa) {	
		IPlcController container = (IPlcController)_container;
		mapa.put(getUcName(), container.getEntityCollection());
	}
	

	protected Map<String, Class> getDetailsInfo() {
		
		PlcConfigAggregationPOJO configAgg = configUtil.getConfigAggregation(getUcName());
		Map<String, Class> detailClasses = new HashMap<String, Class>();
		for(PlcConfigDetail detail : configAgg.details()){
			detailClasses.put(detail.collectionName(), detail.clazz());
		}
		return detailClasses;
	}

	public IPlcRestRendererUtil getRestRendererUtil() {
		return restRendererUtil;
	}

	public void setRestRendererUtil(IPlcRestRendererUtil restRendererUtil) {
		this.restRendererUtil = restRendererUtil;
	}

	public String getJsonpCallbackParam() {
		return jsonpCallbackParam;
	}

	public void setJsonpCallbackParam(String jsonpCallbackParam) {
		this.jsonpCallbackParam = jsonpCallbackParam;
	}

	public void setUcName(String ucName) {
		this.ucName = ucName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	
	
}
