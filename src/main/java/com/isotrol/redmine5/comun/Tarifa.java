package com.isotrol.redmine5.comun;

import com.isotrol.redmine5.comun.Constantes.ROLES;

public class Tarifa 
{
	public static double getPrecioBase(ROLES rol)
	{
		return switch(rol)
		{ 
			case JP	-> 38.0;
			case CO -> 38.0;
			case AN -> 31.5;
			case PR -> 20.0;
			default -> 0.0;
		};
	}
	
	public static double getPrecioChoque(ROLES rol)
	{
		return switch(rol)
		{ 
			case JP	-> 53.82;
			case CO -> 43.66;
			case AN -> 36.9;
			case PR -> 29.72;
			default -> 0.0;
		};
	}
}
