/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.service;

import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.OneToOne;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.PlcFileContent;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcFileUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.conversors.simple.PlcJsonConversor;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants.PATH_PARAM;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

/**
 * Objeto que interceptá as operações básicas REST, e delega para a camada de
 * {@linkplain IPlcController Controle} do jCompany_Service.

 * 
 * @see IPlcController
 * @see IPlcConversor
 * 
 * @author Adolfo Jr.
 */
@Path("/service/file")
public class PlcServiceResourceFile extends PlcBaseService {

	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	protected PlcCreateContextUtil contextMontaUtil;
	
	public static final String NAME_PATTERN = ".*";

	@GET
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})/{"+PATH_PARAM.IMAGE_PROPERTY+"}")
	public <E, I> Response getImage(
								@PathParam(PATH_PARAM.CONTROLLER_NAME) String controllerName, 
								@PathParam(PATH_PARAM.IMAGE_PROPERTY)String property) {

		IPlcController<E, I> controller = getController();

		controller.setIdentifier((I) getControllerEntityId());
		controller.retrieve(controller.getIdentifier());
		Object entidade = controller.getEntity();
		
		IPlcFile file=null;
		try {
			file = (PlcFile) PropertyUtils.getProperty(entidade, property);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
		
		if (file!=null) {
			
			if (iocControleFacadeUtil==null)
				iocControleFacadeUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcIocControllerFacadeUtil.class, QPlcDefaultLiteral.INSTANCE);
			
			if (contextMontaUtil==null)
				contextMontaUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcCreateContextUtil.class, QPlcDefaultLiteral.INSTANCE);

			file = iocControleFacadeUtil.getFacade().downloadFile(contextMontaUtil.createContextParamMinimum(), file.getClass(), file.getId());
		
			return Response.ok(file.getBinaryContent().getBinaryContent(), file.getType()).build();
		} else {
			return Response.serverError().build();
		}

	}	
	
	
	@GET
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.IMAGE_PROPERTY + "})")
	public <E, I> Response getImageSession(
								@PathParam(PATH_PARAM.CONTROLLER_NAME) String controllerName, 
								@PathParam(PATH_PARAM.IMAGE_PROPERTY)String property) {

		IPlcController<E, I> controller = getController();

		controller.setIdentifier((I) getControllerEntityId());
		controller.retrieve(controller.getIdentifier());
		Object entidade = controller.getEntity();
		
		IPlcFile file = (IPlcFile) ((PlcJsonConversor) getConversor()).getRequest().getSession().getAttribute(controllerName + "_" + property);

		if (file!=null) {
			return Response.ok(file.getBinaryContent().getBinaryContent(), file.getType()).build();
		} else {
			return Response.serverError().build();
		}

	}	
	
	@POST
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.IMAGE_PROPERTY + "})")
	public <E, I> Response setImageSession(
					@PathParam(PATH_PARAM.CONTROLLER_NAME) String controllerName, 
					@PathParam(PATH_PARAM.IMAGE_PROPERTY)String property) {

        PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
        PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
        PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
        PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
        PlcFileUtil fileUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcFileUtil.class, QPlcDefaultLiteral.INSTANCE);
	    
        String filename = ((PlcJsonConversor) getConversor()).getRequest().getHeader("X-File-Name");
        IPlcFile file = null;
        try {
        	
        	InputStream inputStream = ((PlcJsonConversor) getConversor()).getRequest().getInputStream();
        	file = (IPlcFile) reflectionUtil.findFieldHierarchically(configUtil.getConfigAggregation(controllerName).entity(), property).getType().newInstance();
	        	
    		file.setNome(filename);
    		
    		DataInputStream dataInputArq = new DataInputStream(inputStream);
    		int size = Integer.parseInt(((PlcJsonConversor) getConversor()).getRequest().getHeader("content-length"));
    		byte bytesArq[] = new byte[size];
    		
    		dataInputArq.readFully(bytesArq);
    		dataInputArq.close();
    		PlcFileContent conteudo = null;
    		
    		Method m = reflectionUtil.findMethodHierarchically(file.getClass(), "getBinaryContent");
    		
    		if (m!=null && m.getAnnotation(OneToOne.class)!=null) {
    			conteudo = (PlcFileContent)m.getAnnotation(OneToOne.class).targetEntity().newInstance();
    		} else{ 
    			conteudo = (PlcFileContent)reflectionUtil.findFieldHierarchically(file.getClass(), "conteudo").getType().newInstance();
    		}
    		
    		conteudo.setBinaryContent(bytesArq);
    		file.setBinaryContent(conteudo);
    		file.setLength(size);
    		file.setType("image/" + file.getNome().substring(file.getNome().length() -3, file.getNome().length()));
    		
    		PlcFileAttach fileAttach = reflectionUtil.findFieldHierarchically(configUtil.getConfigAggregation(controllerName).entity(), property).getAnnotation(PlcFileAttach.class);

    		//validando o arquivo
    		fileUtil.isValid(file, fileAttach, controllerName);
    		
        	if (fileAttach.image()) {
        		contextUtil.getRequest().getSession().setAttribute(controllerName + "_" + property, file);
        	} else {
        		Map<String, PlcFile> fileMap = (Map<String, PlcFile>) contextUtil.getRequest().getSession().getAttribute(controllerName + "_" + property); 
        		if (fileMap == null) {
        			fileMap  = new HashMap<String, PlcFile>();
        		}
    			fileMap.put(file.getNome(), (PlcFile) file);
        	}
	        	
        } catch (PlcException plcE) {
        	throw plcE;
        } catch (Exception e) {
        	throw new PlcException(e);
        }

		if (file!=null) {
			return Response.ok().build();
		} else {
			return Response.serverError().build();
		}
	}	
	
	
	@DELETE
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.IMAGE_PROPERTY + "})")
	public <E, I> Response clearImageSession(
								@PathParam(PATH_PARAM.CONTROLLER_NAME) String controllerName, 
								@PathParam(PATH_PARAM.IMAGE_PROPERTY)String property) {

		((PlcJsonConversor) getConversor()).getRequest().getSession().removeAttribute(controllerName + "_" + property);
		((PlcJsonConversor) getConversor()).getRequest().getSession().setAttribute(controllerName + "_" + property + "_clear","clear");
		return Response.ok().build();
	}	
	
	

	

}
