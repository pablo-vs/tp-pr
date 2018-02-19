package es.ucm.fdi.sim.objects;

import java.util.Collection.List;
import java.lang.String
import Carretera;
import Cruce;

/**
	Controla el comportamiento general de los vehículos en la simulación.
*/
public class Vehiculo{
	
	private static String type = "vehicle_report";
	private String id;
	private Carretera carreteraActual;
	private List<Cruce> itinerario; //Carreteras??
	private int velMaxima, velActual, localizacion, tiempoAveria;
	private boolean haLlegado, enCola;
	
	public Vehiculo(String id, List<Cruce> itinerario){
		this.id = id;
		this.itinerario = itinerario;
		carreteraActual = -1;
		velMaxima = 0;
		velActual = 0;
		localizacion = 0;
		tiempoAveria = 0;
		haLlegado = false;
		enCola = false;
	}
	
	public void avanza(){
		
		if(tiempoAveria == 0 && !enCola){
			localizacion += velActual;
			
			if(localizacion >= carreteraActual.getLongitud()){ //???? INCORRECTO
				localizacion = carreteraActual.getLongitud();
				carreteraActual.getFin().entraVehiculo(this);
				carreteraActual.saleVehiculo(this);
				velActual = 0;
				enCola = true;
			}
			
		}else if(tiempoAveria > 0){
			tiempoAveria--;
		}
	}
	
	private void moverASiguienteCarretera(){
		localizacion = 0;
		carreteraActual++;
		enCola = false;
		
		if(carreteraActual == itinerario.size()) { //NOPE
			haLlegado = true
			velActual = 0;
		}else{
			velMaxima = carreteraActual.getMaxVel();
		}
	}
	
	public void setTiempoAveria(int t){
		tiempoAveria += t;
	}
	
	public void setVelocidadActual(int v){
		if(v <= velMaxima){
			velActual = v;	
		}else{
			velActual = velMaxima;
		}
	}
	
	public void setVelMaxima(int v){
		velMaxima = v;
	}
	
	public int getLocalizacion(){
		return localizacion;
	}	
	
	public String generaInforme(int paso){
		//Genera el informe en formato INI
		String informe;
		
		informe = "[" + type + "]\n";
		informe += "id = " + id + "\n";
		informe += "time = " + paso + "\n";
		informe += "speed = " + .velActual + "\n";
		informe += "kilometraje = " + .localizacion + "\n";
		informe += "faulty = ";
		
		if(tiempoAveria > 0){
			informe += 1;
		}
		else{
			informe += 0;
		}
		informe += "\n";
		
		informe += "location = ";
		if(haLlegado){
			informe += "arrived";
		}else{
			informe += "(" + carreteraActual.getID() + "," + localizacion;
		}
		
		return informe;
	}
