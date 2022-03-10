import java.util.*;
import java.lang.*;

class GraphVertex implements Comparable
{
	Comparable value;
	GraphEdge firstEdge;
	GraphVertex nextVertex;
	int inDegree;
	GraphVertex parent;
	double distance;
	
	boolean visited;	
	boolean intree;
	
	DisjointSubset subset;
	
	public GraphVertex(Comparable val)
	{
		value = val;
		firstEdge = null;
		nextVertex = null;
		this.inDegree = 0;
		this.parent = null;
		this.distance = 0;
		this.visited = false;
	}
	
	public String toString()
	{
		return value.toString();
	}
	
	@Override
	public int compareTo(Object obj) {
		GraphVertex v2 = (GraphVertex) obj;
		if(this.distance - v2.distance < 0){
			return - 1;
		}else if(this.distance - v2.distance == 0){
			return 0;
		} else return 1;
	}
}

class GraphEdge
{
	Comparable evalue;
	GraphVertex endVertex;
	GraphEdge nextEdge;
	boolean inForest;
	
	public GraphEdge(Comparable eval, GraphVertex eVertex, GraphEdge nEdge)
	{
		evalue = eval;
		endVertex = eVertex;
		nextEdge = nEdge;
		
		this.inForest = false;
	}
}
	

public class DirectedGraph {

	protected GraphVertex fVertex;
	
	public void makenull()
	{
		fVertex = null;
	}
	
	public void insertVertex(GraphVertex v)
	{
		v.nextVertex = fVertex;
		fVertex = v;
	}
	
	public void insertEdge(GraphVertex v1, GraphVertex v2, Comparable eval)
	{
		GraphEdge newEdge = new GraphEdge(eval, v2, v1.firstEdge);
		v1.firstEdge = newEdge;
	}
	
	public GraphVertex firstVertex()
	{
		return fVertex;
	}
	
	public GraphVertex nextVertex(GraphVertex v)
{
		return v.nextVertex;
	}
	
	public GraphEdge firstEdge(GraphVertex v)
	{
		return v.firstEdge;
	}
	
	public GraphEdge nextEdge(GraphVertex v, GraphEdge e)
	{
		return e.nextEdge;
	}
	
	public GraphVertex endPoint(GraphEdge e)
	{
		return e.endVertex;
	}
	
	public void print()
	{
		for (GraphVertex v = firstVertex(); v != null; v = nextVertex(v)) 
		{
			System.out.print(v + ": ");
			for (GraphEdge e = firstEdge(v); e != null; e = nextEdge(v, e))
				System.out.print(endPoint(e) + "(" + e.evalue + ")" + ", ");
			System.out.println();
		}
	}
	
	public void tInit() {
		for(GraphVertex iter = this.firstVertex(); iter != null; iter = this.nextVertex(iter)) {
			iter.distance = 0;
			iter.parent = null;
			for(GraphEdge iter_edge = this.firstEdge(iter); iter_edge != null; iter_edge = this.nextEdge(iter, iter_edge)) {
				GraphVertex end_edge = this.endPoint(iter_edge);
				end_edge.inDegree ++;
			}
		}
	}
	
	//algoritam za iskanje kriticne poti
	public double tDynamic(GraphVertex a, GraphVertex c) {
		/*
		 * a - zacetno vozlisce
		 * c - zakljucno vozlisce
		 */
		this.tInit();
		
		GraphVertex v, w; //trenutno vozlisce in njegov naslednik
		GraphEdge e; //povezava <v, w>
		
		//seznam vozlisc, katerih naslednikov se nismo pregledali
		List<GraphVertex> list = new ArrayList<>();
		list.add(a);
	
		while(!list.isEmpty()) {
			v = list.get(0);	list.remove(0);
			e = this.firstEdge(v);
			
			while(e != null) {
				w = this.endPoint(e);
				if(w.distance < v.distance + (double) e.evalue) {
					w.distance = v.distance + (double) e.evalue;
					w.parent = v;
				}
				w.inDegree --;
				if(w.inDegree == 0) {
					list.add(w);
				}
				e = this.nextEdge(v, e);
			}		
		}
		return c.distance;
	}	
	
	// izpis kriticne poti
	public void tPath(GraphVertex a, GraphVertex c) {
		for(GraphVertex iter = c; iter != null; iter = iter.parent) {
			System.out.print(c.value + " ");
		} 
		System.out.println();	
	}
	
	
	// DIJKSTRA
	
	public void dInit() {
		for(GraphVertex iter = this.firstVertex(); iter != null; iter = this.nextVertex(iter)) {
			iter.visited = false;
			iter.parent = null;
			iter.distance = 0;
		}
	}
	
	public GraphVertex getPriority (Set<GraphVertex> verteces) {
		GraphVertex ver_min = null;
		
		for(GraphVertex iter : verteces) {
			if(ver_min == null || iter.distance < ver_min.distance) {
				ver_min = iter;
			}
		}
		return ver_min;
	}
	
	//DIJKSTRA algoritem za iskanje najkrajše poti 
	public void dijkstra (GraphVertex a) {
		
		this.dInit();
//		Set<GraphVertex> set_ver = new HashSet<GraphVertex>();
		
		Queue<GraphVertex> q_ver = new PriorityQueue<GraphVertex>();
		
		GraphVertex v, w; //trenutno vozlisce in njegov naslednik
		GraphEdge e; //povezava <v, w> 
		
		// priprava zacetnega vozlisca
		a.visited = true;
		a.parent = null;
		a.distance = 0;
		
		q_ver.add(a);
		
		while(!q_ver.isEmpty()) {
			/*
			v = this.getPriority(set_ver);
			set_ver.remove(v);
			*/
			v = q_ver.poll();
			e = this.firstEdge(v);
			
			while(e != null) {
				w = this.endPoint(e);
				if(!w.visited) {
					//uredi w in dodaj v set_ver
					w.visited = true;
					w.parent = v;
					w.distance = v.distance + (double) e.evalue;
					q_ver.add(w);			
				} else if(v.distance + (double) e.evalue < w.distance) {
					//nova, krajsa pot do w
					w.parent = v;
					w.distance = v.distance + (double) e.evalue;	
				}
				
				e = this.nextEdge(v, e);
			}
		}	
	}
	
	
	
	//iskanje najkrajse poti v sirino
	public void breadthFirstSearch(GraphVertex a) {
		
		this.dInit();
		List<GraphVertex> list_ver = new ArrayList<>();
		
		GraphVertex v, w; //trenutno vozlisce in njegov naslednik
		GraphEdge e; //povezava <v, w> 
			
		// priprava zacetnega vozlisca
		a.visited = true;
		a.parent = null;
		a.distance = 0;
			
		list_ver.add(a);
			
		while(!list_ver.isEmpty()) {
			v = list_ver.get(0); list_ver.remove(0);
			e = this.firstEdge(v);
				
			while(e != null) {
				w = this.endPoint(e);
				if(!w.visited) {
					//uredi w in dodaj v set_ver
					w.visited = true;
					w.parent = v;
					w.distance = v.distance + 1;
					list_ver.add(w);			
				} 
				
				e = this.nextEdge(v, e);
			}	
		}
	}
	
	// PRIMOV ALGORITAM NA MVD
	public void pInit() {
		for(GraphVertex iter = this.firstVertex(); iter != null; iter = this.nextVertex(iter)) {
			iter.visited = false;
			iter.parent = null;
			iter.distance = 0;
			iter.intree = false;
		}
	}

	public void prim() {
		
		this.pInit();
		
	//	Set<GraphVertex> set_ver = new HashSet<GraphVertex>();
		Queue<GraphVertex> q_ver = new PriorityQueue<>();
		GraphVertex v, w;
		GraphEdge e;
		
		v = this.fVertex;
		v.visited = true;
		v.parent = null;
		v.intree = false;
		v.distance = 0;
		q_ver.add(v);
		
		while (!q_ver.isEmpty()) {			
		//	v = this.getPriority(set_ver); set_ver.remove(v);
			v = q_ver.poll();
			v.intree = true;
			e = this.firstEdge(v);
			
			while(e != null) { 
				w = this.endPoint(e);
				//e.evalue je razdalja vozlisca w od MVD
				if(! w.visited) {
					w.visited = true; //je v kopici
					w.intree = false; //se ni v MVD
					w.parent = v; //potencialni oce
					
					w.distance = (double) e.evalue;
					q_ver.add(w);
				} else if(!w.intree && (double) e.evalue < w.distance) {
						w.parent = v;
						//popravi razdaljo
						w.distance = (double) e.evalue;
				}
				e = this.nextEdge(v, e);
			}
		}
	}
	
	///	///	///	///	
	
	
	
}
