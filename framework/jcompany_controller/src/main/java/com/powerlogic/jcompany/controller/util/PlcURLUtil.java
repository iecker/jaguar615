/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

@SPlcUtil
@QPlcDefault
public class PlcURLUtil {
	
	private Logger logAdvertenciaDesenv = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO);
	
	public PlcURLUtil() {
		
	}
	
	/**
	 * Identifica padrões de URL:
	 * /f/t/ para JSF/JPS/Tiles
	 * /f/n/ para JSF/Facelets com URL Lógica e leiaute Universal
	 * /f/*.xthml para JSF/Facelets com URL Física
	 * @return Token contendo convenção para URL (Ex: funcionariomdt, departamentoman)
	 */
	public String resolveCollaborationNameFromUrl(HttpServletRequest request)  {
		
		if(request != null) {
			// Evita fazer toda vez que precisar acessar meta-dados
			if (request.getAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA) != null)
				return (String)request.getAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA);
			else {
				String url = request.getRequestURI();
				// TODO - Trocado para funcionar até a expressao regular funcionar (continuou dando problemas)
				/*
				Matcher matcher = pattern.matcher(url);
				if(matcher.matches()) {
					return matcher.group(1);
				}*/
				
				final String padraoFacelets = ".xhtml";
				
				// Pode ser /f/t (Tiles), /f/n/ (Facelets Universal Dinamico) 
				int posIni = url.indexOf("/f/");
				int posFimXHTML = url.indexOf(padraoFacelets);
				
				int posFim = url.length();
				
				if (posIni>-1 && posFimXHTML==-1) {
				
					// Funciona para JSF tanto com JSP/Tiles (/f/t) quanto com Facelets (/f/n)
					int posArg = url.indexOf("?",posIni);
					if (posArg >-1)
						posFim = posArg;
					
					String nomeActionSemBarra = url.substring(posIni+5,posFim);
					if (nomeActionSemBarra.startsWith("plc/"))
						nomeActionSemBarra = nomeActionSemBarra.substring(4);
					
					request.setAttribute(PlcJsfConstantes.URL_SEM_BARRA,nomeActionSemBarra);
					
					return nomeActionSemBarra;
				
				} else if (url.contains(padraoFacelets)){
					
					// Neste caso é endereçamento físico (terminado com nome do artefato e .xhtml
					posFim = url.indexOf(padraoFacelets);
					String nomeActionAux = url.substring(0,posFim);
					posIni = nomeActionAux.lastIndexOf("/");
					int posArg = url.indexOf("?",posIni);
					if (posArg >-1)
						posFim = posArg;
					
					String nomeActionSemBarra = url.substring(posIni+1,posFim);
					
					
					/* TODO Remover após homologação
					if (nomeActionSemBarra.toUpperCase().endsWith(FormPattern.Tab.name())){
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, FormPattern.Tab.name()) + "tab";
					} else if (nomeActionSemBarra.endsWith(FormPattern.Mdt.name())) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, FormPattern.Mdt.name()) + "mdt";
					} else if (nomeActionSemBarra.endsWith(FormPattern.Mds.name())) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, FormPattern.Mds.name()) + "mds";
					} else if (nomeActionSemBarra.endsWith(FormPattern.Mad.name())) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, FormPattern.Mad.name()) + "mad";
					} else if (nomeActionSemBarra.endsWith(FormPattern.Mas.name())) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, FormPattern.Mas.name()) + "mas";
					} else if (nomeActionSemBarra.endsWith("Man")) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, "Man") + "man";
					} else if (nomeActionSemBarra.endsWith("Sel")) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, "Sel") + "sel";
					} else if (nomeActionSemBarra.endsWith("Usu")) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, "Usu") + "usu";
					} else if (nomeActionSemBarra.endsWith("Apl")) {
						nomeActionSemBarra = StringUtils.substringBeforeLast(nomeActionSemBarra, "Apl") + "apl";
					}
					*/
					
					request.setAttribute(PlcJsfConstantes.URL_SEM_BARRA,nomeActionSemBarra);
					
					return nomeActionSemBarra;
					
				}
				
				if (!url.contains("__ADFv__"))// Para url de data, 
					logAdvertenciaDesenv.debug( this.getClass().getCanonicalName()+": Url nao esta seguindo convencao do jCompany /f/t/ URL: "+url);
				
				return "";
			}
		}
		return "";
	}
	
	public String getUrlXhtml(String url) {
		
		int posIni = url.indexOf("/f/t/");
		int posFim = url.length();
		
		posFim = url.indexOf(".xhtml");
		String nomeActionAux = url.substring(0,posFim);
		posIni = nomeActionAux.lastIndexOf("/");
		int posArg = url.indexOf("?",posIni);
		if (posArg >-1) {
			posFim = posArg;
		}
		
		String nomeActionOriginalSemBarra = url.substring(posIni+1,posFim) + ".xhtml";
		
		return nomeActionOriginalSemBarra;

	}
	
	public String resolvePackageFromUrl(HttpServletRequest request)  {
		String aux = resolveCollaborationNameFromUrl(request);
		if(aux != null) {
			if (aux.indexOf('/')>-1) {
				return "."+aux.replace('/', '.');
			}
			else {    
				return ".app."+aux;
			}
		}
		return ".app";
	}

	public String resolveModuleAcronymFromCollaboration(HttpServletRequest request)  {
		String siglaMod = "";
		String urlColaboracao =	resolveCollaborationNameFromUrl(request);
		if (urlColaboracao.contains("/")){
			Pattern pattern = Pattern.compile("(.*)(\\/)(.*)");
			Matcher matcher = pattern.matcher(urlColaboracao);
			if (matcher.find())
				siglaMod = matcher.group(1); 
		}
		return siglaMod;
	}
	
	/**
	 * Evita chamada do filter para tentar pegar metadados para imagens e URLs "secundárias". Revisar utilizando /res-plc/midia/ e /res-plc/css e /res-plc/javascript, etc...
	 * porque somente /plc confunde com opcoes do modulo plc.
	 */
	public boolean isMainRequest(HttpServletRequest request) {
		
		String url = request.getRequestURI();
		
		return url != null; 
	
	}
	
    /**
     * jCompany 2.0 Define nome do alias do caso de uso em função do action-mapping.
     * @param nome
     * @return String contendo o nome do Action sem a termição.
     */
 	public String getAliasUseCase (String nomeAction) {
 		
 	    String nome = nomeAction.toLowerCase();

 	    /*
 	     * TODO Remover após homologaçao
 	     *
		if (nome.endsWith("popupsel")) {
        	nome = nomeAction.substring(1,nome.lastIndexOf("popupsel"));
		} else if (nome.endsWith("selpopup")) {
       		nome = nomeAction.substring(1,nome.lastIndexOf("selpopup"));
        } else if (nome.endsWith("popup")) {
            nome = nomeAction.substring(1,nome.lastIndexOf("popup"));
        } else if (nome.endsWith("mestdetman")) {
            nome = nomeAction.substring(1,nome.lastIndexOf("mestdetman"));
        } else if (nome.endsWith("mestdet")) {
            nome = nomeAction.substring(1,nome.lastIndexOf("mestdet"));
        } else if (nome.endsWith("rel")) {
            nome = nomeAction.substring(1,nome.lastIndexOf("rel"));
        } else */
        if (nome.length()>3) {
			String sufixo = nome.substring(nome.length()-3);
			int idx = ArrayUtils.indexOf(PlcConfigUtil.SUFIXOS_URL, sufixo);
			if (idx>-1) {
				if(nomeAction.startsWith("/")){
					nome = nomeAction.substring(1,nome.lastIndexOf(sufixo));
				} else {
					nome = nomeAction.substring(0,nome.lastIndexOf(sufixo));
				}
			} else {
				// não existe alias
				if(nome.startsWith("/plc/")) { 
					return nomeAction.substring(5, nome.length());
				} else if(nome.startsWith("/")){
		        	return nomeAction.substring(1,nome.length());
				}	
				return nomeAction;
			}
        } else if (nome.equals("/")) {
        	return "inicial";
        } else if(nome.startsWith("/")){
        	nome = nomeAction.substring(1,nome.length());
        } else {
        	nome = nomeAction;
        }

 	    return nome;

 	}

	public String getSuffixUseCase(String nomeActionAux) {
		String aux = getAliasUseCase(nomeActionAux);
		return StringUtils.capitalize(nomeActionAux.replace(aux, "").replace("/", ""));
	}

}
