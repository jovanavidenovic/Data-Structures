class HeapNode implements Comparable {
	Comparable value;
	//indeks elementa v kopici
	int idx;
	
	public HeapNode() {
		this.value = null;
		this.idx = 0;
	}
	
	public HeapNode(Comparable val) {
		this.value = val;
		this.idx = 0;
	}
	
	@Override
	public int compareTo(Object x) {
		HeapNode x_hn = (HeapNode) x;
		return this.value.compareTo(x_hn.value);
	}	
}


public class Heap {
	
	static final int DEFAULT_SIZE = 100;
	static final int DEGREE = 2;
	HeapNode nodes[];
	int noNodes, size;
	
	public Heap() {
		this(DEFAULT_SIZE);
	}
	public Heap (int maxSize) {
		this.size = maxSize;
		this.nodes = new HeapNode[this.size];
		noNodes = 0;
		nodes[0] = new HeapNode();
	}	
	
	public void insert (Comparable x_val) {
		int newNode, parent; //indeks novega vozlisca in oceta
		
		this.noNodes = this.noNodes + 1;
		newNode = this.noNodes; //dodamo element na prvo prazno mesto
		parent = newNode / 2; //zracunamo ko je oce newNode-a
		
		this.nodes[this.noNodes] = new HeapNode();
		HeapNode x = new HeapNode(x_val);
		x.value = x_val;
		
		//dokler je oce vecji od sina, zamenjamo jih
		while(parent > 0 && nodes[parent].compareTo(x) > 0) {
			nodes[newNode].value = nodes[parent].value;
			nodes[newNode].idx = newNode;
		
			newNode = parent;
			parent = parent / 2;
		}
		
		//element vpise sele ko poznamo koncan polozaj
		nodes[newNode].value = x.value;
		nodes[newNode].idx = newNode;
	}
	
	// brisanje najmanjsega elementa iz kopice (korena)
	public HeapNode deleteMin(){
		int newNode; //novi indeks prejsnjega zadnjega elementa
		int son; //indeks manjsega od sinova
		HeapNode temp = new HeapNode();
		
		if(this.noNodes == 0) {
			return null; //prazna kopica
		} else {
			HeapNode retValue = this.nodes[1]; //min element je v korenu
			newNode = 1; //novi polozaj zadnjega elementa je v korenu
			
			int idx_leftSon = 2 * newNode;
			int idx_rightSon = 2 * newNode + 1;
			
			if(idx_rightSon <= this.noNodes) { //preverjamo ali desni sin obstaja
				if(this.nodes[idx_leftSon].compareTo(this.nodes[idx_rightSon]) < 0) {
					son = idx_leftSon;
				} else son = idx_rightSon;	
			} else if (idx_leftSon <= this.noNodes) { //preverjamo ali levi sin obstaja
				son = idx_leftSon;
			} else son = this.noNodes + 1; //sin ne obstaja
			
			while(son <= this.noNodes && this.nodes[this.noNodes].compareTo(this.nodes[son])> 0) {
				//manjsi od sinov se zamenja z ocetom
				nodes[newNode].value = nodes[son].value;
				nodes[newNode].idx = newNode;
				
				newNode = son;
				idx_leftSon = 2 * newNode;
				idx_rightSon = 2 * newNode + 1;
				
				if(idx_rightSon <= this.noNodes) { //preverjamo ali desni sin obstaja
					if(this.nodes[idx_leftSon].compareTo(this.nodes[idx_rightSon]) < 0) {
						son = idx_leftSon;
					} else son = idx_rightSon;	
				} else if (idx_leftSon <= this.noNodes) { //preverjamo ali levi sin obstaja
					son = idx_leftSon;
				} else son = this.noNodes + 1; //sin ne obstaja
			}
			
			//bivsi zadnji v kopici se premakne na nov polozaj
			this.nodes[newNode].value = this.nodes[this.noNodes].value;
			this.nodes[newNode].idx = newNode;
			this.noNodes --; //kopica se zmanjsa za 1 element
			return retValue;
		}
	}
	
	public void print_heap() {
		for(int i = 1; i <= this.noNodes; i++) {
			System.out.print(this.nodes[i].value + " ");
		}
		System.out.println();
	}
	
	/*
	 * zmanjsa prioriteto elementa x na x_new, zahtevnost log n
	 */
	public void decrease_key(HeapNode x, Comparable x_new) {
		HeapNode temp = new HeapNode(x_new);
		//x.value = x_new;
			
		int cur_idx = x.idx;
		int parent = cur_idx / 2; //zracunamo ko je oce x-a
		
		
		//dokler je oce vecji od sina, zamenjamo jih
		while(parent > 0 && nodes[parent].compareTo(temp) > 0) {
			nodes[cur_idx].value = nodes[parent].value;
			nodes[cur_idx].idx = cur_idx;
				
			cur_idx = parent;
			parent = parent / 2;
		}
		
		//element vpise sele ko poznamo koncan polozaj
		nodes[cur_idx].value = x_new;
		nodes[cur_idx].idx = cur_idx;
	}
	
	
	public void decrease() {
		this.decrease_key(this.nodes[this.noNodes], 15);
		this.print_heap();
		this.decrease_key(this.nodes[this.noNodes], 1);
		this.print_heap();
	}
}


