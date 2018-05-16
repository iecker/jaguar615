/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.tagfile;

import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Length;

import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.template.PlcRiaJavaScriptUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat;
import com.powerlogic.jcompany.domain.validation.PlcValSimpleFormat.SimpleFormat;

@SPlcUtil
@QPlcDefault
public class PlcTagFileUtil {

	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault
	PlcRiaJavaScriptUtil riaJavaScriptUtil;
	
	@Inject @QPlcDefault
	PlcReflectionUtil reflectionUtil;

	@Inject @QPlcDefault
	PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;

	/**
	 * Verifica se um campo ï¿½ obrigatorio com base em suas anotaï¿½ï¿½es, podendo estas estarem na propriedade (Field) ou getter (Method). 
	 * Desconsidera propriedades com convenï¿½ï¿½o de argumentos (sufixo _Arg) e trata ancestrais apropriadamente.<br>
	 * @param classePrincipal Classe principal da lï¿½gica corrente
	 * @param obj Objeto em questao, devendo ser informado somente para detalhes e sub-detalhes
	 * @param propriedade Propriedade a ser averiguada.
	 * @return true se for obrigatorio e false se for opcional ou nï¿½o encontrou anotaï¿½ï¿½o.
	 */
	public Boolean required(Class classePrincipal,Object nomeDetalhe, String propriedade) {
			
		// Todo o tratamento de required serï¿½ feito pelo EXTVAL.
		return false;
	}

	
	/**
	 * Verifica o tamanho mï¿½ximo maxLength de uma propriedade, conforme definido em JPA ou hiberante validator.
	 * @param classePrincipal Classe principal da lï¿½gica corrente
	 * @param obj Objeto em questao, devendo ser informado somente para detalhes e sub-detalhes
	 * @param propriedade Propriedade a ser averiguada.
	 * @return tamanho mï¿½ximo declarado ou 5 se nï¿½o encontrou anotaï¿½ï¿½o.
	 */
	public Integer getMaximumLength(Class classePrincipal,Object nomeDetalhe, String propriedade) {
			
		if (classePrincipal ==null || propriedade == null)
			return 5;				
		
		try {

			Field field = getField(classePrincipal, nomeDetalhe, propriedade);
			
			int max = 5;
			
			try {
				max = getMaximumLengthAnnotation(field, propriedade);
				
			} catch (Exception e) {
				
			}
		
			if (max==255 && PropertyUtils.getPropertyType(classePrincipal.newInstance(),propriedade) != null && Date.class.isAssignableFrom(PropertyUtils.getPropertyType(classePrincipal.newInstance(),propriedade))) {
				return 11;
			} else {
				return max;
			}
			
		} catch (Exception e) {
			log.warn( "Erro ao tentar assumir tamanho para o classe: "+classePrincipal+" nomeDetalhe: "+nomeDetalhe+" propriedade :"+propriedade);
			e.printStackTrace();
			return 5;
		}

	}
	
	private Field getField(Class classePrincipal, Object nomeDetalhe, String propriedade) {
		
		Class classeComAnotacao = classePrincipal;
		
		PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) classePrincipal.getAnnotation(PlcPrimaryKey.class);
		if (chavePrimaria != null && nomeDetalhe == null){
			String[] propriedades = chavePrimaria.propriedades();
			boolean ehChavePrimaria = false;
			for (String prop : propriedades) {
				if (prop.equals(propriedade) || (prop+"_Arg").equals(propriedade) || (prop+"Str").equals(propriedade)){
					ehChavePrimaria = true;
					break;
				}
			}
			if (ehChavePrimaria && !propriedade.startsWith("idNatural."))
				propriedade = "idNatural." + propriedade;
			
		}
		
		// Acha classe de detalhe, desconsiderando tabulares
		if (nomeDetalhe !=null && !("itensPlc".equals(nomeDetalhe))) {
			
			// Sub-detalhe
			String subDetalheProp = null;
			if (nomeDetalhe.toString().indexOf(".")>-1) {
				subDetalheProp= nomeDetalhe.toString().substring(nomeDetalhe.toString().indexOf(".")+1);
				nomeDetalhe=nomeDetalhe.toString().substring(0,nomeDetalhe.toString().indexOf("."));
			}

			Field fDet = null;
			try{ 
				fDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, (String) nomeDetalhe); 
			}
			catch (Exception e) {
				log.warn( "Nï¿½o conseguiu encontrar uma propriedade na classe.");
			}
			
			if (fDet != null && fDet.getAnnotation(OneToMany.class)!=null){
				classeComAnotacao = fDet.getAnnotation(OneToMany.class).targetEntity();
				classePrincipal = classeComAnotacao;
			} else if (fDet != null && fDet.getAnnotation(OneToOne.class)!=null) {
				classeComAnotacao = fDet.getAnnotation(OneToOne.class).targetEntity();
			} else if (fDet != null) {
				try {
					classeComAnotacao = (Class) ((ParameterizedType)fDet.getGenericType()).getActualTypeArguments()[0];
				}catch(Exception e) {
					return null;
				}
			} else {
				return null;
			}
	
			// Acha classe de Sub-Detalhes
			if (subDetalheProp !=null) {
			
				Field fSubDet = null;
				try {fSubDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, subDetalheProp); }
				catch (Exception e) {log.warn( "Nï¿½o conseguiu encontrar uma propriedade na classe.");}
				
				if (fSubDet != null && fSubDet.getAnnotation(OneToMany.class)!=null) {
					classeComAnotacao = fSubDet.getAnnotation(OneToMany.class).targetEntity();
				} else 
					return null;
			}
				
		}
		
		//retirar prefixo e sufixo das propriedades do argumento
		if (propriedade.contains("argumentos.") || propriedade.contains(".valor")){
			propriedade = propriedade.replace("argumentos.", "").replace(".valor", "");
		}
		
		// Se for de detalhe e tiver "Aux" como sufixo, segundo a convenï¿½ï¿½o, retira
		if ((nomeDetalhe !=null && propriedade.endsWith("Aux"))
				|| (nomeDetalhe==null && propriedade.endsWith("Str")))
			propriedade = propriedade.substring(0,propriedade.length()-3);
					
		if (propriedade.endsWith("_Arg"))
			propriedade = propriedade.substring(0,propriedade.indexOf("_Arg"));
		
		//para propripedades de argumento de inicio e argumento fim
		if (propriedade.endsWith("_ArgINI"))
			propriedade = propriedade.substring(0,propriedade.indexOf("_ArgINI"));
		
		if (propriedade.endsWith("_ArgFIM"))
			propriedade = propriedade.substring(0,propriedade.indexOf("_ArgFIM"));
			
		// Trata componentes, se tiver
		if (propriedade.indexOf("_")>-1 || propriedade.indexOf(".")>-1) {
			char c = '.';
			if (propriedade.indexOf("_")>-1)
			    c = '_';
		    
			String componente = propriedade.toString().substring(0,propriedade.toString().indexOf(c));
			propriedade = propriedade.toString().substring(propriedade.toString().indexOf(c)+1);
			
			Field fDet = null;
			try{fDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, componente);}
			catch (Exception e) {log.warn( "Nï¿½o conseguiu encontrar uma propriedade na classe.");}
			
			if (fDet != null && (fDet.getAnnotation(Embedded.class)!=null || fDet.getAnnotation(OneToOne.class)!=null || fDet.getAnnotation(ManyToOne.class)!=null || fDet.getAnnotation(EmbeddedId.class)!=null )){
				classeComAnotacao = fDet.getType();
			} else {
				return null;
			}
			
		}
		Field field = null;
		
		try{
			field = reflectionUtil.findFieldHierarchically(classeComAnotacao, propriedade);
		} catch (Exception e){}

		return field;
	}

	public Integer getMaximumLengthFraction(Class classePrincipal,Object nomeDetalhe, String propriedade) {
		
		Field field = null;
		Digits d = null;
		Integer tamanhoFraction = null;
		
		try{
			field = getField(classePrincipal, nomeDetalhe, propriedade);
		} catch(Exception e){
			return null;
		}
		
		if (field == null) {
			return null;
		} else if (field.getType().isAssignableFrom(BigDecimal.class)
				|| field.getType().isAssignableFrom(Float.class)				
				|| field.getType().isAssignableFrom(Double.class)			
				|| field.getType().toString().equals("double")
				|| field.getType().toString().equals("float")) {
			
			d = field.getAnnotation(Digits.class);		
		
		}
		
		if(d!=null){
			tamanhoFraction = d.fraction();
		}		
		
		return tamanhoFraction;
	}
	


	private Integer getMaximumLengthAnnotation(AccessibleObject object, String propriedade) {
		if ("id".equals(propriedade) || object ==null) {
			return 5;
		}
		
		Integer max = null;		
		Digits d = null;

		if (max==null) {
			// Verifica anotaï¿½ï¿½o @Size do Hibernate Validator
			Size l =  object.getAnnotation(Size.class);
			if (l != null && l.max()>0) {
				max = l.max();
			}
		}
		
		if (max==null) {
			// Verifica anotaï¿½ï¿½o @Lenght do Hibernate Validator
			Length l =  object.getAnnotation(Length.class);
			if (l != null && l.max()>0) {
				max = l.max();
			}
		}
		
		if (max==null) {
			//verifica se ï¿½ BigDecimal, Double, Float, Short, Long, Integer,
			// double, float, short, long ou int.
			if (((Field)object).getType().isAssignableFrom(BigDecimal.class)
					|| ((Field)object).getType().isAssignableFrom(Float.class)
					|| ((Field)object).getType().isAssignableFrom(Long.class)
					|| ((Field)object).getType().isAssignableFrom(Double.class)
					|| ((Field)object).getType().isAssignableFrom(Short.class)
					|| ((Field)object).getType().isAssignableFrom(Integer.class)
					|| ((Field)object).getType().toString().equals("short")
					|| ((Field)object).getType().toString().equals("long")
					|| ((Field)object).getType().toString().equals("int")
					|| ((Field)object).getType().toString().equals("double")
					|| ((Field)object).getType().toString().equals("float")){
				d = object.getAnnotation(Digits.class);				
				if (d != null) {
					max = d.integer();
				}			
			} 
		}		
		
		if (((Field)object).getType().isAssignableFrom(BigDecimal.class)
			|| ((Field)object).getType().isAssignableFrom(Float.class)
			|| ((Field)object).getType().isAssignableFrom(Double.class)
			|| ((Field)object).getType().toString().equals("double")
			|| ((Field)object).getType().toString().equals("float")){	
						
			int numPontos = ((max-1)-((max-1) % 3))/3;
				
			max = max + numPontos+1+d.fraction(); //caracteres antes da virgula + num. de pontos + virgula + fraï¿½ï¿½o.
			
		}
		
		if(((Field)object).getType().isAssignableFrom(Date.class)){
			max = 10;
		}
	
		return max==null ? 5 : max;
	}
	
	/**
	 * Mï¿½todo adaptador para manter compatibilidade com legado Struts, que utiliza PlcFormatoSimples e nao PlcValFormatoSimples
	 */
	public SimpleFormat format(Class classePrincipal,Object nomeDetalhe, String propriedade) {
		
		String formatoSimplesNovo = simpleFormat(classePrincipal,nomeDetalhe,propriedade,true);
		
		if (StringUtils.isBlank(formatoSimplesNovo))
			formatoSimplesNovo = simpleFormat(classePrincipal,nomeDetalhe,propriedade,false);
		
		if (formatoSimplesNovo==null)
			return null;
		else if (formatoSimplesNovo.equals("MAIUSCULO"))
			return SimpleFormat.UPPER_CASE;
		else if (formatoSimplesNovo.equals("MINUSCULO"))
			return SimpleFormat.LOWER_CASE;
		else if (formatoSimplesNovo.equals("NUMERICO"))
			return SimpleFormat.NUMBER;
		else 
			return null;
			
	}
	
	/**
	 * Verifica o formato de campo texto conforme anotaï¿½ï¿½o e seu tipo
	 * @param classePrincipal Classe principal da lï¿½gica corrente
	 * @param obj Objeto em questao, devendo ser informado somente para detalhes e sub-detalhes
	 * @param propriedade Propriedade a ser averiguada.
	 * @param usaDeprecated Se ï¿½ para  usar o padrao PlcFormatoSimples. False para usar PlcValFormatoSimples
	 * @return formato ou null se nï¿½o encontrou anotaï¿½ï¿½o ou tipo numï¿½rico.
	 */
	public String simpleFormat(Class classePrincipal,Object nomeDetalhe, String propriedade, boolean usaDeprecated) {
			
		if (classePrincipal ==null || propriedade == null)
			return null;				
		
		try {
			
			Class classeComAnotacao = classePrincipal;
								
			// Acha classe de detalhe, desconsiderando tabulares
			if (nomeDetalhe !=null && !("itensPlc".equals(nomeDetalhe))) {
				
				// Sub-detalhe
				String subDetalheProp = null;
				if (nomeDetalhe.toString().indexOf("[")>-1) {
					subDetalheProp= nomeDetalhe.toString().substring(nomeDetalhe.toString().indexOf(".")+1);
					nomeDetalhe=nomeDetalhe.toString().substring(0,nomeDetalhe.toString().indexOf("["));
				}
	
				Field fDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, (String)nomeDetalhe);
				
				if (fDet.getAnnotation(OneToMany.class)!=null) {
					classeComAnotacao = fDet.getAnnotation(OneToMany.class).targetEntity();
				} else 
					return null;
		
				// Acha classe de Sub-Detalhes
				if (subDetalheProp !=null) {
				
					Field fSubDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, (String)subDetalheProp);
					
					if (fSubDet.getAnnotation(OneToMany.class)!=null) {
						classeComAnotacao = fSubDet.getAnnotation(OneToMany.class).targetEntity();
					} else 
						return null;
					
				}					
			
			}
			
			// Se for de detalhe e tiver "Aux" como sufixo, segundo a convenï¿½ï¿½o, retira
			if (propriedade.endsWith("Aux"))
				propriedade = propriedade.substring(0,propriedade.indexOf("Aux"));

			if (propriedade.endsWith("_Arg"))
				propriedade = propriedade.substring(0,propriedade.indexOf("_Arg"));
			
			// Trata componentes, se tiver
			if (propriedade.indexOf("_")>-1) {
				
				String componente = propriedade.toString().substring(0,propriedade.toString().indexOf("_"));
				propriedade = propriedade.toString().substring(propriedade.toString().indexOf("_")+1);
				
				Field fDet = reflectionUtil.findFieldHierarchically(classeComAnotacao, componente);
				
				if (fDet.getAnnotation(Embedded.class)!=null || fDet.getAnnotation(EmbeddedId.class)!=null ){
					classeComAnotacao = fDet.getType();
				} else 
					return null;
				
			}
			
			return checkDeclaredFieldFormat(classeComAnotacao,propriedade,usaDeprecated);
		
		} catch (Exception e) {
			return null;
		}

	}
	
	/**
	 * No caso de anotaï¿½ï¿½o em getters, nï¿½o precisa de recursividade jï¿½ que a reflexï¿½o traz todos os mï¿½todos publicos
	 * @param classeComAnotacao Classe contendo anotaï¿½ï¿½o PlcValidaUnificado
	 * @param propriedade Nome da propriedade
	 * @return true se for obrigatorio e false ao contrario, ou caso nï¿½o seja encontrada anotaï¿½ï¿½o para a propriedade.
	 */
	private static SimpleFormat checkGetterFormat(Class classeComAnotacao, String propriedade) {
		
		try {
			
			Method getter = classeComAnotacao.getMethod("get"+propriedade.substring(0,1).toUpperCase()+propriedade.substring(1),null);
			
			// Verifica primeiro em JPA
			PlcValSimpleFormat formato = (PlcValSimpleFormat) getter.getAnnotation(PlcValSimpleFormat.class);
			
			if (formato!=null)
				return formato.format();
			else {
	
				// Se nï¿½o tiver formato verifica se ï¿½ numï¿½rico para assumir
				Class classeRetorno = getter.getReturnType();
				if (Long.class.isAssignableFrom(classeRetorno) ||
					Integer.class.isAssignableFrom(classeRetorno) ||
					Double.class.isAssignableFrom(classeRetorno))
					return SimpleFormat.NUMBER;
			}
			
		} catch (Exception e) {	}
		
		return null;
		
	}
	
	/**
	 * No caso de anotaï¿½ï¿½o em getters, nï¿½o precisa de recursividade jï¿½ que a reflexï¿½o traz todos os mï¿½todos publicos
	 * @param classeComAnotacao Classe contendo anotaï¿½ï¿½o PlcValidaUnificado
	 * @param propriedade Nome da propriedade
	 * @return true se for obrigatorio e false ao contrario, ou caso nï¿½o seja encontrada anotaï¿½ï¿½o para a propriedade.
	 */
	private static SimpleFormat getGetterSimpleFormat(Class classeComAnotacao, String propriedade) {
		
		try {
			
			Method getter = classeComAnotacao.getMethod("get"+propriedade.substring(0,1).toUpperCase()+propriedade.substring(1),null);
			
			// Verifica primeiro em JPA
			PlcValSimpleFormat formatoSimples = (PlcValSimpleFormat) getter.getAnnotation(PlcValSimpleFormat.class);
			
			if (formatoSimples!=null)
				return formatoSimples.format();
			else {
	
				// Se nï¿½o tiver formato verifica se ï¿½ numï¿½rico para assumir
				Class classeRetorno = getter.getReturnType();
				if (Long.class.isAssignableFrom(classeRetorno) ||
					Integer.class.isAssignableFrom(classeRetorno) ||
					Double.class.isAssignableFrom(classeRetorno))
					return SimpleFormat.NUMBER;
			}
			
		} catch (Exception e) {	}
		
		return null;
		
	}

	/**
	 * Verifica recursivamente no caso de Fields, ja que a reflexï¿½o nï¿½o traz Fields privados em ancestrais (getDeclaredFields)
	 * @param classeComAnotacao Classe contendo anotaï¿½ï¿½o PlcValidaUnificado
	 * @param propriedade Nome da propriedade
	 * @return true se for obrigatorio e false ao contrario, ou caso nï¿½o seja encontrada anotaï¿½ï¿½o para a propriedade
	 */
	private String checkDeclaredFieldFormat(Class classeComAnotacao,String propriedade, boolean usaDeprecated) {
		
		// Se nï¿½o achou no nivel corrente e no superior tem anotacao de replicacao, procura recursivamente.
		Field f=null;
		
		try {  
			f = reflectionUtil.findFieldHierarchically(classeComAnotacao, propriedade);
		} catch (Exception e){ 
			
		}
		
		if (f==null) {
			if (classeComAnotacao!=null) 
				return checkDeclaredFieldFormat(classeComAnotacao.getSuperclass(),propriedade,usaDeprecated);
			else 
				return null;
		}
		
		if (usaDeprecated) {
			// Verifica primeiro em JPA
			PlcValSimpleFormat formato = (PlcValSimpleFormat) f.getAnnotation(PlcValSimpleFormat.class);
			if (formato !=null) {
				return formato.format().toString();
			} else {
				
				// Se nï¿½o tiver formato verifica se ï¿½ numï¿½rico para assumir
				Class classeRetorno = f.getType();
				if (Long.class.isAssignableFrom(classeRetorno) || Integer.class.isAssignableFrom(classeRetorno) || Short.class.isAssignableFrom(classeRetorno)) {
					return SimpleFormat.NUMBER.toString();
				}
	
			}
		} else {
			//Verifica primeiro em JPA
			PlcValSimpleFormat formato = (PlcValSimpleFormat) f.getAnnotation(PlcValSimpleFormat.class);
			if (formato !=null) {
				return formato.format().toString();
			} else {
				// Se nï¿½o tiver formato verifica se ï¿½ numï¿½rico para assumir
				Class classeRetorno = f.getType();
				if (Long.class.isAssignableFrom(classeRetorno) || Integer.class.isAssignableFrom(classeRetorno) || Double.class.isAssignableFrom(classeRetorno))
					return SimpleFormat.NUMBER.toString();
			}
		}
	
		return null;
	}


	/**
	 * @return Devolve o nome da propriedade que serve de referï¿½ncia para desprezar linhas em detalhes
	 *  
	 */
	public String despiseProperty(Class classePrincipal)  {
		
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (classePrincipal != null && metamodelUtil.isEntityClass(classePrincipal)){
			return configUtil.getConfigDomain(classePrincipal).despiseProperty(); 
		}
		else
			return null;

	}

	/**
	 * @return Devolve o nome da propriedade que serve de referï¿½ncia para desprezar linhas em detalhes
	 */
	public List messageByColor(HttpServletRequest request,String chave) {
		return (List) request.getAttribute(chave);
	}

	/**
	 * Processa os templates e retorna uma String com o resultado.
	 * @param defaultParameters
	 * @param defaultTemplates
	 * @param customTemplates
	 * @return String com o resultado do processamento do template com o contexto informado.
	 *  Caso ocorra algum erro ao processar o template.
	 */
	public String processJavaScriptRia(String defaultParameters, String defaultTemplates, String customTemplates)  {
		// customTemplates pode desativar a utilizaï¿½ï¿½o de templates com o parï¿½metro "N"
		if ("N".equalsIgnoreCase(customTemplates)) {
			return null;
		}
		return processJavaScriptRia(defaultParameters, defaultTemplates);
	}

	private String processJavaScriptRia(String defaultParameters, String defaultTemplates)  {
		StringWriter writer = new StringWriter();
		writer.write("\n<script avaliar=\"S\" type=\"text/javascript\">\n");
		riaJavaScriptUtil.include(defaultParameters, defaultTemplates, writer);
		writer.write("\n</script>\n");
		return writer.toString();
	}


}
