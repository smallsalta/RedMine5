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
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.ROLES;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.recursos.Recursos;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.extern.log4j.Log4j;

@Log4j
@Component("informeLunesTop")
public class InformeLunesTop 
extends InformeLunes
{
	public InformeLunesTop() 
	throws NumberFormatException, ParseException, IOException 
	{
		super();
	}

	public void getInformeTop1(int ... peticiones) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Top importes ...");
		this.generarInforme( Constantes.INF_TOP1, peticiones);
	}
	
	public void getInformeTop2(int ... peticiones) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Top fechas ...");
		this.generarInforme( Constantes.INF_TOP2, peticiones);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	protected void generarInforme(String csv, int ... peticiones) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		PrintWriter writer		= Miscelanea.crearFicheroSalida(csv);
		int cont				= 0;
		
		writer.println( Constantes.CABECERA_CSV4 );
		
		log.info( peticiones.length + " peticiones" );
		
		for( int i : peticiones )
		{
			log.info( ++cont + ". #" + i );
			
			this.escribirFichero( writer, this.getInformeLinea( i, false ) );
		}
		
		writer.close();
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
		double rJP				= res2.get( ROLES.JP );
		double rCO				= res2.get( ROLES.CO );
		double rAN				= res2.get( ROLES.AN );
		double rPR				= res2.get( ROLES.PR );
		double hinc				= rJP + rCO + rAN + rPR;
		double inc				= this.getImporte1( rJP, rCO, rAN, rPR );
		double val1				= this.getImporte1( eJP, eCO, eAN, ePR );
		double val2				= val1 * Constantes.EXTRA_OT;
		float est				= is.getEstimatedHours() == null ? 0F : is.getEstimatedHours();
		
		res.put( "id", id );
		res.put( "pro", is.getProjectName() );
		res.put( "trk", tipo );
		res.put( "est", is.getStatusName() );
		res.put( "sbj", is.getSubject().replaceAll( ";", " " ).replaceAll( "\"", " " ) );
		res.put( "por", is.getDoneRatio() );
		res.put( "inc", fmt1.format(inc) );
		res.put( "mar1", fmt1.format( val2 - inc ) );
		res.put( "mar2", fmt1.format( est - hinc ) );
		res.put( "cre", fmt2.format( is.getCreatedOn() ) );
		
		return res;
	}
	
	protected void escribirFichero(PrintWriter writer, Map<String, Object> res)
	{
		writer.print( res.get("id") + Constantes.SEPARADOR );
		writer.print( res.get("pro") + Constantes.SEPARADOR );
		writer.print( res.get("trk") + Constantes.SEPARADOR );
		writer.print( res.get("est") + Constantes.SEPARADOR );
		writer.print( res.get("sbj") + Constantes.SEPARADOR );
		writer.print( res.get("cre") + Constantes.SEPARADOR );
		writer.print( res.get("por") + Constantes.SEPARADOR );
		writer.print( res.get("inc") + Constantes.SEPARADOR );
		writer.print( res.get("mar1") + Constantes.SEPARADOR );
		writer.print( res.get("mar2") );
		writer.println();
		
		writer.flush();
	}
	
	protected Recursos getRecursosProy()
	{
		return this.recursos;
	}
}