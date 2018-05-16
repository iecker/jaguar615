/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons;

import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Classe que mantém a versão atual do jCompany
 * @since jCompany 3.1.1
 */
@SPlcUtil
@QPlcDefault
public class PlcVersionUtil {

	protected static final Logger log = Logger.getLogger(PlcVersionUtil.class.getCanonicalName());
	  
	public static String VERSAO = "" ;

	static {
		String nomeJar = PlcVersionUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		if (nomeJar.indexOf("jcompany_commons-")>0)
			VERSAO = nomeJar.substring(nomeJar.indexOf("jcompany_commons-")+17, nomeJar.indexOf(".jar"));
		log.info(  "===================================================================" );
		log.info(  "=========== jCompany Developer Suite " + VERSAO + " ===============" );
		log.info(  "===================================================================" );
	}

	public static void touch() {
	}

	private String versao;
	private String metaInfProductCode;
	
	
	public void start(String webXmlVersion, String metainf) {
		
		// Buscar versao do build projeto do manifest.mf
		try {
			Properties p = new Properties();
			p.load(new StringReader(metainf));

			String metaInfVersion = p.getProperty("Implementation-Version");
			String metaInfBuild = p.getProperty("Implementation-Build");
			String metaInfData = p.getProperty("Implementation-Date");
			setMetaInfProductCode(p.getProperty("PrjCodigo-Produto"));

			if (metaInfVersion != null) {

				if (StringUtils.isNotBlank(webXmlVersion) && metaInfVersion.contains("build.") && metaInfBuild == null) {
					versao = webXmlVersion + "." + metaInfVersion.replaceAll("build.", "");
				} else if (!metaInfVersion.contains("build.") && metaInfBuild != null) {
					versao = metaInfVersion + "." + metaInfBuild;
				} else if (!metaInfVersion.contains("build.") && metaInfBuild == null) {
					versao = metaInfVersion;
				}

				if (metaInfData != null) {
					versao += "[" + metaInfData + "]";
				}

			}
		} catch (Exception e) {
			log.warn( "Ocorreu um erro ao tentar registrar o numero de build a partir no arquivo manifest.mf: " + e);
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(versao)) {
			if (StringUtils.isNotEmpty(webXmlVersion)) {
				versao = webXmlVersion;
			} else {
				versao = "1.0.0";
			}	
		}
		
	}


	/**
	 * @param metaInfCodigoProduto the metaInfCodigoProduto to set
	 */
	private void setMetaInfProductCode(String metaInfCodigoProduto) {
		this.metaInfProductCode = metaInfCodigoProduto;
	}


	/**
	 * @return the metaInfCodigoProduto
	 */
	public String getMetaInfProductCode() {
		return metaInfProductCode;
	}


	public String getVersion() {
		return versao;
	}


}
