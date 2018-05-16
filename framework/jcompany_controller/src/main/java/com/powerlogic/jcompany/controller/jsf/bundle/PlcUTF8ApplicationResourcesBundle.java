package com.powerlogic.jcompany.controller.jsf.bundle;


import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class PlcUTF8ApplicationResourcesBundle extends ResourceBundle {
    
    public PlcUTF8ApplicationResourcesBundle() {
        setParent(ResourceBundle.getBundle("ApplicationResources", 
            FacesContext.getCurrentInstance().getViewRoot().getLocale(), new PlcUTF8Control()));
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }

  
}