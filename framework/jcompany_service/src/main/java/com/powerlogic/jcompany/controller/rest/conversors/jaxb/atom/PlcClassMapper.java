package com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntity;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Wrapper de Mapper para escrever adequadamente o nome de uma classe no XML.
 * @author Bruno Carneiro
 *
 */
public class PlcClassMapper extends DefaultImplementationsMapper implements Mapper{
	
	private static Mapper mapper = new XStream().getMapper();
	
	private PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

	
	public PlcClassMapper(Mapper wrapped) {
		super(mapper);
	}
	
	public PlcClassMapper(){
		this(null);
	}
	
	@Override
	public String serializedClass(Class type) {
		
		//verificar se é entidade, se for devolver o nome dela.
		if(type!=null){
			String className=type.getSimpleName();
			return StringUtils.uncapitalize(className);
		}
		else{
			return null;
		}
	}

	public static Mapper getMapper() {
		return mapper;
	}

	public static void setMapper(Mapper mapper) {
		PlcClassMapper.mapper = mapper;
	}

	@Override
	public Class realClass(String elementName) {
		
		for(PlcEntity<?> plcEntity : metamodelUtil.getEntities()){
			if(plcEntity.getEntityClass().getSimpleName().equals(StringUtils.capitalize(elementName))){
				return plcEntity.getEntityClass();
			}
		}
			return super.realClass(elementName);
	}
}
