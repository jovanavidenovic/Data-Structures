/*
	IMPLEMENTACIJA MNOZICE Z ENOSMERNIMI KAZALCI
	
	Mnozica je dolocena z prvim elementom mnozice
	Vrstni red ni pomemben in nima duplikata
	
	Ohranimo polozaj elementov, radi lazje implementacije funkcij
	Polozaj elementov je zamaknjen 
	
	Pomanjkljivosti:
	-- 
 */

class SetElement {
	Object element;
	SetElement next;
		
	public SetElement() {
		this.element = null;
		this.next = null;
	}
		
	public SetElement(Object element, SetElement next) {
		this.element = element;
		this.next = next;	
	}		
}


public class Set {
	
	protected SetElement first;
	
	public Set() {
		makenull();
	}
	
	public void makenull() {
		this.first = new SetElement();
	}
	
	public SetElement first() {
		return this.first;
	}

	///
	//ko uporabljamo tisto funkcijo, nam ne bo vrnil napako
	//ce je pos = null -- pri pos.next bo napaka
	public SetElement next(SetElement pos) {
		if(pos == null)
			return null;
		
		return pos.next;
	}
	
	public boolean overEnd(SetElement pos) {
		if(this.next(pos) == null)
			return true;
		else return false;
	}
	
	public boolean empty() {
		return this.first.next == null;
	}
	
	///
	
	public int size() {
		int size = 0;
		for(SetElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			size ++;
		}
		return size;
	}
	
	/// 
	
	//vrne element na poziciji pos (fizicki pos + 1) v list-u
	public Object retrieve(SetElement pos){
		if(pos != null && this.next(pos) != null) {
			return pos.next.element;
		} else return null;	
	}
	
	/*
    poisce polozaj elementa x v set-u. 
    Ce ni taksnega elementa, vrne null.
	-- zahtevnost O(n)
	 */
	public SetElement locate(Object x) {
		for(SetElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			if(x.equals(this.retrieve(iter))) {
				return iter;
			} 
		}
		return null;
	}
	
	///

	public void insert(Object x) {	
		
		if(this.locate(x) == null) {
			//vstavimo na zacetek set-a, ker nam vrstni red ni pomemban
			SetElement newEl = new SetElement(x, this.next(this.first));
			this.first.next = newEl;
		}
	}
	
	///
	
	public void delete(SetElement pos) {
		if(pos != null) {
			pos.next = this.next(this.next(pos));	
		}
	}
	
	///
	
	public void print() {
		System.out.print("{");
		for(SetElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			System.out.print(this.retrieve(iter));
			if(!this.overEnd(this.next(iter)))
				System.out.print(", ");
		}
		System.out.print("}");
		System.out.println();
	}
	
	public void union(Set s1) {
		for(SetElement iter = s1.first(); !s1.overEnd(iter); iter = s1.next(iter)) {
			this.insert(s1.retrieve(iter));
		}
	}
	
	public void copy(Set s1) {
		this.makenull();
		for(SetElement iter = s1.first(); !s1.overEnd(iter); iter = s1.next(iter)) {
			this.insert(s1.retrieve(iter));
		}
		
	}
	
	public void intersection(Set s1) {
	
		SetElement iter = this.first();
		while(!this.overEnd(iter)) {
			if(s1.locate(s1.retrieve(iter)) == null) {
				this.delete(iter);
			} else iter = this.next(iter);
			
			/*
			 pozor!! ker v funkciji delete brisemo element na poziciji pos
			 tj. vstavljamo element sa pozicije pos + 1, ni nam treba 
			 narediti iter = this.next(iter)
			*/
		}
	}
	
	/*
	mnozice su jednake, ako imaju isti broj elemenata i iste elemente, 
	neodvisno od rasporeda elemenata
	 */
	@Override
	public boolean equals(Object s1) {
		if(this == s1)
			return true;
		if(s1 instanceof Set) {
			Set ss1 = (Set) s1;
			if(ss1.size() != this.size())
				return false;
			
			for(SetElement iter = ss1.first(); !ss1.overEnd(iter); iter = ss1.next(iter)) {
				if(this.locate(ss1.retrieve(iter)) == null)
					return false;
			}
			return true;
		}
		return false;
	}

}
