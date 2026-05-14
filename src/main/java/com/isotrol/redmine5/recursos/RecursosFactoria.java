package com.isotrol.redmine5.recursos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isotrol.redmine5.comun.Constantes.ROLES;

import lombok.Data;

@Component("recursosFactoria")
@Data
public class RecursosFactoria 
implements Recursos
{
	private Map<Integer, String> nombre;
	private Map<Integer, ROLES> rol;
	
	public RecursosFactoria()
	{
		this.nombre = new HashMap<>();
		this.rol	= new HashMap<>();
		
		this.rellenarIsotrol();
		this.rellenarNTT();
	}
	
	protected void rellenarIsotrol()
	{
		this.rellenar( 523, "Adrian Carmona Arras", ROLES.PR );
		this.rellenar( 354, "Adrián Urbano", ROLES.AN );
		this.rellenar( 583, "Adrian Villenas Masso", ROLES.PR );
		this.rellenar( 562, "Alejandro Norte", ROLES.AN );
		this.rellenar( 567, "Alvaro Caro Jimenez", ROLES.PR );
		this.rellenar( 482, "Alvaro Lopez Pizarro", ROLES.PR );
		this.rellenar( 658, "Carlos Melgar Núñez", ROLES.AN );
		this.rellenar( 469, "Francisco Javier Moreno Gabarrri", ROLES.PR );
		this.rellenar( 657, "Francisco Javier Baena Loza", ROLES.AN );
		this.rellenar( 503, "Ismael Pozo González", ROLES.PR );
		this.rellenar( 485, "Iván Morales", ROLES.PR );
		this.rellenar( 524, "Jesús Manuel Hernandez Puro", ROLES.PR );
		this.rellenar( 525, "Joaquin Alonso Perianez", ROLES.PR );	
		this.rellenar( 522, "Jorge M. Rodríguez", ROLES.AN );
		this.rellenar( 620, "Jose Antonio Silva Gonzalez", ROLES.JP );
		this.rellenar( 467, "Jose Luis Camargo Linares", ROLES.AN );
		this.rellenar( 582, "Jose Manuel Martinez Palacio", ROLES.PR );
		this.rellenar( 466, "Jose Miguel Garcia Lluch", ROLES.CO );
		this.rellenar( 466, "Jose Miguel Garcia Lluch", ROLES.CO );
		this.rellenar( 93, "Remedios Gutiérrez Martínez", ROLES.AN );
		this.rellenar( 555, "Roberto Pérez", ROLES.JP );
		this.rellenar( 484, "Rocio Hidalgo Arias", ROLES.AN );
		this.rellenar( 67, "Susana Guerrero", ROLES.CO );
		this.rellenar( 660, "Urko López López", ROLES.AN );
		this.rellenar( 470, "Xiomara Suárez Arras", ROLES.AN );
		this.rellenar( 558, "Miguel Ángel Megías Ramos", ROLES.PR );
		this.rellenar( -1, "Esperanza M. Escacena", ROLES.AN );					// Anónimos ... ID = 2
		this.rellenar( -2, "Juan A. Ruiz", ROLES.PR );							// Anónimos ... ID = 2
	}
	
	protected void rellenarNTT()
	{
		this.rellenar( 554, "Abel García Pascual", ROLES.PR );
		this.rellenar( 553, "Alejandro Martínez Hernández", ROLES.PR );
		this.rellenar( 578, "Alejandro Urdiales Manzanaro", ROLES.PR ); 		// Las horas no cuentan en el periodo [***, 08/09/25]
		this.rellenar( 650, "Alejandro Lillo López", ROLES.PR );
		this.rellenar( 653, "Alejandro Segura Villa", ROLES.PR );
		this.rellenar( 639, "Alicia Sánchez", ROLES.AN );
		this.rellenar( 520, "Ana Isabel Albarracín Cortes", ROLES.PR );
		this.rellenar( 665, "Ángel Amo Sánchez", ROLES.PR );
		this.rellenar( 531, "Ángel Vicente Romero Galindo", ROLES.AN );
		this.rellenar( 540, "Antonio Manuel Benítez García", ROLES.AN );
		this.rellenar( 651, "Antonio Martínez Yllan", ROLES.AN );
		this.rellenar( 495, "Antonio Miguel López Vargas", ROLES.PR );	
		this.rellenar( 474, "Carlos Fernández Rojas", ROLES.AN );
		this.rellenar( 543, "Diego Alejandro Berroterán Navarro", ROLES.AN );
		this.rellenar( 654, "David Recio Dominguez", ROLES.CO );
		this.rellenar( 404, "Edmundo Diaz González", ROLES.JP );
		this.rellenar( 502, "Eduardo Collado Méndez", ROLES.PR );
		this.rellenar( 638, "Eduardo Martínez Pardeiro", ROLES.JP );
		this.rellenar( 664, "Enrique Jordano García", ROLES.PR );
		this.rellenar( 493, "Fernando Osuna Herrera", ROLES.AN );
		this.rellenar( 500, "Francisco Javier García Sánchez", ROLES.PR );
		this.rellenar( 471, "Francisco Javier Marín Reyes", ROLES.JP );
		this.rellenar( 548, "Francisco José Simoni García", ROLES.AN );
		this.rellenar( 519, "Francisco Martínez Martínez", ROLES.PR );
		this.rellenar( 667, "Gabriel Jesús Bernal Copete", ROLES.PR );
		this.rellenar( 530, "Gadi Macías Sánchez", ROLES.PR );
		this.rellenar( 641, "Guillermo Ibarra Carmona", ROLES.PR );
		this.rellenar( 661, "Guillermo Jiménez Martínez", ROLES.PR );
		this.rellenar( 496, "Gregorio Bas Bugnon", ROLES.PR );
		this.rellenar( 640, "Guadalupe Flores", ROLES.AN );
		this.rellenar( 514, "Herminia Mozo Moreno", ROLES.PR );
		this.rellenar( 642, "Jerónimo Silva Mulero", ROLES.PR );
		this.rellenar( 444, "Jesús Javier Marroquí Ayesa", ROLES.PR );
		this.rellenar( 527, "Jesús Martínez Fernández", ROLES.JP );
		this.rellenar( 499, "Joaquín Doncel Fernández", ROLES.CO );
		this.rellenar( 652, "Jorge Dominguez Sanchez", ROLES.PR );
		this.rellenar( 668, "Jorge Navarro García", ROLES.AN );
		this.rellenar( 515, "José Antonio Ortega Yepes", ROLES.PR );
		this.rellenar( 659, "José Antonio Ruíz Salto", ROLES.AN );
		this.rellenar( 480, "José Arques Gómez", ROLES.PR );
		this.rellenar( 421, "José Manuel Domínguez Carvajal", ROLES.CO );
		this.rellenar( 669, "José Pablo Toro Trujillo", ROLES.PR );
		this.rellenar( 516, "José Vicente Fernández Martínez", ROLES.PR );
		this.rellenar( 505, "Juan Francisco Romera Regol", ROLES.PR );
		this.rellenar( 529, "Juan Manuel Ortega Martín", ROLES.CO );
		this.rellenar( 518, "Juan Torres Sánchez", ROLES.PR );
		this.rellenar( 662, "Lorena Moreno Morillas", ROLES.CO );
		this.rellenar( 473, "Manuel Romero Delgado", ROLES.CO );
		this.rellenar( 352, "María José Mateos Rojas", ROLES.CO );
		this.rellenar( 501, "Maryury Katherine Ramírez", ROLES.PR );
		this.rellenar( 542, "Mikhael Ekadasi Aguila Sánchez", ROLES.AN );
		this.rellenar( 487, "Myriam Zorrero Blanco", ROLES.AN );
		this.rellenar( 497, "Pablo González Noguer", ROLES.PR );
		this.rellenar( 574, "Pablo Muñoz Del Bot", ROLES.PR );
		this.rellenar( 494, "Pablo Vargas García", ROLES.PR );
		this.rellenar( 666, "Patricia Muñiz Moliner", ROLES.PR );
		this.rellenar( 507, "Rafael Espinar Rodríguez", ROLES.PR );
		this.rellenar( 374, "Samuel Orozco", ROLES.PR );
		this.rellenar( 478, "Santiago Celada González", ROLES.PR );
		this.rellenar( 498, "Veronica Polo Guerra", ROLES.PR );					// Las horas no cuentan en el periodo [01/06/25, 08/09/25]
		this.rellenar( 504, "Victor Martínez Martínez", ROLES.AN );	
		this.rellenar( -3, "Antonio M. López", ROLES.PR );						// Anónimos ... ID = 2
		
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
