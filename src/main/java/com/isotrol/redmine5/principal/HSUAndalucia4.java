package com.isotrol.redmine5.principal;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.TimeEntryFactory;

import lombok.extern.log4j.Log4j;

// @Component("prueba4")
@Log4j
public class HSUAndalucia4 
implements CommandLineRunner 
{
	private Double horas_imputar;
	private String fecha_imputar;
	private String comentario;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		/*
		 * #06	01/06/2026	30/06/2026	21	17	4	172,50
		 * #07	01/07/2026	31/07/2026	23	18	5	188,00
		 * #08	01/08/2026	31/08/2026	21	17	4	172,50
		 * #09	01/09/2026	30/09/2026	22	18	4	181,00
		 * #10	01/10/2026	31/10/2026	21	16	5	171,00
		 * #11	01/11/2026	30/11/2026	21	17	4	172,50
		 * #12	01/12/2026	31/12/2026	19	16	3	157,00
		 */
		
		log.info("P4 ...");
		
		log.info("Imputar en Redmine cliente ...");
		
		this.horas_imputar	= 0.5 * 164;
		this.fecha_imputar	= "01/05/2026";
		this.comentario 	= Constantes.COM_ROBER;
		
		this.crearEntrada( Constantes.API_KEY_SILVA_CSBS );
				
		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	/**
	 * Crea una entrada de tiempo dentro de Redmine
	 * 
	 * Constantes ... 
	 * La URL del Redmine, el ID del proyecto y el ID de la actividad
	 * 
	 * Medio constantes ...
	 * El mensaje de la imputación cambia por usuario.
	 * Es muy genérico.
	 * 
	 * Medio constantes ...
	 * Las horas a imputar 
	 * Roberto 25% del mes
	 * Silva 1h por reunión
	 * 
	 * Medio constantes ...
	 * Roberto final de mes.
	 * Silva el día de la reunión.
	 * 
	 * apikey		API KEY del usuario 
	 * 
	 * @throws ParseException
	 * @throws RedmineException 
	 */
	private void crearEntrada(String apikey) 
	throws ParseException, RedmineException
	{
		var rm 		= RedmineManagerFactory.createWithApiKey( Constantes.RM_URL_CSBS ,apikey );
		var entry	= TimeEntryFactory.create();
		var df		= new SimpleDateFormat("dd/MM/yyyy");
		
		entry.setProjectId( Constantes.HSU_ID_CSBS );
        entry.setIssueId( Constantes.HSU_PETICION_CSBS );
        entry.setActivityId( Constantes.ACTIVIDAD_CSBS );
        
        entry.setComment( this.comentario );
        entry.setHours( this.horas_imputar.floatValue() );
        entry.setCreatedOn( df.parse( this.fecha_imputar ) );
        
        log.info(entry);
        
        rm.getTimeEntryManager().createTimeEntry(entry);
	}
}
