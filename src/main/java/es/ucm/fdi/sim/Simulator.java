package es.ucm.fdi.sim;


import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;

import java.util.Map;
import java.util.List;
import java.io.OutputStream;


/**
 * Contains the complete simulator model.
 *
 * @version 05.03.2018
 */
public class Simulator {

    MultiTreeMap<Int, Event> eventList; //Ordenados por tiempo y, a igualdad, orden de llegada
    RoadMap roadMap;
    int timeLimit, timer;
	
    public Simulator(int limit){
	eventList = new MultiTreeMap<Int, Event>((a, b) -> a.getTime() - b.getTime()); 
	timeLimit = limit;
	timer = 0;
    }
	
    /**
     * Adds a mew <code>Event</code> to the list, ordered by time.
     *
     * @param e The <code>Event</code> to insert.
     */
    public void insertEvent(Event e){
	eventList.put(e.getTime(), e);
    }
	
    /**
     * Advances the simulation, writing a report after each step.
     *
     * @param simulationSteps The number of steps to simulate
     * @param outputFIle <code>OutputStream</code> to store the reports.
     */
    public void execute(int simulationSteps, OutputStream outputFile)
    {
	timeLimit = timer + simulationSteps - 1;
	while (timer <= timeLimit) {
	    // 1. ejecutar los eventos correspondientes a ese tiempo
	    for(Event e : eventList.get(timer)) {
		e.execute(roadMap);
	    }
		
	    // 2. invocar al método avanzar de las carreteras
	    for(Road r : roadMap.getRoads()) {
		r.move();
	    }
			
	    // 3. invocar al método avanzar de los cruces
	    for(Junction j : roadMap.getJunctions()) {
		j.move();
	    }
			
	    // 4. this.contadorTiempo++;
	    this.timer++;
			
	    // 5. esciribir un informe en OutputStream
	    // en caso de que no sea null
	    if(outputFile != null) {
		prepareReport().store(outputFile);
	    }
	}
    }
	
    /**
     * Prepares a report of all the objects in the simulation,
     * first the roads, then the junctions and then the vehicles,
     * by order of insertion.
     *
     * @return An <code>Ini</code> object storing the report
     */
    public Ini prepareReport()
    {
	Ini report;
	for(Road r : roadMap.getRoads()) {
	    report.addSection(r.report(timer));
	}
	for(Junction j : roadMap.getJunctions()) {
	    report.addSection(j.report(timer));
	}
	for(Vehicle v : roadMap.getVehicles()) {
	    report.addSection(v.report(timer));
	}
	return report;
    }
}
