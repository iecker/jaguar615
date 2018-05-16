/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.util;

import java.beans.PropertyDescriptor;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

@SPlcUtil
@QPlcDefault
public abstract class PlcPersistenceUtil {
	
	@Inject
	protected transient Logger log;
	
	public PlcPersistenceUtil() {
		
	}
	
	/**
     * Monta parte select de um HQL com alias "obj" para a relação de propriedades informada
     * @since jCompany 2.7
     * @param props String[] contendo relação de propriedades a serem montadas (Ex: {id,nome})
     * @return String contendo parte HQL (Ex: "obj.id, obj.nome")
     */
    public String createSelectFromStringArray(String[] props)  {

        log.debug( "########## Entrou em createSelectFromStringArray");

        try {

            if (props.length==0)
                return "";

            StringBuffer sb = new StringBuffer("");

            for (int i=0;i<props.length;i++) {

                sb.append(props[i]);

                if ((i+1)<props.length)
                    sb.append(",");
            }

            return sb.toString();

        } catch(PlcException plcE){
			throw plcE;            
        } catch (Exception e) {
			throw new PlcException("PlcPersistenceUtil", "createSelectFromStringArray" , e, log, null);
        }

    }
    
    /**
     * Recupera a primeira propriedade dos meta-dados da Hibernate que possui o tipo classeAgregada,
     * na classeBase. Importante: Para caso de dois relacionamentos entre classes do mesmo tipo, esta
     * lógica pode não atender e deve ser especializada.
     * Importante: Somente funciona para Object-IDs
     * @since jCompany 3.0
     * @return Nome da propriedade da classeBase que é do tipo classeAgregada.
     */
    public String getManyToOnePropertyFromClass(Class classe, Class classeProcura)  {

        log.debug( "########## Entrou em getManyToOnePropertyFromClass");
        
        PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
        
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(classe);
        try {
	
        	for (int i = 0; i < pds.length; i++) {
				if (/*!metamodelUtil.isEntityClass(pds[i].getPropertyType()) && */
						 pds[i].getPropertyType().isAssignableFrom(classeProcura)
						&& !pds[i].getName().equals("idNatural")
						&& !pds[i].getName().equals("idNaturalDinamico"))
					return pds[i].getName();
			}

        } catch(PlcException plcE){
			throw plcE;        	
		} catch (Exception e) {
			throw new PlcException("PlcPersistenceUtil", "getManyToOnePropertyFromClass" , e, log, null);
		}
		throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ORIGEM_ATTRIBUTE, new Object[] {
				"getManyToOnePropertyFromClass", classe }, true);
    }
    

	/**
	 * Testa se uma classe é um proxy dinâmico da GCLIG e converte para a
	 * classe original
	 * 
	 * @since jCompnay 5.0
	 * 
	 * @param classe Possivel proxy dinâmico
	 * @return classe original
	 *  causa raiz ClassNotFoundException
	 */
	public Class convertDynamicProxyToRealClass(Class classe)  {
		Class classeAux = null;
		
		if (classe.getName().indexOf("$")>-1) {
			String classeS = classe.getName();
			classeS = classeS.substring(0,classeS.indexOf("$"));
			try {
				classeAux = Class.forName(classeS);
			} catch (Exception e) {
				throw new PlcException("PlcPersistenceUtil", "convertDynamicProxyToRealClass" , e, log, null);
			}
			
			return classeAux;
		}
		
		return classe;
	}

}


