package es.ucm.fdi.view.util;

public enum Actions{
	LOAD_EVENT("Load"), 
	SAVE_EVENT("Save"), 
	CLEAR_EDITOR("Clear"), 
	INSERT_EVENT_DATA("Insert"), 
	PLAY("Play"), 
	STOP("Stop"),
	RESET("Reset"), 
	EXIT("Exit"),
	REPORT("Report"), 
	LOAD_REPORT("Load Report"),
	SAVE_REPORT("Save Report"), 
	DELETE_REPORT("Delete Report"),
	REDIRECT_OUTPUT("Redirect Output"),
	REPORT_SELECTED("Report Selected");
	
	String s;	
	private Actions(String s){
		this.s = s;
	}
	
	public String toString(){
		return s;
	}
}