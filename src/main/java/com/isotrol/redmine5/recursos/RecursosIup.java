package com.isotrol.redmine5.recursos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes.ROLES;

import lombok.Data;

@Data
@Component("recursosIup")
public class RecursosIup 
implements Recursos
{
	public Map<Integer, String> nombre;
	public Map<Integer, ROLES> rol;
	
	public RecursosIup()
	{
		this.nombre	= new HashMap<>();
		this.rol	= new HashMap<>();
		
		this.rellenar( 96, "Adrian Carmona Arras", ROLES.PR );
		this.rellenar( 538, "Adrián Urbano", ROLES.AN );
		this.rellenar( 713, "Adrian Villenas Masso", ROLES.PR );
		this.rellenar( 84, "Alejandro Norte", ROLES.AN );
		this.rellenar( 175, "Alvaro Caro Jimenez", ROLES.PR );
		this.rellenar( 360, "Alvaro Lopez Pizarro", ROLES.PR );
		this.rellenar( 260, "Carlos Melgar Núñez", ROLES.AN );
		this.rellenar( 230, "Esperanza M. Escacena", ROLES.AN );
		this.rellenar( 355, "Francisco Javier Baena Loza", ROLES.AN );
		this.rellenar( 281, "Francisco Javier Moreno Gabarrri", ROLES.PR );
		this.rellenar( 273, "Ismael Pozo González", ROLES.PR );
		this.rellenar( 196, "Iván Morales", ROLES.PR );
		this.rellenar( 133, "Jesús Manuel Hernandez Puro", ROLES.PR );
		this.rellenar( 557, "Joaquin Alonso Perianez", ROLES.PR );	
		this.rellenar( 276, "Jorge M. Rodríguez", ROLES.AN );
		this.rellenar( 68, "Jose Antonio Silva Gonzalez", ROLES.AN );
		this.rellenar( 352, "Jose Luis Camargo Linares", ROLES.AN );
		this.rellenar( 712, "Jose Manuel Martinez Palacio", ROLES.PR );
		this.rellenar( 259, "Jose Miguel Garcia Lluch", ROLES.CO );
		this.rellenar( 300, "Juan A. Ruiz", ROLES.PR );
		this.rellenar( 195, "Miguel Ángel Megías Ramos", ROLES.PR );
		this.rellenar( 178, "Remedios Gutiérrez Martínez", ROLES.AN );
		this.rellenar( null, "Roberto Pérez", ROLES.JP );
		this.rellenar( 320, "Rocio Hidalgo Arias", ROLES.AN );
		this.rellenar( 177, "Susana Guerrero", ROLES.CO );
		this.rellenar( 264, "Urko López López", ROLES.AN );
		this.rellenar( 233, "Xiomara Suárez Arras", ROLES.AN );	
	}
	
	/**
	 * Método que va rellenando los mapas auxiliares
	 * @param id		ID del usuario en Redmine
	 * @param nombre	Nombre 
	 * @param rol		Rol ... Importante para facturar
	 */
	protected void rellenar(Integer id, String nombre, ROLES rol)
	{
		this.nombre.put( id, nombre );
		this.rol.put( id, rol );
	}
}
