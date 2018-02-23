package es.ucm.fdi.sim.objects;

import es.ucm.fdi.ini.IniSection;

public abstract class SimObject {
	private String id;
	
	public SimObject(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}

	public boolean equals(SimObject other) {
		boolean equal = false;
		
		//Full check
		if(getClass() == other.getClass() && id == other.getID()){
			equal = true;
		}
		
		return equal;
	}
	
	public abstract IniSection generateReport(int t);
}
