/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util;

import java.util.Comparator;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.comparator.PlcComparatorId;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;


/**
 * Classe utilizada para auxiliar tratamento de detalhes 
 */
@SPlcUtil
@QPlcDefault
public class PlcDetailUtil {

	@Inject
	private transient Logger log;
	
	public PlcDetailUtil() { 
		
	}

	@SuppressWarnings("unchecked")
	public static TreeSet<Object> instanceTreeSet(Class comparator)  {
			try {
				if (comparator == null || Comparator.class.equals(comparator)){
					return new TreeSet<Object>(new PlcComparatorId());
				} else if (comparator != null){
					return new TreeSet<Object> ((Comparator)comparator.newInstance());
				} else {
					return new TreeSet <Object> (new Comparator<Object>(){
						public int compare(Object obj1, Object obj2) {
							PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
							
							PlcEntityInstance obj1Instance = metamodelUtil.createEntityInstance(obj1);
							PlcEntityInstance obj2Instance = metamodelUtil.createEntityInstance(obj1);
							
							String idAux1 = obj1Instance.getIdAux();
							String idAux2 = obj2Instance.getIdAux();
							if (StringUtils.isNotBlank(idAux1) && StringUtils.isNotBlank(idAux2)){
								if (StringUtils.isNumeric(idAux1) && StringUtils.isNumeric(idAux2)){
									long id1 = NumberUtils.toLong(idAux1);
									long id2 = NumberUtils.toLong(idAux2);
									if (id1 > id2)
										return +1;
									else 
										return -1;
								}
							}
							return +1;
						}
					});
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
			
	}
 	
}
