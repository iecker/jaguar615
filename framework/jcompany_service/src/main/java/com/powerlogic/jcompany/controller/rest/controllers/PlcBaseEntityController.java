/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.controllers;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.controller.jsf.interceptors.PlcExceptionHandlerJSF;
import com.powerlogic.jcompany.controller.jsf.util.PlcDeclarativeValidationUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

public abstract class PlcBaseEntityController<E, I> extends PlcBaseController<E, I> {

	private IPlcFacade facade;

	private PlcBaseContextVO context;

	private Integer collectionPage;

	private Integer collectionPageRecords;

	private String collectionOrder;

	private Long collectionTotal;
	
	private String[] selectedProperties;
	
	@Inject
	@QPlcDefault 
	protected PlcMsgUtil msgUtil;
	
	@Inject
	@QPlcDefault
	private PlcClassLookupUtil lookupUtil;
	
	@Inject
	@QPlcDefault
	protected PlcDeclarativeValidationUtil declarativeValidationUtil;
	
	@Inject
	@QPlcDefault
	PlcExceptionHandlerJSF exceptionHandlerJSF;
	
	// Template Method
	protected void insertBefore() {
	}

	@Override
	public void insert() {
		try {
			insertBefore();
			if (getEntityCollection() != null && !getEntityCollection().isEmpty()) {
				getFacade().saveTabular(getContext(), getEntityType(), (List<?>) getEntityCollection());
			} else {
				setEntity((E) getFacade().saveObject(getContext(), getEntity()));
			}
			lookupUtil.storeClassLookup(getEntityType(), (List<?>) getEntityCollection(), true);
			msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[]{});
			insertAfter();
		} catch (Exception e) {
			handleExceptions(e);
		}	
	}

	// Template Method
	protected void insertAfter() {
	}

	// Template Method
	protected void retrieveBefore(I identificadorEntidade) {
	}

	@Override
	public void retrieve(I identificadorEntidade) {
		retrieveBefore(identificadorEntidade);
		if (identificadorEntidade == null) {
			try {
				setEntity(getEntityType().newInstance());
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_OBJECT_INSTANCE, e, null);
			}
		} else {
			Object[] result = getFacade().edit(getContext(), getEntityType(), identificadorEntidade);
			if (result != null && result.length > 0) {
				setEntity((E) result[0]);
			} else {
				setEntity(null);
			}
		}
		retrieveAfter();
	}

	// Template Method
	protected void retrieveAfter() {
	}

	// Template Method
	protected void updateBefore() {
	}

	@Override
	public void update() {
		try {
			updateBefore();
			getContext().setMode(PlcBaseContextVO.Mode.ALTERACAO);
			setEntity((E) getFacade().saveObject(getContext(), getEntity()));
			updateAfter();
			msgUtil.msg(PlcBeanMessages.MSG_SAVE_SUCCESS, new Object[] {});
		} catch (Exception e) {
			handleExceptions(e);
		}
	}

	// Template Method
	protected void updateAfter() {
	}

	// Template Method
	protected void deleteBefore() {
	}

	@Override
	public void delete() {
		deleteBefore();
		getFacade().deleteObject(getContext(), getEntity());
		setEntity(null);
		deleteAfter();
		msgUtil.msg(PlcBeanMessages.MSG_DELETE_SUCCESS, new Object[] {});
	}

	// Template Method
	protected void deleteAfter() {
	}

	// Template Method
	protected void retrieveCollectionBefore() {
	}

	@Override
	public void retrieveCollection() {
		retrieveCollectionBefore();
		IPlcFacade facade = getFacade();
		try {
			if (getCollectionPage() != null && getCollectionPageRecords() != null) {
				retrievePagedCollection();
			} else {
				setEntityCollection(facade.findList(getContext(), getEntity(), getCollectionOrder(), -1, 100));
			}
		} catch (Exception e) {
			handleExceptions(e);
		}
		retrieveCollectionAfter();
	}

	protected void retrievePagedCollection() {
		
		setCollectionTotal(facade.findCount(getContext(), getEntity()));
		
		Integer page = getCollectionPage();
		Integer pageRecords = getCollectionPageRecords();
		String order = getCollectionOrder();
		
		Long pageTotal = getCollectionTotal();
		if (pageTotal > 0) {
			if ((pageTotal % pageRecords) > 0) {
				pageTotal = ((pageTotal / pageRecords) + 1);
			} else {
				pageTotal = (pageTotal / pageRecords);
			}
		}
		
		if (page > pageTotal)
			setCollectionPage(Integer.valueOf(String.valueOf(pageTotal)));

		setEntityCollection(facade.findList(getContext(), getEntity(), order, ((getCollectionPage() - 1) * pageRecords), pageRecords));
	}

	// Template Method
	protected void retrieveCollectionAfter() {
	}

	protected void handleExceptions(Exception plcE) {
		if (plcE instanceof PlcException) {
			if (PlcBeanMessages.JCOMPANY_ERROR_BEAN_VALIDATION.toString().equals(((PlcException) plcE).getMessageError())) {
				msgUtil.availableInvariantMessages((Set<ConstraintViolation<Object>>) ((PlcException) plcE).getObjectError(), null);
			} else {
				msgUtil.createMessageVariant((PlcException) plcE, null, PlcMessage.Cor.msgVermelhoPlc.toString());
			}
		} else {	
			msgUtil.msg(PlcBeanMessages.MSG_SAVE_ERROR, plcE.getMessage());
		}
		try {
			exceptionHandlerJSF.handle(plcE);
		} catch (ServletException e) {
			e.printStackTrace();
		}

	}
	
	
	@Inject
	public void setContext(@QPlcCurrent PlcBaseContextVO context) {
		this.context = context;
	}

	public PlcBaseContextVO getContext() {
		return context;
	}

	@Inject
	public void setFacade(@QPlcDefault IPlcFacade facade) {
		this.facade = facade;
	}

	public IPlcFacade getFacade() {
		return facade;
	}

	public Integer getCollectionPage() {
		return collectionPage;
	}

	@Inject
	public void setCollectionPage(@QueryParam("plcPage") Integer collectionPage) {
		this.collectionPage = collectionPage;
	}

	public Integer getCollectionPageRecords() {
		return collectionPageRecords;
	}

	@Inject
	public void setCollectionPageRecords(@QueryParam("plcViewRecords") Integer collectionPageRecords) {
		this.collectionPageRecords = collectionPageRecords;
	}

	public String getCollectionOrder() {
		return collectionOrder;
	}

	@Inject
	public void setCollectionOrder(@QueryParam("order") String collectionOrder) {
		this.collectionOrder = collectionOrder;
	}

	public Long getCollectionTotal() {
		return collectionTotal;
	}

	@Inject
	public void setCollectionTotal(@QueryParam("total") Long collectionTotal) {
		this.collectionTotal = collectionTotal;
	}

	@Inject
	public void setSelectedProperties(@QueryParam("plcSelProps") String selectedProperties) {
		this.selectedProperties = (StringUtils.isNotEmpty(selectedProperties) ? selectedProperties.split(",") : null) ;
	}

	public String[] getSelectedProperties() {
		return selectedProperties;
	}

}
