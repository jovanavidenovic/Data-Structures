/*
	ENOSMERNI SEZNAM S KAZALCI
	
 	List je dolocen z prvim in zadnjim elementom, 
 	elementi se lahko ponavljajo in je vrstni red pomemben
 	
 	Polozaj elementa list-a je podan s kazalcem na celico,
 	ki vsebuje kazalec (next) na celico z elementom
 	-- narejeno zaradi ucinkovitosti vstavljanja in brisanja
 	
 	Ce je list prazen, potem je this.last = null;
 	Ce list vsebuje le 1 element, potem je this.last == this.first
 		-- oba kazeta na glavo seznama
 	
 	List zaseda le toliko pomnilnika, kolikor res potrebuje	
 		
 	Pomaknjiljivosti:
 	-- Linearna casovna kompleksnost PREVIOUS, LOCATE, WRITE in DELETE
 		-- DELETE ima linearno, le ko brisemo zadnji element
 */

class ListElement {
		Object element;
		ListElement next;
		
		public ListElement() {
			this.element = null;
			this.next = null;
		}
		
		public ListElement(Object element, ListElement next) {
			this.element = element;
			this.next = next;	
	}
}

public class List {
	
	protected ListElement first, last;
	
	public List() {
		makenull();
	}
	
	public void makenull() {
		this.first = new ListElement();
		this.last = null;
	}
	
	public ListElement first() {
		return this.first;
	}
	
	public ListElement last() {
		return this.last;
	}
	
	///
	
	public ListElement next (ListElement pos) {
		if(pos == null)
			return null;
		
		return pos.next;
	}

	public ListElement previous(ListElement pos) {
		if(pos == this.first())
			return null;
		
		for(ListElement iter = this.first(); !overEnd(iter); iter = next(iter)) {
			if(iter.next == pos)
				return iter;
		}
		return null;
	}
	
	public ListElement end() {
		return this.next(this.last());
	}
	
	public boolean overEnd(ListElement pos) {
		if(pos == this.end())
			return true;
		else return false;
	}
	
	public boolean empty() {
		return this.last == null;
	}
	
	/// 
	//vrne element na poziciji pos (fizicki pos + 1) v list-u
	public Object retrieve(ListElement pos){
		if(pos != null && this.next(pos) != null) {
			return pos.next.element;
		} else return null;	
	}
	
	/*
    poisce polozaj elementa x v list-u. 
    Ce se v seznamu nahaja vec enakih elementov c, vrne polozaj prvega elementa. 
    Ce ni taksnega elementa, vrne null.
	-- zahtevnost O(n)
	 */
	public ListElement locate(Object x) {
		for(ListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			if(x.equals(this.retrieve(iter))) {
				return iter;
			} 
		}
		return null;
	}
	
	//enako kot locate, ampak zacne z iskanjem od podane pozicije
	public ListElement findDuplicates(Object x, ListElement pos) {	
		for(ListElement iter = this.next(pos); !this.overEnd(iter); iter = this.next(iter)) {
			if(x.equals(this.retrieve(iter))) {
				return iter;
			} 
		}
		return null;
	}
	
	///
	
	public void insert(Object x, ListElement pos) {		
		ListElement newEl = new ListElement(x, pos.next);
		pos.next = newEl;
			
		if (this.last == null) {//ce smo dodali edini element
			this.last = this.first;
		} else if (this.last == pos || this.last.next == pos) //ce smo dodali predzadnji ali zadnji element 
			this.last = this.last.next;
		
		/*
		else if (this.last.next == pos) //ce smo dodali zadnji element
			this.last = pos;
		*/
	}
	
	public void addFirst(Object x) {
		insert(x, this.first());
	}
	
	public void addLast(Object x) {
		if(this.last == null) {
			insert(x, this.first);
		} else insert(x, this.last.next);
		// pozor! dodajemo na this.last.next, ne na this.last 
	}	
	
	public void insertNth(Object x, int n) {
		ListElement iter = this.first;
	
		for (int i = 0; i < n; i++){
			iter = next(iter);
			if (iter == null)
				return;
		}
		
		insert(x, iter);
	}
	
	///
	
	public void delete(ListElement pos) {
		
		if(!this.empty()) {
			if(pos == this.last) {
				if(this.last == this.first) {
					this.makenull();
				} else {
					this.last.next = null;
					this.last = this.previous(this.last);
				}
			} else if(pos.next == this.last) { //brisemo predzadnjeg
				pos.next = this.next(this.next(pos));
				this.last = pos;
			} else pos.next = this.next(this.next(pos));
		}
		
		
		/*
		//cuvamo, ce bo potrebno kasneje da zamenjamo this.last
		ListElement temp = this.next(pos); 
		pos.next = this.next(this.next(pos));
		
		// brisemo poslednji element - kompleksnost O(n)
		if(pos == this.last()) {
			if(pos == this.first()) //lista je prazna
				this.last = null; 
			else this.last = this.previous(pos);	
		} else if(temp == this.last) { 
			this.last = pos;	//obrisali predzadnjeg
		}	
		*/
	}
	
	public void deleteNth(int n) {
		ListElement iter = this.first;
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
		
		for(ListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)){
			length++;
		}	
		return length;
	}
	
	
	public int lengthRek() {
		return lengthRek(this.first.next);
	}
	
	public int lengthRek(ListElement pos) {
		if(pos != null) {
			return 1 + lengthRek(this.next(pos));
		} else return 0;		
	}
	
	///
	
	public void write() {
		for(ListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			System.out.print(this.retrieve(iter));
			if(iter != this.last)
				System.out.print(", ");
		}
		System.out.println();
	}
	
	///
	
	public void reverse() {
		ListElement zacetni = this.first();
		ListElement krajnji = this.last();
		int n = this.length();
		for(int i = 0; i < n / 2; i ++) {
			Object zacetniElement = this.retrieve(zacetni);
			Object krajnjiElement = this.retrieve(krajnji);
			
			this.insertNth(krajnjiElement, i);
			this.deleteNth(i + 1);
			this.insertNth(zacetniElement, n - i);
			this.deleteNth(n - i - 1);
			
			zacetni = zacetni.next;
			krajnji = this.previous(krajnji);
		}
	}

	public void reverseRek() {
		this.reverseRek(this.first.next);
	}
	
	private void reverseRek(ListElement el) {
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
		for(ListElement iter = this.first(); !this.overEnd(iter); iter = this.next(iter)) {
			while(true) {
				ListElement mybDuplicate = this.findDuplicates(this.retrieve(iter), iter);
				if(mybDuplicate != null) {
					this.delete(mybDuplicate);
				} else break;
			}
		}
	}
	
	///
	
	public void concatenate(List l1) {
		/*
		 pozor! povezujemo this.end(), ne this.last
		 this.last je fizicki predzadnji element
		 
		 povezujemo z l1.first.next, ker nam ni treba 
		 glava lista l1
		 */
		
		this.end().next = l1.first.next;
		this.last = l1.last;
	}
	
}
