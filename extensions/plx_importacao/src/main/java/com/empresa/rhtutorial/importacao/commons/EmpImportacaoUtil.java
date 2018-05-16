package com.empresa.rhtutorial.importacao.commons;

import java.util.logging.Logger;

import com.empresa.rhtutorial.importacao.metadata.EmpConfigImportacao;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;

public class EmpImportacaoUtil {
    private static final Logger LOG = Logger.getLogger(EmpImportacaoUtil.class.getCanonicalName());
    private static EmpImportacaoUtil INSTANCE = new EmpImportacaoUtil ();

    public static EmpImportacaoUtil getInstance() {
        return INSTANCE;
    }

    public EmpConfigImportacao getConfigImportacao() {
        EmpConfigImportacao config = null;		
        PlcConfigUtil configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
       PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
       PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

        //obtendo a classe mb
        Class mb = metamodelUtil.getUriIocMB(
(String)contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA)).getBeanClass();
        config = (EmpConfigImportacao) mb.getAnnotation(EmpConfigImportacao.class);
        return config;
    }

}

