package com.isotrol.redmine5.comun;

public class Constantes 
{
	// Redmine
	public static final String RM_URL_CSBS 			= "http://redmine.csbs.junta-andalucia.es";
	public static final String RM_IUP_URL 			= "https://redmine-iup.isotrol.com/";
	
	// API KEY
	public static final String API_KEY_ROBERTO_CSBS	= "7c5014eff99c24ad16798380df4ecace7b5500f2";
	public static final String API_KEY_SILVA_CSBS	= "e68fbc868d7a2710c851992d47cb5ab89a39a2d4";
	public static final String API_KEY_SILVA_IUP 	= "5b645dfb85fc3292c89ead6b1861a8fba26cb056";
	
	// Máximo de registros en cada solicitud ... 
	// Hay que jugar con limit=xxx&offset=xxx
	public static final int LIMIT					= 100;
	
	// ID de los proyectos
	public static final int HSU_ID_IUP				= 2341;
	public static final int HSU_ID_CSBS				= 3;
	
	// Para las imputaciones
	public static final int HSU_PETICION_CSBS		= 128902;
	public static final int ACTIVIDAD_CSBS			= 28;
	public static final String COM_SILVA			= "Gestión y coordinación del proyecto";
	public static final String COM_ROBER			= "Dirección y gestión del servicio Jefatura Proyecto";
	
	// Informes
	public static final int INF_CERRADAS_UMES 		= 1352;
	public static final int INF_CERRADAS_JM 		= 1399;
	public static final int INF_ABIERTAS 			= 1353;
	public static final int INF_GESTION				= 1354;
	
	public static final String INF_ABI_LUN1			= "abiertas_todas.csv";
	public static final String INF_ABI_LUN2			= "abiertas_umes.csv";
	public static final String INF_ABI				= "abiertas.csv";
	public static final String INF_ABI_B			= "abiertas_base.csv";
	public static final String INF_ABI_C			= "abiertas_choque.csv";
	public static final String INF_CER_LUN1			= "cerradas_todas.csv";
	public static final String INF_CER_LUN2			= "cerradas_umes.csv";
	public static final String INF_CER				= "cerradas.csv";
	public static final String INF_CER_B			= "cerradas_base.csv";
	public static final String INF_CER_C			= "cerradas_choque.csv";
	public static final String INF_GES_LUN1			= "gestion_todas.csv";
	public static final String INF_GES_LUN2			= "gestion_umes.csv";
	public static final String INF_GES				= "gestion.csv";
	public static final String INF_PICADAS			= "imputaciones.csv";
	public static final String INF_TOP1				= "top1_lunes.csv";
	public static final String INF_TOP2				= "top2_lunes.csv";
	public static final String INF_REPARTO1			= "reparto1.csv";
	public static final String INF_REPARTO2			= "reparto2.csv";
	public static final String INF_ANONIMOS			= "_anonimos.csv";
	public static final String INF_IMPORTES			= "_importes.csv";
	public static final String INF_CHOQUES			= "_choques.csv";
	public static final String INF_CARPETA 			= "src/main/resources/";
	
	public static final String CABECERA_CSV1		= "LÍNEA;"
														+ "CANAL;"
														+ "ID PETICIÓN;"
														+ "TÍTULO PETICIÓN;"
														+ "TIPO PETICIÓN;"
														+ "HORAS JP;"
														+ "HORAS CO;"
														+ "HORAS AN;"
														+ "HORAS PR;"
														+ "TOTAL HORAS;"
														+ "ESTIMADAS JP;"
														+ "ESTIMADAS CO;"
														+ "ESTIMADAS AN;"
														+ "ESTIMADAS PR;"
														+ "TOTAL;"
														+ "MES;"
														+ "FECHA INICIO;"
														+ "FECHA CIERRE;"
														+ "ID GESTIC;"
														+ "DIRECCIÓN DE PROYECTO;"
														
//														+ "ESTADO;"
//														+ "DP;"
//														+ "PRIORIZADO;"
														
														+ "OBSERVACIONES";												
	
	public static final String CABECERA_CSV2		= "ID PETICIÓN;"
														+ "TIPO PETICIÓN;"
														+ "HORAS JP;"
														+ "HORAS CO;"
														+ "HORAS AN;"
														+ "HORAS PR;"
														+ "HORAS TOTAL;"
														+ "ESTIMADAS JP;"
														+ "ESTIMADAS CO;"
														+ "ESTIMADAS AN;"
														+ "ESTIMADAS PR;"
														+ "ESTIMADAS TOTAL;"
														+ "INCURRIDO;"
														+ "VALORADO I;"
														+ "VALORADO II;"
														+ "MARGEN;"
														+ "IMPORTE;"
														+ "CREADA";
														
	public static final String CABECERA_CSV3		= "RECURSO;"
														+ "HORAS IUP;"
														+ "HORAS CSBS";
	
						 		
	
	public static final String CABECERA_CSV4		= "ID PETICIÓN;"
														+ "PROYECTO;"
														+ "TIPO PETICIÓN;"
														+ "ESTADO;"
														+ "ASUNTO;"
														+ "CREADO;"
														+ "AVANCE;"
														+ "IMPORTE;"
														+ "PENDIENTE I;"
														+ "PENDIENTE II";
	
	public static final String CABECERA_CSV5		= "ID PETICIÓN;"
														+ "HORAS;"
														+ "HORAS ISOTROL;"
														+ "HORAS NTT;"
														+ "INCURRIDO ISOTROL;"
														+ "INCURRIDO NTT;"
														+ "REPARTO;"
														+ "REPARTO ISOTROL;"
														+ "REPARTO NTT";
	
	public static final String CABECERA_CSV6		= "LÍNEA;"
														+ "CANAL;"
														+ "ID PETICIÓN;"
														+ "ID GESTIC;"
														+ "TÍTULO PETICIÓN;"
														+ "TIPO PETICIÓN;"
														+ "FECHA INICIO;"
														+ "FECHA CIERRE;"
														+ "Horas JP Est.;"
														+ "Horas CO Est.;"
														+ "Horas AN Est.;"
														+ "Horas PR Est.;"
														+ "Total Horas Estimadas;"
														+ "Importe estimado;"
														+ "Horas JP Eje.;"
														+ "Horas CO Eje.;"
														+ "Horas AN Eje.;"
														+ "Horas PR Eje.;"
														+ "Total Horas Ejecutadas;"
														+ "Importe inicial Incurridos;"
														+ "Importe Defectos;"
														+ "Otras Reducciones Aceptadas SWF;"
														+ "Importe Propuesto para Facturar;"
														+ "DIRECCIÓN DE PROYECTO;"
														+ "OBSERVACIONES";
												
	public static final String SEPARADOR			= ";";
	
	// Tipos de las peticiones
	public static final String DEF					= "Defecto";
	public static final String EVO_ANS				= "Evolutivo ANS";
	
	// Sobrevaloración
	public static final double EXTRA_OT				= 1.1;
	
	// Fechas
	public static final long F_01_06_25				= 1748728800000L;
	public static final long F_08_09_25				= 1757282400000L;
														
	// Observaciones
	public static final String TXT_DESARROLLO		= "Análisis y diseño de la solución. "
														+ "Elaboración del documento de valoración. "
														+ "Desarrollo de la solución. "
														+ "Ejecución de pruebas de verificación funcional en entorno Desarrollo Local. "
														+ "Elaboración del MICDA de la aplicación. Entrega software. "
														+ "Implantación en entorno de Desarrollo CISJUFI. "
														+ "Ejecución de pruebas de verificación funcional en entorno Desarrollo de CISJUFI. "
														+ "Gestión Agendado en entorno de Pruebas CISJUFI. "
														+ "Ejecución de pruebas de verificación funcional en entorno Pruebas de CISJUFI. "
														+ "Gestión Agendado en entorno de Producción CISJUFI.";
	public static final String TXT_GESTION1			= "Seguimiento y coordinación";
	public static final String TXT_GESTION2			= "Traspasos unificados";
	public static final String TXT_OPERACION		= "Análisis de los datos, estudio de la solución. "
														+ "Scripts para correcciones y comprobaciones. "
														+ "Pruebas. "
														+ "Envío de la solución al traspaso unificado. "
														+ "Resolución de la petición";
	public static final String TXT_OTRO				= "Desarrollo de la solución";
	public static final String TXT_NTT				= "Lore ipsum dolor";
	
	public static final int ESTADO_CERRADA			= 5;
	
	///////////////////////////////////////////////////////////////////
	
	public enum ROLES { JP, CO, AN, PR, XX };
	public enum TIPO { DESARROLLO, OPERACION, GESTION, OTRO };
	public enum SPENT_ON { LW, M, LM, W };
	public enum TARIFA { BASE, CHOQUE };
}
