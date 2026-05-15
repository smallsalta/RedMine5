package com.isotrol.redmine5.informes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.isotrol.redmine5.comun.Constantes.ROLES;
import com.isotrol.redmine5.comun.Miscelanea;
import com.isotrol.redmine5.recursos.Recursos;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.extern.log4j.Log4j;

@Log4j
@Component("informeReparto")
public class InformeReparto 
extends InformeLunes
{
	private Recursos recursos;
	
	@Autowired
	@Qualifier("recursosIsotrol")
	protected Recursos recursosIsotrol;
	
	@Autowired
	@Qualifier("recursosNTT")
	protected Recursos recursosNTT;
	
	private Map<Integer, Double> importes;
	
	public InformeReparto() 
	throws NumberFormatException, ParseException, IOException
	{
		this.importes = this.cargarImportes();
	}
			
	public void getInformeCerradas() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info("Cerradas ...");
		
		this.generarInforme( Constantes.INF_REPARTO1, Constantes.INF_CERRADAS_UMES, false );
	}
	
	public void getInformeGestion() 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		log.info("Gestión ...");
		
		this.generarInforme( Constantes.INF_REPARTO2, Constantes.INF_GESTION, true );
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	protected void generarInforme(String csv, int idInforme, boolean mes) 
	throws URISyntaxException, IOException, RedmineException, ParseException
	{
		PrintWriter writer		= Miscelanea.crearFicheroSalida(csv);
		List<Issue> peticiones 	= this.getPeticionesInforme(idInforme);
		int cont				= 0;
		
		log.info( "Importes ... " + importes.size() );
		
		writer.println( Constantes.CABECERA_CSV5 );
		
		log.info( peticiones.size() + " peticiones" );
		
		for( Issue i : peticiones )
		{
			log.info( ++cont + ". #" + i.getId() );
			
			this.escribirFichero( writer, this.getInformeLinea( i, mes ) );
		}
		
		writer.close();
	}
	
	private Map<Integer, Double> cargarImportes() 
	throws ParseException, NumberFormatException, IOException
	{
		String relativePath 	= Constantes.INF_CARPETA + Constantes.INF_IMPORTES;
        FileInputStream fis		= new FileInputStream( relativePath );
        BufferedReader br 		= new BufferedReader( new InputStreamReader(fis) );
        
        NumberFormat nf			= new DecimalFormat("#0.00");

        String linea			= null;
        boolean primeraLinea 	= true;
        Map<Integer, Double> m	= new HashMap<>();
        
        while( ( linea = br.readLine() ) != null ) 
        {
            if( primeraLinea ) 
            {
                primeraLinea = false;
                continue;
            }
            
            String[] columnas	= linea.split( Constantes.SEPARADOR );
            
            m.put
            ( 
            	Integer.valueOf( columnas[0] ), 
            	nf.parse( columnas[1] ).doubleValue()
            );
        }
        
        br.close();
        
        return m;
	}
	
	public Map<String, Object> getInformeLinea(Issue is, boolean mes) 
	throws RedmineException, URISyntaxException, IOException, ParseException
	{
		NumberFormat fmt1		= new DecimalFormat("#0.00");
		
		Map<String, Object> res	= new HashMap<>();
		var res1 				= this.getTiempoPeticion(is, mes);
		var res2 				= this.getTiempoPerfiles(res1);
		
		int id					= is.getId();
		double hTot				= res1.values().stream().mapToDouble(Double::doubleValue).sum();
		Double imp				= this.importes.get(id);
		
		res.put( "id", id );
		
		if( hTot != 0 && imp != null && imp != 0 )
		{	
			double rJPi			= res2.get("Isotrol").get( ROLES.JP );
			double rCOi			= res2.get("Isotrol").get( ROLES.CO );
			double rANi			= res2.get("Isotrol").get( ROLES.AN );
			double rPRi			= res2.get("Isotrol").get( ROLES.PR );
			double rTot			= rJPi + rCOi + rANi + rPRi;
			double impi			= this.getImporte1( rJPi, rCOi, rANi, rPRi );
			
			double rJPn			= res2.get("NTT").get( ROLES.JP );
			double rCOn			= res2.get("NTT").get( ROLES.CO );
			double rANn			= res2.get("NTT").get( ROLES.AN );
			double rPRn			= res2.get("NTT").get( ROLES.PR );
			double impn			= this.getImporte1( rJPn, rCOn, rANn, rPRn );
			
			double impIso		= ( impi / ( impi + impn ) ) * imp.doubleValue();
			
			res.put( "hTot", fmt1.format(hTot) );
			res.put( "hIso", fmt1.format(rTot) );
			res.put( "hNtt", fmt1.format( hTot - rTot ) );
			res.put( "impi", fmt1.format(impi) );
			res.put( "impn", fmt1.format(impn) );
			res.put( "imp", fmt1.format(imp) );
			res.put( "impIso", fmt1.format(impIso) );
			res.put( "impNtt", fmt1.format( imp - impIso ) );
		}
		
		return res;
	}
	
	protected void escribirFichero(PrintWriter writer, Map<String, Object> res)
	{
		writer.print( res.get("id") + Constantes.SEPARADOR );
		writer.print( res.get("hTot") + Constantes.SEPARADOR );
		writer.print( res.get("hIso") + Constantes.SEPARADOR );
		writer.print( res.get("hNtt") + Constantes.SEPARADOR );
		writer.print( res.get("impi") + Constantes.SEPARADOR );
		writer.print( res.get("impn") + Constantes.SEPARADOR );
		writer.print( res.get("imp") + Constantes.SEPARADOR );
		writer.print( res.get("impIso") + Constantes.SEPARADOR );
		writer.print( res.get("impNtt") );
		writer.println();
		
		writer.flush();
	}

	/**
	 * Variante que no escribe si alguno de los valores del mapa es null.
	 */
	protected void escribirFichero2(PrintWriter writer, Map<String, Object> res)
	{
		if( res == null || res.values().stream().anyMatch(Objects::isNull) )
		{
			log.info("escribirFichero2: salto por valor nulo, id=" + (res == null ? "null" : res.get("id")) );
		}
		else
		{
			this.escribirFichero(writer, res);
		}
	}
	
	protected Recursos getRecursosProy()
	{
		return this.recursos;
	}
	
	protected void gestionarFiltroMes(Map<String, String> m)
	{
		// from=2025-08-01&to=2025-08-31
		
		m.put( "from", this.getDesde() );
		m.put( "to", this.getHasta() );
	}
	
	/**
	 * Calcula las horas por roles
	 * 
	 * @param res1	Horas totales
	 * @return		Horas de cada empresa por roles
	 *  
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RedmineException
	 */
	protected Map<String, Map<ROLES, Double>> getTiempoPerfiles(Map<Integer, Double> res1) 
	throws URISyntaxException, IOException, RedmineException
	{
		this.recursos = this.recursosIsotrol;
		
		Map<ROLES, Double> res2 = this.getTiempoIsotrol(res1);
		
		this.recursos = this.recursosNTT;
		
		Map<ROLES, Double> res3 = this.getTiempoIsotrol(res1);
		
		return Map.of( "Isotrol", res2, "NTT", res3 );
	}
}