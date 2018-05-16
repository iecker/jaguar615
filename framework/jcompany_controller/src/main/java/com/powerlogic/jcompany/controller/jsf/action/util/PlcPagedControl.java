/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;


/**
 * Controle para paginação de seleção. Controla a posição da paginação  e o numero total de paginas.
 */
@Named(PlcJsfConstantes.PLC_CONTROLE_PAGINACAO)
@ConversationScoped
public class PlcPagedControl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Mapa de controles para cada detalhe
	 */
	private Map <String, PaginacaoController> mapaControles = new HashMap<String, PaginacaoController> ();
	
	
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
	public class PaginacaoController {
		
		/*
		 * Posição atual da paginação de detalhes
		 */
		private int posAtual = 0;
		
		/*
		 * Posição atual da paginação de detalhes
		 */
		private int paginaAtual = 0;
		
		private int navDe;
		private int navAte;
		private int navAnterior;
		private int navProxima;
		private int navUltima;
		private int numPorPagina;
		
		/*
		 * Numero total de registros do detalhe
		 */
		private long numTotalRegistros = 0;
		
		/*
		 * Configuração de detalhe
		 */
		private PlcConfigSelection configSelecao;
		
		/*
		 * Numero total de páginas, baseado no total de registros
		 */
		private int numTotalPaginas = 0;
		
		private List<Integer>numTotalPaginasList = new ArrayList();
		

		public PaginacaoController (int posAtual, long numTotalRegistros, PlcConfigSelection configSelecao){
			this.posAtual = posAtual;
			this.configSelecao = configSelecao;
			this.numTotalRegistros = numTotalRegistros;
			
			int numPorPagina = configSelecao.pagination().numberByPage();
			this.numTotalPaginas = (int)numTotalRegistros / numPorPagina;
			
			if ((numTotalRegistros % numPorPagina) != 0) {
				this.numTotalPaginas = this.numTotalPaginas + 1;
			}
			for (int i = 0; i < numTotalPaginas; i++) {
				numTotalPaginasList.add(i);
			}
			
		}

		public int getNavAte() {
			return navAte;
		}
		
		public void setNavAte(int navAte) {
			this.navAte = navAte;
		}
		
		public int getNavDe() {
			return navDe;
		}
		
		public void setNavDe(int navDe) {
			this.navDe = navDe;
		}
		public PaginacaoController (PlcConfigSelection configSelecao){
			this.configSelecao = configSelecao;
		}
		
		public int getPosAtual() {
			return posAtual;
		}

		public void setPosAtual(int posAtual) {
			this.posAtual = posAtual;
		}

		public PlcConfigSelection getConfigSelecao() {
			return configSelecao;
		}

		public void setConfigSelecao(PlcConfigSelection configSelecao) {
			this.configSelecao = configSelecao;
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

		public List getNumTotalPaginasList() {
			return numTotalPaginasList;
		}

		public void setNumTotalPaginasList(List numTotalPaginasList) {
			this.numTotalPaginasList = numTotalPaginasList;
		}

		public void reset (){
			this.posAtual = 0;
			this.numTotalRegistros = 0;
			this.numTotalPaginas = 0;
			this.paginaAtual = 0;
			this.navDe = 0;
			this.navAnterior = 0;
			this.navProxima = 0;
			this.navUltima = 0;
			this.navAte = 0;
			this.numPorPagina = 0;
			this.numTotalPaginasList = new ArrayList();
		}
		
		public int getNavAnterior() {
			return navAnterior;
		}
		
		public void setNavAnterior(int navAnterior) {
			this.navAnterior = navAnterior;
		}
		
		public int getNavProxima() {
			return navProxima;
		}
		
		public void setNavProxima(int navProxima) {
			this.navProxima = navProxima;
		}
		
		public int getNavUltima() {
			return navUltima;
		}
		
		public void setNavUltima(int navUltima) {
			this.navUltima = navUltima;
		}

		public int getNumPorPagina() {
			return numPorPagina;
		}
		
		public void setNumPorPagina(int numPorPagina) {
			this.numPorPagina = numPorPagina;
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
	public PlcPagedControl (){}
	
	
	public void setSelecaoPaginada (PlcConfigSelection selecao, String url, long numTotalRegistros, int proxima){
		
		PaginacaoController controller = this.mapaControles.get(url);
		
		if (controller == null){
			controller = new PaginacaoController (0, numTotalRegistros, selecao);
			controller.setPaginaAtual(1);
		} else {
			controller.setPosAtual(proxima / selecao.pagination().numberByPage());
			controller.setNumTotalRegistros(numTotalRegistros);
			
			controller.setNumPorPagina(selecao.pagination().numberByPage());
			int numTotalPaginas = (int)numTotalRegistros / controller.getNumPorPagina();
			
			List numTotalPaginasList = new ArrayList();
			if ((numTotalRegistros % controller.getNumPorPagina()) != 0) {
				numTotalPaginas = numTotalPaginas + 1;
			} 

			for (int i = 0; i < numTotalPaginas; i++) {
				numTotalPaginasList.add(i);
			}
			
			controller.setNumTotalPaginas(numTotalPaginas);
			controller.setNumTotalPaginasList(numTotalPaginasList);
			controller.setPaginaAtual(controller.getPosAtual());
			
		}
		
		this.mapaControles.put (url, controller);
		
	}
	
	public PlcConfigSelection getSelecaoPorNome (String nomeSelecao){
		PaginacaoController controller = this.mapaControles.get(nomeSelecao);

		if (controller != null)
			return controller.getConfigSelecao();
		
		return null;
	
	}
	
	public int  getNavAte(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNavAte();
	}
	
	public int  setNavAte(String url, int navAte) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNavAte(navAte);
		return controller.getNavAte();
	}
	
	public int  getNavAnt(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNavAnterior();
	}
	
	public int  setNavAnt(String url, int navAnt) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNavAnterior(navAnt);
		return controller.getNavAnterior();
	}
	
	public long setNumTotalRegistros (String url, long numRegistros){
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNumTotalRegistros(numRegistros);
		return controller.getNumTotalRegistros();
	}
	
	public int  getNavDe(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNavDe();
	}
	
	public int  setNavDe(String url, int navDe) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNavDe(navDe);
		return controller.getNavDe();
	}
	
	public int  getNavProx(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNavProxima();
	}
	
	public int  setNavProx(String url, int navProxima) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNavProxima(navProxima);
		return controller.getNavProxima();
	}
	
	public int  getNavUlt(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNavUltima();
	}
	
	public int  setNavUlt(String url, int navUltima) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNavUltima(navUltima);
		return controller.getNavUltima();
	}
	
	public int  getNumPorPagina(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNumPorPagina();
	}
	
	public int  setNumPorPagina(String url, int numPorPagina) {
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setNumPorPagina(numPorPagina);
		return controller.getNumPorPagina();
	}
	
	public int getPosicaoAtual (String url){
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getPosAtual();
	}
	
	public int setPosicaoAtual (String url, int pos){
		PaginacaoController controller = this.mapaControles.get(url);
		controller.setPosAtual(pos);
		
		int numPorPagina = controller.getConfigSelecao().pagination().numberByPage();
		
		int paginaAtual = pos / numPorPagina + 1;
		
		controller.setPaginaAtual(paginaAtual);
		
		return controller.getPosAtual();
	}
	
	public long getNumTotalRegistros (String url){
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNumTotalRegistros();
	}
	
	public int getNumTotalPaginas (String url){
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNumTotalPaginas();
	}
	
	public List getNumTotalPaginasList(String url) {
		PaginacaoController controller = this.mapaControles.get(url);
		return controller.getNumTotalPaginasList();
	}

	public PaginacaoController getController (String url){
		return this.mapaControles.get(url);
	}


	public Map<String, PaginacaoController> getMapaControles() {
		return this.mapaControles;
	}


	public void setMapaControles(Map<String, PaginacaoController> mapaControles) {
		this.mapaControles = mapaControles;
	}


	public String getDetCorrPlcPaginado() {
		return this.detCorrPlcPaginado;
	}


	public void setDetCorrPlcPaginado(String detCorrPlcPaginado) {
		this.detCorrPlcPaginado = detCorrPlcPaginado;
	}
	
	public Map <String, PlcConfigSelection> getMapaSelecaoPaginacao(){
		
		Map<String, PlcConfigSelection> mapaDetalhesPaginados = new HashMap<String, PlcConfigSelection>();
		
		Set<String> keys = mapaControles.keySet();
		
		for (String key : keys) {
			PaginacaoController controller = mapaControles.get(key);
			PlcConfigSelection configSelecao = controller.getConfigSelecao();
			mapaDetalhesPaginados.put(key, configSelecao);
		}
		
		return mapaDetalhesPaginados;
	}
	
	public void reset (){
	
		Set<String> keys = mapaControles.keySet();
		
		for (String key : keys) {
			PaginacaoController controller = mapaControles.get(key);
			controller.reset();
		}
	}

	
	
}
