package com.isotrol.redmine5.principal;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.isotrol.redmine5.informes.InformeMes;

import lombok.extern.log4j.Log4j;

// @Component("prueba1")
@Log4j
public class HSUAndalucia1 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeLunes")
	private InformeMes ie;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P1 ...");
		
		log.info("Datos de una petición ...");
		
		this.ie.setDesde("2026-03-01");
		this.ie.setHasta("2026-03-31");
		
		int id 			= 128902;
		boolean mes		= true;
		var res3 		= new TreeMap<>( this.ie.getInformeLinea( id, mes ) );
		
		res3.entrySet().forEach( t -> log.info( t.getKey() + "\t" + t.getValue() ) );

		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
}
