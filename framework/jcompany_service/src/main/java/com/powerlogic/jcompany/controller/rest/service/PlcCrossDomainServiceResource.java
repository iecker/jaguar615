package com.powerlogic.jcompany.controller.rest.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.service.PlcServiceConstants.PATH_PARAM;

/**
 * Objeto que intercepta as operações básicas REST crossdomain, e delega para a camada de
 * {@linkplain IPlcController Controle} do jCompany_Service.
 * Ou seja, aceita requisições vindas de outros domínios pela forma conhecida como JSONP.
 * Assim, todas as requisições são realizadas através do protocolo HTTP GET
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
 * @see PlcServiceResource
 * 
 * @author Bruno Carneiro.
 */

@Path("/crossdomain")
public class PlcCrossDomainServiceResource extends PlcServiceResource {
	
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
		return super.retrieveCollection();
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
	public <E, I> Response retrieveResource() {
		return super.retrieveResource();
	}

	/**
	 * Atende a URL "../service/{IPlcController}"
	 * 
	 * Metodo que responde a requisição POST que corresponde a operação de
	 * inclusão de um objeto. Dispara o metodo {@link IPlcController#insert()}
	 */
	@GET
	@Path("/POST/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}")
	@NoCache
	public <E, I> Response insertResource(@Context HttpServletRequest request) throws Exception {
		InputStream is = new ByteArrayInputStream(request.getParameter("json").getBytes("UTF-8"));
		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		conversor.readEntity(controller, is);

		controller.insert();

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}({ResourceId})"
	 * 
	 * Metodo que responde a requisição PUT que corresponde a atualização de um
	 * objeto identificado. Dispara o metodo {@link IPlcController#insert()}
	 */
	@GET
	@Path("/POST/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})")
	@NoCache
	public <E, I> Response updateResource(@QueryParam("$delete") String delete, @Context HttpServletRequest request) throws Exception {
		InputStream is = new ByteArrayInputStream(request.getParameter("json").getBytes("UTF-8"));
		if ("true".equalsIgnoreCase(delete)) {
			return deleteResource(request);
		}

		IPlcController<E, I> controller = getController();

		IPlcConversor<IPlcController<E, I>> conversor = getConversor();

		conversor.readEntity(controller, is);

		controller.update();

		return getResponse(controller);
	}

	/**
	 * Atende a URL "../service/{IPlcController}({ResourceId})"
	 * 
	 * Metodo que responde a requisição DELETE que corresponde a exclusão de um
	 * objeto identificado. Dispara o metodo {@link IPlcController#delete()}
	 */
	@GET
	@Path("/DELETE/{" + PATH_PARAM.CONTROLLER_NAME + ":" + NAME_PATTERN + "}({" + PATH_PARAM.RESOURCE_ID + "})")
	@NoCache
	public <E, I> Response deleteResource(@Context HttpServletRequest request) throws Exception {

		return super.deleteResource(request);
	}

}
