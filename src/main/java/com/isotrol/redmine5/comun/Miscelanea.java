package com.isotrol.redmine5.comun;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;

public class Miscelanea 
{
	public static PrintWriter crearFicheroSalida(String csv) 
	throws FileNotFoundException, UnsupportedEncodingException
	{
		String relativePath 	= Constantes.INF_CARPETA + csv;
		FileOutputStream fos 	= new FileOutputStream( relativePath );
        OutputStreamWriter osw 	= new OutputStreamWriter( fos, "UTF-8" );
        PrintWriter writer		= new PrintWriter( osw, true );
        
        return writer;
	}
	
	/**
	 * Carga los ID de las peticiones relacionadas con el Plan de Choque 
	 * 
	 * @return	Los IDs de las peticiones Redmine
	 * 
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static Set<Integer> cargarChoques() 
	throws ParseException, NumberFormatException, IOException
	{
		String relativePath 	= Constantes.INF_CARPETA + Constantes.INF_CHOQUES;
        FileInputStream fis		= new FileInputStream( relativePath );
        BufferedReader br 		= new BufferedReader( new InputStreamReader(fis) );
        
        String linea			= null;
        boolean primeraLinea 	= true;
        Set<Integer> m			= new HashSet<>();
        
        while( ( linea = br.readLine() ) != null ) 
        {
            if( primeraLinea ) 
            {
                primeraLinea = false;
                continue;
            }
            
            m.add( Integer.valueOf( linea.trim() ) );
        }
        
        br.close();
        
        return m;
	}
	
	/**
	 * Centraliza la creación del cliente de Redmine para evitar duplicar las credenciales
	 */
	public static RedmineManager getRedmineManager() 
	{
		return RedmineManagerFactory.createWithApiKey(Constantes.RM_URL_CSBS, Constantes.API_KEY_SILVA_CSBS);
	}

	/**
	 * Centraliza el cálculo de totales y el volcado de todas las horas formateadas en el mapa
	 */
	public static void poblarMapaHoras(Map<String, Object> res, double eJP, double eCO, double eAN, double ePR, double rJP, double rCO, double rAN, double rPR) 
	{
		
		NumberFormat fmt1 = new DecimalFormat("#0.00");
		
		res.put( "eJP", fmt1.format(eJP) );
		res.put( "eCO", fmt1.format(eCO) );
		res.put( "eAN", fmt1.format(eAN) );
		res.put( "ePR", fmt1.format(ePR) );
		res.put( "eTot", fmt1.format(eJP + eCO + eAN + ePR) );
		
		res.put( "rJP", fmt1.format(rJP) );
		res.put( "rCO", fmt1.format(rCO) );
		res.put(" rAN", fmt1.format(rAN) );
		res.put( "rPR", fmt1.format(rPR) );
		res.put( "rTot", fmt1.format(rJP + rCO + rAN + rPR) );
	}
}