/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.model.mock;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;

/**
 * jCompany 2.7 Mock base para interface Façade. Herdar e devolver valores
 * conforme necessário nos testes.
 * @since jCompany 2.7.3
 */
public class PlcFacadeImplMock implements IPlcFacade {

	private Object voMock = new Object();

	private Object objMock = new Object();

	private Object[] objsMock = new Object[] {};

	private Long totalRegistrosMock = new Long(0);

	private PlcFile arquivoVOMock = new PlcFile();

	private List listaMock = new ArrayList();

	/**
	 * @since jCompany 2.7.3
	 * @return Retorna o listaMock.
	 */
	public List getListaMock() {
		return this.listaMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @param listaMock
	 *           O listaMock a ser definido.
	 */
	public void setListaMock(List listaMock) {
		this.listaMock = listaMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @return Retorna o objMock.
	 */
	public Object getObjMock() {
		return this.objMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @param objMock
	 *           O objMock a ser definido.
	 */
	public void setObjMock(Object objMock) {
		this.objMock = objMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @return Retorna o objsMock.
	 */
	public Object[] getObjsMock() {
		return this.objsMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @param objsMock
	 *           O objsMock a ser definido.
	 */
	public void setObjsMock(Object[] objsMock) {
		this.objsMock = objsMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @return Retorna o totalRegistrosMock.
	 */
	public Long getTotalRegistrosMock() {
		return this.totalRegistrosMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @param totalRegistrosMock
	 *           O totalRegistrosMock a ser definido.
	 */
	public void setTotalRegistrosMock(Long totalRegistrosMock) {
		this.totalRegistrosMock = totalRegistrosMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @return Retorna o voMock.
	 */
	public Object getVoMock() {
		return this.voMock;
	}

	/**
	 * @since jCompany 2.7.3
	 * @param voMock
	 *           O voMock a ser definido.
	 */	
	public void setVoMock(Object voMock) {
		this.voMock = voMock;
	}

	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public Object saveObject(PlcBaseContextVO context, Object VO)  {
		return voMock;
	}

	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public void saveTabular(PlcBaseContextVO context, Class classe, List lista)  {
	}

	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public void deleteObject(PlcBaseContextVO context, Object vo){
	}

	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public Object[] edit(PlcBaseContextVO context, Class classe, Object id)  {
		return null;
	}

	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public PlcFile downloadFile(PlcBaseContextVO context, Class classe, Object id)  {
		return arquivoVOMock;
	}


	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public java.util.List findSimpleList(PlcBaseContextVO contextParam,
			Class classe, String orderByDinamico)  {
		return listaMock;
	}


	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public List findNavigation(PlcBaseContextVO context, Class classeOrigemPlc, Object pk, Class classeDestinoPlc)  {
		
		return listaMock;
	}
	
	/**
	 * @since jCompany 2.7.3
	 */
	@Override	
	public String[] findExceptionMessage(Throwable causaRaiz) {
		
		return null;
	}

	
	@Override
	public List findListTreeView(PlcBaseContextVO context, Class classeBase, Object id, Class classeFilha, long posIni)  {
		return listaMock;
	}


	@Override
	public Object findAggregateLookup(PlcBaseContextVO context, Object baseVO, Map<String, Object> propriedadesValores)  {
//		if (StringUtils.isBlank(baseVO.getIdAux()))
//			baseVO.setId(new Long("12345"));
		return baseVO;
	}


	@Override
	public List findList(PlcBaseContextVO context, Object entidadeArg,
			String orderByDinamico, int primeiraLinha, int maximoLinhas) {

		return null;
	}

	@Override
	public Long findCount(PlcBaseContextVO context, Object entidadeArg) {

		return null;
	}

	@Override
	public Collection findListPagedDetail(PlcBaseContextVO context, PlcConfigDetail configDetalhe, Object entidadeMestre, int numPorPagina, String ordenacaoPlc, int posicaoAtual, boolean incluiArgPai) throws NoSuchFieldException {
		return null;
	}

	@Override
	public Object[] findObjectPagedDetail(PlcBaseContextVO context, Class classe, Object id, Long posAtual, String ordenacaoPlc, PlcConfigDetail... detalhes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, InstantiationException {
		return null;
	}

	@Override
	public void checkBeanValidation(Object entityPlc, Class<?>... groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBeanValidation(List entityListPlc, Class<?>... groups) {
		// TODO Auto-generated method stub
		
	}
}
