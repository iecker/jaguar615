/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

public class PlcSetDataModel extends DataModel {


    public PlcSetDataModel() {

        this(null);

    }


    public PlcSetDataModel(Set model) {

        super();
        setWrappedData(model);

    }

    private int index = -1;


    private Set model;

    public boolean isRowAvailable() {

        if (model == null) {
	    return (false);
        } else if ((index >= 0) && (index < model.size())) {
            return (true);
        } else {
            return (false);
        }

    }


    public int getRowCount() {

        if (model == null) {
	    return (-1);
        }
        return (model.size());

    }


    public Object getRowData() {

        if (model == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (model.toArray()[index]);
        }

    }


    public int getRowIndex() {

        return (index);

    }


    public void setRowIndex(int rowIndex) {

        if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }
        int old = index;
        index = rowIndex;
	if (model == null) {
	    return;
	}
	DataModelListener [] listeners = getDataModelListeners();
        if ((old != index) && (listeners != null)) {
            Object rowData = null;
            if (isRowAvailable()) {
                rowData = getRowData();
            }
            DataModelEvent event = new DataModelEvent(this, index, rowData);
            int n = listeners.length;
            for (int i = 0; i < n; i++) {
		if (null != listeners[i]) {
		    listeners[i].rowSelected(event);
		}
            }
        }

    }


    public Object getWrappedData() {

        return (this.model);

    }


    public void setWrappedData(Object data) {

        if (data == null) {
            model = null;
            setRowIndex(-1);
        } else {
            model = (Set) data;
            index = 0;
            setRowIndex(-1);
        }

    }

    
}
