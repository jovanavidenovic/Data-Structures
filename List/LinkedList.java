/*
	DVOSMERNI SEZNAM S KAZALCI
	
 	List je dolocen z prvim in zadnjim elementom, 
 	elementi se lahko ponavljajo in je vrstni red pomemben
 	
 	Polozaj elementa v linkedlist je podan
 	s kazalcem na celico, ki ta element vsebuje
 	Ne uporabljamo header list-a (glavo seznama), niti zamaknjenega polozaja.
 	
 	Ce je list prazen, potem je this.first = null && this.last = null;
 	Ce list vsebuje le 1 element, potem je this.last == this.first
 	
 	List zaseda le toliko pomnilnika, kolikor res potrebuje
 	Casovna kompleksnost vseh osnovnih funkcij, razen PRINTLIST() je O(1)
 	
 	Pomaknjiljivosti:
 	-- Vecja zasedenost pomnilnika
 */

public class LinkedList {
	
	public static class LinkedListElement {
		Object element;
		LinkedListElement previous;
		LinkedListElement next;
		
		public LinkedListElement() {
			this.element = null;
			this.previous = null;
			this.next = null;
		}
		
		public LinkedListElement(Object element, LinkedListElement previous, LinkedListElement next) {
			this.element = element;
			this.previous = previous;
			this.next = next;	
		}
	}
	
	protected LinkedListElement first, last;
	
	public LinkedList() {
		makenull();
	}
	
	public void makenull() {
		this.first = null;
		this.last = null;
	}
	
	public LinkedListElement first() {
		return this.first;
	}
	
	public LinkedListElement last() {
		return this.last;
	}
	
	///
	
	public LinkedListElement next (LinkedListElement pos) {
		if(pos == null)
			return null;
		
		return pos.next;
	}

	public LinkedListElement previous(LinkedListElement pos) {
		if(pos == null)
			return null;
		
		return pos.previous;
	}
	
	public LinkedListElement end() {
		return this.next(this.last());
	}
	
	public boolean overEnd(LinkedListElement pos) {
		if(pos == this.end())
			return true;		
		else return false;
	}
	
	public boolean empty() {
		return this.last == null;
	}
	
	/// 
	//vrne element na poziciji pos (fizicki tudi pos!) v linkedlist-u
	public Object retrieve(LinkedListElement pos){
		if(pos == null)
			return null;
		
		return pos.element;
	}
	
	/*
    poisce polozaj elementa x v list-u. 
    Ce se v seznamu nahaja vec enakih elementov c, vrne polozaj prvega elementa. 
    Ce ni taksnega elementa, vrne null.
	-- zahtevnost O(n)
	 */
	public LinkedListElement locate(Object x) {
		for(LinkedListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			if(x.equals(this.retrieve(iter))) {
				return iter;
			} 
		}
		return null;
	}
	
	//enako kot locate, ampak zacne z iskanjem od podane pozicije
	public LinkedListElement findDuplicates(Object x, LinkedListElement pos) {	
		for(LinkedListElement iter = this.next(pos); !this.overEnd(iter); iter = this.next(iter)) {
			if(x.equals(this.retrieve(iter))) {
				return iter;
			} 
		}
		return null;
	}
	
	///
	
	public void insert(Object x, LinkedListElement pos) {
		if(this.first == null) {
			this.first = new LinkedListElement(x, null, null);
			this.last = this.first;
		} else if(pos == null) {
			LinkedListElement newEl = new LinkedListElement(x, null, this.first);
			this.first.previous = newEl;
			this.first = newEl;
		} else {
			LinkedListElement newEl = new LinkedListElement(x, pos, pos.next);
			
			if(pos.next != null)
				pos.next.previous = newEl;
			
			pos.next = newEl;
				
			if (this.last == pos) //ce smo dodali novi zadnji element
				this.last = newEl;
		}
	}
	
	public void addFirst(Object x) {
		insert(x, null);
	}
	
	public void addLast(Object x) {
		if(this.last == null) {
			insert(x, null);
		} else insert(x, this.last); 
	}	
	
	public void insertNth(Object x, int n) {
		if(n == 0) {
			this.addFirst(x);
			return;
		}
		
		LinkedListElement iter = this.first;
	
		for (int i = 1; i < n; i++){
			iter = next(iter);
			if (iter == null) 
				return;
		}
		
		insert(x, iter);
	}
	
	///
	
	public void delete(LinkedListElement pos) {
		if(pos == this.first) {
			this.first = this.first.next;
			this.first.previous = null;
		} else 
			if(pos == this.last) {
				this.last = this.last.previous;
				this.last.next = null;
			} else {
				LinkedListElement prethodnik = pos.previous;
				LinkedListElement naslednik = pos.next;
				
				if(prethodnik != null)
					prethodnik.next = naslednik;
				
				if(naslednik != null)
					naslednik.previous = prethodnik;
			}
	}
	
	public void deleteNth(int n) {
		LinkedListElement iter = this.first;
		for (int i = 0; i < n; i++){
			iter = next(iter);
			if (iter == null)
				return;
		}	
		delete(iter);
	}
	
	///
	
	public int length() {
		int length = 0;
		
		for(LinkedListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)){
			length++;
		}	
		return length;
	}
	
	
	public int lengthRek() {
		return lengthRek(this.first);
	}
	
	public int lengthRek(LinkedListElement pos) {
		if(pos != null) {
			return 1 + lengthRek(this.next(pos));
		} else return 0;		
	}
	
	///
	
	public void write() {
		for(LinkedListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			System.out.print(this.retrieve(iter));
			if(iter != this.last)
				System.out.print(", ");
		}
		System.out.println();
	}
	
	public void writeRev() {
		for(LinkedListElement iter = this.last(); iter != null; iter = this.previous(iter)) {
			System.out.print(this.retrieve(iter));
			if(iter != this.first)
				System.out.print(", ");
		}
		System.out.println();
	}
	
	///
	
	public void reverse() {
		LinkedListElement zacetni = this.first();
		LinkedListElement krajnji = this.last();
		int n = this.length();
		for(int i = 0; i < n / 2; i ++) {
			Object zacetniElement = this.retrieve(zacetni);
			Object krajnjiElement = this.retrieve(krajnji);
			
			this.insertNth(krajnjiElement, i);
			this.deleteNth(i + 1);
			this.insertNth(zacetniElement, n - i);
			this.deleteNth(n - i - 1);

			zacetni = zacetni.next;
			krajnji = krajnji.previous;
		}
	}
	
	//todo: reverseRek
	public void reverseRek() {
		this.reverseRek(this.first.next);
	}
	
	private void reverseRek(LinkedListElement el) {
		if (el == null)
			return;
		
		if (el.next == null){
			this.first.next = el;
			this.last = this.first;
		} else {
			reverseRek(el.next);
			el.next = null;
			this.last = this.last.next;
			this.last.next = el;
		}
	}
	
	///	
	/*
	funkcija gre skozi vse clene (dinamicki, preko kazalcev)
	in za vsaki clen izbacuje vse duplikate, (ce) ki jih najde
	 */
	
	public void removeDuplicates() {
		for(LinkedListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			while(true) {
				LinkedListElement mybDuplicate = this.findDuplicates(this.retrieve(iter), iter);
				if(mybDuplicate != null) {
					this.delete(mybDuplicate);
				} else break;
			}
		}
	}
	
	///
	
	public void concatenate(LinkedList l1) {
		this.last.next = l1.first;
		l1.first.previous = this.last;
		this.last = l1.last;
	}
	
}
