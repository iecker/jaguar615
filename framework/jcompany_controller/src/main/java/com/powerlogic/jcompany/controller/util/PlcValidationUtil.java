/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

/**
 * Valida Duplicidade de detalhes
 */
@SPlcUtil
@QPlcDefault
public class PlcValidationUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	@Inject
	@QPlcDefault
	protected PlcI18nUtil i18nUtil;

	@Inject
	@QPlcDefault
	protected PlcMsgUtil msgUtil;

	@Inject	@QPlcDefault
	protected PlcURLUtil urlUtil;

	@Inject	@QPlcDefault 
	protected PlcContextUtil contextUtil;

	@Inject	@QPlcDefault
	protected PlcConfigUtil configUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

	public PlcValidationUtil() {

	}

	/**
	 * Recebe uma Coleção de VOs PlcEntityInstance e testa duplicatas
	 * 
	 * @param zeroSignificativo Se deve ou não considerar zero como valor válido
	 */
	public void validateDuplicate(Collection c, boolean zeroSignificativo,
			String flagDesprezar) {

		log.debug("################## Entrou evento valida Duplicados");

		if (c != null && !c.isEmpty()) {
			List l = null;
			Set s = null;
			Object vo = null;
			List auxl = null;
			Set auxs = null;
			Boolean usaLista = null;
			if (c instanceof List) {
				l = (List) c;
				auxl = l;
				vo = l.get(0);
				usaLista = true;
			} else if (c instanceof Set) {
				s = (Set) c;
				auxs = s;
				vo = s.toArray()[0];
				usaLista = false;
			}

			Iterator i = c.iterator();

			int cont1 = 0;

			PlcPrimaryKey chavePrimaria = vo.getClass().getAnnotation(PlcPrimaryKey.class);
			List props = null;

			if (chavePrimaria != null){
				props = new ArrayList<String>();
				String[] propriedades = chavePrimaria.propriedades();
				for (String prop : propriedades) {
					props.add(prop);
				}
			}

			try {

				PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
				
				while (i.hasNext()) {

					cont1++;

					Object plcVO = i.next();

					if (props == null || !props.contains(flagDesprezar)) {

						String propDespresar = flagDesprezar;
						if (flagDesprezar.contains("_")) {
							int index = flagDesprezar.indexOf("_");
							String propPrincipal = flagDesprezar.substring(0, index);
							plcVO = propertyUtilsBean.getNestedProperty(plcVO,	propPrincipal);
							propDespresar = flagDesprezar.substring(index + 1);
						}

						PlcEntityInstance plcVOInstance = metamodelUtil.createEntityInstance(plcVO);

						Object plcVOPropDespresar = propertyUtilsBean.getNestedProperty(plcVO, propDespresar);

						if (plcVO != null && plcVOPropDespresar != null
								&& !plcVOPropDespresar.equals("")
								&& plcVOInstance != null
								&& !("S".equals(plcVOInstance.getIndExcPlc()))) {
							Iterator j = null;
							if (usaLista)
								j = auxl.iterator();
							else
								j = auxs.iterator();

							int cont2 = 0;

							while (j.hasNext()) {

								cont2++;

								Object plcVOAux = j.next();
								propDespresar = flagDesprezar;
								if (flagDesprezar.contains("_")) {
									int index = flagDesprezar.indexOf("_");
									String propPrincipal = flagDesprezar.substring(0, index);
									plcVOAux = propertyUtilsBean.getNestedProperty(plcVOAux, propPrincipal);
									propDespresar = flagDesprezar.substring(index + 1);
								}

								PlcEntityInstance plcVOAuxInstance = metamodelUtil.createEntityInstance(plcVOAux);

								Object plcVOAuxPropDespresar = propertyUtilsBean.getNestedProperty(plcVOAux, propDespresar);
								
								if (!plcVOPropDespresar.equals(plcVOAuxPropDespresar)) {
									plcVOPropDespresar = getPropDesprezarIfEntity(metamodelUtil, plcVO, propDespresar, plcVOPropDespresar);
									plcVOAuxPropDespresar = getPropDesprezarIfEntity(metamodelUtil, plcVOAux, propDespresar, plcVOAuxPropDespresar);
								}

								if (propertyUtilsBean.getNestedProperty(plcVOAux, propDespresar) != null
										&& !plcVOAuxPropDespresar.equals("")
										&& plcVOPropDespresar.equals(plcVOAuxPropDespresar)
										&& cont1 != cont2
										&& !("S".equals(plcVOAuxInstance.getIndExcPlc()))) {
									throw new PlcException(
											PlcBeanMessages.JCOMPANY_ERROR_TABULAR_DUPLICITY,
											new Object[] { plcVOPropDespresar }, false);
								}
							}

						}

					} else {
						// Procura por chaveNatural
						if (plcVO != null) {

							Object propChaveNatural = propertyUtilsBean
									.getNestedProperty(plcVO, "idNatural");

							PlcEntityInstance plcVOInstance = metamodelUtil.createEntityInstance(plcVO);

							if (propChaveNatural != null && 
									propertyUtilsBean.getNestedProperty(propChaveNatural, flagDesprezar) != null
									&& !(propertyUtilsBean.getNestedProperty(propChaveNatural, flagDesprezar)).equals("")
									&& !("S".equals(plcVOInstance.getIndExcPlc()))) {

								Iterator j = null;
								if (usaLista)
									j = auxl.iterator();
								else
									j = auxs.iterator();

								int cont2 = 0;

								while (j.hasNext()) {

									cont2++;

									Object plcVOAux = j.next();
									Object propChaveNaturalAux = propertyUtilsBean.getNestedProperty(plcVOAux, "idNatural");

									PlcEntityInstance plcVOAuxInstance = metamodelUtil.createEntityInstance(plcVO);

									if (propertyUtilsBean.getNestedProperty(propChaveNaturalAux, flagDesprezar) != null
											&& !(propertyUtilsBean.getNestedProperty(propChaveNaturalAux, flagDesprezar)).equals("")
											&& propertyUtilsBean.getNestedProperty(propChaveNatural,flagDesprezar)
													.equals(propertyUtilsBean.getNestedProperty(propChaveNaturalAux,flagDesprezar))
											&& cont1 != cont2
											&& !("S".equals(plcVOAuxInstance.getIndExcPlc()))) {
										throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_TABULAR_DUPLICITY, new Object[] { propertyUtilsBean
														.getNestedProperty(propChaveNatural, flagDesprezar) }, false);
									}
								}

							}
						}

					}

				}

			} catch(PlcException plcE){
				throw plcE;						
			} catch (Exception e) {
				throw new PlcException("PlcValidationUtil", "validateDuplicate", e, log, "");
			}
		}

	}

	/**
	 * Recebe uma lista de VOs PlcEntityInstance e testa duplicatas
	 * 
	 * @param zeroSignificativo
	 *            Se deve ou não considerar zero como valor válido
	 */
	public void validateDuplicate(List l, boolean zeroSignificativo, String flagDesprezar) {
		if (l != null && !l.isEmpty()) {
			Collection c = (Collection) l;
			validateDuplicate(c, zeroSignificativo, flagDesprezar);
		}
	}

	/**
	 * Verifica duplicatas de detalhes
	 */
	protected void validaDuplicatas(Object valorATestar, List l,
			String propriedade, String nomeClasseDetalhe,
			boolean zeroSignificativo) {

		log.debug("############## Entrou em testa duplicatas");

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class,	QPlcDefaultLiteral.INSTANCE);

		Iterator i = l.iterator();
		Object valor = null;

		if (valorATestar == null)
			return;

		while (i.hasNext()) {

			Object plcVO = i.next();

			try {
				if (propriedade.startsWith("idNatural_")) {
					Object idNatural = propertyUtilsBean.getProperty(plcVO,	"idNatural");
					String propriedadeSemIdNatural = propriedade.replace("idNatural_", "");
					valor = propertyUtilsBean.getProperty(idNatural,propriedadeSemIdNatural);
				} else {
					valor = propertyUtilsBean.getProperty(plcVO, propriedade);
				}

			} catch(PlcException plcE){
				throw plcE;						
			} catch (Exception e) {
				throw new PlcException("PlcValidationUtil", "validaDuplicatas", e, log, "");
			}

			PlcEntityInstance plcVOInstance = metamodelUtil.createEntityInstance(plcVO);

			// Testa somente contra os itens que não forem para exclusao
			if (plcVOInstance != null) {
				if (((plcVOInstance.getIndExcPlc() == null) || (plcVOInstance.getIndExcPlc() != null && plcVOInstance.getIndExcPlc().equals("N")))
						&& (zeroSignificativo || !valor.equals("0"))) {

					if (valorATestar.equals(valor)) {
						throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_MASTERDETAIL_DUPLICITY, new Object[] { nomeClasseDetalhe.toLowerCase(), valor }, false);
					}
				}
			}

		}

	}

	private Object getPropDesprezarIfEntity(PlcMetamodelUtil metamodelUtil,
			Object plcVO, String propDespresar, Object valorPropDespresar ) {
		try {
			Class classEntity = propertyUtilsBean.getPropertyType(plcVO, propDespresar);
			String retorno = getPropDesprezarValueIfEntity(classEntity,	metamodelUtil, plcVO, propDespresar);
			if (retorno == null) {
				classEntity = Class.forName(propertyUtilsBean.getPropertyType(plcVO, propDespresar).getName()
						+ configUtil.getConfigApplication().suffixClass().entity());
				if (metamodelUtil.isEntityClass(classEntity)) {
					return getPropDesprezarValueIfEntity(classEntity,	metamodelUtil, plcVO, propDespresar);
				} else {
					return valorPropDespresar;	
				}
			} else {
				return retorno;
			}
		} catch(PlcException plcE){
			throw plcE;					
		} catch (Exception e) {
			if (e instanceof ClassNotFoundException)
				return valorPropDespresar;	
			throw new PlcException("PlcValidationUtil", "getPropDesprezarIfEntity", e, log, "");
		}
	}

	private String getPropDesprezarValueIfEntity(Class classEntity,
			PlcMetamodelUtil metamodelUtil, Object plcVO, String propDesprezar) {
		try {
			if (classEntity != null && metamodelUtil.isEntityClass(classEntity)) {
				Object objeto = propertyUtilsBean.getProperty(plcVO, propDesprezar);
				PlcEntityInstance entityPlcInstance = metamodelUtil.createEntityInstance(objeto);
				propDesprezar = String.valueOf(entityPlcInstance.getId());
				return propDesprezar;
			}
		} catch(PlcException plcE){
			throw plcE;					
		} catch (Exception e) {
			throw new PlcException("PlcValidationUtil", "getPropDesprezarValueIfEntity", e, log, "");
		}
		return null;
	}
	
}