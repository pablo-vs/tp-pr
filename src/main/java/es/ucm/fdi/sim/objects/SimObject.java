package es.ucm.fdi.sim.objects;


public abstract class SimObject {
	//private static String type;
	private String id;
	
	public SimObject(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}

	public boolean equals(SimObject other) {
		return id == other.getID();
	}
	
	public abstract String generateReport(int t);
}
