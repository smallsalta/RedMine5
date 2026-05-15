package com.isotrol.redmine5.informes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.ROLES;
import com.isotrol.redmine5.comun.Miscelanea;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
@Component("informeLunes")
public class InformeLunes 
extends InformeMes
{
	private Set<Integer> choques;
	
	public InformeLunes() 
	throws NumberFormatException, ParseException, IOException
	{
		this.choques	= Miscelanea.cargarChoques();
	}
	
	public void getInformeAbiertas1() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Vuelo > Todas las horas ...");
		
		this.generarInforme( Constantes.INF_ABI_LUN1,  Constantes.INF_ABIERTAS, false );
	}
	
	public void getInformeAbiertas2() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Vuelo > Horas del mes ...");
		
		this.generarInforme( Constantes.INF_ABI_LUN2,  Constantes.INF_ABIERTAS, true );
	}
	
	public void getInformeCerradas1() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas > Todas las horas ...");
		
		this.generarInforme( Constantes.INF_CER_LUN1,  Constantes.INF_CERRADAS_UMES, false );
	}
	
	public void getInformeCerradas2() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas > Horas del mes ...");
		
		this.generarInforme( Constantes.INF_CER_LUN2,  Constantes.INF_CERRADAS_UMES, true );
	}
	
	public void getInformeGestion() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Gestión > Horas del mes ...");
		
		this.generarInforme( Constantes.INF_GES_LUN2,  Constantes.INF_GESTION, true );
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	protected void generarInforme(String csv, int idInforme, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		PrintWriter writer1		= Miscelanea.crearFicheroSalida( "1_" + csv );
		PrintWriter writer2		= Miscelanea.crearFicheroSalida( "2_" + csv );
		PrintWriter writer		= null;
				
		List<Issue> peticiones 	= this.getPeticionesInforme(idInforme);
		int cont				= 0;
		
		writer1.println( Constantes.CABECERA_CSV2 );
		writer2.println( Constantes.CABECERA_CSV2 );
		
		log.info( peticiones.size() + " peticiones" );
		
		for( Issue i : peticiones )
		{
			log.info( ++cont + ". #" + i.getId() );
			
			writer = this.choques.contains( i.getId() ) ? writer2 : writer1;
			
			this.escribirFichero( writer, this.getInformeLinea( i, mes ) );
		}
		
		writer1.close();
		writer2.close();
	}
	
	public Map<String, Object> getInformeLinea(Issue is, boolean mes) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		Map<String, Object> res	= new HashMap<>();
		var res1 				= this.getTiempoPeticion(is, mes);
		var res2 				= this.getTiempoIsotrol(res1);
		
		NumberFormat fmt1		= new DecimalFormat("#0.00");
		DateFormat fmt2			= new SimpleDateFormat("dd/MM/yyyy");
		
		String tipo				= is.getTracker().getName();
		int id					= is.getId();
		double eJP 				= this.procesaCampoDouble( is, 47 );
		double eCO 				= this.procesaCampoDouble( is, 50 );
		double eAN 				= this.procesaCampoDouble( is, 44 );
		double ePR 				= this.procesaCampoDouble( is, 46 );
//		double eTot 			= eJP + eCO + eAN + ePR;
		double rJP				= res2.get( ROLES.JP );
		double rCO				= res2.get( ROLES.CO );
		double rAN				= res2.get( ROLES.AN );
		double rPR				= res2.get( ROLES.PR );
//		double rTot				= rJP + rCO + rAN + rPR;
		double inc				= this.getImporte1( rJP, rCO, rAN, rPR );
		double val1				= this.getImporte1( eJP, eCO, eAN, ePR );
		double val2				= val1 * Constantes.EXTRA_OT;
		double imp				= this.getImporte2( tipo, inc, val2 );
		
		Miscelanea.poblarMapaHoras(res, eJP, eCO, eAN, ePR, rJP, rCO, rAN, rPR);
		
		res.put( "id", id );
		res.put( "trk", tipo );
//		res.put( "eJP", fmt1.format(eJP) );
//		res.put( "eCO", fmt1.format(eCO) );
//		res.put( "eAN", fmt1.format(eAN) );
//		res.put( "ePR", fmt1.format(ePR) );
//		res.put( "eTot", fmt1.format(eTot) );
//		res.put( "rJP", fmt1.format(rJP) );
//		res.put( "rCO", fmt1.format(rCO) );
//		res.put( "rAN", fmt1.format(rAN) );
//		res.put( "rPR", fmt1.format(rPR) );
//		res.put( "rTot", fmt1.format(rTot) );
		res.put( "inc", fmt1.format(inc) );
		res.put( "val1", fmt1.format(val1) );
		res.put( "val2", fmt1.format(val2) );
		res.put( "mar", fmt1.format( val2 - inc ) );
		res.put( "imp", fmt1.format(imp) );
		res.put( "fec", fmt2.format( is.getCreatedOn() ) );
		
		return res;
	}
	
	protected void escribirFichero(PrintWriter writer, Map<String, Object> res)
	{
		writer.print( res.get("id") + Constantes.SEPARADOR );
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
		writer.print( res.get("inc") + Constantes.SEPARADOR );
		writer.print( res.get("val1") + Constantes.SEPARADOR );
		writer.print( res.get("val2") + Constantes.SEPARADOR );
		writer.print( res.get("mar") + Constantes.SEPARADOR );
		writer.print( res.get("imp") + Constantes.SEPARADOR );
		writer.print( res.get("fec") );
		writer.println();
		
		writer.flush();
	}

    
	
	/**
	 * Si es DEFECTO no se puede cobrar ... 0
	 * Si es EVOLUTIVO ANS ... Las horas incurridas pero no podemos exceder de las valoradas
	 * En otro caso ... Las incurridas
	 * 
	 * @param tipo	Cadena con el tipo de la petición
	 * @param inc	Coste de las horas incurridas
	 * @param val2	Coste de las horas valoradas + 5%
	 * @return		Coste final de la petición
	 */
	protected double getImporte2(String tipo, double inc, double val2)
	{
		double total = 0.0;
		
		if( tipo.equalsIgnoreCase( Constantes.DEF ) )
		{
			total = 0.0;
		}
		else if( tipo.equalsIgnoreCase( Constantes.EVO_ANS ) )
		{
			total = val2 == 0 ? inc : Math.min( inc, val2 );
		}
		else
		{
			total = inc;
		}
		
		return total;
	}
	
//	protected Recursos getRecursosProy()
//	{
//		return this.recursos;
//	}
//	
//	protected void gestionarFiltroMes(Map<String, String> m)
//	{
//		m.put( "spent_on", SPENT_ON.M.toString().toLowerCase() );
//		
//		m.put( "from", this.desde );
//		m.put( "to", this.hasta );
//	}
}