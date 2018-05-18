package com.empresa.persistency.jpa;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.empresa.commons.entity.PreferenciaUsuario;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;

public class PreferenciaUsuarioDAO extends EmpJpaDAO {

	@Inject
	@QPlcDefault
	protected PlcAnnotationPersistenceUtil annotationPersistenceUtil;

	public void atualizaPreferenciaAplicacao(PlcBaseContextVO context,
			String login, String skin, String leiaute) {

		String query = annotationPersistenceUtil.getNamedQueryByName(
				PreferenciaUsuario.class, "querySelByLogin").query();

		PreferenciaUsuario usuario = null;

		try {
			usuario = (PreferenciaUsuario) apiCreateQuery(context,
					PreferenciaUsuario.class, query).setParameter("login",
					login).getSingleResult();
		} catch (NoResultException exception) {
			log.info("Nenhum registro encontrado com o login.:" + login);
		}

		if (usuario == null) {

			usuario = new PreferenciaUsuario();
			usuario.setLogin(login);

			usuario.setPele(skin == null ? "itunes" : skin);
			usuario.setLeiaute(leiaute == null ? "sistema" : leiaute);
			insert(context, usuario);

		} else {
			if (skin != null) {
				usuario.setPele(skin);
			}
			if (leiaute != null) {
				usuario.setLeiaute(leiaute);
			}
			update(context, usuario);
		}

	}

	public PreferenciaUsuario findUserByLogin(PlcBaseContextVO context,
			String login) {

		String query = annotationPersistenceUtil.getNamedQueryByName(
				PreferenciaUsuario.class, "querySelByLogin").query();

		PreferenciaUsuario usuario = null;

		try {
			usuario = (PreferenciaUsuario) apiCreateQuery(context,
					PreferenciaUsuario.class, query).setParameter("login",
					login).getSingleResult();
		} catch (NoResultException exception) {
			log.info("Nenhum registro encontrado com o login.:" + login);
		}

		return usuario;

	}

}
