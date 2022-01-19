package Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Index {


	public Index() {
	}



	public Map<Integer, Map<Integer, ArrayList<Integer>>> SPO = new TreeMap<>(); // cluster 1
	public Map<Integer, Map<Integer, ArrayList<Integer>>> SOP = new TreeMap<>(); // cluster 2
	public Map<Integer, Map<Integer, ArrayList<Integer>>> PSO = new TreeMap<>(); // cluster 3
	public Map<Integer, Map<Integer, ArrayList<Integer>>> OPS = new TreeMap<>(); // cluster 4
	public Map<Integer, Map<Integer, ArrayList<Integer>>> POS = new TreeMap<>(); // cluster 5
	public Map<Integer, Map<Integer, ArrayList<Integer>>> OSP = new TreeMap<>(); // cluster 6


	public void addTripletSPO(Integer s, Integer p, Integer o) {
		if(SPO.containsKey(s)) {
			if(SPO.get(s).containsKey(p)){
				if(!SPO.get(s).get(p).contains(o)) {
					SPO.get(s).get(p).add(o);
				}
			}else {
				SPO.get(s).put(p,new ArrayList<Integer>( Arrays.asList(o)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(p, new ArrayList<>(Arrays.asList(o)));
			SPO.put(s,t);
		}
	}

	public void addTripletSOP(Integer s, Integer o, Integer p) {
		if(SOP.containsKey(s)) {
			if(SOP.get(s).containsKey(o)){
				if(!SOP.get(s).get(o).contains(p)) {
					SOP.get(s).get(o).add(p);
				}
			}else {
				SOP.get(s).put(o,new ArrayList<Integer>( Arrays.asList(p)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(o, new ArrayList<Integer>( Arrays.asList(p)));
			SOP.put(s,t);
		}
	}

	public void addTripletPSO(Integer p, Integer s, Integer o) {
		if(PSO.containsKey(p)) {
			if(PSO.get(p).containsKey(s)){
				if(!PSO.get(p).get(s).contains(o)) {
					PSO.get(p).get(s).add(o);
				}
			}else {
				PSO.get(p).put(s,new ArrayList<Integer>( Arrays.asList(o)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(s, new ArrayList<Integer>( Arrays.asList(o)));
			PSO.put(p,t);
		}	}

	public void addTripletOPS(Integer o, Integer p, Integer s) {
		if(OPS.containsKey(o)) {
			if(OPS.get(o).containsKey(p)){
				if(!OPS.get(o).get(p).contains(s)) {
					OPS.get(o).get(p).add(s);
				}
			}else {
				OPS.get(o).put(p,new ArrayList<Integer>( Arrays.asList(s)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(p, new ArrayList<Integer>( Arrays.asList(s)));
			OPS.put(o,t);
		}	
	}

	public void addTripletPOS(Integer p, Integer o, Integer s) {
		if(POS.containsKey(p)) {
			if(POS.get(p).containsKey(o)){
				if(!POS.get(p).get(o).contains(s)) {
					POS.get(p).get(o).add(s);
				}
			}else {
				POS.get(p).put(o,new ArrayList<Integer>( Arrays.asList(s)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(o, new ArrayList<Integer>( Arrays.asList(s)));
			POS.put(p,t);
		}	
	}

	public void addTripletOSP(Integer o, Integer s, Integer p) {
		if(OSP.containsKey(o)) {
			if(OSP.get(o).containsKey(s)){
				if(!OSP.get(o).get(s).contains(p)){
					OSP.get(o).get(s).add(p);
				}
			}else {
				OSP.get(o).put(s,new ArrayList<Integer>( Arrays.asList(p)));
			}
		}else {
			Map<Integer, ArrayList<Integer>> t = new TreeMap<Integer,ArrayList<Integer>>();
			t.put(s, new ArrayList<Integer>(Arrays.asList(p)));
			OSP.put(o,t);
		}	
	}



}
