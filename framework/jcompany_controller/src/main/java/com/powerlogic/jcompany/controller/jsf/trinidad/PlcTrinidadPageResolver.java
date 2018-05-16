/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.trinidad;

import org.apache.myfaces.trinidad.context.PageResolver;

/**
 * Resolve a página física baseada na viewId informada
 * 
 * @author george
 * 
 */
public class PlcTrinidadPageResolver extends PageResolver {

	static final String DEFAULT_FACELETS_SUFFIX = ".xhtml";
	static final String FCL_NIVEL_INTERMEDIARIO = "/fcls/client/formularioEspecificoPlc.xhtml";
	static final String FCL_NIVEL_AVANCADO = "/fcls/client/formularioComutaPlc.xhtml";

	@Override
	public String encodeActionURI(String actionURI) {
		return actionURI;
	}

	/**
	 * Decodifica uma URL "lógica" no padrão "/f/l/*" ou "/f/n/*", que não expõe
	 * nomes e localização de arquivos internos e a devolve para regras de
	 * navegação com o nome físico somente reconhecido e acessível internamente,
	 * pelos programas da aplicação. Preserva o endereço RESTFul dos formulários
	 * ao longo de manutenções e refatorações, preservando bookmark, links de
	 * iframe/portlets e acessos REST em geral.
	 * 
	 * @param visaoLogica
	 *            Endereço lógico (Ex: /f/l/uf ou /f/n/uf)
	 * @return visaoFisica correspondente por convenção. Ex:
	 *         WEB-INF/fcls/tabular/ufTabular.xhtml, para uma lógica tabular
	 *         cujo dirBaseFcls seja "WEB-INF/fcls/tabular".
	 */
	@Override
	public String getPhysicalURI(String viewId) {
		String uri;
		if (viewId == null) {
			throw new IllegalArgumentException("viewId não pode ser nula");
		}
		if (viewId.startsWith("/n/")) {
			// Nível Avançado
			// Segue convenção de nomenclatura para renderizar componentes de
			// formulario em layouts genéricos
			uri = FCL_NIVEL_AVANCADO;
		} else if (viewId.startsWith("/l/")) {
			// Nível Intermediário
			// Segue convenção de nomenclatura adicionando o *.xhtml a URL se
			// nao informado.
			uri = FCL_NIVEL_INTERMEDIARIO;
		} else {
			// Nivel Básico
			// Neste caso não foi seguida a convenção entao considera como nome
			// fisico - padrão básico de uso de layout
			uri = resolveVisaoLogicaApi(viewId);
		}
		return uri;
	}

	/**
	 * Template Method
	 * 
	 * @param visaoLogica
	 *            Endereço de recurso lógico, que não segue o prefixo padrão
	 *            "/f/l/*" ou "/f/d/*
	 * @return deve retornar um endereço de recurso físico
	 */
	protected String resolveVisaoLogicaApi(String visaoLogica) {
		if (!visaoLogica.endsWith(DEFAULT_FACELETS_SUFFIX)) {
			return visaoLogica.concat(DEFAULT_FACELETS_SUFFIX);
		}
		return visaoLogica;
	}
}
