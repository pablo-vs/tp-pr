package es.ucm.fdi.sim.objects;

import java.util.Collection.*
import Vehiculo
import Carretera

public class Cruce{
	
	//Semaforos (???)
	List<Vehiculo> cola; //QUEUE better
	List<Carretera> carreteras;
	List<boolean> semaforos;
	
	public Cruces(){
		
	}
	
	//Invocado por vehiculos -> Ordenados por orden de llegada
	public void entraVehiculo(Vehiculo v){
		cola.add(v);
	}
	
	//Invocado por vehiculos -> Ordenados por orden de llegada
	public void saleVehiculo(Vehiculo v){
		cola.pop(); //REVISAR - pop_back()
	}
	
	public void avanza(){
		
	}
	
	public String generaInforme(){
		
	}
}