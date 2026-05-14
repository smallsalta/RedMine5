package com.isotrol.redmine5.recursos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.taskadapter.redmineapi.bean.TimeEntry;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
@Component("anonimos")
public class Anonimos 
{
	private List<Map<String, Object>> datos;
	
	public Anonimos() 
	throws NumberFormatException, ParseException, IOException
	{
		this.datos = new LinkedList<>();
		
		this.cargar();
	}
	
	/**
	 * Método que carga los anónimos del CSV
	 * En Redmine están con ID=2
	 * 
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void cargar() 
	throws ParseException, NumberFormatException, IOException
	{
		String relativePath 	= Constantes.INF_CARPETA + Constantes.INF_ANONIMOS;
        FileInputStream fis		= new FileInputStream( relativePath );
        BufferedReader br 		= new BufferedReader( new InputStreamReader(fis) );

        String linea			= null;
        boolean primeraLinea 	= true;
        SimpleDateFormat sdf 	= new SimpleDateFormat("dd/MM/yyyy");
        
        while( ( linea = br.readLine() ) != null ) 
        {
            if( primeraLinea ) 
            {
                primeraLinea = false;
                continue;
            }
            
            String[] columnas	= linea.split( Constantes.SEPARADOR );
            
            this.datos.add
            (
            	Map.of
            	( 
            		"IDP", Integer.parseInt( columnas[2] ), 
            		"FECHA", sdf.parse( columnas[0] ), 
            		"HORAS", Float.parseFloat( columnas[3].replace(",", ".") ), 
            		"IDU", Integer.parseInt( columnas[4] ) 
            	)
            );
        }
        
        br.close();
	}
	
	/**
	 * Si encontramos una entrada de tiempo con ID=2
	 * Buscamos en _anonimos.csv esa entrada de tiempo para determinar el ID de usuario real
	 * 
	 * @param t		Entrada de tiempo con la información de la picada en Redmine
	 * @return		-1 ó -2
	 */
	public Integer buscar(TimeEntry t)
	{
		Integer id		= null;
		int poeticion	= t.getIssueId();
		Date fecha		= t.getSpentOn();
		float horas 	= t.getHours();
		
		for( Map<String, Object> m : this.datos )
		{
			Integer midp	= (Integer) m.get("IDP"); 
			Date mfecha		= (Date) m.get("FECHA");
			Float mhoras	= (Float) m.get("HORAS");
			
			if( midp.equals(poeticion) && mfecha.getTime() == fecha.getTime() && mhoras.equals(horas) )
			{
				id = (Integer) m.get("IDU");
				
				break;
			}
		}
		
		return id;
	}
	
}
