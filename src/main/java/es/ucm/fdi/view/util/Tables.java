package es.ucm.fdi.view.util;

public enum Tables {
	EVENT_LIST(
			"#",
			"Time",
			"Type"
			),
	VEHICLES(
			"ID",
			"Road",
			"Location",
			"Speed",
			"Km",
			"Faulty Units",
			"Itinerary"
			),
	ROADS(
			"ID",
			"Source",
			"Target",
			"Length",
			"Max Speed",
			"Vehicles"
			),
	JUNCTIONS(
			"ID",
			"Green",
			"Red"
			);
	
	String[] tags;
	
	private Tables(String ... tags){
		this.tags = tags;
	}
	
	public String[] getTags() {
		return tags;
	}
}
