/*
 	IMPLEMENTACIJA MAPPING (preslikave) z ZGOSCEVALO TABELO
 	
 	Preslikava M vsakemu elementu d iz domene
 	priredi ustrezen element r iz zaloge vrednosti M(d) = r
 	
 	Zgoscena tabela hrani pare (d, r) tako, da je omogocen
 	direkten dostop do para glede na prvi argument d.
 	
 	Pri ODPRTI zgosceni tabeli v polju hranimo kazalce na
 	sezname parov, ki sovpadajo. 
 	
 	Pri ZAPRTI zgosceni tabeli v polju hranimo elemente.
 
 	Pomankljivosti:
 	-- Zgoscena tabela ni dinamicna podatkovna struktura
 	-- Ponovno zgoscenje (rehasnihg) ima linearno casovno kompleksnost
 	-- Operacij urejenosti (min, max, ...) ni mogoce ucinkovito implementirati 	
 */

class HashMapNode {
	private Object key;
	private Object value;
	
	public HashMapNode() {
		this.key = null;
		this.value = null;
	}
	
	public HashMapNode(Object key, Object value) { 
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean equals(Object obj) {
		if (obj instanceof HashMapNode){
			HashMapNode node = (HashMapNode)obj;
			return key.equals(node.key);
		}	
		return false;
	}
	
	public String toString() { 
		return "[" + key + ", " + value + "]";
	}
}



public class HashMap {
	public static final int DEFAULT_SIZE = 10;
	Set [] table;
	
	public HashMap() {
		makenull(DEFAULT_SIZE);
	}
	
	public HashMap(int size) {
		makenull(size);
	}

	public void makenull() {
		makenull(DEFAULT_SIZE);
	}
	
	public void makenull(int size) {
		// create an empty table and initialize the linked lists
		table = new Set[size];
				
		for (int i = 0; i < table.length; i++) {
			table[i] = new Set();
		}
	}
	
	// returns the index of the object in the table
	private int hash(Object d) {
		return Math.abs(d.hashCode()) % this.table.length;
 	} 

	public void print() {
		for (int i = 0; i < table.length; i++) {
			table[i].print();
		}
	}
	
	/*
	 	Funkcija doda nov par (d, r) v preslikavo M. To pomeni, da velja M(d) = r.
	 	Ce v preslikavi M ze obstaja par s kljucem d, 
	 	se obstojeci shranjeni par spremeni v (d, r).
	 */
	public void assign(Object d, Object r) {
		int index = this.hash(d);
		Set s = this.table[index];
		SetElement pos = s.locate(new HashMapNode(d, null));
		if(pos != null) {
			HashMapNode node = (HashMapNode) s.retrieve(pos);
			node.setValue(r);
		} else 
			s.insert(new HashMapNode(d, r));	
	}

	 /*
		Funkcija vrne vrednost M(d).
		Ce vrednost M(d) ni definirana, funkcija vrne null.
	 */
	public Object compute(Object d) {
		int index = this.hash(d);
		Set s = this.table[index];
		SetElement pos = s.locate(new HashMapNode(d, null));
		
		if(pos != null) {
			HashMapNode node = (HashMapNode) s.retrieve(pos);
			return node.getValue();
		}
		return null;
	}
	
	 /*
		Funkcija zbrise par (d, r) iz preslikave M.
		To pomeni, da vrednost M(d) ni vec definirana.
	 */
	public void delete(Object d) {
		int index = this.hash(d);
		Set s = this.table[index];
		
		SetElement pos = s.locate(new HashMapNode(d, null));
		if(pos != null) {
			s.delete(pos);
		} 
	}

	 /*
	  Funkcija zgradi novo zgosceno tabelo podane velikosti.
	  Obstojeci pari (d, r) se prenesejo v novo tabelo.
	 */
	public void rehash(int size) {		
		Set[] oldTable = this.table;
		
		// NE TO! ovako ne incijalizaramo setove unutar tabele
		//this.table = new Set[size];
		
		this.makenull(size);
		
		for(int i = 0; i < oldTable.length; i++) {
			for(SetElement iter = oldTable[i].first(); !oldTable[i].overEnd(iter); iter = oldTable[i].next(iter)) {
				HashMapNode node = (HashMapNode) oldTable[i].retrieve(iter);
				this.assign(node.getKey(), node.getValue());
			}			
		}
	}
}