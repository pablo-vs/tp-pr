package es.ucm.fdi.sim.objects;


import es.ucm.fdi.ini.IniSection;

/**
 *	Abstract class grouping the behavior of every object of the simulation.
 *
 *	@version 26.02.2018
 */
public abstract class SimObject {
	private String id;
	private String report_header;
	
	/**
	 * Constructor.
	 * 
	 * @param id	ID of the current object.
	 */
	public SimObject(String id, String header){
		this.id = id;
		report_header = header;
	}
	
	/**
	 * Getter for the attribute {@link SimObject#id}
	 * 
	 * @return ID of the current object.
	 */
	public String getID(){
		return id;
	}

	/**
	 * Equals implementation.
	 * 
	 * @param other Object to compare to
	 * @return	true if the objects are of the same class and with equal attributes or false
	 * otherwise.
	 */
	public boolean equals(SimObject other) {
		boolean equal = false;
		
		//Full check
		if(getClass() == other.getClass() && id == other.getID()){
			equal = true;
		}
		
		return equal;
	}

	/**
	* HashCode implementation using the object identifier.
	*
	* @return a unique hash of the object
	*/
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * Method for generating a report of this object's status.
	 * 
	 * @param t Time at which the report is issued.
	 * @param out Reference to a map where the report will be stored.
	 */
	public IniSection report(int t) {
		IniSection out = new IniSection(report_header);
		out.setValue("id", id);
		out.setValue("time", Integer.toString(t));
		fillReportDetails(out);
		
		return out;
	}

	/**
	* Fills the given map with the details of the state of the object.
	*
	* @param out Map to store the report.
	*/
	public abstract void fillReportDetails(IniSection out);
}
