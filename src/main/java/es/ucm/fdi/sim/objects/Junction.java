package es.ucm.fdi.sim.objects;

import java.util.List;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;

public class Junction{
	
	//semaphore (???)
	List<Vehicle> queue; //QUEUE better
	List<Road> roads;
	//List<boolean> trafficLight;
	
	public Junction(){
		
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public void vehicleIn(Vehicle v){
		queue.add(v);
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public void vehicleOut(Vehicle v){
		queue.remove(queue.size()-1); //REVISAR - pop_back()
	}
	
	public void advance(){
		
	}
	
	public String generateReport(){
		return "";
	}
}