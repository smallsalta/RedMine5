package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import com.isotrol.redmine5.informes.InformeLunes;

import lombok.extern.log4j.Log4j;

// @Component("prueba5")
@Log4j
public class HSUAndalucia5 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeLunes")
	private InformeLunes ie;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		// CONSIDERACIONES:
		// 1. Ajustar la fecha hasta en los informes Redmine
		// 2. Ajustar las peticiones de Choque 
		//    https://everisgroup.sharepoint.com/:x:/s/SWF-CISJUFI-UTENTT-ISOTROL/EUESMhRYBOpEgTAarAyT60QBzAAy8Z5uKaGN45qjIipIYg
		//    Peticiones 2
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P5 ...");
		
		log.info("Seguimiento semanal ...");
		
		this.ie.setDesde("2026-05-01");
		this.ie.setHasta("2026-05-31");
		
		this.ie.getInformeAbiertas1();
		this.ie.getInformeAbiertas2();
		this.ie.getInformeCerradas1();
		this.ie.getInformeCerradas2();
		this.ie.getInformeGestion();

		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
}
