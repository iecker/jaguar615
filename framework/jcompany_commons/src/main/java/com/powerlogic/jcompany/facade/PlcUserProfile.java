package com.powerlogic.jcompany.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PlcUserProfile implements Serializable{

	private static final long serialVersionUID = 1L;
	private String login;
	private Map plcSegurancaVertical;
	
	// jSecurity
	private List<String> grupos;
	private List<String> roles;

	
}
