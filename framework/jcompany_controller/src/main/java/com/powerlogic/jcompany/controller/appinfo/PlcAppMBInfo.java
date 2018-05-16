/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.appinfo;

import org.apache.commons.lang.StringUtils;

public class PlcAppMBInfo {

	String path;
	String description;
	String prefix;

	public PlcAppMBInfo(){
		
	}
	
	public PlcAppMBInfo(String path, String description){
		setPath(path);
		setDescription(description);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "prefix=\"" + prefix + "\" path=\"" + path + "\" description=\"" + description + "\"";
	}

	public String getFullPath() {
		if (StringUtils.isBlank(prefix)) {
			return path;
		} else {
			return prefix + "/" + path;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public boolean equals(Object obj) {
		//se a url e igual, então o info também é.
		if(path!=null && obj instanceof PlcAppMBInfo){
			PlcAppMBInfo appMBInfo = (PlcAppMBInfo)obj;
			return path.equals(appMBInfo.getPath());
		}
		else
			return super.equals(obj);
	}
}