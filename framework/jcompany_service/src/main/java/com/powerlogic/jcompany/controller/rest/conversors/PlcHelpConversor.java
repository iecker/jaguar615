package com.powerlogic.jcompany.controller.rest.conversors;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseHelpController;
import com.powerlogic.jcompany.controller.rest.extensions.PlcControllerInfo;
import com.powerlogic.jcompany.controller.rest.extensions.PlcControllerInfo.PlcParamInfo;

@SPlcConversor
@QPlcConversorMediaType( { "*/*","text/html" })
@QPlcConversorToController(type=PlcBaseHelpController.class)
public class PlcHelpConversor<C> extends PlcBaseConversor<C> {
	
	@Override
	public void writeEntityCollection(C _controller, OutputStream outputStream) {
		
		try {
			PlcBaseHelpController controller = (PlcBaseHelpController)_controller;
			
			StringBuilder buffer = new StringBuilder();
			buffer.append("<html>");
			buffer.append("<head>");
			buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			buffer.append("<title>API jCompany Service</title>");
			buffer.append("</head>");
			buffer.append("<body>");
			buffer.append("<h1>API jCompany Service</h1>");
			buffer.append("<h2>Serviços disponíveis:</h2><br>");
			buffer.append("<ul>");
			for(Object _info : controller.getEntityCollection()){
				PlcControllerInfo info = (PlcControllerInfo)_info;
				buffer.append("<li>");
				buffer.append("<b>Nome:</b> "+info.getName());
				buffer.append("<br><a href='"+controller.getRequest().getContextPath()+controller.getRequest().getServletPath()+"/service/help("+info.getName()+")'>Link Documentação</a>");
				buffer.append("<br><b>Url:</b> "+info.getPath());
				if(!info.getParameters().isEmpty()){
					buffer.append("<br><b>Parâmetros:</b><br>");
					buffer.append("<ul>");
					for(PlcParamInfo paramInfo : info.getParameters()){
						buffer.append("<li>"+paramInfo+"</li>");
					}
					buffer.append("</ul>");
				}
				buffer.append("</li><br>");
				
			}
			buffer.append("</ul>");
			buffer.append("</body></html>");
			IOUtils.write(buffer.toString(), outputStream, "utf-8");
		} catch(PlcException plcE){
			throw plcE;			
		} catch (IOException e) {
			throw new PlcException("PlcHelpConversor", "writeEntityCollection", e, null, "");
		}
	}
	
	@Override
	public void writeEntity(C _controller, OutputStream outputStream) {
		try {
			PlcBaseHelpController controller = (PlcBaseHelpController)_controller;
			
			IOUtils.write(((PlcControllerInfo)controller.getEntity()).getHtmlDocPath(), outputStream);
		} catch(PlcException plcE){
			throw plcE;				
		} catch (IOException e) {
			throw new PlcException("PlcHelpConversor", "writeEntity", e, null, "");
		}
	}

}
