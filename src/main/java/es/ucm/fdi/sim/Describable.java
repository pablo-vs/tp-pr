package es.ucm.fdi.sim;

import java.util.Map;

public interface Describable {
	/**
	 * Return a  description of the object.
	 *
	 * @param out A <code>Map<String, String></code> which will contain the representation of the object.
	 */
	void describe(Map<String, String> out);
}
