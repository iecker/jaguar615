package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value="rowCounter")
@RequestScoped
public class PlcRowCounter implements Serializable {

    private Long index 					= 0l;
    private Long indexIteration 		= 0l;
    private Long indexDetBeforeCounter 	= 0l;
    private Long indexSubDet 			= 0l;

    public void setIndex(Long index) {
    	this.indexIteration = index;
		this.index = index;
	}

	public Long getIndex() {
		++indexIteration;
        return ++index;
    }
	
	public Long getIndexIteration() {
		return indexIteration;
	}

	public void setIndexIteration(Long indexIteration) {
		this.indexIteration = indexIteration;
	}

	public Long getIndexDetBeforeCounter() {
		indexDetBeforeCounter = index;
        return indexDetBeforeCounter;
    }

	public Long getIndexSubDet() {
		return ++indexSubDet;
	}

	public void setIndexSubDet(Long indexSubDet) {
		this.indexSubDet = indexSubDet;
	}
	
	

}
