package com.isotrol.redmine5.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.isotrol.redmine5.informes.InformeLunesTop;

import lombok.extern.log4j.Log4j;

// @Component("prueba6")
@Log4j
public class HSUAndalucia6 
implements CommandLineRunner 
{
	@Autowired
	private InformeLunesTop ie;
	
	@Override
	public void run(String... args) 
	throws Exception 
	{
		// CONSIDERACIONES:
		// 1. FECHANUMERO
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		log.info("P6 ...");
		
		log.info("TOP 20 ...");
		
		this.ie.getInformeTop1( 133466, 127509, 141207, 139921, 125293, 136294, 141140, 138771, 141168, 140940, 135745, 136298, 122917, 125541, 141408, 125079, 142048, 141266, 130855, 129173 );
		this.ie.getInformeTop2( 122917, 125026, 125053, 125079, 125293, 125436, 125541, 125962, 127410, 127409, 127509, 129173, 129180, 129485, 130855, 130974, 131048, 131161, 131162, 131825 );

		log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
}
