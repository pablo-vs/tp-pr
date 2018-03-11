package es.ucm.fdi.sim.objects;

public class Highway extends Road{
	int lanes;
	
	public Highway(String id, int l, int maxV, Junction ini, Junction end, int nLanes){
		super(id,l,maxV,ini,end);
		lanes = nLanes;
	}
}
