package com.powerlogic.jcompany.extension.manytomanypanel.commons;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Converter utilizado para manipulação das listas
 *  
 * @author Mauren Ginaldo Souza
 *
 */
@FacesConverter(value="entityConverter")
public class SimpleEntityConverter implements Converter {  
  
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {  
        if (value != null) {  
            return this.getAttributesFrom(component).get(value);  
        }  
        return null;  
    }  
  
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {  
  
        if (value != null  
                && !"".equals(value)) {  
  
            // adiciona item como atributo do componente  
            String key = "";
			try {
				key = PropertyUtils.getProperty(value, "id").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}   
            this.getAttributesFrom(component).put(key, value);  
  
            return key;  
        }  
  
        return (String) value;  
    }  
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    }  
  
} 