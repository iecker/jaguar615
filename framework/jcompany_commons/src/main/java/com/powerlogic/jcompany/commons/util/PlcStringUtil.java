/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * jCompany 2.5.3. Singleton. Classe utilitária para datas
 */
// TODO - Refazer utilizando Apache StringUtils
@QPlcDefault
@SPlcUtil
public class PlcStringUtil implements Serializable {

	private static final long serialVersionUID = -5024354235515919974L;

	public PlcStringUtil() {

	}

	@Inject
	private transient Logger log;

	/**
	 * @since jCompany 3.0 Devolve uma lista de termos que estão entre dois
	 *        tokens.
	 * @param base
	 *            String base
	 * @param comecaCom
	 *            Token inicial
	 * @param terminaCom
	 *            Token final
	 * @return Coleção de termos
	 */
	public List<String> findSubstrings(String base, String comecaCom,
			String terminaCom) {

		List<String> termos = new ArrayList<String>();
		int ultPosSeparador = 0;
		int proximoIni = 0;
		int proximoFim = 0;

		do {

			proximoIni = base.indexOf(comecaCom, ultPosSeparador);

			String termo = "";

			if (proximoIni != -1) {

				proximoIni++;

				// Acha fim
				proximoFim = base.indexOf(terminaCom, proximoIni);

				if (proximoFim == -1) {
					termo = base.substring(proximoIni, base.length());
				} else {
					termo = base.substring(proximoIni, proximoFim);
				}

				ultPosSeparador = proximoFim + 1;

				termos.add(termo);

				if (log.isDebugEnabled())
					log.debug( "achou termo=" + termo);

			}

		} while (proximoIni != -1 && proximoFim != -1
				&& ((base.length() - ultPosSeparador) > 2));

		if (log.isDebugEnabled())
			log.debug( "Achou " + termos.size() + " termos");

		return termos;
	}

	/**
	 * @since jCompany 3.0 jCompany. Recebe uma string base, um termo de origem
	 *        e um novo termo e troca todas as ocorrências do original pelo
	 *        novo.
	 * @param base
	 *            String contendo todo o texto
	 * @param original
	 *            String contendo o termo a ser substituído
	 * @param novo
	 *            String contendo o novo termo
	 * @return String com termos substituídos.
	 */
	public String changeSubstring(String base, String original, String novo) {

		if (log.isDebugEnabled())
			log.debug( "Entrou em troca termo para trocar=" + original
					+ " para=" + novo + " em:" + base);

		String resultante = "";
		int inicial = 0;
		int proximo = 0;
		int fimTermo = 0;
		do {
			// Separa Token
			proximo = base.indexOf(original, inicial);
			if (proximo != -1) {
				fimTermo = proximo + original.length();
				resultante = resultante + base.substring(inicial, proximo)
						+ novo;

				if (base.substring(fimTermo, base.length()).indexOf("_") == -1)
					resultante = resultante
							+ base.substring(fimTermo, base.length());

			}
			inicial = proximo + 1;

			if (log.isDebugEnabled())
				log.debug( "troca termo resultante=" + resultante + " original="
						+ original + " inicial=" + inicial + " proximo="
						+ proximo);

		} while (proximo != -1 && ((base.length() - inicial) > 2));

		if (resultante.equals(""))
			resultante = base;

		if (log.isDebugEnabled())
			log.debug( "String resultante=" + resultante);

		return resultante;
	}

	/**
	 * @since jCompany 3.0 Devolve coleção de termos separados por virgula
	 */
	public List<String> splitSubstringList(String base) {

		return splitSubstringList(base, ",");

	}

	/**
	 * @since jCompany 3.0 Separa termos separados por um separador em uma
	 *        String Base. Se a base contiver String vazio ou o caracter "#"
	 *        despreza, retornando um List vazio.
	 * @param base
	 *            String no padrão "termo1,termo2,termo3", onde a virgula é o
	 *            separador
	 * @param separador
	 *            Substituto para a vírgula. Exs: ponto, hífen, etc..
	 * @return Coleção de Strings com os termos devidamente separados 
	 *         Refactoring para usar StringTokenizer
	 */
	public List<String> splitSubstringList(String base, String separador) {

		if (log.isDebugEnabled())
			log.debug( "Entrou em separaListaTermos para separar=" + base
					+ " com separardor=" + separador);

		List<String> destino = new ArrayList<String>();

		if (base == null || base.equals("") || base.equals("#"))
			return destino;

		int ultPosSeparador = 0;
		int proximo = 0;
		do {

			proximo = base.indexOf(separador, ultPosSeparador);
			String termo = "";
			if (proximo == -1) {
				termo = base.substring(ultPosSeparador, base.length()).trim();
			} else {
				termo = base.substring(ultPosSeparador, proximo).trim();
			}
			ultPosSeparador = proximo + 1;
			log.debug( "Separou termo=" + termo);

			destino.add(termo);
		} while (proximo != -1);

		return destino;
	}

	/**
	 * @since jCompany 1.5 Retira trecho de uma String a partir de um
	 *        identificador de início e fim. Faz isso uma única vez
	 * @param base
	 *            String de base
	 * @param tokenIni
	 *            Identificador de inicio
	 * @param tokenFim
	 *            Identificador de fim
	 * @return String sem o trecho entre os dois identificadores. Se não acha
	 *         token devolve o conteudo original
	 */
	public String removeSubstring(String base, String tokenIni, String tokenFim) {

		int posIni = base.indexOf(tokenIni);
		int posFim = base.indexOf(tokenFim);

		if (posIni == -1 || posFim == -1)
			return base;
		else {

			posFim = posFim + tokenFim.length();

			if (log.isDebugEnabled())
				log.debug( "Vai tentar retirar trecho da posicao " + posIni
						+ " ate " + posFim + " de um conteudo com "
						+ base.length() + "de tamanho");

			base = StringUtils.replaceOnce(base,
					base.substring(posIni, posFim), "");

			return base;
		}
	}

	/**
	 * @since jCompany 3.0 Coloca primeira letra maiúscula
	 * @param termo
	 *            String para retirar acentos
	 * @return String sem acentos
	 */
	public String capitalize(String termo) {

		log.debug( "Entrou para capitalizar");

		if (log.isDebugEnabled())
			log.debug( "########Termo inicial : " + termo);

		termo = StringUtils.capitalize(termo);

		if (log.isDebugEnabled())
			log.debug( "########Termo capitalizado: " + termo);

		return termo;
	}

	/**
	 * @since jCompany 3.0 Retira os acentos de uma string
	 * @param termo
	 *            String para retirar acentos
	 * @return String sem acentos
	 */
	public String removeAccents(String termo) {

		log.debug( "Entrou para tirarAcentos conteudos acentuados");

		String acentos = "áàãâäèéêëìíïîòóõôöûùúüçÁÀÃÂÄÈÉÊËÌÍÏÎÒÓÕÔÖÛÙÚÜÇ";
		String semAcentos = "aaaaaeeeeiiiiooooouuuucAAAAAEEEEIIIIOOOOOUUUUC";

		termo = StringUtils.replaceChars(termo, acentos, semAcentos);

		return termo;
	}

	/**
	 * @since jCompany 3.0 Recebe uma String com vários "tokens" concatenados e
	 *        separados por um caracter especial e um token a ser removido desta
	 *        String. Devolve um StringBuffer sem o token.
	 * @param valorBase
	 *            String contendo vários tokens
	 * @param tokenARemover
	 *            Token a ser removido do valorBase
	 * @param separadorTokens
	 *            String que separa cada token
	 * @return StringBuffer baseada no valorBase sem tokenARemover.
	 */
	public String removeToken(String valorBase, String tokenARemover,
			String separadorTokens) {

		if (log.isDebugEnabled())
			log.debug( "Recebeu lista:" + valorBase + " para remover:"
					+ tokenARemover + " com separador:" + separadorTokens);

		StringTokenizer st = new StringTokenizer(valorBase, separadorTokens);
		StringBuffer sb = new StringBuffer();
		int cont = 0;

		while (st.hasMoreTokens()) {
			String idCookie = st.nextToken();
			if (!idCookie.equals(tokenARemover)) {
				cont++;
				sb.append(idCookie);
				sb.append(separadorTokens);
			} else if (log.isDebugEnabled())
				log.debug( "Removeu item:" + idCookie);
		}

		// Retira o último separador, se tiver
		String ret = "";
		if (!sb.toString().equals("")) {
			ret = sb.toString().substring(0, sb.toString().length() - 1);
		}

		if (log.isDebugEnabled())
			log.debug( "Vai devolver:" + ret);
		return ret;
	}

	/**
	 * @since jCompany 1.5 Troca um nome com sublinhado por pontos. Na
	 *        declaração de clausulas para restrição de existência (duplicidade)
	 *        e outras, no caso de propriedades de argumentos serem de classes
	 *        agregadas, o usuário deve informar o separador entre propriedades
	 *        como sublinhados ou invés de ponto, para não ferir a lógica
	 *        automática do jCompany. Este método reconverte para a notação de
	 *        ponto para envio ao SGBD.
	 *        <p>
	 *        Exemplo: Declaração no struts-config.xml:
	 * 
	 *        obj.idAgregado.nome=:idAgregado_nome
	 * 
	 * @param nome
	 *            String a ser inspecionada, com sublinhado
	 * @return String alterada, com pontos
	 */
	protected String checkName(String nome) {

		if (nome.indexOf("_") > 0) {

			nome = changeSubstring(nome, "_", ".");

			if (log.isDebugEnabled())
				log.debug( " nome com pontos = " + nome);

			return nome;

		} else
			return nome;

	}

	/**
	 * @since jCompany 3.04
	 * @param campos
	 *            Recebe um Array
	 * @return String contendo valores do array concatenados.
	 */
	public String arrayToString(Object[] array) {
		if (array == null)
			return "";
		StringBuffer s = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			Object obj = array[i];
			if (obj != null)
				s.append(obj.toString());
		}
		return s.toString();
	}

	/**
	 * @since jCompany 3.05
	 * @param colecaoDeStrings
	 *            List de Strings
	 * @return Vetor de Strings
	 */
	public String[] listToStrings(List<String> colecaoDeStrings) {
		String[] valArray = new String[colecaoDeStrings.size()];
		colecaoDeStrings.toArray(valArray);
		return valArray;
	}

	public String replaceIgnoreCase(String conteudo, String oldString,
			String newString) {
		conteudo = StringUtils.replace(conteudo, oldString, newString);
		conteudo = StringUtils.replace(conteudo, oldString.toUpperCase(),
				newString);
		conteudo = StringUtils.replace(conteudo, oldString.toLowerCase(),
				newString);

		return conteudo;
	}

}