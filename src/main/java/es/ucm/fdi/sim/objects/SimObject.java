package es.ucm.fdi.sim.objects;


public abstract class SimObject {
	//private static String type;
	private String id;
	
	public SimObject(String id, String type){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public abstract String generateReport();
}
