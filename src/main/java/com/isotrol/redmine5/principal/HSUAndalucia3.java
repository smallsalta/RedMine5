package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import com.isotrol.redmine5.informes.InformeImputaciones;

import lombok.extern.log4j.Log4j;

// @Component("prueba3")
@Log4j
public class HSUAndalucia3 
implements CommandLineRunner 
{
	@Autowired
	@Qualifier("informeImputaciones")
	private InformeImputaciones informe;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P3 ...");
		
		log.info("Imputaciones en Redmine ...");
		
		// CONSIDERACIONES:
		// 1. Ajustar el SPENT_ON del informe a LM o M
		
		this.informe.getDatosSemana();
		this.informe.getDatosCsbsMes();

		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	
}
