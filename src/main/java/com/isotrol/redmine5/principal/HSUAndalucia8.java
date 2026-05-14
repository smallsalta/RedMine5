package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import com.isotrol.redmine5.informes.InformeMesChoque;

import lombok.extern.log4j.Log4j;

// @Component("prueba8")
@Log4j
public class HSUAndalucia8 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeChoque")
	private InformeMesChoque im;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		// CONSIDERACIONES:
		// 1. Ajustar la fecha hasta en los informes Redmine
		// 2. Ajustar las peticiones de Choque > Peticiones 2
		//    https://everisgroup.sharepoint.com/:x:/s/SWF-CISJUFI-UTENTT-ISOTROL/EUESMhRYBOpEgTAarAyT60QBzAAy8Z5uKaGN45qjIipIYg
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P8 ...");
		
		log.info("Plan Choque ...");
				
		this.im.getInformeAbiertasTarifaBase();
// 		this.im.getInformeAbiertasTarifaChoque();
		
// 		this.im.getInformeCerradasTarifaBase();
// 		this.im.getInformeCerradasTarifaChoque();
		
		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
}
