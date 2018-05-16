/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.mock;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;

public class IPlcFacadeMock implements IPlcFacade {

	private Object entity;

	@Override
	public IPlcFile downloadFile(PlcBaseContextVO context,
			Class classe, Object id)  {
		return null;
	}
	
	@Override
	public void deleteObject(PlcBaseContextVO context, Object vo) {
	}


	@Override
	public Object saveObject(PlcBaseContextVO context, Object VO)  {
		return null;
	}

	@Override
	public void saveTabular(PlcBaseContextVO context, Class classe, List lista)  {
	}

	public void setPlcBaseVO(Object entity){
		this.entity = entity;
	}

	
	@Override
	public Object findAggregateLookup(PlcBaseContextVO context,
			Object baseVO, Map<String, Object> propriedadesValores) {
		return baseVO;
	}


	@Override
	public List findListTreeView(PlcBaseContextVO context,
			Class classeBase, Object id, Class classeFilha, long posIni) {
		return null;
	}

	
	@Override
	public List findSimpleList(PlcBaseContextVO contextParam,
			Class classe, String orderByDinamico)  {
		return new ArrayList<Object>();
	}

	@Override
	public String[] findExceptionMessage(Throwable causaRaiz) {
		return null;
	}

	@Override
	public List findNavigation(PlcBaseContextVO context,
			Class classeOrigemPlc, Object pk, Class classeDestinoPlc) {
		return null;
	}

	@Override
	public Object[] edit(PlcBaseContextVO context, Class classe, Object id)  {
		return null;
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
