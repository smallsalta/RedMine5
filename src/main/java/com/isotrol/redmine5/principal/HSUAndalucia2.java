package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.informes.InformeMes;
import com.isotrol.redmine5.informes.InformeMesCierre;

import lombok.extern.log4j.Log4j;

// @Component("prueba2")
@Log4j
public class HSUAndalucia2 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeMes")
	private InformeMes im;
	
	@Autowired
	@Qualifier("informeCierre")
	private InformeMesCierre imc;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		/*
		 * RECUERDA:
		 * 1. Ajustar la fecha hasta en los informes Redmine
		 * 2. Poner los Recursos correctos
		 */  
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P2 ...");
		
		log.info("Cierre del mes ...");
		
		//////////////////////////////////////////////
		
//		this.im.setDesde("2026-05-01");
//		this.im.setHasta("2026-05-31");

//		this.im.getInformeGestion();
//		this.im.getInformeCerradas();
//		this.im.getInformeAbiertas1();
//		this.im.getInformeAbiertas2();
//		this.im.getInformeJM();
		
		//////////////////////////////////////////////

		this.imc.setDesde("2026-04-01");
		this.imc.setHasta("2026-04-30");
		
		this.imc.getInformeGestion();
		this.imc.getInformeCerradas();
//		this.imc.getInformeAbiertas();

		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	
}
