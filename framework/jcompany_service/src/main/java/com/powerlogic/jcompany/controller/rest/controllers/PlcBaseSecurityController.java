package com.powerlogic.jcompany.controller.rest.controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileDTO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.integration.IPlcJSecurityApi;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;

/**
 * Controle que recupera os recursos negados para o usuário corrente.
 * /service/security
 */
@SPlcController
@QPlcControllerName("security")
public class PlcBaseSecurityController<E, I> extends PlcBaseController<E, I> {
	//<Map<String, Object>, String>
	
	@Inject
	private HttpServletRequest request;

	@Inject
	private IPlcJSecurityApi jSecurityApi;

	@Override
	public void retrieveCollection() {

		PlcBaseUserProfileDTO usuarioPerfilDTO = getUserProfileDTO();

		if (usuarioPerfilDTO != null) {

			Map<String, Object> securityData = new LinkedHashMap<String, Object>();

			securityData.put("session", request.getSession().getId());
			securityData.put("login", usuarioPerfilDTO.getLogin());
			securityData.put("nickname", usuarioPerfilDTO.getNickname());
			securityData.put("name", StringUtils.defaultIfEmpty(usuarioPerfilDTO.getName(), usuarioPerfilDTO.getName()));

			Map<String, Object> recursos = usuarioPerfilDTO.getResources();

			if (recursos != null) {

				Map<String, Object> securityResources = new HashMap<String, Object>();

				for (Map.Entry<String, Object> entry : recursos.entrySet()) {

					boolean accessDenied = jSecurityApi.isAcessoNegado(entry.getKey(), usuarioPerfilDTO);

					if (accessDenied) {
						securityResources.put(entry.getKey(), Boolean.FALSE);
					} else {
						// Somente busca eventos negados, se a URL tiver acesso.
						Map<String, Object> resource = new HashMap<String, Object>();
						if (PropertyUtils.isReadable(entry.getValue(), "eventosNegados")) {
							try {
								List<?> eventosNegados = (List<?>) PropertyUtils.getProperty(entry.getValue(), "eventosNegados");
								if (eventosNegados != null) {
									List<String> events = new ArrayList<String>();
									for (Object evt : eventosNegados) {
										events.add(evt.toString());
									}
									if (!events.isEmpty()) {
										resource.put("events", events);
									}
								}
							} catch (Exception e) {
								// Desconsidera erro de get.
							}
						}
						// Se a URL nao tiver nenhum evento, ele é apenas adicionado com null
						if (!resource.isEmpty()) {
							securityResources.put(entry.getKey(), resource);
						}
					}
				}
				securityData.put("resources", securityResources);
			}
			setEntity((E)securityData);
		}
	}

	protected List<String> getURLs(PlcBaseUserProfileDTO userProfileDTO) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		List<String> urls = new ArrayList<String>();

		if (userProfileDTO != null) {
			for (String recurso : userProfileDTO.getResources().keySet()) {
				if (jSecurityApi.isAcessoNegado(recurso, userProfileDTO)) {
					urls.add(recurso);
				}
			}
		}
		return urls;
	}

	protected PlcBaseUserProfileDTO getUserProfileDTO() {

		PlcBaseUserProfileDTO userProfileDTO = null;

		if (request!=null && request.getSession()!=null) {
			Object userProfile = request.getSession().getAttribute(PlcConstants.USER_INFO_KEY);

			if (userProfile != null) {
				if (userProfile instanceof PlcBaseUserProfileDTO) {
					userProfileDTO = (PlcBaseUserProfileDTO) userProfile;
				} else {
					userProfileDTO = new PlcBaseUserProfileDTO();
					try {
						PropertyUtils.copyProperties(userProfileDTO, userProfile);
					} catch (Exception e) {
						// Erro ao copiar propriedades.
					}
				}
			}
		}
		return userProfileDTO;
	}
}
