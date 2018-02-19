package es.ucm.fdi.sim;

import java.util.Collection.*
import Evento

public class Simulator {

	List<Evento> listaEventos; //Ordenados por tiempo y, a igualdad, orden de llegada
	
	public Simulador(){
		
	}
	
	public void insertaEvento(Evento e){
		
	}
	
	public void ejecuta(int pasosSimulacion, OutputStream ficheroSalida)
	{
		limiteTiempo = contadorTiempo + pasosSimulacion - 1;
		while (contadorTiempo <= limiteTiempo) {
			// 1. ejecutar los eventos correspondientes a ese tiempo
			// 2. invocar al método avanzar de las carreteras
			// 3. invocar al método avanzar de los cruces
			// 4. this.contadorTiempo++;
			// 5. esciribir un informe en OutputStream
			// en caso de que no sea null
		}

	}
	
	public String generaInforme() //???
	{
		
	}
}
