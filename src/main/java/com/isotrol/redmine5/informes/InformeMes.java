package com.isotrol.redmine5.informes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.ROLES;
import com.isotrol.redmine5.comun.Constantes.TIPO;
import com.isotrol.redmine5.comun.Linea;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.comun.Responsable;
import com.isotrol.redmine5.comun.Tarifa;
import com.isotrol.redmine5.recursos.Anonimos;
import com.isotrol.redmine5.recursos.Recursos;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.TimeEntry;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Component("informeMes")
@Data
public class InformeMes 
{
	@Autowired
//	@Qualifier("recursosFactoria")
	@Qualifier("recursosNTT")
//	@Qualifier("recursosIsotrol")
	protected Recursos recursos;
	
	@Autowired
	protected Responsable responsable;
	
	@Autowired
	protected Linea linea;
	
	@Autowired
	protected Anonimos anonimos;
	
	protected String desde;
	protected String hasta;
	
	public InformeMes() 
	{
	    this.desde	= LocalDate.now().withDayOfMonth(1).toString();
	    this.hasta	= LocalDate.now().with( TemporalAdjusters.lastDayOfMonth() ).toString();
	}
	
	/**
	 * Generamos el informe de peticiones cerradas.
	 * Tiene como base una consulta almacenada que retorna las peticiones cerradas del mes anterior.
	 * Las horas mostradas son todas las del equipo Isotrol.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException 
	 */
	public void getInformeCerradas() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas ... ");
		
		this.generarInforme( Constantes.INF_CER, Constantes.INF_CERRADAS_UMES, false );
	}
	
	/**
	 * Informe de peticiones de gestión.
	 * Tiene como base un informe que retorna todas las tareas de gestión del proyecto (entre 2 fechas).
	 * Las horas que se muestran son las del último mes.
	 * Las horas mostradas son todas las del equipo Isotrol.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException 
	 */
	public void getInformeGestion() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Gestión ... ");
		
		this.generarInforme( Constantes.INF_GES, Constantes.INF_GESTION, true );
	}
	
	/**
	 * Informe de peticiones abiertas ... Menos las de gestión.
	 * Todas las horas.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException 
	 */
	public void getInformeAbiertas1() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Abiertas todas las horas ... ");
		
		this.generarInforme( Constantes.INF_ABI, Constantes.INF_ABIERTAS, false );
	}
	
	/**
	 * @see getInformeAbiertas1 
	 * Igual pero con filtro de tiempo en las entradas de tiempo
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException
	 */
	public void getInformeAbiertas2() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Abiertas filtro de ... ");
		
		this.generarInforme( Constantes.INF_ABI, Constantes.INF_ABIERTAS, true );
	}
	
	public void getInformeJM() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas 2025 ... ");
		
		this.generarInforme( Constantes.INF_CER, Constantes.INF_CERRADAS_JM, false );
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Método que obtiene los datos de una fila de los informes mensuales.
	 * 
	 * <custom_field id="44" name="Horas AN">
	 * <custom_field id="45" name="Horas TST">
	 * <custom_field id="46" name="Horas PR">
	 * <custom_field id="47" name="Horas JP">
	 * <custom_field id="50" name="Horas CO">
	 * <custom_field id="33" name="ID GesTIC">
	 * 
	 * Importante ... Quitar en el asunto los ; y "
	 * 
	 * @param is	Petición de la que deseamos la información
	 * @param mes	Indica si las entradas de tiempo son todas o sólo las del mes anterior
	 * @return		Mapa con toda la información necesaria para pintar una línea
	 *  
	 * @throws RedmineException
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ParseException 
	 */
	public Map<String, Object> getInformeLinea(Issue is, boolean mes) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		Map<String, Object> res	= new HashMap<>();
		var res1 				= this.getTiempoPeticion(is, mes);
		var res2 				= this.getTiempoIsotrol(res1);
		
		Calendar cal			= Calendar.getInstance();
		
		NumberFormat fmt1		= new DecimalFormat("#0.00");
		DateFormat fmt2			= new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat fmt3		= new DecimalFormat("#00");
		
		double eJP 				= this.procesaCampoDouble( is, 47 );
		double eCO 				= this.procesaCampoDouble( is, 50 );
		double eAN 				= this.procesaCampoDouble( is, 44 );
		double ePR 				= this.procesaCampoDouble( is, 46 );
		double eTot 			= eJP + eCO + eAN + ePR;
		double rJP				= res2.get( ROLES.JP );
		double rCO				= res2.get( ROLES.CO );
		double rAN				= res2.get( ROLES.AN );
		double rPR				= res2.get( ROLES.PR );
		double rTot				= rJP + rCO + rAN + rPR;
		String igt 				= this.procesaCampo( is, 33 );
		String tipo				= is.getTracker().getName();
		String prj				= is.getProjectName();
		Date ini				= is.getCreatedOn();		// START_DATE > CREATE_DATE
		Date fin				= is.getClosedOn();			// DUE_DATE > CLOSED_DATE
		TIPO linea				= this.linea.getLinea(tipo);
		int id					= is.getId();
		
		res.put( "id", id );
		res.put( "sbj", is.getSubject().replaceAll( ";", " " ).replaceAll( "\"", " " ) );
		res.put( "trk", tipo );
		res.put( "prj", prj );
		res.put( "eJP", fmt1.format(eJP) );
		res.put( "eCO", fmt1.format(eCO) );
		res.put( "eAN", fmt1.format(eAN) );
		res.put( "ePR", fmt1.format(ePR) );
		res.put( "eTot", fmt1.format(eTot) );
		res.put( "rJP", fmt1.format(rJP) );
		res.put( "rCO", fmt1.format(rCO) );
		res.put( "rAN", fmt1.format(rAN) );
		res.put( "rPR", fmt1.format(rPR) );
		res.put( "rTot", fmt1.format(rTot) );
		res.put( "ini", ini == null ? "" : fmt2.format(ini) );
		res.put( "fin", fin == null ? "" : fmt2.format(fin) );
		res.put( "igt", igt );
		res.put( "mes", fmt3.format( cal.get(Calendar.YEAR) ) + fmt3.format( cal.get(Calendar.MONTH) ) );
		res.put( "lin", linea.toString() );
		res.put( "dir", this.responsable.getNombre().get(prj) );
		res.put( "obv", this.procesaObservaciones( linea, id, res1.values().stream().mapToDouble(Double::doubleValue).sum(), rTot ) );
		
//		res.put( "est", is.getStatusName() );
//		res.put( "pd", is.getCustomFieldById(42) == null || is.getCustomFieldById(42).getValue() == null ? "" : is.getCustomFieldById(42).getValue() );
//		res.put( "chk", is.getCustomFieldById(26) == null || is.getCustomFieldById(26).getValue() == null ? "" : is.getCustomFieldById(26).getValue() );
		
		return res;
	}
	
	/**
	 * @throws ParseException 
	 * @see getInformeLinea(Issue is) 
	 */
	public Map<String, Object> getInformeLinea(int id, boolean mes) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		RedmineManager rm	= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS );
		Issue i				= rm.getIssueManager().getIssueById(id);
		
		return this.getInformeLinea(i, mes);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Obtiene el informe de horas de una petición.
	 * Las horas están agrupadas por ID usuario Redmine JdA.
	 * 
	 * @param i		Petición para la que necesitamos el informe de horas
	 * @param mes	Indica si las entradas de tiempo son las del mes anterior o todas
	 * @return		Mapa<ID usuario, total horas>
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException 
	 */
	protected Map<Integer, Double> getTiempoPeticion(Issue i, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		return this.resumenTiempo( this.getEntradasTiempo( i, mes ) );
	}
	
	/**
	 * @throws ParseException 
	 * @see getInformeTiempoTarea(Issue i)
	 */
	protected Map<Integer, Double> getTiempoPeticion(int id, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		RedmineManager rm	= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS );
		Issue i				= rm.getIssueManager().getIssueById(id);
		
		return this.getTiempoPeticion( i, mes );
	}
	
	/**
	 * Método que dada una petición, decarta las imputaciones de los no Isotrol.
	 * Los datos están agrupados por rol.
	 * 
	 * Filtramos los no Isotrol y luego creamos un mapa con los datos agrupados por rol.
	 * 
	 * XX se usa para saber el total de horas imputadas en la petición
	 * 
	 * @param res	Mapa<id usuario, horas> con las imputaciones de la petición
	 * @return		Entradas de tiempo <rol usuario, horas>
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 */
	protected Map<ROLES, Double> getTiempoIsotrol(Map<Integer, Double> res1) 
	throws URISyntaxException, IOException, RedmineException
	{
		Map<Integer, Double> res2	= new HashMap<>();
		Map<ROLES, Double> res3		= new HashMap<>();
		
		for( ROLES rol : ROLES.values() )
		{
			res3.put( rol, 0D );
		}

		res1.entrySet().forEach
		( 
			e ->
			{
				if( this.getRecursosProy().getNombre().containsKey( e.getKey() ) )
				{
					res2.put( e.getKey() , e.getValue() );
				}
			}
		);
		
		res2.entrySet().forEach
		( 
			e ->
			{
				ROLES rol = this.getRecursosProy().getRol().get( e.getKey() );
				res3.put( rol, res3.get(rol) + e.getValue() );
			}
		);
		
		return res3;
	}
	
	/**
	 * Obtiene el listado de peticiones del informe.
	 * Para ello hacemos uso de una consulta almacenada en Redmine.
	 * Hay que ir jugando con limit=A&offset=B
	 * 
	 * @param idInforme		Petición para la que necesitamos el informe de horas
	 * @return				Listado de peticiones Redmine ... Las que devuelve el informe
	 * 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws RedmineException 
	 */
	protected List<Issue> getPeticionesInforme(int idInforme) 
	throws URISyntaxException, IOException, RedmineException
	{
		RedmineManager rm		= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS );
		IssueManager im			= rm.getIssueManager();
		List<Issue> ltmp 		= new LinkedList<>();
		List<Issue> ltot		= new LinkedList<>();
		
		int offset				= 0;
		Map<String, String>	m	= new HashMap<>();
        
        m.put( "key", Constantes.API_KEY_SILVA_CSBS );
        m.put( "query_id", String.valueOf(idInforme) );
        m.put( "limit", String.valueOf( Constantes.LIMIT ) );
        m.put( "offset", String.valueOf(offset) );
        
		while( ( ltmp = im.getIssues(m).getResults() ).size() != 0 )
		{
			ltot.addAll(ltmp);
			
			offset += Constantes.LIMIT;
			
			m.put( "offset", String.valueOf(offset) );
		}
		
		return ltot;
	}
	
	/**
	 * Obtiene las entradas de tiempo de una petición
	 * 
	 * @param is	Petición
	 * @param mes	true significa que las entradas de tiempo no son todas, sólo las del mes anterior
	 * @return		Lista de entradas de tiempo
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 */
	protected List<TimeEntry> getEntradasTiempo(Issue is, boolean mes) 
	throws URISyntaxException, IOException, RedmineException
	{
		RedmineManager rm		= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS );
		TimeEntryManager te		= rm.getTimeEntryManager();
        Map<String, String>	m	= new HashMap<>();
		List<TimeEntry> ltmp 	= new LinkedList<>();
		List<TimeEntry> ltot	= new LinkedList<>();
		int offset				= 0;
		
		m.put( "key", Constantes.API_KEY_SILVA_CSBS );
        m.put( "issue_id", is.getId().toString() );
        m.put( "limit", String.valueOf( Constantes.LIMIT ) );
        m.put( "offset", String.valueOf(offset) );
        
        if(mes)
        {
        	// Las entradas de tiempo tiene que ir dentro del mes ...
        	this.gestionarFiltroMes(m);
        }
        else
        {
        	// No puede haber entradas de tiempo después del cierre del mes ...
        	m.put( "spent_on", "<=" + this.hasta );					
        }
        
//        log.info(m); 
		
		while( (ltmp = te.getTimeEntries(m).getResults() ).size() != 0 )
		{
			
//			ltmp.forEach( t -> log.info( t.getCreatedOn() ) );
			
			ltmp = ltmp.stream().filter( e -> e.getIssueId().equals( is.getId() ) ).collect( Collectors.toList() );
			
			ltot.addAll(ltmp);
			
			offset += Constantes.LIMIT;
			
			m.put( "offset", String.valueOf(offset) );
		}
		
		return ltot;
	} 
	
	/**
	 * Cuándo sacamos los números del mes anterior para el cierre ... LM
	 * Cuándo sacamos los números del mes actual para el seguimientro ... M
	 * @param m	Mapa con los parámetros de la consulta de tiempo 
	 */
	protected void gestionarFiltroMes(Map<String, String> m)
	{
//		m.put( "spent_on", SPENT_ON.LM.toString().toLowerCase() );
		
		m.put( "from", this.desde );
		m.put( "to", this.hasta );
	}
	
	/**
	 * Coge las entradas de tiempo y retorna un mapa<nombre usuario, horas>.
	 * 
	 * @param lt	Entradas de tiempo
	 * @return		Mapa<ID usuario, horas imputadas> 
	 * @throws ParseException 
	 */
	protected Map<Integer, Double> resumenTiempo(List<TimeEntry> lt) 
	throws ParseException 
	{
		Map<Integer, Double> m 	= new HashMap<>();
		
		lt.forEach
		( 
			t -> 
			{
				switch( t.getUserId() )
				{
					case 2:		// Anónimo
						
						t.setUserId( this.anonimos.buscar(t) );
						
						break;
						
					case 578:	// Alejandro 
						
						if( t.getSpentOn().getTime() <= Constantes.F_08_09_25  )
						{
							t.setHours(0F);
						}
						
						break;
						
					case 498:	// Verónica
						
						if( t.getSpentOn().getTime() >= Constantes.F_01_06_25 && t.getSpentOn().getTime() <= Constantes.F_08_09_25 )
						{
							t.setHours(0F);
						}
						
						break;
				}
				
				m.put
				( 
					t.getUserId(), 
					m.getOrDefault( t.getUserId(), 0D ) + t.getHours() 
				);
			}
		);
        
        return m;
	}
	
	/**
	 * Método que obtiene el valor de un campo custom.
	 * Si tiene un valor vacío o nulo, retorna cadena vacía.
	 * 
	 * @param is		Petición
	 * @param idCampo	ID del campo custom a consultar
	 * @return			Valor del campo
	 */
	protected String procesaCampo(Issue is, int idCampo)
	{
		CustomField cf	= is.getCustomFieldById(idCampo);
		String valor	= "";
		
		if( cf != null && cf.getValue() != null && !cf.getValue().isEmpty() )
		{
			valor = cf.getValue();
		}
		
		return valor;
	}
	
	/**
	 * @see procesaCampo(Issue is, int idCampo)
	 */
	protected double procesaCampoDouble(Issue is, int idCampo)
	{
		String valor1	= procesaCampo( is, idCampo );
		double valor2	= !valor1.isEmpty() ? Double.valueOf(valor1) : 0D;
		
		return valor2;
	}
	
	/**
	 * Método que genera el CSV resumen de un informe de tareas.
	 * 
	 * @param csv			Nombre del CSV de salida
	 * @param idInforme		ID del informe de tareas
	 * @param mes			Indica si las entradas de tiempo son todas o sólo las del mes anterior
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 * @throws ParseException 
	 */
	protected void generarInforme(String csv, int idInforme, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
        PrintWriter writer		= Miscelanea.crearFicheroSalida(csv);
		List<Issue> peticiones 	= this.getPeticionesInforme(idInforme);
		int cont				= 0;
		
		writer.println( Constantes.CABECERA_CSV1 );
		
		log.info( peticiones.size() + " peticiones" );
		
		for(Issue i : peticiones)
		{
			log.info( ++cont + ". " + i.getId() );
			
			this.escribirFichero( writer, this.getInformeLinea( i, mes ) );
		}
		
		writer.close();
	}
	
	/**
	 * Método que escribe una línea en el fichero CSV de salida.
	 * Tiene que ser en un orden concreto ...
	 * 
	 * @param writer	Fichero
	 * @param res		Mapa con los datos a escribir
	 */
	protected void escribirFichero(PrintWriter writer, Map<String, Object> res)
	{
		writer.print( res.get("lin") + Constantes.SEPARADOR );
		writer.print( res.get("prj") + Constantes.SEPARADOR );
		writer.print( res.get("id") + Constantes.SEPARADOR );
		writer.print( res.get("sbj") + Constantes.SEPARADOR );
		writer.print( res.get("trk") + Constantes.SEPARADOR );
		writer.print( res.get("rJP") + Constantes.SEPARADOR );
		writer.print( res.get("rCO") + Constantes.SEPARADOR );
		writer.print( res.get("rAN") + Constantes.SEPARADOR );
		writer.print( res.get("rPR") + Constantes.SEPARADOR );
		writer.print( res.get("rTot") + Constantes.SEPARADOR );
		writer.print( res.get("eJP") + Constantes.SEPARADOR );
		writer.print( res.get("eCO") + Constantes.SEPARADOR );
		writer.print( res.get("eAN") + Constantes.SEPARADOR );
		writer.print( res.get("ePR") + Constantes.SEPARADOR );
		writer.print( res.get("eTot") + Constantes.SEPARADOR );
		writer.print( res.get("mes") + Constantes.SEPARADOR );
		writer.print( res.get("ini") + Constantes.SEPARADOR );
		writer.print( res.get("fin") + Constantes.SEPARADOR );
		writer.print( res.get("igt") + Constantes.SEPARADOR );
		writer.print( res.get("dir") + Constantes.SEPARADOR );

//		writer.print( res.get("est") + Constantes.SEPARADOR );
//		writer.print( res.get("pd") + Constantes.SEPARADOR );
//		writer.print( res.get("chk") + Constantes.SEPARADOR );
		
		writer.print( res.get("obv") );
		writer.println();
		
		writer.flush();
	}
	
	/**
	 * Método que calcula el campo de observaciones.
	 * Todos son estándar excepto 2.
	 * 
	 * Desarrollo
	 * Es una OT
	 * Tiene horas imputadas 
	 * Ninguna de las horas son de Isotrol
	 * 
	 * Gestión
	 * #128902 tiene un texto especiañ
	 * 
	 * @param linea	Tipo de la petición
	 * @param id	ID de la petición
	 * @param x1	Horas totales de la petición en positivo
	 * @param rTot	Horas totales de la peticón por Isotrol
	 * @return		Texto final
	 */
	protected String procesaObservaciones(TIPO linea, int id, double x1, double rTot)
	{
		String resp = null;
				
		switch(linea)
		{
			case DESARROLLO:	
				resp	= Constantes.TXT_DESARROLLO; 
				resp	= x1 != 0 && rTot == 0 ? Constantes.TXT_NTT : Constantes.TXT_DESARROLLO;	
				break;
			case GESTION: 		
				resp	= Constantes.TXT_GESTION1;
				resp 	= ( id == Constantes.HSU_PETICION_CSBS ) ? Constantes.TXT_GESTION2 : resp;
				break;
			case OPERACION: 	
				resp 	= Constantes.TXT_OPERACION; 
				break;
			default: 			
				resp 	= Constantes.TXT_OTRO; 
				break;
		};
						
		return resp;
	}
	
	/**
	 * Multiplicar las horas de cada perfil por su tarifa
	 * 
	 * @param hJP	Horas JP
	 * @param hCO	Horas CO
	 * @param hAN	Horas AN
	 * @param hPR	Horas PR
	 * @return		Importe final horas x tarifa
	 */
	protected double getImporte1(double hJP, double hCO, double hAN, double hPR)
	{
		return 	hJP * Tarifa.getPrecioBase( ROLES.JP ) +
				hCO * Tarifa.getPrecioBase( ROLES.CO ) +
				hAN * Tarifa.getPrecioBase( ROLES.AN ) +
				hPR * Tarifa.getPrecioBase( ROLES.PR );
	}
	
	/**
	 * Método que retorna los recursos a tener en consideración:
	 * Para el informe mensual, sólo Isotrol.
	 * Para el informe de los lunes, toda la SF
	 * 
	 * @return	Equipo de trabajo
	 */
	protected Recursos getRecursosProy()
	{
		return this.recursos;
	}
}