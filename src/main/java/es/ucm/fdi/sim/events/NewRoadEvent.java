package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
 * Represents the New Road Event.
 *
 * @version 10.03.2018
 */
public class NewRoadEvent extends Event {
    private String roadID, ini, end;
    private int length, maxVel;

    /**
     * Empty constructor.
     */
    public NewRoadEvent(){}

    /**
     * Full constructor.
     *
     * @param t Time of the event.
     * @param id ID of the road.
     * @param ini Initial junction of the road.
     * @param end Final junction of the road.
     * @param v Maximum speed of the road.
     * @param l Length of the road.
     */
    public NewRoadEvent(int t, String id, String ini, String end, int v, int l){
	super(t);
	roadID = id;
	this.ini = ini;
	this.end = end;
	maxVel = v;
	length = l;
    }

    /**
     * Instantiates a new road, given the parameters are valid.
     *
     * @param r The <code>RoadMap</code> of the current simulation.
     */
    @Override
    public void execute(RoadMap r) throws InvalidEventException{
	Junction iniJ, endJ;
		
	try{
	    iniJ = r.getJunction(ini);
	    endJ = r.getJunction(end);
	    Road newRoad = new Road(roadID, length, maxVel, iniJ, endJ);
	    r.addRoad(newRoad);
			
	} catch (ObjectNotFoundException e){
	    throw new InvalidEventException("Error: Junction not found.\n" + e.getMessage());
	}
    }
	
    /**
     * Builder for this event.
     */
    public static class Builder extends EventBuilder{
	public static final String TAG = "new_road";

	/**
	 * Build the event from a given INI section, returns null if the section tag does
	 * not match the event tag.
	 *
	 * @param section The <code>IniSection</code> from which to parse the event.
	 */
	@Override
	public NewRoadEvent build(IniSection ini){
	    NewRoadEvent event;
	    String timeStr, idStr, iniStr, endStr, maxVelStr, lengthStr;
			
	    event = null;
	    if(TAG.equals(ini.getTag()))
		{
		    try{
			timeStr = ini.getValue("time");
			idStr = ini.getValue("id");
			iniStr = ini.getValue("src");
			endStr = ini.getValue("dest");
			maxVelStr = ini.getValue("max_speed");
			lengthStr = ini.getValue("length");
					
			checkIDValidity(idStr);
			checkIDValidity(iniStr);
			checkIDValidity(endStr);
					
			//event = NewRoadEvent.this; ^^
			event = new NewRoadEvent(Integer.parseInt(timeStr), idStr, iniStr,
						 endStr, Integer.parseInt(maxVelStr),
						 Integer.parseInt(lengthStr));
		    } catch (Exception e){
			throw new InvalidEventException("Error while parsing event:\n" + e.getMessage(), e);
		    }	
		}

	    return event;
	}
    }
}
