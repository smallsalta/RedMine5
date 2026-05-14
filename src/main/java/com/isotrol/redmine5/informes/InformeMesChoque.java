package com.isotrol.redmine5.informes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.ROLES;
import com.isotrol.redmine5.comun.Constantes.TARIFA;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.comun.Tarifa;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.extern.log4j.Log4j;

@Log4j
@Component("informeChoque")
public class InformeMesChoque 
extends InformeMesCierre 
{
	protected List<Issue> abiertas;
	protected List<Issue> cerradas;
	
	public InformeMesChoque() 
	throws NumberFormatException, ParseException, IOException
	{
		this.abiertas			= new LinkedList<Issue>();
		this.cerradas			= new LinkedList<Issue>();
		Set<Integer> peticiones	= Miscelanea.cargarChoques();
		
		RedmineManager rm		= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS );
		IssueManager im			= rm.getIssueManager();
		
		for( Integer i : peticiones )
		{
			try 
			{
				Issue is = im.getIssueById(i);
				
				switch( is.getStatusId() )
				{
					case Constantes.ESTADO_CERRADA: 
						this.cerradas.add(is);
						break;
						
					default: 
						this.abiertas.add(is);
						break;
				}				
			} 
			catch (RedmineException e) 
			{
				log.info( "Error al leer información de #" + i );
			}
		}
	}
	
	public void getInformeCerradasTarifaBase() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas con tarifas Línea Base ... ");
		
		this.generarInforme( Constantes.INF_CER_B, Constantes.INF_CERRADAS_UMES, false, TARIFA.BASE );
	}
	
	public void getInformeCerradasTarifaChoque() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Cerradas con tarifas Choque ... ");
		
		this.generarInforme( Constantes.INF_CER_C, Constantes.INF_CERRADAS_UMES, false, TARIFA.CHOQUE );
	}
			
	public void getInformeAbiertasTarifaBase() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Abiertas con tarifas Línea Base  ... ");
		
		this.generarInforme( Constantes.INF_ABI_B, Constantes.INF_ABIERTAS, false, TARIFA.BASE );
	}
	
	public void getInformeAbiertasTarifaChoque() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info( "Abiertas con tarifas Choque ... ");
		
		this.generarInforme( Constantes.INF_ABI_C, Constantes.INF_ABIERTAS, false, TARIFA.CHOQUE );
	}
	
	protected void generarInforme(String csv, int idInforme, boolean mes, TARIFA t) 
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
			
			this.escribirFichero( writer, this.getInformeLinea( i, mes, t ) );
		}
		
		writer.close();
	}
	
	/**
	 * Las peticiones no salen del Redmine sino de un XLS interno
	 */
	protected List<Issue> getPeticionesInforme(int idInforme) 
	throws URISyntaxException, IOException, RedmineException
	{
		List<Issue> l = null;
		
		switch(idInforme)
		{
			case Constantes.INF_ABIERTAS: 
				l = this.abiertas;
				break;
			case Constantes.INF_CERRADAS_UMES:
				l = this.cerradas;
				break;
			default: 
				break;
		}
		
		return l;
	}
	
	public Map<String, Object> getInformeLinea(Issue is, boolean mes, TARIFA t) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		Map<String, Object> res = super.getInformeLinea(is, mes);
		NumberFormat fmt1		= new DecimalFormat("#0.00");
		
		double eJP 				= fmt1.parse( res.get("eJP").toString() ).doubleValue();
		double eCO 				= fmt1.parse( res.get("eCO").toString() ).doubleValue();
		double eAN 				= fmt1.parse( res.get("eAN").toString() ).doubleValue();
		double ePR 				= fmt1.parse( res.get("ePR").toString() ).doubleValue();
		double eImp				= this.getImporte( eJP, eCO, eAN, ePR, t );
		
		double rJP				= fmt1.parse( res.get("rJP").toString() ).doubleValue();
		double rCO				= fmt1.parse( res.get("rCO").toString() ).doubleValue();
		double rAN				= fmt1.parse( res.get("rAN").toString() ).doubleValue();
		double rPR				= fmt1.parse( res.get("rPR").toString() ).doubleValue();
		double rImp				= this.getImporte( rJP, rCO, rAN, rPR, t );
		
		double dImp				= this.getImporteDefecto( res.get("trk").toString(), rImp );

		res.put( "rImp", fmt1.format(rImp) );
		res.put( "eImp", fmt1.format(eImp) );
		res.put( "dImp", fmt1.format(dImp) );
		
		return res;
	}
	
	/**
	 * Importe de la petición según la tarifa aplicada
	 * @param hJP	Horas JP
	 * @param hCO	Horas CO
	 * @param hAN	Horas AN
	 * @param hPR	Horas PR
	 * @param t		Tarifa a aplicar {Base, Choque}
	 * @return		Horas x Tarifa
	 */
	protected double getImporte(double hJP, double hCO, double hAN, double hPR, TARIFA t)
	{
		/*
		double importe = 0D;
		
		switch(t)
		{
			case BASE: 
				importe = this.getImporte1( hJP, hCO, hAN, hPR );
				break;
			case CHOQUE:
				importe = this.getImporte2( hJP, hCO, hAN, hPR );
				break;
		}
		
		return importe;
		*/
		
		return switch(t)
		{
			case BASE -> this.getImporte1( hJP, hCO, hAN, hPR );
			case CHOQUE -> this.getImporte2( hJP, hCO, hAN, hPR );
			default -> 0.0;
		};
	}
	
	/**
	 * Copia de getImporte1 pero usando las tarifas del Plan de Choque
	 * 
	 * @param hJP	Horas JP
	 * @param hCO	Horas CO
	 * @param hAN	Horas AN
	 * @param hPR	Horas PR
	 * @return		Horas x Tarifa
	 */
	protected double getImporte2(double hJP, double hCO, double hAN, double hPR)
	{
		return 	hJP * Tarifa.getPrecioChoque( ROLES.JP ) +
				hCO * Tarifa.getPrecioChoque( ROLES.CO ) +
				hAN * Tarifa.getPrecioChoque( ROLES.AN ) +
				hPR * Tarifa.getPrecioChoque( ROLES.PR );
	}
}
