package es.ucm.fdi.sim.events;

public abstract class Event {
	private String id;
	
	public Event(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public abstract String generateReport();
}
