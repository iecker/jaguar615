/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.producers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcDefaultValue;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcHttpParam;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcProjection;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcHtmlRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcJsonRestRenderUtil;
import com.powerlogic.jcompany.controller.rest.projection.PlcProjectionProperty;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants;

@RequestScoped
public class PlcJAXRSProducer {

	private HttpHeaders httpHeaders;

	private UriInfo uriInfo;

	private Request request;

	private HttpServletRequest servletRequest;

	private HttpServletResponse servletResponse;

	private ServletConfig servletConfig;

	private ServletContext servletContext;

	private SecurityContext securityContext;

	private MediaType mediaType;

	private Providers providers;

	@Produces
	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
		List<MediaType> medias = httpHeaders.getAcceptableMediaTypes();
		if (medias.size() > 0) {
			setMediaType(medias.get(0));
		} else {
			setMediaType(null);
		}
	}

	@Produces
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	@Produces
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	@Produces
	public HttpServletRequest getHttpServletRequest() {
		return servletRequest;
	}

	@Produces
	public HttpServletResponse getHttpServletResponse() {
		return servletResponse;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setHttpServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	@Produces
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	@Produces
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Produces
	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	@Produces
	public MediaType getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		if (mediaType == null) {
			this.mediaType = MediaType.valueOf(MediaType.WILDCARD);
		} else {
			this.mediaType = mediaType;
		}
	}

	public void setProviders(Providers providers) {
		this.providers = providers;
	}

	@Produces
	public Providers getProviders() {
		return providers;
	}

	@Produces
	public String getParam(InjectionPoint ip) {

		Annotated annotated = ip.getAnnotated();

		if (annotated.isAnnotationPresent(QueryParam.class)) {
			return getQueryParam(ip, uriInfo);
		}
		if (annotated.isAnnotationPresent(MatrixParam.class)) {
			return getMatrixParam(ip, uriInfo);
		}
		if (annotated.isAnnotationPresent(FormParam.class)) {
			return getFormParam(ip, servletRequest);
		}
		if (annotated.isAnnotationPresent(PathParam.class)) {
			return getPathParam(ip, uriInfo);
		}
		if (annotated.isAnnotationPresent(CookieParam.class)) {
			return getCookieParam(ip, httpHeaders);
		}
		if (annotated.isAnnotationPresent(HeaderParam.class)) {
			return getHeaderParam(ip, httpHeaders);
		}
		return null;
	}

	@Produces
	public Integer getParamInteger(InjectionPoint ip) {
		String value = getParam(ip);
		if (value != null) {
			return Integer.valueOf(value);
		}
		return null;
	}

	@Produces
	public Long getParamLong(InjectionPoint ip) {
		String value = getParam(ip);
		if (value != null) {
			return Long.valueOf(value);
		}
		return null;
	}

	@Produces
	public Float getParamFloat(InjectionPoint ip) {
		String value = getParam(ip);
		if (value != null) {
			return Float.valueOf(value);
		}
		return null;
	}

	@Produces
	public Double getParamDouble(InjectionPoint ip) {
		String value = getParam(ip);
		if (value != null) {
			return Double.valueOf(value);
		}
		return null;
	}

	@Produces
	public Boolean getParamBoolean(InjectionPoint ip) {
		String value = getParam(ip);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return null;
	}

	public String getQueryParam(InjectionPoint ip, UriInfo uriInfo) {

		String name = ip.getAnnotated().getAnnotation(QueryParam.class).value();
		String value = uriInfo.getQueryParameters().getFirst(name);

		return getDefaultValue(ip, value);
	}

	public String getMatrixParam(InjectionPoint ip, UriInfo uriInfo) {

		String name = ip.getAnnotated().getAnnotation(MatrixParam.class).value();
		String value = null;

		List<PathSegment> segments = uriInfo.getPathSegments();
		if (segments != null) {
			for (PathSegment segment : segments) {
				MultivaluedMap<String, String> matrix = segment.getMatrixParameters();
				if (matrix != null && !matrix.isEmpty() && matrix.containsKey(name)) {
					value = matrix.getFirst(name);
					break;
				}
			}
		}
		return getDefaultValue(ip, value);
	}

	public String getFormParam(InjectionPoint ip, HttpServletRequest request) {

		String name = ip.getAnnotated().getAnnotation(FormParam.class).value();
		String value = request.getParameter(name);

		return getDefaultValue(ip, value);
	}

	public String getPathParam(InjectionPoint ip, UriInfo uriInfo) {

		String name = ip.getAnnotated().getAnnotation(PathParam.class).value();
		String value = uriInfo.getPathParameters().getFirst(name);

		return getDefaultValue(ip, value);
	}

	public String getCookieParam(InjectionPoint ip, HttpHeaders httpHeaders) {

		String name = ip.getAnnotated().getAnnotation(CookieParam.class).value();
		String value = null;

		Cookie cookie = httpHeaders.getCookies().get(name);
		if (cookie != null) {
			value = cookie.getValue();
		}
		return getDefaultValue(ip, value);
	}

	public String getHeaderParam(InjectionPoint ip, HttpHeaders httpHeaders) {

		String name = ip.getAnnotated().getAnnotation(HeaderParam.class).value();
		String value = httpHeaders.getRequestHeaders().getFirst(name);

		return getDefaultValue(ip, value);
	}

	public String getDefaultValue(InjectionPoint ip, String value) {
		if (StringUtils.isEmpty(value)) {
			DefaultValue dv = ip.getAnnotated().getAnnotation(DefaultValue.class);
			if (dv != null) {
				return dv.value();
			}
		}
		return value;
	}

	/**
	 * @deprecated Use JAXRS params.
	 */
	@Produces
	@QPlcHttpParam
	@QPlcDefaultValue
	public String getHttpParamString(InjectionPoint ip, HttpServletRequest request) {
		String defaultValue = null;
		if (ip.getAnnotated().getAnnotation(QPlcDefaultValue.class) != null) {
			defaultValue = ip.getAnnotated().getAnnotation(QPlcDefaultValue.class).value();
		}
		return obtemHttpParam(request, ip.getAnnotated().getAnnotation(QPlcHttpParam.class).value(), defaultValue);
	}

	/**
	 * @deprecated Use JAXRS params.
	 */
	@Produces
	@QPlcHttpParam
	@QPlcDefaultValue
	public Integer getHttpParamInteger(InjectionPoint ip, HttpServletRequest request) {
		String defaultValue = null;
		if (ip.getAnnotated().getAnnotation(QPlcDefaultValue.class) != null) {
			defaultValue = ip.getAnnotated().getAnnotation(QPlcDefaultValue.class).value();
		}
		String param = obtemHttpParam(request, ip.getAnnotated().getAnnotation(QPlcHttpParam.class).value(), defaultValue);
		if (!StringUtils.isNumeric(param)) {
			if (defaultValue != null)
				return Integer.parseInt(defaultValue);
			else
				return null;
		} else {
			return Integer.parseInt(param);
		}
	}

	/**
	 * @deprecated Use JAXRS params.
	 */
	@Produces
	@QPlcHttpParam
	@QPlcDefaultValue
	public Long getHttpParamLong(InjectionPoint ip, HttpServletRequest request) {
		String defaultValue = null;
		if (ip.getAnnotated().getAnnotation(QPlcDefaultValue.class) != null) {
			defaultValue = ip.getAnnotated().getAnnotation(QPlcDefaultValue.class).value();
		}
		String param = obtemHttpParam(request, ip.getAnnotated().getAnnotation(QPlcHttpParam.class).value(), defaultValue);
		if (!StringUtils.isNumeric(param)) {
			return null;
		} else {
			return Long.parseLong(param);
		}
	}

	/**
	 * @deprecated Use JAXRS params.
	 */
	protected String obtemHttpParam(HttpServletRequest request, String paramName, String defaultValue) {
		String param = request.getParameter(paramName);
		if (param == null && defaultValue != null) {
			param = defaultValue;
		}
		return param;
	}

	@Produces
	@QPlcProjection
	public List<String> getProjection(@QueryParam(PlcServiceConstants.QUERY_PARAM.SELECT) String inlineSelect) {
		
		List<String> selectList = new ArrayList<String>();
		
		if (StringUtils.isNotEmpty(inlineSelect)) {
			
			for (String s : StringUtils.split(inlineSelect, ",")) {
				
				String trimed = s.trim();
				
				if (StringUtils.isNotEmpty(trimed)) {
					selectList.add(trimed);
				}
			}
		}
		return selectList;
	}

	@Produces
	@QPlcProjection
	public List<PlcProjectionProperty> getProjectionProperties(@QPlcProjection List<String> projectionsString) {

		if (projectionsString == null) {
			return new ArrayList<PlcProjectionProperty>();
		}

		List<PlcProjectionProperty> properties = new ArrayList<PlcProjectionProperty>();

		for (String propString : projectionsString) {
			properties.add(createProperty(propString));
		}

		return properties;
	}

	private PlcProjectionProperty createProperty(String propString) {
		PlcProjectionProperty pParent = null;
		String[] chainedProps = propString.split("\\.");
		for (String prop : chainedProps) {
			pParent = new PlcProjectionProperty(pParent, prop);
		}
		return pParent;
	}
	
	@Produces @QPlcProjection
	public String getProjectionAlias(@QPlcControllerName String controleNome, @QPlcControllerQualifier String controleQualificador){
		if(controleNome.startsWith("grid")){
			return "rows";
		}
		return StringUtils.defaultIfEmpty(controleQualificador, controleNome);
	}
	
	@Produces @QPlcDefault @QPlcConversorMediaType
	public IPlcRestRendererUtil getRestRendererUtil(InjectionPoint ip){
		
		Set<Annotation> qualifiers = ip.getQualifiers();
		String [] mediaType=null;
		for(Annotation annotation : qualifiers){
			if(annotation instanceof QPlcConversorMediaType){
				QPlcConversorMediaType qplcConversorMediaType = (QPlcConversorMediaType)annotation;
				mediaType=qplcConversorMediaType.value();
				
			}
		}
		if(ArrayUtils.isNotEmpty(mediaType)){
			for(String media :mediaType){
				if(!MediaType.WILDCARD.equals(media)){
					if(mediaType[0].equals("text/html")){
						return PlcCDIUtil.getInstance().getInstanceByType(PlcHtmlRestRendererUtil.class, QPlcDefaultLiteral.INSTANCE);
					}
					else if(mediaType[0].equals("application/json")){
						return PlcCDIUtil.getInstance().getInstanceByType(PlcJsonRestRenderUtil.class, QPlcDefaultLiteral.INSTANCE);
					}
				}
			}
		}
		//default
		return PlcCDIUtil.getInstance().getInstanceByType(PlcJsonRestRenderUtil.class, QPlcDefaultLiteral.INSTANCE);
		
	}
}
