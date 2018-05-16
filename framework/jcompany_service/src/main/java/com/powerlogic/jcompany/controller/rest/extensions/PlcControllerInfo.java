package com.powerlogic.jcompany.controller.rest.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;

public class PlcControllerInfo {

	private static String[] suffixes = { "Controle", "Controller" };

	public static class PlcParamInfo {

		private String paramName;

		private String defaultValue;

		private Class<? extends Annotation> paramType;

		private Class<?> valueType;

		public PlcParamInfo(Class<? extends Annotation> paramType, String paramName, Class<?> valueType, String defaultValue) {
			this.paramType = paramType;
			this.paramName = paramName;
			this.valueType = valueType;
			this.defaultValue = defaultValue;
		}

		public String getParamName() {
			return paramName;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public Class<? extends Annotation> getParamType() {
			return paramType;
		}

		public Class<?> getValueType() {
			return valueType;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(valueType.getSimpleName());
			sb.append(" @").append(paramType.getSimpleName());
			sb.append("(").append(paramName).append(")");
			if (StringUtils.isNotEmpty(defaultValue)) {
				sb.append(" @").append(DefaultValue.class.getSimpleName());
				sb.append("(").append(defaultValue).append(")");
			}
			return sb.toString();
		}
	}

	private String name;
	private String qualifier;
	private String path;
	private Bean<?> bean;
	private List<PlcParamInfo> parameters;

	public PlcControllerInfo(Bean<?> bean) {

		Set<Annotation> qualifiers = bean.getQualifiers();

		QPlcControllerName name = PlcServiceExtension.findQualifier(qualifiers, QPlcControllerName.class);
		QPlcControllerQualifier qualifier = PlcServiceExtension.findQualifier(qualifiers, QPlcControllerQualifier.class);

		this.path = null;
		// Checa se existe a anotação de ControllerName
		if (name != null) {
			this.name = name.value();
		}
		// Caso não tenha anotado nenhum nome, utiliza o default, nome da classe
		// sem o prefixo.
		if (StringUtils.isEmpty(this.name)) {
			this.name = StringUtils.uncapitalize(bean.getBeanClass().getSimpleName());
			for (String suffix : suffixes) {
				if (StringUtils.endsWith(this.name, suffix)) {
					this.name = StringUtils.removeEnd(this.name, suffix);
					break;
				}
			}
		}
		// Checa se foi informado algum qualificador.
		if (qualifier != null) {
			this.qualifier = StringUtils.defaultIfEmpty(qualifier.value(), "*");
		}
		// munta o nome completo do controller.
		if (this.qualifier != null) {
			this.path = this.name.concat(".").concat(this.qualifier);
		} else {
			this.path = this.name;
		}
		// referencia para o Bean, para obter a instância do CDI.
		this.bean = bean;
		// Busca os parâmetros da api JAXRS dependentes.
		this.parameters = findParameters(bean);
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	String getQualifier() {
		return qualifier;
	}

	Bean<?> getBean() {
		return bean;
	}

	public List<PlcParamInfo> getParameters() {
		return parameters;
	}

	public IPlcController<?, ?> createInstance() {
		return (IPlcController<?, ?>) PlcCDIUtil.getInstance().getReference(getBean());
	}

	public String matchPathName(String path) {
		String[] ns = StringUtils.split(name, ".");
		String[] ps = StringUtils.split(path, ".");
		for (int i = 0; i < ps.length; i++) {
			if (i < ns.length) {
				if ("*".equals(ns[i])) {
					ns[i] = ps[i];
				}
			}
		}
		return StringUtils.join(ns, ".");
	}

	public String matchPathQualifier(String path) {
		return StringUtils.removeStart(StringUtils.removeStart(path, matchPathName(path)), ".");
	}

	protected List<PlcParamInfo> findParameters(Bean<?> bean) {

		List<PlcParamInfo> parameters = new LinkedList<PlcParamInfo>();

		Set<InjectionPoint> ips = getBean().getInjectionPoints();

		for (InjectionPoint ip : ips) {

			Member member = ip.getMember();

			if (member instanceof Method) {
				findMethodParameters(parameters, (Method) member);
			} else if (member instanceof Field) {
				findFieldParameters(parameters, (Field) member);
			}
		}
		return parameters;
	}

	protected void findMethodParameters(List<PlcParamInfo> parameters, Method method) {
		Class<?>[] types = method.getParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < types.length; i++) {
			findAnnotatedParameter(parameters, types[i], annotations[i]);
		}
	}

	protected void findFieldParameters(List<PlcParamInfo> parameters, Field field) {
		findAnnotatedParameter(parameters, field.getType(), field.getAnnotations());
	}

	protected void findAnnotatedParameter(List<PlcParamInfo> parameters, Class<?> valueType, Annotation[] annotations) {

		String paramName = null;
		String defaultValue = null;
		Class<? extends Annotation> paramType = null;
		
		for (Annotation ann : annotations) {
			if (QueryParam.class.equals(ann.annotationType())) {
				paramType = QueryParam.class;
				paramName = QueryParam.class.cast(ann).value();
			} else if (MatrixParam.class.equals(ann.annotationType())) {
				paramType = MatrixParam.class;
				paramName = MatrixParam.class.cast(ann).value();
			} else if (FormParam.class.equals(ann.annotationType())) {
				paramType = FormParam.class;
				paramName = FormParam.class.cast(ann).value();
			} else if (PathParam.class.equals(ann.annotationType())) {
				paramType = PathParam.class;
				paramName = PathParam.class.cast(ann).value();
			} else if (CookieParam.class.equals(ann.annotationType())) {
				paramType = CookieParam.class;
				paramName = CookieParam.class.cast(ann).value();
			} else if (HeaderParam.class.equals(ann.annotationType())) {
				paramType = HeaderParam.class;
				paramName = HeaderParam.class.cast(ann).value();
			} else if (DefaultValue.class.equals(ann.annotationType())) {
				defaultValue = DefaultValue.class.cast(ann).value();
			}
		}
		
		if (paramType != null) {
			parameters.add(new PlcParamInfo(paramType, paramName, valueType, defaultValue));
		}
	}
	
	
	/**
	 * Lê o html de documentação do Controller. Este html por convenção deve estar localizado na pasta
	 * docs e deve possuir o mesmo nome da classe controller.
	 * @return html de documentação estática.
	 */
	public String getHtmlDocPath(){
		if(bean!=null && bean.getBeanClass()!=null){
			InputStream is = bean.getBeanClass().getResourceAsStream("/docs/"+bean.getBeanClass().getSimpleName()+".html");
			if(is==null){
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_RETRIEVE_FILE_CONTENT, new Object[]{bean.getBeanClass().getSimpleName()});
			}
			else{
				try {
					return IOUtils.toString(is);
				} catch (IOException e) {
					throw new PlcException("PlcControllerInfo", "getHtmlDocPath", e, null, "");
				}
				finally{
					IOUtils.closeQuietly(is);
				}
			}
		}
		return "";
	}
}