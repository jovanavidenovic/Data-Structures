
public class DisjointSubset {
	
	Object value;
	DisjointSubset parent;
	int noNodes; //moc podmnozice
	
	public DisjointSubset () {
		this(null, null, 0);
	}
	
	public DisjointSubset (Object val, DisjointSubset par, int noN) {
		this.value = val;
		this.parent = par;
		this.noNodes = noN;
	}
	
	public DisjointSubset makeset(Object x) {
		DisjointSubset newEl = new DisjointSubset();
		newEl.value = x;
		newEl.noNodes = 1;
		newEl.parent = newEl;
		return newEl;
	}
	
	/*
	 * najde koren mnozice, ki joj x pripada
	 * kompleksnost se priblizuje konstanti
	 */
	public DisjointSubset find(DisjointSubset x) {
		if(x == x.parent) { //prisli smo do korena podmnozice
			return x;
		} else {
			x.parent = this.find(x.parent); //prevezemo
			return x.parent;
		}	
	}
	
	/*
	 * koren drevesa z manjsim stevilo elementov prevezemo 
	 * na koren drevesa z vecjim stevilom elementov
	 */
	public void union(DisjointSubset a1, DisjointSubset a2) {
		DisjointSubset s1 = this.find(a1);
		DisjointSubset s2 = this.find(a2);
		
		if(s1.noNodes >= s2.noNodes) {
			s2.parent = s1;
			s1.noNodes += s2.noNodes;
		} else {
			s1.parent = s2;
			s2.noNodes += s1.noNodes;
		}
	}	
}
