package es.ucm.fdi.sim.objects;

import es.ucm.fdi.ini.IniSection;

/**
 *	Abstract class grouping the behavior of every object of the simulation.
 *
 *	@version 26.02.2018
 */
public abstract class SimObject {
	private String id;
	
	/**
	 * Constructor.
	 * 
	 * @param id	ID of the current object.
	 */
	public SimObject(String id){
		this.id = id;
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
		int result = 0;
		for (char c : id.toCharArray()) {
			result = result*26 + c;
		}
		return result;
	}
	
	/**
	 * Method for generating a report of this object's status.
	 * 
	 * @param t Time at which the report is issued.
	 * @return Report of the current <code>SimObject</code>'s state as an <code>IniSection</code>.
	 */
	public abstract IniSection generateReport(int t);
}
