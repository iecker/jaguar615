package br.com.plc.jcompany_fcls.persistence.dao.categoria;

import br.com.plc.jcompany_fcls.entity.categoria.CategoriaEntity;
import br.com.plc.jcompany_fcls.persistence.dao.AppJpaDAO;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

@PlcAggregationDAOIoC(CategoriaEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class CategoriaDAO extends AppJpaDAO {
	
	@Override
	public void update(PlcBaseContextVO context, Object vo) throws PlcException {
		// TODO Auto-generated method stub
		super.update(context, vo);
	}

}
