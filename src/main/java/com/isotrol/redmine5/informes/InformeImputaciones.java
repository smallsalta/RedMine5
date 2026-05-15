package com.isotrol.redmine5.informes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.SPENT_ON;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.recursos.Recursos;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.TimeEntry;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
@Component("informeImputaciones")
public class InformeImputaciones 
{
	@Autowired
	@Qualifier("recursosIsotrol")
	private Recursos recCsbs;
	
	@Autowired
	@Qualifier("recursosIup")
	private Recursos recIup;
	
	/**
	 * Recuperamos las entradas de tiempo semanales, de IUP y CSBS.
	 * Las unimos y las imprimimos en un CSV.
	 * #1: Datos IUP
	 * #2: Datos CSBS
	 * #3: Unión de los datos
	 * 
	 * @throws RedmineException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public void getDatosSemana() 
	throws RedmineException, URISyntaxException, IOException
	{
		log.info("Imputaciones semanales IUP vs CSBS ...");
		
		boolean	isotrol;
		Map<String, Double> t1, t2;
		Map<String, Double[]> t3;
		
		// #1
		isotrol = true;
		t1 		= this.getTiempo( Constantes.RM_IUP_URL, Constantes.API_KEY_SILVA_IUP, isotrol, SPENT_ON.W );
		
		this.imprimir(t1);
		
		// #2
    	isotrol = false;
		t2		= this.getTiempo( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS, isotrol, SPENT_ON.W );
		
		this.imprimir(t2);
		
		// #3
		t3 = new HashMap<String, Double[]>();
		
		t1.entrySet().forEach
		( 
			e -> t3.put( e.getKey() , new Double[] { e.getValue(), 0D } ) 
		);
		
		t2.entrySet().forEach
		( 
			e -> 
			{ 
				if( !t3.containsKey( e.getKey() ) )
				{
					t3.put( e.getKey() , new Double[] { 0D, e.getValue() } );
				}
				else
				{
					t3.get( e.getKey() )[1] = e.getValue();
				}
			} 
		);
		
    	this.generarInforme(t3);
	}
	
	/**
	 * Datos mensuales del Redmine de JdA
	 * 
	 * @throws RedmineException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public void getDatosCsbsMes() 
	throws RedmineException, URISyntaxException, IOException
	{
		log.info("Imputaciones mensuales CSBS ...");
		
		boolean	isotrol			= false;
		Map<String, Double> t3	= this.getTiempo( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS, isotrol, SPENT_ON.LM );
		
		this.imprimir(t3);
	}

	/**
	 * Calcula las horas imputadas del mes para los trabajadores de Isotrol.
	 *
	 * @return total de horas imputadas por Isotrol en el periodo (mes)
	 * @throws RedmineException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public double getHorasIsotrolMes()
	throws RedmineException, URISyntaxException, IOException
	{
		log.info("Calculando horas mensuales Isotrol ...");
		
		boolean isotrol = true;
		Map<String, Double> datos = this.getTiempo( Constantes.RM_IUP_URL, Constantes.API_KEY_SILVA_IUP, isotrol, SPENT_ON.LM );
		
		double total = datos.values().stream().mapToDouble( Double::doubleValue ).sum();
		log.info( total + " horas (Isotrol)" );
		
		return total;
	}
	
	/**
	 * Método que obtiene las entradas de tiempo semanales de los 2 Redmine.
	 * 
	 * #1 ...
	 * En el Redmine de Isotrol hay muchos proyectos, sólo nos interesa HSU Valencia
	 * #2 ...
	 * En el Redmine de JdA hay que filtrar las imputaciones, decartando las de NTT.
	 * #3
	 * Obtener los nombres
	 * 
	 * @param url		URL del Redmine
	 * @param apikey	API KEY de acceso
	 * @param isotrol	true si es el Redmine de Isotrol
	 * 
	 * @throws RedmineException
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	private Map<String, Double> getTiempo(String url, String apikey, boolean isotrol, SPENT_ON periodo) 
	throws RedmineException, URISyntaxException, IOException
	{
		RedmineManager rm		= RedmineManagerFactory.createWithApiKey( url, apikey );
		Map<String, String>	m	= new HashMap<>();
		Recursos rec 			= isotrol ? this.recIup : this.recCsbs;
		
		m.put( "key", apikey );
        m.put( "limit", String.valueOf( Constantes.LIMIT ) );
    	m.put( "spent_on", periodo.toString().toLowerCase() );

    	// #1
    	if(isotrol)
    	{
    		m.put( "project_id", String.valueOf( Constantes.HSU_ID_IUP ) );
    	}
    	
    	List<TimeEntry> lte				= this.getEntradasTiempo( rm, m );
    	Map<Integer, Double> t1			= this.resumenTiempo(lte);
    	final Map<Integer, Double> t2	= new HashMap<>();
    	
    	log.info(url);
    	log.info( lte.size() + " entradas" );
    	
    	// #2
    	t1.entrySet().stream().filter( t -> rec.getNombre().containsKey( t.getKey() )  ).forEach( t -> t2.put( t.getKey(), t.getValue() ) );
    	
    	// #3
    	final Map<String, Double> t3	= new HashMap<>();
    	
    	t2.entrySet().forEach( e -> t3.put( rec.getNombre().get( e.getKey() ), e.getValue()  ) );
    	
    	return t3;
 	}
	
	/**
	 * Coge las entradas de tiempo y retorna un mapa<nombre usuario, horas>.
	 * 
	 * @param lt	Entradas de tiempo
	 * @return		Mapa<ID usuario, horas imputadas> 
	 */
	private Map<Integer, Double> resumenTiempo(List<TimeEntry> lt) 
	{
		Map<Integer, Double> m = new HashMap<>();
		
		lt.forEach
		( 
			t -> 
			{
				double tmp = m.getOrDefault( t.getUserId(), 0D );
				m.put( t.getUserId(), tmp + t.getHours() );
			}
		);
        
        return m;
	}
	
	/**
	 * Imprime los resultados
	 * 
	 * @param t3	Resumen de las entradas de tiempo
	 */
	private void imprimir(Map<String, Double> t3)
	{
    	log.info( t3.values().stream().mapToDouble( Double::doubleValue ).sum() + " horas" );
    	
    	t3.entrySet().forEach( e -> log.info(e) );
	}
	
	/**
	 * Escribimos las entradas de tiempo en un CSV
	 * 
	 * @param t3		Entradas de tiempo
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 */
	private void generarInforme(Map<String, Double[]> t3) 
	throws URISyntaxException, IOException, RedmineException
	{
        PrintWriter writer		= Miscelanea.crearFicheroSalida( Constantes.INF_PICADAS );
		NumberFormat nf			= new DecimalFormat("#0.00");
		
		writer.println( Constantes.CABECERA_CSV3 );
		
		t3.entrySet().forEach
		( 
			e ->
			{
				writer.print( e.getKey() + Constantes.SEPARADOR );
				writer.print( nf.format( e.getValue()[0] ) + Constantes.SEPARADOR );
				writer.print( nf.format( e.getValue()[1] ) );
				writer.println();
				
				writer.flush();
			}
		);
		
		writer.close();
	}

	/**
	 * Recorre las entradas de tiempo jugando con el limit y offset
	 * 
	 * @param rm	Redmine a usar
	 * @param m		Parámetros para el time_entry
	 * @return		Entradas de tiempo
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 */
	private List<TimeEntry> getEntradasTiempo(RedmineManager rm, Map<String, String> m) 
	throws URISyntaxException, IOException, RedmineException
	{
		TimeEntryManager te		= rm.getTimeEntryManager();
		List<TimeEntry> ltmp 	= new LinkedList<>();
		List<TimeEntry> ltot	= new LinkedList<>();
		int offset				= 0;
		
		while( (ltmp = te.getTimeEntries(m).getResults() ).size() != 0 )
		{
			ltot.addAll(ltmp);
			
			offset += Constantes.LIMIT;
			
			m.put( "offset", String.valueOf(offset) );
		}
		
		return ltot;
	} 
}