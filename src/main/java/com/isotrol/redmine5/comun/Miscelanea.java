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
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

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
}