package com.isotrol.redmine5.comun;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes.TIPO;

import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
@Component("linea")
public class Linea 
{
	/*
	 * Consulta 2N ANS					Operación
	 * Consultoría ANS					***
	 * Corrección de Datos ANS			Operación
	 * Entregas							***
	 * Error Conocido ANS				***
	 * Evolutivo ANS					Desarrollo
	 * Hito								***
	 * Incidencia Bloqueante ANS		Operación
	 * Incidencia Inmediata ANS			Operación
	 * Incidencia No Bloqueante ANS		Operación
	 * Otras Peticiones ANS				Operación				
	 * Tarea de Gestión					Gestión
	 * Tarea de Gestión Incidencias		Gestión
	 * Tarea de Valoración				***
	 * Tareas							***
	 * Traspaso EO						Gestión
	 * Defecto 							Operación
	 */
	
	public Map<String, TIPO> tipo;
	
	public Linea()
	{
		this.tipo = new HashMap<>();
		
		this.tipo.put( "Evolutivo ANS", TIPO.DESARROLLO );
		this.tipo.put( "Tarea de Gestión", TIPO.GESTION );
		this.tipo.put( "Tarea de Gestión Incidencias", TIPO.GESTION );
		this.tipo.put( "Traspaso EO", TIPO.GESTION );
		this.tipo.put( "Consulta 2N ANS", TIPO.OPERACION );
		this.tipo.put( "Corrección de Datos ANS", TIPO.OPERACION );
		this.tipo.put( "Defecto", TIPO.OPERACION );
		this.tipo.put( "Incidencia Bloqueante ANS", TIPO.OPERACION );
		this.tipo.put( "Incidencia Inmediata ANS", TIPO.OPERACION );
		this.tipo.put( "Incidencia No Bloqueante ANS", TIPO.OPERACION );
		this.tipo.put( "Otras Peticiones ANS", TIPO.OPERACION );
	}

	/**
	 * Método que retorna la línea de cada tipo de petición
	 * 
	 * @param id	Tipo de la petición
	 * @return		{Gestión, Operación, Desarrollo, Otros}
	 */
	public TIPO getLinea(String id)
	{
		return this.tipo.getOrDefault( id.trim() , TIPO.OTRO );
	}
}
