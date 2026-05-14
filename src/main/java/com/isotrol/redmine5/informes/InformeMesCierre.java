package com.isotrol.redmine5.informes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.recursos.Recursos;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.extern.log4j.Log4j;

@Log4j
@Component("informeCierre")
public class InformeMesCierre 
extends InformeMes
{
	public void getInformeCerradas() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas ... ");
		
		this.generarInforme( Constantes.INF_CER, Constantes.INF_CERRADAS_UMES, false );
	}
	
	public void getInformeGestion() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Gestión ... ");
		
		this.generarInforme( Constantes.INF_GES, Constantes.INF_GESTION, true );
	}
	
	public void getInformeAbiertas() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Abiertas ... ");
		
		this.generarInforme( Constantes.INF_ABI, Constantes.INF_ABIERTAS, false );
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	protected void generarInforme(String csv, int idInforme, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
        PrintWriter writer		= Miscelanea.crearFicheroSalida(csv);
		List<Issue> peticiones 	= this.getPeticionesInforme(idInforme);
		int cont				= 0;
		
		writer.println( Constantes.CABECERA_CSV6 );
		
		log.info( peticiones.size() + " peticiones" );
		
		for(Issue i : peticiones)
		{
			log.info( ++cont + ". " + i.getId() );
			
			this.escribirFichero( writer, this.getInformeLinea( i, mes ) );
		}
		
		writer.close();
	}
	
	public Map<String, Object> getInformeLinea(Issue is, boolean mes) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		Map<String, Object> res = super.getInformeLinea(is, mes);
		NumberFormat fmt1		= new DecimalFormat("#0.00");
		
		double eJP 				= fmt1.parse( res.get("eJP").toString() ).doubleValue();
		double eCO 				= fmt1.parse( res.get("eCO").toString() ).doubleValue();
		double eAN 				= fmt1.parse( res.get("eAN").toString() ).doubleValue();
		double ePR 				= fmt1.parse( res.get("ePR").toString() ).doubleValue();
		double eImp				= this.getImporte1( eJP, eCO, eAN, ePR );
		
		double rJP				= fmt1.parse( res.get("rJP").toString() ).doubleValue();
		double rCO				= fmt1.parse( res.get("rCO").toString() ).doubleValue();
		double rAN				= fmt1.parse( res.get("rAN").toString() ).doubleValue();
		double rPR				= fmt1.parse( res.get("rPR").toString() ).doubleValue();
		double rImp				= this.getImporte1( rJP, rCO, rAN, rPR );
		
		double dImp				= this.getImporteDefecto( res.get("trk").toString(), rImp );

		res.put( "rImp", fmt1.format(rImp) );
		res.put( "eImp", fmt1.format(eImp) );
		res.put( "dImp", fmt1.format(dImp) );
		
		return res;
	}
	
	protected void escribirFichero(PrintWriter writer, Map<String, Object> res)
	{
		writer.print( res.get("lin") + Constantes.SEPARADOR );
		writer.print( res.get("prj") + Constantes.SEPARADOR );
		writer.print( res.get("id") + Constantes.SEPARADOR );
		writer.print( res.get("igt") + Constantes.SEPARADOR );
		writer.print( res.get("sbj") + Constantes.SEPARADOR );
		writer.print( res.get("trk") + Constantes.SEPARADOR );
		writer.print( res.get("ini") + Constantes.SEPARADOR );
		writer.print( res.get("fin") + Constantes.SEPARADOR );
		writer.print( res.get("eJP") + Constantes.SEPARADOR );
		writer.print( res.get("eCO") + Constantes.SEPARADOR );
		writer.print( res.get("eAN") + Constantes.SEPARADOR );
		writer.print( res.get("ePR") + Constantes.SEPARADOR );
		writer.print( res.get("eTot") + Constantes.SEPARADOR );
		writer.print( res.get("eImp") + Constantes.SEPARADOR );
		writer.print( res.get("rJP") + Constantes.SEPARADOR );
		writer.print( res.get("rCO") + Constantes.SEPARADOR );
		writer.print( res.get("rAN") + Constantes.SEPARADOR );
		writer.print( res.get("rPR") + Constantes.SEPARADOR );
		writer.print( res.get("rTot") + Constantes.SEPARADOR );
		writer.print( res.get("rImp") + Constantes.SEPARADOR );
		writer.print( res.get("dImp") + Constantes.SEPARADOR );
		writer.print( "0" + Constantes.SEPARADOR );
		writer.print( "xxx" + Constantes.SEPARADOR );
		writer.print( res.get("dir") + Constantes.SEPARADOR );
		writer.print( res.get("obv") );
		writer.println();
		
		writer.flush();
	}
	
	protected double getImporteDefecto(String tipo, double inc)
	{
		double total = 0.0;
		
		if( tipo.equalsIgnoreCase( Constantes.DEF ) )
		{
			total = inc;
		}
		
		return total;
	}
	
	protected Recursos getRecursosProy()
	{
		return this.recursos;
	}
}