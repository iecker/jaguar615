package com.powerlogic.jcompany.view.jsf.component;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

public class PlcStaticCombo extends PlcDynamicCombo{
	
	static public final String	COMPONENT_TYPE	= "com.powerlogic.jsf.componente.PlcComboEstatico";
		
	/**
	 * Busca a lista de {@link SelectItem} para o combo.
	 * @return uma lista de SelectItem ou nulo se a lista for vazia.
	 */
	@SuppressWarnings("unchecked")
	protected List<SelectItem> getListaElementos() {
		
		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcI18nUtil i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		List listaRetorno = null;		
				
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		listaRetorno = super.getListaElementos();

		for(SelectItem i: (List<SelectItem>) listaRetorno){
			
			SelectItem item = new SelectItem();
			item.setLabel(i.getLabel());
			item.setValue(i.getValue());
			lista.add(item);
			
		}
			
		if(lista != null){
							
			HttpServletRequest request = contextUtil.getRequest();
			
			for(SelectItem item: lista){
				
				String label = item.getLabel();
									
				label = i18nUtil.mountLocalizedMessageAnyBundle(request, label, null);						
									
				item.setLabel(label);
				
			}				
		
		}			

		return lista;
	}	
	
}
