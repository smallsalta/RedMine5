package com.isotrol.redmine5.recursos;

import java.util.Map;

import com.isotrol.redmine5.comun.Constantes.ROLES;

public interface Recursos 
{
	public Map<Integer, String> getNombre();
	public Map<Integer, ROLES> getRol();
}
