package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import com.isotrol.redmine5.informes.InformeReparto;

import lombok.extern.log4j.Log4j;

// @Component("prueba7")
@Log4j
public class HSUAndalucia7 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeReparto")
	private InformeReparto icf;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		/*
		 * RECUERDA ...
		 * 1. Informe Redmine cerradas > Poner bien la horquilla de tiempo
		 * 2. Informe Redmine gestión > Poner bien la horquilla de tiempo
		 * 3. Cambia las fechas de inicio y fin
		 * 4. Eliminar de los CSV los NULL, 0h y 0€
		 */
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P7 ...");
		
		log.info("Reparto ...");
		
		this.icf.setDesde("2026-03-01");
		this.icf.setHasta("2026-03-31");
		
		this.icf.getInformeCerradas();
		this.icf.getInformeGestion();
		
		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
}
