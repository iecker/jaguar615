/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.appinfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcSecurityFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.servlet.PlcServletContextProducer;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

/**
 * Lê os arquivos web.xml e faces-config.xml para registrar em memória os
 * actions de cada módulo para ser utilizado no cadastro online automático no
 * jSecurity (interface web).
 * 
 * @author Roberto Badaró
 */
@SPlcUtil
@QPlcDefault
public class PlcAppInfoUtil {

	private static final String APP_INFO_CACHE_KEY = "jsecurity_recursos_app";

	private PlcAppInfo appInfo = null;

	@Inject
	@QPlcDefault
	private ServletContext servletContext;

	@Inject
	@QPlcDefault
	protected PlcCacheUtil cacheUtil;

	@Inject
	@QPlcDefault
	protected PlcMetamodelUtil metamodelUtil;
	
	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault
	protected PlcContextUtil contextUtil;

	
	/**
	 * Carrega as informações de actions configurados para a aplicação.
	 * 
	 * @param ctx
	 * @throws ServletException
	 */
	public void config(ServletContext ctx) throws ServletException {

		setServletContext(ctx);
		
		appInfo = getAppInfo();

		if (appInfo == null) {
			
			String siglaApp =configUtil.getConfigApplication().application().definition().acronym();
			if(siglaApp==null){
				siglaApp  = StringUtils.defaultString(ctx.getInitParameter("siglaAplicacao"));
			}
			String nomeApp = configUtil.getConfigApplication().application().definition().name();
			if(nomeApp==null){
				nomeApp = StringUtils.defaultString(ctx.getInitParameter("nomeAplicacao"));
			}

			appInfo = new PlcAppInfo(siglaApp.toUpperCase(), nomeApp, new ArrayList<PlcAppMBInfo>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());

		}

		// Caso as actions já estejam cadastradas, não há necessidade de
		// configurá-las novamente
		if (!appInfo.getActions().isEmpty())
			return;

		appInfo.setConfigured(true);

		try {
			// Registra navigation-rules JSF
			configJSF(ctx, appInfo);

			// Retorna filterDefs declarados
			configFilterDefs(ctx, appInfo);
			// configuraFilterParam (ctx, info);

			configWithMetamodel(ctx, appInfo);
			
			appInfo.setConfigured(false);
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Throwable e) {
			if (e instanceof ServletException) {
				throw (ServletException) e;
			} else {
				throw new ServletException(e);
			}
		}
	}

	/**
	 * Altera os dados de um PlcAppInfo a partir dos dados do Metamodel.
	 * @param ctx
	 * @param appInfo
	 */
	protected void configWithMetamodel(ServletContext ctx, PlcAppInfo appInfo) {
		
		if(ctx!=null){
			setServletContext(ctx);
		}
		
		List<String> allUriMB = metamodelUtil.getAllUriMB();
		
		List<PlcAppMBInfo> allActions = appInfo.getActions();
		
		for (String uriMB : allUriMB) {
			List<PlcAppMBInfo> l = findAllUCWithURI(uriMB);
			for(PlcAppMBInfo info : l){
				if (!allActions.contains(info)) {
					allActions.add(info);
				}
			}
		}
	}

	/**
	 * Descobre todas as urls de recursos a partir de uma uri (identificador) de caso de uso.
	 * @param uri
	 * @return
	 */
	public List<PlcAppMBInfo> findAllUCWithURI(String uri) {

		List<PlcAppMBInfo> appMBInfos = new ArrayList<PlcAppMBInfo>();
		PlcConfigCollaborationPOJO configCollaborationPOJO = configUtil.getConfigCollaboration(uri);
		PlcAppMBInfo info;
		String url = null, dirBase = null;
		File file = null;
		File[] pages = null;
		if (configCollaborationPOJO != null) {
			// obtendo diretorio base dos xhtmls
			dirBase = configCollaborationPOJO.formLayout().dirBase();
			file = getBaseDirectory(dirBase, uri);

			// obtendo as paginas.
			if ((pages = file.listFiles()) != null) {
				for (File page : pages) {
					for (String suffix : PlcConfigUtil.SUFIXOS_URL) {
						// descobrindo qual o caso de uso (man, mdt...)

						if (page.getName().contains(StringUtils.capitalize(suffix+"."))) {
							
							/*
							 * Garante que estou trantando arquivos para a uri em questão,
							 * pois um mesmo diretorio pode servir 2 ou + uri's
							 * Padrão de arquivos: {identificadorCDU}{sufixo}.{extensao}
							 */
							if (StringUtils.startsWithIgnoreCase(page.getName(), uri) ){
								// monta a url a partir do nome do xhtml
								url = uri + (uri.endsWith(suffix)?"":suffix);
								
								//TODO: Fábio Mendes => Rever esta parte do código, partnner de URL
								info = new PlcAppMBInfo("f/n/" + url, url);
								appMBInfos.add(info);
							}
						}
					}
				}
			}
		}
		return appMBInfos;
	}
	


	/**
	 * Descobre o diretorio base dos xhtmls.
	 * @param dirBase
	 * @param uri
	 * @return
	 */
	protected File getBaseDirectory(String dirBase, String uri) {
		File file;
		
		if(getServletContext()==null){
			PlcServletContextProducer contextProducer = PlcCDIUtil.getInstance().getInstanceByType(PlcServletContextProducer.class, QPlcDefaultLiteral.INSTANCE);
			if(contextProducer.getServletContext()!=null) {
				setServletContext(contextProducer.getServletContext());
			}
			else{
				setServletContext(contextUtil.getApplicationContext());
			}
		}
		
		String complemento = getServletContext().getRealPath("/");
		if (StringUtils.isEmpty(complemento)) {
			try {
				complemento = getServletContext().getResource("/").getFile();
			} catch(Exception e) {}	
		}	
		if (StringUtils.isEmpty(dirBase)) {
			dirBase = "/WEB-INF/fcls/" + uri;
			file = new File(complemento + dirBase);
			if(!file.isDirectory()){
				dirBase=dirBase.substring(0, dirBase.length()-3);
				file = new File(complemento + dirBase);
			}
		}
		else {
			file = new File(complemento + dirBase);
		}
		return file;
	}

	
	protected PlcAppInfo getAppInfo() {
		return appInfo;
	}

	protected void configFilterDefs(ServletContext servletContext, PlcAppInfo info) {
		try {
			IPlcSecurityFacade serviceFacade = getServiceFacade(IPlcSecurityFacade.class);
			List<String> filterDefs = serviceFacade.recuperaFilterDefs(null, null);
			info.getFilterDefs().addAll(filterDefs);
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lê configurações JSF, se a aplicação utilizar. Verifica a declaração
	 * dentro do web.xml.
	 * 
	 * @param ctx
	 * @param info
	 * @throws ServletException
	 */
	protected void configJSF(ServletContext ctx, PlcAppInfo info) throws ServletException, IOException {
		List<String> modulos = findFacesConfigJSF(ctx);
		if (modulos != null) {
			readFaceConfigFiles(ctx, info, modulos);
		}
	}

	/**
	 * Lê o arquivo web.xml para descobrir os módulos JSF configurados.
	 * 
	 * @param ctx
	 * @return
	 * @throws IOException
	 */
	protected List<String> findFacesConfigJSF(ServletContext ctx) throws IOException {

		List<String> l = new ArrayList<String>();

		StringBuffer bf = new StringBuffer(loadFile(ctx, "/WEB-INF/web.xml"));

		if (bf.indexOf("javax.faces.webapp.FacesServlet") == -1) {
			return null;
		}

		// Procura por arquivos faces-config.xml
		String[] pathsDefault = { "/META-INF", "/WEB-INF" };

		for (String path : pathsDefault) {
			String f = path + "/faces-config.xml";
			if (ctx.getResourceAsStream(f) != null) {
				l.add(f);
			}
		}

		// Módulos declarados
		int ini = bf.indexOf("javax.faces.CONFIG_FILES");
		if (ini != -1) {
			int fim = bf.indexOf("</param-value>", ini);

			String modulos = bf.substring(ini, fim + "</param-value>".length());
			modulos = StringUtils.substringBetween("<param-value>", "</param-value>");

			if (!StringUtils.isBlank(modulos)) {
				String[] cfgs = modulos.split(",");
				for (int i = 0; i < cfgs.length; i++) {
					l.add(cfgs[i].trim());
				}
			}
		}

		return l;
	}

	protected void readFaceConfigFiles(ServletContext ctx, PlcAppInfo info, List<String> l) throws IOException {

		if (l != null && !l.isEmpty()) {
			int t = l.size();
			for (int i = 0; i < t; i++) {
				String cfg = l.get(i);

				registryNavigationRules(ctx, cfg, info);
			}
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param arquivo
	 * @param info
	 * @throws IOException
	 */
	protected void registryNavigationRules(ServletContext ctx, String arquivo, PlcAppInfo info) throws IOException {

		String xml = loadFile(ctx, arquivo);
		if (xml.indexOf("<navigation-rule>") != -1) {

			List<PlcAppMBInfo> l = getNavigationRules(xml);

			if (l != null) {
				info.getActions().addAll(l);
			}
		}
	}

	/**
	 * 
	 * @param bf
	 * @return
	 */
	protected List<PlcAppMBInfo> getNavigationRules(String bf) {
		List<PlcAppMBInfo> l = new ArrayList<PlcAppMBInfo>();

		String s = StringUtils.substringBetween(bf, "<navigation-rule>", "</navigation-rule>");
		while (s != null) {

			String path = StringUtils.substringBetween(bf, "<from-view-id>", "</from-view-id>");
			String dsc = StringUtils.substringBetween(bf, "<display-name>", "</display-name>");

			if (!StringUtils.isBlank(path)) {

				PlcAppMBInfo act = new PlcAppMBInfo();

				act.setPrefix("");
				act.setPath("f" + path.trim());
				act.setDescription(dsc);

				l.add(act);
			}

			String sRemove = "<navigation-rule>" + s + "</navigation-rule>";

			bf = bf.replace(sRemove, "");
			s = StringUtils.substringBetween(bf, "<navigation-rule>", "</navigation-rule>");
		}

		return l;
	}

	/**
	 * Retorna uma linha do StringBuffer. Procura o delimitador LF (\n) ou CR
	 * (\r). A substring encontrada é removida do buffer.
	 * 
	 * @param bf
	 * @return
	 */
	protected String getLine(StringBuffer bf) {
		String linha = null;

		int lf = bf.indexOf("\n");
		if (lf == -1) {
			lf = bf.indexOf("\r");
		}
		if (lf != -1) {
			linha = bf.substring(0, lf).trim();
			bf.delete(0, lf + 1);
		}

		return linha;
	}

	public void responseAppInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Guarda o charset em uso para reconfiguracao apos reset!
		String appInfoXml = getAppInfoXml();
		String characterEncoding = response.getCharacterEncoding();
		// Prepara o response.
		response.reset();
		response.setCharacterEncoding(characterEncoding);
		response.setContentType("text/html");
		// Escreve a saida.
		PrintWriter writer = response.getWriter();
		writer.write(appInfoXml);
		writer.flush();
	}

	public String getAppInfoXml() {
		
		PlcAppInfo appInfo = getAppInfo();

		StringBuilder out = new StringBuilder();

		out.append("<app-info>\n");

		if (appInfo != null) {

			out.append("<aplicacao sigla=\"").append(appInfo.getApplicationAcronym()).append("\" nome=\"").append(appInfo.getApplicationName()).append("\"/>\n");
			out.append("<recursos>\n");

			List<PlcAppMBInfo> l = appInfo.getActions();
			if (l != null && !l.isEmpty()) {
				int t = l.size();
				for (int i = 0; i < t; i++) {
					PlcAppMBInfo act = l.get(i);
					out.append("<recurso nome=\"").append(act.getFullPath()).append("\"");

					if (act.getDescription() != null) {
						out.append(" descricao=\"").append(act.getDescription().trim().replace("\"", "'")).append("\"");
					}
					out.append("/>\n");
				}
			}
			
			out.append("</recursos>\n");
			
			List<String> filterDefs = appInfo.getFilterDefs();
			if (filterDefs != null && !filterDefs.isEmpty()) {
				out.append("<filterDefinitions>\n");
				for (String filterDef : filterDefs) {
					out.append(filterDef).append("\n");
				}
				out.append("</filterDefinitions>\n");
			}
		}
		out.append("</app-info>");
		
		return out.toString();
	}

	/**
	 * Recupera um InputStream e transforma-o em String.
	 * 
	 * @param servletContext
	 * @param arquivo
	 * @return
	 * @throws IOException
	 */
	protected String loadFile(ServletContext servletContext, String arquivo) throws IOException {
		InputStream in = servletContext.getResourceAsStream(arquivo);
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		String s = new String(b);

		// verifica encode do xml para criar string sem caracteres trocados.
		// <?xml version="1.0" encoding="UTF-8" ?>
		StringBuffer bf = new StringBuffer(s);
		String encode = getLine(bf);
		if (encode.startsWith("<?xml")) {
			encode = StringUtils.substringBetween(encode, "encoding=\"", "\"");
			s = new String(b, encode);
		}
		return s;
	}

	/**
	 * JCompany. Retorna a Interface com Implementação Padrão de Serviço da
	 * Camada Modelo
	 * <p>
	 * Exemplo:
	 * <p>
	 * IPlcFacade appFac = (IPlcFacade) getFacadeService();
	 * <p>
	 * 
	 * List estOrgs = appFac.meuServico(a,b);
	 * <p>
	 * <p>
	 * ...
	 * 
	 * @return Interface para acesso a camada de persistencia.
	 * @since jCompany 3.0
	 */
	protected <T> T getServiceFacade(Class<T> iFacade) {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcIocControllerFacadeUtil.class, QPlcDefaultLiteral.INSTANCE).getFacadeSpecific(iFacade);
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}	
}