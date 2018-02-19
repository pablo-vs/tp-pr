package es.ucm.fdi.sim.objects;

import java.util.Collection.*
import java.lang.String
import Vehiculo
import Cruce

public class Carretera{
	
	private static String type = "road_report"
	private List<Vehiculo> listaVehiculos; //La localizacion 0 ocupa la ultima posicion.
	private String id;
	private int longitud, maxVel;
	private Cruce ini,fin;
	
	public Carretera(String id, int l, int maxV, Cruce ini, Cruce fin){
		this.id = id;
		longitud = l;
		maxVel = maxV;
		this.ini = ini;
		this.fin = fin;
	}
	
	//Invocado por vehiculos - Insercion ordenada
	public void entraVehiculo(Vehiculo v){
		boolean end = false;
		int i = 0;
		
		while(i < listaVehiculos.size() && !end){
			if(v.getLocalizacion() < listaVehiculos[i].getLocalizacion()){
				++i;
			}else{
				listaVehiculos.add(i,v);
				end = true;
			}
		}
	}
	
	//Invocado por vehiculos
	public void saleVehiculo(Vehiculo v){
		int i = 0; //SIEMPRE SALE EL ÚLTIMO NO HACE FALTA BUSCAR
		while(!this.listaVehiculos[i].equals(v)){
			++i;
		}	
		listaVehiculos.remove(i);
	}
	
	//Invocado por el simulador
	public void avanza(){
		//Avanzar + Calcular velocidadBase + reajustar la velocidad + hacer avanzar al vehiculo 
		for(v : listaVehiculos){
			//VELOCIDADBASE
			v.avanza();
		}
	}
	
	public String getID(){
		return id;
	}
	
	public int getLongitud(){
		return longitud;
	}
	
	public int getMaxVel(){
		return maxVel;
	}
	
	public Cruce getIni(){
		return ini;
	}
	
	public Cruce getFin(){
		return fin;
	}
	
	public String generaInforme(){
		String informe;
		boolean first;
		
		first = true;
		informe = "[" + type + "]\n";
		informe += "id = " + id + "\n";
		informe += "state = "
		
		for(v : listaVehiculos){
			if(!first){
				informe += ",";
			}else{
				first = true;
			}
			
			informe += "(" + v.getID() + "," + v.getLocalizacion() + ")";
		}
		
		return informe;
	}
}