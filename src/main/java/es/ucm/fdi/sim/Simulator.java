package es.ucm.fdi.sim;

import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
//import es.ucm.fdi.util.MultiTreeMap;

import java.util.Map;
import java.util.List;
import java.io.OutputStream;

public class Simulator {

	List<Event> listaEventos; //Ordenados por tiempo y, a igualdad, orden de llegada
	int timeLimit, timer;
	
	public Simulator(){
		
	}
	
	public void insertaEvento(Event e){
		
	}

	public SimObject getObject(String id) throws ObjectNotFoundException {
		return null;
	}
	
	public void ejecuta(int pasosSimulacion, OutputStream ficheroSalida)
	{
		timeLimit = timer + pasosSimulacion - 1;
		while (timer <= timeLimit) {
			// 1. ejecutar los eventos correspondientes a ese tiempo
			// 2. invocar al método avanzar de las carreteras
			// 3. invocar al método avanzar de los cruces
			// 4. this.contadorTiempo++;
			// 5. esciribir un informe en OutputStream
			// en caso de que no sea null
		}

	}
	
	public String prepareReport(Map<String, String> report) //???
	{
		return "";
	}
}
