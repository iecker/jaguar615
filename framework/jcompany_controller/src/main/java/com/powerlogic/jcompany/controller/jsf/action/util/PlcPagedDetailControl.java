/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;


/**
 * Controle para paginação de detalhes. Controla a posição da paginação de cada detalhe 
 * e o numero total de paginas.
 * 
 * É utilizado tambem para argumentos em detalhes.
 *  
 * @author Moisés Paula
 */
@Named(PlcJsfConstantes.PLC_CONTROLE_DETALHE_PAGINADO)
@ConversationScoped
public class PlcPagedDetailControl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Mapa de controles para cada detalhe
	 */
	private Map <String, DetalhePaginadoController> mapaControles = new HashMap<String, DetalhePaginadoController> ();
	
	/**
	 * Detalhe corrente que sera utilizado na paginação
	 */
	@Inject @HttpParam ("detCorrPlcPaginado")
	private String detCorrPlcPaginado;
	
	/**
	 * 
	 * Classe interna para controle de paginação do detalhe
	 * 
	 * @author Moisés Paula
	 *
	 */
	public class DetalhePaginadoController {
		
		/*
		 * Posição atual da paginação de detalhes
		 */
		private int posAtual = 0;
		
		/*
		 * Posição atual da paginação de detalhes
		 */
		private int paginaAtual = 0;
		
		/*
		 * Numero total de registros do detalhe
		 */
		private long numTotalRegistros = 0;
		
		/*
		 * Configuração de detalhe
		 */
		private PlcConfigDetail configDetalhe;
		
		/*
		 * Numero total de páginas, baseado no total de registros
		 */
		private int numTotalPaginas = 0;
		

		public DetalhePaginadoController (int posAtual, long numTotalRegistros, PlcConfigDetail configDetalhe){
			this.posAtual = posAtual;
			this.configDetalhe = configDetalhe;
			this.numTotalRegistros = numTotalRegistros;
			
			int numPorPagina = configDetalhe.navigation().numberByPage();
			this.numTotalPaginas = (int)numTotalRegistros / numPorPagina;
			
			if ((numTotalRegistros % numPorPagina) != 0)
				this.numTotalPaginas = this.numTotalPaginas + 1;
			
			
		}

		public DetalhePaginadoController (PlcConfigDetail configDetalhe){
			this.configDetalhe = configDetalhe;
		}
		
		public int getPosAtual() {
			return posAtual;
		}

		public void setPosAtual(int posAtual) {
			this.posAtual = posAtual;
		}

		public PlcConfigDetail getConfigDetalhe() {
			return configDetalhe;
		}

		public void setConfigDetalhe(PlcConfigDetail configDetalhe) {
			this.configDetalhe = configDetalhe;
		}

		public long getNumTotalRegistros() {
			return numTotalRegistros;
		}

		public void setNumTotalRegistros(long numTotalRegistros) {
			this.numTotalRegistros = numTotalRegistros;
		}

		public int getNumTotalPaginas() {
			return numTotalPaginas;
		}

		public void setNumTotalPaginas(int numTotalPaginas) {
			this.numTotalPaginas = numTotalPaginas;
		}

		public void reset (){
			this.posAtual = 0;
			this.numTotalRegistros = 0;
			this.numTotalPaginas = 0;
			this.paginaAtual = 0;
			
		}

		public int getPaginaAtual() {
			return paginaAtual;
		}

		public void setPaginaAtual(int paginaAtual) {
			this.paginaAtual = paginaAtual;
		}
		
	};
	
	/**
	 * Construtor default
	 */
	public PlcPagedDetailControl (){}
	
	
	public void setDetalhePaginado (PlcConfigDetail detalhe, long numTotalRegistros){
		
		DetalhePaginadoController controller = this.mapaControles.get(detalhe.collectionName());
		
		if (controller == null){
			controller = new DetalhePaginadoController (0, numTotalRegistros, detalhe);
			controller.setPaginaAtual(1);
		} else {
			controller.setPosAtual(0);
			controller.setNumTotalRegistros(numTotalRegistros);
			
			int numPorPagina = detalhe.navigation().numberByPage();
			int numTotalPaginas = (int)numTotalRegistros / numPorPagina;
			
			if ((numTotalRegistros % numPorPagina) != 0)
				numTotalPaginas = numTotalPaginas + 1;
			
			controller.setNumTotalPaginas(numTotalPaginas);
			controller.setPaginaAtual(1);
			
		}
		
		this.mapaControles.put (detalhe.collectionName(), controller);
		
	}
	
	public PlcConfigDetail getDetalhePorNome (String nomeDetalhe){
		DetalhePaginadoController controller = this.mapaControles.get(nomeDetalhe);

		if (controller != null)
			return controller.getConfigDetalhe();
		
		return null;
	
	}
	
	public int getPosicaoAtual (String nomeDetalhe){
		DetalhePaginadoController controller = this.mapaControles.get(nomeDetalhe);
		return controller.getPosAtual();
	}
	
	public int setPosicaoAtual (String nomeDetalhe, int pos){
		DetalhePaginadoController controller = this.mapaControles.get(nomeDetalhe);
		controller.setPosAtual(pos);
		
		long numTotalRegistros = controller.getNumTotalRegistros ();
		int numPorPagina = controller.getConfigDetalhe().navigation().numberByPage();
		
		int paginaAtual = pos / numPorPagina + 1;
		
		controller.setPaginaAtual(paginaAtual);
		
		return controller.getPosAtual();
	}
	
	public long getNumTotalRegistros (String nomeDetalhe){
		DetalhePaginadoController controller = this.mapaControles.get(nomeDetalhe);
		return controller.getNumTotalRegistros();
	}
	
	public int getNumTotalPaginas (String nomeDetalhe){
		DetalhePaginadoController controller = this.mapaControles.get(nomeDetalhe);
		return controller.getNumTotalPaginas();
	}
	
	public DetalhePaginadoController getController (String nomeDetalhe){
		return this.mapaControles.get(nomeDetalhe);
	}


	public Map<String, DetalhePaginadoController> getMapaControles() {
		return this.mapaControles;
	}


	public void setMapaControles(Map<String, DetalhePaginadoController> mapaControles) {
		this.mapaControles = mapaControles;
	}


	public String getDetCorrPlcPaginado() {
		return this.detCorrPlcPaginado;
	}


	public void setDetCorrPlcPaginado(String detCorrPlcPaginado) {
		this.detCorrPlcPaginado = detCorrPlcPaginado;
	}
	
	public Map <String, PlcConfigDetail> getMapaDetalhesPaginados(){
		
		Map<String, PlcConfigDetail> mapaDetalhesPaginados = new HashMap<String, PlcConfigDetail>();
		
		Set<String> keys = mapaControles.keySet();
		
		for (String key : keys) {
			DetalhePaginadoController controller = mapaControles.get(key);
			PlcConfigDetail configDetalhe = controller.getConfigDetalhe();
			mapaDetalhesPaginados.put(key, configDetalhe);
		}
		
		return mapaDetalhesPaginados;
	}
	
	public void reset (){
	
		Set<String> keys = mapaControles.keySet();
		
		for (String key : keys) {
			DetalhePaginadoController controller = mapaControles.get(key);
			controller.reset();
		}
	}

	
	
}
