/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanymatrix.controller.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO.Mode;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseDynamicController;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;
import com.powerlogic.jcompany.extension.manytomanymatrix.entity.OpcaoMatrix;
import com.powerlogic.jcompany.extension.manytomanymatrix.metadata.PlcConfigManyToManyMatrix;

/**
 * Controle do extension ManyToManyMatrix. <br/>
 * Nome do controle: matrix <br/>
 * Url: ..soa/service/matrix(nome_caso_uso)
 * 
 * @author Mauren Ginaldo Souza
 * @since out/2010
 * 
 */
@SPlcController
@QPlcControllerName("matrix")
@QPlcControllerQualifier("*")
public class PlcManyToManyMatrixController extends PlcBaseDynamicController<Object, Object> {

	@Inject
	protected HttpServletRequest request;

	@Inject
	@QPlcCurrent
	private PlcConfigManyToManyMatrix plcConfigManyToManyMatrix;

	@Inject
	@QPlcDefault
	private PlcBeanPopulateUtil beanPopulateUtil;

	// Propriedades utilizadas para armazenar as entidades para montagem do json
	private Collection<PlcManyToManyMatrixDto> colecaoEntidadeAssociativa;
	private Collection<Object> colecaoEntidade1;
	private Collection<Object> colecaoEntidade2;
	private String nomeEntidade1;
	private String nomeEntidade2;

	private OpcaoMatrix opcaoMatrix;

	/*
	 * FIXME: Erro no WELD 1.1.0-CR3: remover apos correcao de injecao de
	 * ExternalAnnotatedType.
	 */
	@Inject
	@Override
	public void setContext(@QPlcCurrent PlcBaseContextVO context) {
		super.setContext(context);
	}
	
	/*
	 * FIXME: Erro no WELD 1.1.0-CR3: remover apos correcao de injecao de
	 * ExternalAnnotatedType.
	 */
	@Inject
	@Override
	public void setFacade(@QPlcCurrent IPlcFacade facade) {
		super.setFacade(facade);
	}
	
	/**
	 * Recupera os objetos na camada de persistencia
	 * 
	 * @see com.powerlogic.jcompany.controle.rest.controles.PlcBaseControle#retrieve(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void retrieve(Object identificadorEntidade) {

		try {
			// Buscando a entidade associativa
			Class<?> classeEntidadeAssociativa = plcConfigManyToManyMatrix.classeAssociativa();
			Object objetoInstanciaAssociativa = classeEntidadeAssociativa.newInstance();
			setEntity(objetoInstanciaAssociativa);
			// populando a entidade associativa com os filtros vindos do form
			beanPopulateUtil.transferMapToBean(request.getParameterMap(), objetoInstanciaAssociativa);

			// buscando a opção de seleção da matrix
			opcaoMatrix = (OpcaoMatrix) PropertyUtils.getProperty(objetoInstanciaAssociativa, plcConfigManyToManyMatrix.propriedadeOpcaoMatrix());

			//Preenchendo os MARCADOS
			List entidadeAssociativa = (List) getFacade().findList(getContext(), objetoInstanciaAssociativa, null, 0, 100);
			setColecaoEntidadeAssociativa(criaColecaoMatrixBean(entidadeAssociativa));

			setColecaoEntidade1(new ArrayList<Object>());
			setNomeEntidade1(StringUtils.capitalize(plcConfigManyToManyMatrix.propriedadeEntidade1()));
			setColecaoEntidade2(new ArrayList<Object>());
			setNomeEntidade2(StringUtils.capitalize(plcConfigManyToManyMatrix.propriedadeEntidade2()));
			
			if (opcaoMatrix.equals(OpcaoMatrix.MARCADOS)) {
				preencheEntidadesPelaAssociacao(entidadeAssociativa);
			} else if (opcaoMatrix.equals(OpcaoMatrix.DESMARCADOS)) {	
				getContext().setApiQuerySel("querySelDesmarcados");
				List listaEntidadeAssociativa =  (List) getFacade().findList(getContext(), objetoInstanciaAssociativa, null, 0, 100);
				preencheEntidadesPelaAssociacao(listaEntidadeAssociativa);
			} else {	
				// Buscando a entidade 1
				setColecaoEntidade1(recuperaObjetoEntidade(plcConfigManyToManyMatrix.classeEntidade1(), objetoInstanciaAssociativa, plcConfigManyToManyMatrix.propriedadeEntidade1()));
				// Buscando a entidade 2
				setColecaoEntidade2(recuperaObjetoEntidade(plcConfigManyToManyMatrix.classeEntidade2(), objetoInstanciaAssociativa, plcConfigManyToManyMatrix.propriedadeEntidade2()));
			}	

		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "recupera", e }, true);
		}
		retrieveAfter();
	}

	/**
	 * Realiza a persistencia dos objetos conforme parametros passados pela
	 * chamada Rest
	 * 
	 * Os obejtos chegam em um map com os pares no formato: <br>
	 * {[2_3,true],[4,false],[...]} <br>
	 * onde se o valor do segundo parametro for "true", o primeiro parametro é a
	 * junção dos códigos da entidade 1 e 2 separados por "_". Se o valor for
	 * "false", o primeiro parametro é o código da entidade associativa.
	 * 
	 * @see com.powerlogic.jcompany.controle.rest.controles.PlcBaseControle#update()
	 */
	@Override
	public void update() {
		// Recupera os parametros com os objetos
		String[] associacoes = request.getParameterValues("associacao");
		String[] campos;

		getContext().setMode(Mode.ALTERACAO);

		if (associacoes != null) {
			for (String associacao : associacoes) {
				campos = associacao.split(",");
				// Se o valor for true é para incluir (associar), false para
				// excluir (desassociar)
				if (Boolean.parseBoolean(campos[1])) {
					associa(campos[0]);
				} else {
					desassocia(campos[0]);
				}
			}
		}
		msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[] {});
		retrieve(null);
	}

	private List recuperaObjetoEntidade(Class<?> classeEntidade, Object objetoInstanciaAssociativa, String nome) {

		// Verifica a opção selecionada para pegar uma query especifica
		if (opcaoMatrix.equals(OpcaoMatrix.MARCADOS)) {
			getContext().setApiQuerySel("querySelMarcados");
		} else if (opcaoMatrix.equals(OpcaoMatrix.DESMARCADOS)) {
			getContext().setApiQuerySel("querySelDesmarcados");
		}

		List listaEntidade =  (List) getFacade().findList(getContext(), recuperaObjetoEntidadeFiltro(classeEntidade, objetoInstanciaAssociativa, nome), null, 0, 100);
		return listaEntidade;
	}


	private void preencheEntidadesPelaAssociacao(List entidadeAssociativa)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		for (Object ea : entidadeAssociativa) {
			if (!getColecaoEntidade1().contains(PropertyUtils.getProperty(ea, plcConfigManyToManyMatrix.propriedadeEntidade1()))) {
				getColecaoEntidade1().add(PropertyUtils.getProperty(ea, plcConfigManyToManyMatrix.propriedadeEntidade1()));
			}	
			if (!getColecaoEntidade2().contains(PropertyUtils.getProperty(ea, plcConfigManyToManyMatrix.propriedadeEntidade2()))) {
				getColecaoEntidade2().add(PropertyUtils.getProperty(ea, plcConfigManyToManyMatrix.propriedadeEntidade2()));
			}	
		}
		Collections.sort((List) getColecaoEntidade1(), new MatrixComparator());
		Collections.sort((List) getColecaoEntidade2(), new MatrixComparator());
	}
	
	/**
	 * Recebe os identificadores das entidades 1 e 2 e realiza a associação
	 * 
	 * @param ids
	 *            - identificadores das entidades
	 */
	private void associa(String ids) {
		String[] idObjetos = ids.split("_");
		try {
			// Criando as entidade 1
			Object objetoEntidade1 = plcConfigManyToManyMatrix.classeEntidade1().newInstance();
			PropertyUtils.setProperty(objetoEntidade1, "id", Long.parseLong(idObjetos[0]));
			// Criando as entidade 2
			Object objetoEntidade2 = plcConfigManyToManyMatrix.classeEntidade2().newInstance();
			PropertyUtils.setProperty(objetoEntidade2, "id", Long.parseLong(idObjetos[1]));
			// Criando a entidade associativa
			Object objeto = plcConfigManyToManyMatrix.classeAssociativa().newInstance();
			// Setando objetos na classe associativa
			PropertyUtils.setProperty(objeto, plcConfigManyToManyMatrix.propriedadeEntidade1(), objetoEntidade1);
			PropertyUtils.setProperty(objeto, plcConfigManyToManyMatrix.propriedadeEntidade2(), objetoEntidade2);
			setEntity(objeto);
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "associa", e }, true);
		}
		// Incluindo o objeto (realizando a associação)
		insert();
	}

	/**
	 * Recebe o id da entidade associativa e exclui (desassocia)
	 * 
	 * @param idObjeto
	 *            - id da entidade associativo
	 */
	private void desassocia(String idObjeto) {
		try {
			// recuperando a entidade associativa para exclui-la, conforme id
			// passado
			Object objeto = getFacade().edit(getContext(), plcConfigManyToManyMatrix.classeAssociativa(), Long.parseLong(idObjeto))[0];
			setEntity(objeto);
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "desassocia", e }, true);
		}
		// Excluindo o objeto (realizando a desassociação
		delete();
	}

	/**
	 * Cria a coleção de entidade associativa
	 * 
	 * @param colecao
	 *            - coleção da entidade associativa
	 * @return Coleção do objeto Dto auxiliar
	 */
	private Collection<PlcManyToManyMatrixDto> criaColecaoMatrixBean(Collection<? extends Object> colecao) {
		Collection<PlcManyToManyMatrixDto> listaAssociacoes = new ArrayList<PlcManyToManyMatrixDto>();

		for (Object objeto : colecao) {
			try {

				listaAssociacoes.add(new PlcManyToManyMatrixDto(
				// Pega o id da entidade associativa
						(Long) PropertyUtils.getProperty(objeto, "id"),
						// Pega o id da entidade 1
						(Long) PropertyUtils.getProperty(PropertyUtils.getProperty(objeto, plcConfigManyToManyMatrix.propriedadeEntidade1()), "id"),
						// Pega o id da entidade 2
						(Long) PropertyUtils.getProperty(PropertyUtils.getProperty(objeto, plcConfigManyToManyMatrix.propriedadeEntidade2()), "id"), true));
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "criaColecaoMatrixBean", e }, true);
			}
		}
		return listaAssociacoes;
	}

	/**
	 * Verifica se na classe associativa o objeto da entidade tem preenchimento
	 * e retorna o mesmo para que sua pesquisa seja feita com o filtro, caso
	 * contrário cria um
	 * 
	 * @param classeEntidade
	 *            Classe da entidade
	 * @param objetoInstanciaAssociativa
	 *            Objeto da classe Associativa preenchido com o filtro
	 * @param nomeEntidade
	 *            nome da entidade
	 * @return
	 */
	private Object recuperaObjetoEntidadeFiltro(Class classEntidade, Object objetoInstanciaAssociativa, String nomeEntidade) {

		try {
			Object objeto = PropertyUtils.getProperty(objetoInstanciaAssociativa, nomeEntidade);

			if (objeto == null) {
				objeto = classEntidade.newInstance();
			}
			return objeto;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "criaColecaoMatrixBean", e }, true);
		}
	}
	
	class MatrixComparator implements Comparator {
		  public int compare(Object o1, Object o2) {
		    String s1 = (String) o1.toString();
		    String s2 = (String) o2.toString();
		    return s1.toLowerCase().compareTo(s2.toLowerCase());
		  }
		} ///:

	public Collection<PlcManyToManyMatrixDto> getColecaoEntidadeAssociativa() {
		return colecaoEntidadeAssociativa;
	}

	public void setColecaoEntidadeAssociativa(Collection<PlcManyToManyMatrixDto> colecaoEntidadeAssociativa) {
		this.colecaoEntidadeAssociativa = colecaoEntidadeAssociativa;
	}

	public Collection<Object> getColecaoEntidade1() {
		return colecaoEntidade1;
	}

	public void setColecaoEntidade1(Collection<Object> colecaoEntidade1) {
		this.colecaoEntidade1 = colecaoEntidade1;
	}

	public Collection<Object> getColecaoEntidade2() {
		return colecaoEntidade2;
	}

	public void setColecaoEntidade2(Collection<Object> colecaoEntidade2) {
		this.colecaoEntidade2 = colecaoEntidade2;
	}

	public String getNomeEntidade1() {
		return nomeEntidade1;
	}

	public void setNomeEntidade1(String nomeEntidade1) {
		this.nomeEntidade1 = nomeEntidade1;
	}

	public String getNomeEntidade2() {
		return nomeEntidade2;
	}

	public void setNomeEntidade2(String nomeEntidade2) {
		this.nomeEntidade2 = nomeEntidade2;
	}

}