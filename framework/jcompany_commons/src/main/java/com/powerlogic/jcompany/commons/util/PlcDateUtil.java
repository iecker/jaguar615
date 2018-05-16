/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * @since jCompany 2.5.3
 * Singleton. Classe utilitária para datas
 */
@SPlcUtil
@QPlcDefault
public class PlcDateUtil  {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private transient Logger log;
	
    public PlcDateUtil() { 
    	
    }
    
	
	/**
	 * @since jCompany 1.5.3
	 * Método que exibe uma data utilizando .toLocaleString(), somente se ela não for nula.
	 * @param dataAExibir
	 * @return 	Se data for nula exibe "", senão exibe .toLocaleString();
	 */
	public static String showWithHoursNull(Date dataAExibir) {
		if (dataAExibir == null) {
			return "";
		} else {
			return dataAExibir.toLocaleString();
		}
	}
	
	/**
	 * @since jCompany 1.5.3
	 * Método que exibe uma data utilizando .toLocaleString().substring(0,10), 
	 * somente se ela não for nula.
	 * @param dataAExibir
	 * @return 	Se data for nula exibe "", senão exibe somente parte data (sem hora);
	 */
	public static String showWithoutHoursNull(Date dataAExibir) {
		if (dataAExibir == null)
			return "";
		else
			return dataAExibir.toLocaleString().substring(0,10);
	}


	/**
	 * @since jCompany 3.0
	 * Devolve o número de dias entre duas datas. Nota exemplo: Se hoje é 1/12/2004 e amanhã é 2/12/2004, devolve 0 (Zero),
	 * pois não há nenhum dia entre as duas datas!
	 */
	public float daysBetweenDates(Date dataInicial, Date dataFinal) {

		log.debug( "###################### Entrou no evento diasEntreDatas");

		//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		//Locale lc = new Locale("br", "BRA");
		//Calendar cal = Calendar.getInstance(tz, lc);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		log.debug( "DataInicial:" + formatter.format(dataInicial));
		log.debug( "DataFinal:" + formatter.format(dataFinal));

		float dif =
			(dataFinal.getTime() - dataInicial.getTime())
				/ (24 * 60 * 60 * 1000);

		if (log.isDebugEnabled())
			log.debug( "Diferenca entre datas = " + dif);

		return dif;
	}
	
	/**
	 * @since jCompany 5.0 Devolve o número de anos entre duas datas. 
	 * Pode ser utilizado para calcular idade, por exemplo
	 * Nota exemplo: Se dataInicial é 1/12/2000 e a final é 2/12/2004, devolve 4 (quatro)
	 */
	public long yearsBetweenDates(Date dataInicial, Date dataFinal) {

		 long dt = (dataFinal.getTime() - dataInicial.getTime()) + 3600000;

		if (log.isDebugEnabled())
			log.debug( "Diferenca milisegundos entre datas = " + dt);

         Date dataFinalAux = new Date(dt);

         return dataFinalAux.getYear()-70;

	}
	
	/**
	 * Retorna uma data passada ou futura, com diferença de anos, em relação á data atual
	 * 
	 * @param dataReferencia Data utilizada como referência para o cálculo
	 * @param numeroAnosMinimos Anos a serem adicionados ou subtraidos da dataReferencia
	 * @param noPassado true para calcular a data no passado e true para futuro
	 * @return a data calculada conforme parâmetros
	 */
	public Date dateAccodingToYears(Date dataReferencia, long numeroAnosMinimos, boolean noPassado )
	{
		GregorianCalendar cal = new GregorianCalendar();
		
		cal.setTime(dataReferencia);
		
		cal.roll(Calendar.YEAR, (int) (noPassado ? ( numeroAnosMinimos * -1 ) : numeroAnosMinimos) );
		
		return cal.getTime();
	}



	/**
	 * @since jCompany 3.0
		* Devolve o número de segundos entre duas data-horas.
		* 
		* @param dataInicial
		* @param dataFinal
		* @return dif
		*/
	public float secondsBetweenDates(Date dataInicial, Date dataFinal) {

		log.debug( 
			"###################### Entrou no evento segundosEntreDatas");

		try {
			//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
			//Locale lc = new Locale("br", "BRA");
			//Calendar cal = Calendar.getInstance(tz, lc);
			
			if(log.isDebugEnabled()) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			    log.debug( "DataInicial:" + formatter.format(dataInicial));
			    log.debug( "DataFinal:" + formatter.format(dataFinal));
			}

			float dif = (dataFinal.getTime() - dataInicial.getTime()) / 1000;

			if (log.isDebugEnabled())
				log.debug( "Diferença entre datas = " + dif);

			return dif;

		} catch (Exception e) {
			log.error( e.toString(), e);
			return 0;
		}
	}

	/**
	 * @since jCompany 3.0
		* Devolve próximo início de mês.
		* @param mes no formato String com zeros. Ex: "01", "11", "12"
		* @param ano no formato String com quatro dígitos. Ex: "2004", "2005"
		* @return Retorna um Date com parte hora 00:00:00 e primeiro dia do mês seguinte
		*/
	public Date firstDayOfNextMonth(String mes, String ano) throws Exception {

		log.debug( "###################### Entrou no evento proximoInicioMes");

		//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		//Locale lc = new Locale("br", "BRA");
		//Calendar cal = Calendar.getInstance(tz, lc);

		SimpleDateFormat formatter =
			new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Verifica próximo ano
		int proxAno = new Integer(ano).intValue();
		int proxMes = new Integer(mes).intValue();
		if (mes.equals("12")) {
			proxAno++;
			proxMes = 1;
		} else
			proxMes++;

		if (proxMes <= 9)
			mes = proxMes + "";
		else
			mes = "0" + proxMes;

		Date proxInicioMes =
			formatter.parse("01/" + mes + "/" + proxAno + " 00:00:00");

		if (log.isDebugEnabled())
			log.debug( "DataFinal:" + proxInicioMes.toLocaleString());

		return proxInicioMes;

	}

	/**
	 * @since jCompany 3.0
	* Devolve início de mês.
	* @param mes no formato String com zeros. Ex: "01", "11", "12"
	* @param ano no formato String com quatro dígitos. Ex: "2004", "2005"
	* @return Retorna um Date com parte hora 00:00:00 e primeiro dia do mês
	*/
	public Date firsDayOfMonth(String mes, String ano) throws Exception {

		log.debug( "###################### Entrou no evento proximoInicioMes");

		//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		//Locale lc = new Locale("br", "BRA");
		//Calendar cal = Calendar.getInstance(tz, lc);

		SimpleDateFormat formatter =
			new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date inicioMes = formatter.parse("01/" + mes + "/" + ano + " 00:00:00");

		if (log.isDebugEnabled())
			log.debug( "DataFinal:" + inicioMes.toLocaleString());

		return inicioMes;

	}

	/**
	 * @since jCompany 3.0
	 * Retorna se a segunda data é maior que a primeira 
	 * @param dataInicial Data inicial
	 * @param dataFinal Data Final
	 * @return True se a segunda data for maior, incluindo precisão de milionésimos
	 * de segundo.
	 */
	public boolean compareDates(Date dataInicial, Date dataFinal) {
		if (daysBetweenDates(dataInicial, dataFinal) > 0)
			return true;
		else
			return false;
	}

	/**
	 * @since jCompany 3.0
		* Retorna se a segunda data é maior ou igual à primeira 
		* @param dataInicial Data inicial
		* @param dataFinal Data Final
		* @return True se a segunda data for maior, incluindo precisão de milionésimos
		* de segundo.
		*/
	public boolean compareDatesEqual(Date dataInicial, Date dataFinal) {
		if (daysBetweenDates(dataInicial, dataFinal) >= 0)
			return true;
		else
			return false;
	}

	/**
	 * @since jCompany 3.0
	 * Retorna se a segunda data é maior que a primeira 
	 * @param dataInicial Data inicial
	 * @param dataFinal Data Final
	 * @return True se a segunda data for maior, incluindo precisão de milionésimos
	 * de segundo.
	 */
	public boolean compareDatesSeconds(
		Date dataInicial,
		Date dataFinal) {
		if (secondsBetweenDates(dataInicial, dataFinal) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * @since jCompany 3.0
	 * Retorna se a segunda data é maior ou igual à primeira 
	 * @param dataInicial Data inicial
	 * @param dataFinal Data Final
	 * @return True se a segunda data for maior, incluindo precisão de milionésimos
	 * de segundo.
	 */
	public boolean compareDatesEqualSeconds(
		Date dataInicial,
		Date dataFinal) {
		if (secondsBetweenDates(dataInicial, dataFinal) >= 0)
			return true;
		else
			return false;
	}

	/**
	 * @since jCompany 3.0
	 * Soma ou subtrar um determinado número de dias de uma data inicial, devolvendo a
	 * data resultante
	 * @param dataInicial Data inicial
	 * @param numDias Número de dias, positivo ou negativo, para conta.
	 * @return Data resultante
	 */
	public Date addDays(Date dataInicial, int numDias) {

		if (log.isDebugEnabled())
			log.debug( "###################### Entrou no evento dataNumDias para "+dataInicial.toLocaleString()+" e"+
				numDias);

		//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		//Locale lc = new Locale("br", "BRA");
		//Calendar cal = Calendar.getInstance(tz, lc);

		//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (log.isDebugEnabled())
			log.debug( "DataInicial:" + dataInicial.toLocaleString()+" dataInicial.time="+dataInicial.getTime());
		
		long aAdicionar = ((24 * 60 * 60 * 1000) * (long)numDias);
		
		long tempoFinal =	dataInicial.getTime() + aAdicionar;
		
		if (log.isDebugEnabled())
			log.debug( "inicio = "+dataInicial.getTime()+" vai somar "+aAdicionar+
				" vai dar Tempo final = "+tempoFinal);

		Date dataFinal = new Date(tempoFinal);

		if (log.isDebugEnabled())
			log.debug( "Data Resultante = " + dataFinal.toLocaleString());

		return dataFinal;

	}

	/**
	 * @since jCompany 3.0
	 * Devolve uma data que representa o primeiro dia do mês e outra que representa o último.
	 * @param dataAtual Data corrente
	 * @return Object[] contendo dois objetos java.util.Date com dataInicial e dataFinal nas posições 0 e 1
	 */
	public Date lastDayOfMonth(Date dataAtual) {

		Calendar c = new GregorianCalendar();
		c.setTime(dataAtual);
		
		TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		Locale lc = new Locale("br", "BRA");
		Calendar cal = Calendar.getInstance(tz, lc);
		cal.set(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		int diaFinal = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar dataFinal = new GregorianCalendar();
		dataFinal.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), diaFinal, 23, 59, 59);
		if (log.isDebugEnabled())
			log.debug( 
				"O dia final para o mes="
					+ c.get(Calendar.MONTH)
					+ " eh ="
					+ dataFinal.getTime());

		return dataFinal.getTime();
	}

	/**
	 * @since jCompany 3.0
	 * Recebe uma Data e devolve o nome do mês, conforme o locale.
	 * @param data
	 * @return Nome do Mês
	 */
	public String monthName(Date data) {

		SimpleDateFormat f = new SimpleDateFormat("MMMMMMMMMMMMMMM");
		return f.format(data);

	}

	/**
	 * @since jCompany 3.0
	  * Retorna a data do último dia do mês de um determinado ano e mês dados, 
	  * com a hora zerada.
	  *  
	  * @param ano Ano
	  * @param mes Mês (1 a 12)
	  * @return Dia (1 a 31)
	 */
	public static Date lastDayOfMonth(int ano, int mes) {

		return lastDayOfMonth(ano, mes, 0, 0, 0);

	}

	/**
	 * @since jCompany 3.0
	 * Retorna a data do último dia do mês de um determinado ano e mês dados	
	 * Este método cria também uma data com parâmetros de hora fornecidos. 
	 * 
	 * @param ano Ano	
	 * @param mes Mês (1 a 12)	
	 * @param hora Hora (0 a 23)	
	 * @param minuto Minuto (0 a 59	
	 * @param segundo Segundo (0 a 59)
	 * @return Date contendo a data do último dia do mês de um determinado ano e mês dados
	*/
	public static Date lastDayOfMonth(
		int ano,
		int mes,
		int hora,
		int minuto,
		int segundo) {

		GregorianCalendar cal = new GregorianCalendar();

		cal.set(ano, mes - 1, 1, 0, 0, 0);

		int dia = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		cal.set(ano, mes, dia, hora, minuto, segundo);

		return cal.getTime();

	}

	/**
	 * @since jCompany 3.0
	 * Retorna a data com os parâmetros fornecidos, com a hora zerada.
	 * 
	 * @param ano Ano
 	 * @param mes Mês (1 a 12)
	 * @param dia (1 a 31)
	 * @return Date com a hora zerada
	*/
	public static Date newDate(int ano, int mes, int dia) {

		return newDate(ano, mes, dia, 0, 0, 0);

	}

	/**
	 * @since jCompany 3.0
	 * Retorna a datahora com os parâmetros fornecidos.
	 * 
	 * @param ano Ano 
	 * @param mes Mês (1 a 12)
	 * @param dia Dia (1 a 31)
	 * @param hora Hora (0 a 23) 
	 * @param minuto Minuto (0 a 59)
	 * @param segundo Segundo (0 a 59)
	 * @return Date uma nova data com os parâmetros fornecidos
	 */
	public static Date newDate(
		int ano,
		int mes,
		int dia,
		int hora,
		int minuto,
		int segundo) {

		GregorianCalendar cal = new GregorianCalendar();

		cal.set(ano, mes - 1, dia, hora, minuto, segundo);

		return cal.getTime();

	}
	
	/**
	 * @since jCompany 3.0
	 * @param dataInicial Data inicio inclusive
	 * @param dataFinal Data fim inclusive
	 * @param hhUteisPorDia HH uteis por dia
	 * @param ferPeriodo Mapa de feriados no intervalo passado,
	 * contendo a data como chave e o String I-Integral ou P-Parcial como valor
	 * @param opcaoSabadoDomingo  Opção para considerar ou não fins de semana como descanso. Na atual versão,
	 * somente SABADO_E_DOMINGO" está funcionando
	 * @return float
	 */
	public float hhRestingDaysBetweenDates(Date dataInicial, Date dataFinal,long hhUteisPorDia,
			Map ferPeriodo,
			String opcaoSabadoDomingo) {
	
		log.debug( "########## Entrou em hhDescansoEntreDatas");

		//TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
		//Locale lc = new Locale("br", "BRA");
		//Calendar cal = Calendar.getInstance(tz, lc);
	
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (log.isDebugEnabled()) {
			log.debug( "DataInicial:" + formatter.format(dataInicial));
			log.debug( "DataFinal:" + formatter.format(dataFinal));
		}
		
		float hhDescansoFimSemana = 0;
	
		// Pega sabados e domingos entre as duas datas e coloca em um
		long totFimSemana = getWeekendsBetweenDates(dataInicial,dataFinal,opcaoSabadoDomingo);
		
		hhDescansoFimSemana = totFimSemana * hhUteisPorDia;
		
		float hhDescansoFeriados = 0;
		
		// Pega feriados entre as duas datas, e que não caiam em sábado ou domingo
		if (ferPeriodo != null) {
						
			Iterator i = ferPeriodo.keySet().iterator();
			while (i.hasNext()) {
				
				Date dataFeriado = (Date) i.next();
				String tipo = (String) ferPeriodo.get(dataFeriado);
				if ((compareDatesEqual(dataInicial,dataFeriado) &&
						compareDatesEqual(dataFeriado,dataFinal)) &&
						!isWeekend(dataFeriado,opcaoSabadoDomingo)) {
					if (tipo.equals("I"))
						hhDescansoFeriados = hhDescansoFeriados + hhUteisPorDia;
					else
						hhDescansoFeriados = hhDescansoFeriados + (hhUteisPorDia / 2);
				}

			}
		}
		
		return hhDescansoFimSemana + hhDescansoFeriados;
	}
	
	/**
	 * @since jCompany 3.0
	 * Recupera número de dias de fim de semana entre duas datas, utilizando o seguinte algoritimo:
	 * -Verifica se a datainicial começa no fim de semana. Se começar, contabiliza os dias e ajusta para o próximo dia útil. (proxima segunda)
	 * -Verifica se a dataFinal encerra no fim de semana. se encerrar, contabiliza os dias e ajusta para o dia útil anterior (última sexta)
	 * -Feitos os ajustes, pode considerar fins de semana como divisões por 7
	 * @param dataInicialA
	 * @param dataFinalA
	 * @param opcaoSabadoDomingo Somente "SABADO_E_DOMINGO" deve ser passado nesta versão
	 * @return long numero de dias de fim de semana 
	 */
	public long getWeekendsBetweenDates(Date dataInicialA, Date dataFinalA, String opcaoSabadoDomingo) {
	    return getWeekendDaysBetweenDates(dataInicialA,dataFinalA);
	}

   /**
    * Calcula o número de dias de final de semana que existe entre duas datas.
    * <br>
    * <br>
    * 
    */
   private long getWeekendDaysBetweenDates(Date dataIni, Date dataFim) {
       log.debug( "########## Entrou no diasDeFinaisDeSemanaEntreDatas");

       Calendar calINI = Calendar.getInstance();
       Calendar calFIM = Calendar.getInstance();
       
       dataIni = new Date(dataIni.getYear(), dataIni.getMonth(), dataIni.getDate());
       dataFim = new Date(dataFim.getYear(), dataFim.getMonth(), dataFim.getDate());
       
       calINI.setTime(dataIni);
       calFIM.setTime(dataFim);
       
       log.debug( " Período data inicial: " + calINI.get(Calendar.DAY_OF_MONTH)+"/"+calINI.get(Calendar.MONTH)+"/"+calINI.get(Calendar.YEAR)+" e data final: " + calFIM.get(Calendar.DAY_OF_MONTH)+"/"+calFIM.get(Calendar.MONTH)+"/"+calFIM.get(Calendar.YEAR));
       
       int restoArredondamento = 0;

       // se a data inicio for no primeiro dia da semana (Domingo), adiciona
       // mais dois dias no arredondamento pois sera somado no final.
       if (calINI.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
           restoArredondamento += 2;
       else
           // se a data inicio não for no primeiro dia da semana (sábado),
           // adiciona mais um dia no arredondamento.
           restoArredondamento += 1;
       
       //se a data fim for no ultimo dia da semana (sábado), adiciona mais
       // dois dias no arredondamento.
       if (calFIM.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
           restoArredondamento += 2;
       else
           // se a data fim não for no ultimo dia da semana (sábado), adiciona
           // mais um dia  no arredondamento pois sera somado no final.
           restoArredondamento += 1;
       
       int restoIni = Calendar.SATURDAY - calINI.get(Calendar.DAY_OF_WEEK) ;
       int restoFim = calFIM.get(Calendar.DAY_OF_WEEK);
       
       // Adiciona os dias da semana que rtestam para passar para proxima semana
       // retirando assim os dias que não estão completos. 
       //Proporcionando uma divisão exata por 7 
       calINI.add(Calendar.DAY_OF_WEEK, restoIni + 1 );
       calFIM.add(Calendar.DAY_OF_WEEK, - restoFim);
       
       //calcula aquantidade de dias entre o periodo divide por 7 e multiplica por 2
       int dif = (int) (((calFIM.getTime().getTime() - calINI.getTime().getTime()) / (24 * 60 * 60 * 1000)) + 1) / 7 * 2;

       dif += restoArredondamento;

       log.debug( "Total de dias entre final de semana: " + dif);       

       log.debug( "########## Sai do diasDeFinaisDeSemanaEntreDatas");        
       return dif;
   }

	
	/**
	 * @since jCompany 3.0
	 * Retorna true se uma data for fim de semana, conforme criterio passado
	 * @param dataFeriado Dia a verificar
	 * @param opcaoSabadoDomingo "SOMENTE_SABADO", "SOMENTE_DOMINGO" ou "SABADO_E_DOMINGO" (default)
	 * @return boolean true se data for fim de semana e false caso contrario.
	 */
	public boolean isWeekend(Date dataFeriado,String opcaoSabadoDomingo) {
		
		log.debug( "########## Entrou em eFimSemana");

		int diaSemana = dataFeriado.getDay();
		
		if ((opcaoSabadoDomingo.equalsIgnoreCase("SABADO_E_DOMINGO") && (diaSemana == 0 || diaSemana == 6)) ||
			(opcaoSabadoDomingo.equalsIgnoreCase("SABADO") && (diaSemana == 6)) ||
			(opcaoSabadoDomingo.equalsIgnoreCase("DOMINGO") && (diaSemana == 0)))
			return true;
		else
			return false;
	}
	
	/**
	 * Calcula no. de horas úteis em um período, descontando-se feriados e fim-de-semana
	 * @param dataIni data de Início
	 * @param dataFim data de Fim
	 * @param feriados Mapa de Feriados
	 * @param hhUtilPorDia HH úteis por dia
	 * @param sabadoDomingo fixo nesta varsão "SABADO_E_DOMIGO".
	 * @return numero de horas uteis no período
	 */
	public float calculateHHLaborBetweenDates(Date dataIni, Date dataFim, Map feriados,
			long hhUtilPorDia,String sabadoDomingo)  {

		try {
			
			if (log.isDebugEnabled()) {
				log.debug( "########## Entrou em calculateHHLaborBetweenDates para dataIni="+dataIni.toLocaleString()+" e data fim " + dataFim.toLocaleString());
			}
	  		float hhDescanso = hhRestingDaysBetweenDates(dataIni,dataFim,hhUtilPorDia,feriados,"SABADO_E_DOMINGO");
	  		float diasUteis = daysBetweenDates(dataIni,dataFim) + 1;
	  		float hhUteis = (diasUteis * hhUtilPorDia) - hhDescanso;
	  		// Não pode retornar negativo. Neste caso evita
			return hhUteis;
		
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcDateUtil", "calculateHHLaborBetweenDates", e, log, "");
		}
		
  		
	}
	/**
	 * @since jCompany 3.0
	 * Converte data utilizando formato informado
	 * @param data Data em String
	 * @param formato Formato no padrão java (ex: dd/MM/yyyy HH:mm)
	 * @return java.util.Date a partir do String ou null se entrada for nula. Dispara exceção 
	 */
	public Date dateToString(String data, String formato)  {
		
		log.debug( "########## Entrou em converteDataParaString");
		
		if (data == null || data.trim().equals(""))
			return null;
		
		SimpleDateFormat sd = new SimpleDateFormat();

		try {
			
			sd.applyPattern(formato);
			Date d = sd.parse(data);
			return d;
			
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_INVALID_DATE, new Object[] {data,formato}, true);
		}
		
	}

	/**
	 * @since jCompany 3.0
	 * Recupera lista de datas atraves de uma data inicial e fianal
	 * @param inicio Date
	 * @param fim Date
	 * @return Lista contendo objetos Date.
	 */
	public List listOfDatesBetweenDates(Date inicio, Date fim){
	    
	    if(inicio == null || fim == null)
	        return new ArrayList();
	        
	    List listaRetorno = new ArrayList();
       Map lista = new HashMap();

       Float dias = new Float(daysBetweenDates(inicio, fim));

       Calendar cal = Calendar.getInstance();

       cal.setTime(inicio);
       lista.put(cal.getTime(), cal.getTime());
       for (int i = 1; dias.intValue() > i - 1; i++){
           cal.add(Calendar.DAY_OF_YEAR, 1);
           lista.put(cal.getTime(), cal.getTime());
       }
       listaRetorno = new ArrayList(lista.values());
       Collections.sort(listaRetorno);
       return listaRetorno;
	}
	
	public Date stringToDate(String data){
		DateFormat dataFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pt" ,"BR"));
		try{
			return dataFormat.parse(data);
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_INVALID_DATE, new Object[] {data, dataFormat}, true);
		}
	}
	
	/**
	 * 
	 * @param dataS
	 * @param mascara
	 * @return
	 * 
	 */
	public Date stringToDate(String dataS,String mascara){
		SimpleDateFormat mascaraSDF = new SimpleDateFormat(mascara);
		try{
			return mascaraSDF.parse(dataS);
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_INVALID_DATE, new Object[] {dataS , mascara}, true);
		}
	}

	/**
	 * Recebe uma long (milisegundos) e transforma em uma data descritiva
	 * em horas, minutos e segundos
	 * @param millis
	 * @return
	 */
	public static String millisToTimeDesc(Long millis) {
		
		Integer segundos = (int) (millis / 1000);
		Integer minutos = 0;
		Integer horas = 0;
		String mensagem = "";
		
		if (segundos >= 60) {
			minutos = segundos / 60;
			if (minutos >= 60) {
				horas = minutos / 60;
				minutos = minutos - (horas * 60);
				segundos = segundos - ((horas * 3600) + (minutos * 60));
				mensagem = horas + " h, " + minutos + " m ";
				if (segundos != 0) {
					mensagem = mensagem + segundos + " s";
				}
			} else {
				segundos = segundos - (minutos * 60);
				mensagem = minutos + " m ";
				if (segundos != 0) {
					mensagem = mensagem + segundos + " s";
				}
			}
		} else if (segundos != 0) {
			mensagem = segundos + " s";
		}
		
		return mensagem;
		
	}

}