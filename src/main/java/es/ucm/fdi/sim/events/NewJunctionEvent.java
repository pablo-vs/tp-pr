package es.ucm.fdi.sim.events;

import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;


/**
 * Represents the <code>Event</code> used to instantiate a new <code>Junction</code>.
 *
 * @version 10.03.2018
 */
public class NewJunctionEvent extends Event {
    private String junctionID;

    /**
     * Empty constructor.
     */
    public NewJunctionEvent(){}

    /**
     * Full constructor.
     *
     * @param t Time of the event.
     * @param id ID of the junction.
     */
    public NewJunctionEvent(int t, String id){
		super(t);
		junctionID = id;
    }

    /**
     * Instantiates a new junction, given the parameters are valid.
     *
     * @param r The <code>RoadMap</code> of the current simulation.
     */
    @Override
    public void execute(RoadMap r){
    	r.addJunction(new Junction(junctionID));
    }

    @Override
    public boolean equals(Object o){
    	return (o instanceof NewJunctionEvent 
    			&& junctionID.equals(((NewJunctionEvent)o).junctionID));
    }
    
    /**
     * Builder for this event.
     */
    public static class Builder extends EventBuilder{
		public static final String TAG = "new_junction";
	
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewJunctionEvent build(IniSection ini){
		    NewJunctionEvent event;
		    String tStr, idStr;
				
		    event = null;
		    if(TAG.equals(ini.getTag()))
			{
			    try{
					tStr = ini.getValue("time");
					idStr = ini.getValue("id");
					checkIDValidity(idStr);
							
					event = new NewJunctionEvent(Integer.parseInt(tStr), idStr);	
			    } catch(Exception e){
			    	throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
			    }	
			}
				
		    return event;
		}
    }
}
