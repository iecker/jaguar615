/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants.PATH_PARAM;

/**
 * Objeto que interceptá as operações básicas REST, e delega para a camada de
 * {@linkplain IPlcController Controle} do jCompany_Service.
 * <p>
 * <li>HTTP GET "../service/[IPlcControle]" -&gt;
 * {@linkplain IPlcController#retrieveCollection() recuperaColecao} -&gt;
 * {@linkplain IPlcConversor#writeEntityCollection(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.OutputStream)
 * escreveColecaoEntidade}</li>
 * <li>HTTP GET "../service/[IPlcControle](ID)" -&gt;
 * {@linkplain IPlcController#retrieve(Object) recupera} -&gt;
 * {@linkplain IPlcConversor#writeEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.OutputStream)
 * escreveEntidade}</li>
 * </li>
 * <li>HTTP POST "../service/[IPlcControle]" -&gt;
 * {@linkplain IPlcConversor#readEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.InputStream)
 * leEntidade} -&gt; {@linkplain IPlcController#insert() inclui} -&gt;
 * {@linkplain IPlcConversor#writeEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.OutputStream)
 * escreveEntidade}</li>
 * </li>
 * <li>HTTP POST/PUT "../service/[IPlcControle](ID)" -&gt;
 * {@linkplain IPlcConversor#readEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.InputStream)
 * leEntidade} -&gt; {@linkplain IPlcController#update() atualiza} -&gt;
 * {@linkplain IPlcConversor#writeEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.OutputStream)
 * escreveEntidade}</li>
 * </li>
 * <li>HTTP DELETE "../service/[IPlcControle](ID)" -&gt;
 * {@linkplain IPlcConversor#readEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.InputStream)
 * leEntidade} -&gt; {@linkplain IPlcController#delete() exclui} -&gt;
 * {@linkplain IPlcConversor#writeEntity(com.powerlogic.jcompany.controller.rest.api.controle.IPlcEntidadeContainer, java.io.OutputStream)
 * escreveEntidade}</li>
 * </li>
 * </p>
 * 
 * @see IPlcController
 * @see IPlcConversor
 * 
 * @author Adolfo Jr.
 */
@Path("/service")
public class PlcServiceResource extends PlcBaseService {

	public static final String NAME_PATTERN = ".*";

	/**
	 * Atende a URL "../service/{IPlcController}"
	 * 
	 * Metodo que responde a requisição GET que corresponde a recuperação de uma
	 * coleção de entidades. Dispara o metodo
	 * {@link IPlcController#retrieveCollection()}
	 */
	@GET
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}")
	@NoCache
	public <E, I> Response retrieveCollection() {

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		// FIXME: Disponibilizar metodo leParametros
		conversor.readEntityCollection(controller, null);

		controller.retrieveCollection();

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}({ResourceId})"
	 * 
	 * Metodo que responde a requisição GET que corresponde a recuperação de um
	 * objeto identificado. Dispara o metodo
	 * {@link IPlcController#retrieve(Object)}
	 */
	@GET
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})")
	@NoCache
	@SuppressWarnings("unchecked")
	public <E, I> Response retrieveResource() {

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		// FIXME: Disponibilizar metodo leParametros
		conversor.readEntity(controller, null);

		Object resourceId = getControllerEntityId();

		controller.retrieve((I) resourceId);

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}"
	 * 
	 * Metodo que responde a requisição POST que corresponde a operação de
	 * inclusão de um objeto. Dispara o metodo {@link IPlcController#insert()}
	 */
	@PUT
	@POST
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}")
	@NoCache
	public <E, I> Response insertResource(@Context HttpServletRequest request) throws Exception {

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		conversor.readEntity(controller, request.getInputStream());

		controller.insert();

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}({ResourceId})"
	 * 
	 * Metodo que responde a requisição PUT que corresponde a atualização de um
	 * objeto identificado. Dispara o metodo {@link IPlcController#insert()}
	 */
	@PUT
	@POST
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})")
	@NoCache
	public <E, I> Response updateResource(@QueryParam("$delete") String delete, @Context HttpServletRequest request) throws Exception {
		// Fallback para "clients" que não suportam @DELETE
		if ("true".equalsIgnoreCase(delete)) {
			return deleteResource(request);
		}

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		conversor.readEntity(controller, request.getInputStream());

		controller.update();

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}({ResourceId})"
	 * 
	 * Metodo que responde a requisição DELETE que corresponde a exclusão de um
	 * objeto identificado. Dispara o metodo {@link IPlcController#delete()}
	 */
	@DELETE
	@Path("/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})")
	@NoCache
	public <E, I> Response deleteResource(@Context HttpServletRequest request) throws Exception {

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();
		controller.setIdentifier((I) getControllerEntityId());
		conversor.readEntity(controller, null);
		controller.delete();

		return getResponse(controller);
	}
}
