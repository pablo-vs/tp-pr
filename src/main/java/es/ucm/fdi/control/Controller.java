package es.ucm.fdi.control;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;

public class Controller {

	/**
	* Transforms a report stored in a <code>Map</code> to an <code>IniSection</code>.
	*
	* @param report Report stored in a <code>Map</code>.
	* @return <code>IniSection</code> containing the report.
	*/
	public static IniSection iniReport(Map<String, String> report) {
		IniSection sec = new IniSection(report.get(""));
		for(Map.Entry<String, String> entry : report.entrySet()) {
			if(!"".equals(entry.getKey())) {
				sec.setValue(entry.getKey(), entry.getValue());
			}
		}
		return sec;
	}
}