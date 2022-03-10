/*
	VRSTA - IMPLEMENTACIJA S KAZALCI
 
 	Vrsta je seznam, kjer elemente vedno:
	-- dodajemo na konec seznama
 	-- brisemo na zacetku seznama
 	--> dela po sistemu FIFO (First In First Out)
 
 	Vrsta je dolocena z prvim (front) in zadnjim (rear) elementom vrste.
 	Polozaj NI zamaknjen.
 
 	Vse operacije so konstantne kompleksnosti.
 
 	this.front --> next ... --> next --> this.rear 
 */

class QueueElement {
	
	Object element;
	QueueElement next;
	
	public QueueElement() {
		this.element = null;
		this.next = null;
	}
	
	public QueueElement(Object element, QueueElement next) {
		this.element = element;
		this.next = next;
	}
	
}


public class Queue {
	
	QueueElement front;
	QueueElement rear;
	
	public Queue() {
		this.makenull();
	}
	
	public void makenull() {
		this.front = null;
		this.rear = null;
	}
	
	public boolean empty() {
		return this.front == null;
	}
	
	///
	
	//vrne prvi ELEMENT v vrsti
	public Object front() {
		if(!this.empty())
			return this.front.element;
		else return null;
	}
	
	//vstavi element x na konec vrste
	public void enqueue(Object x) {
		QueueElement newEl = new QueueElement(x, null);
		if(this.empty()) {
			this.front = newEl;
			this.rear = this.front;
		} else {
			this.rear.next = newEl;
			this.rear = this.rear.next;	
		}
	}
	
	//zbrise prvi element iz vrste
	public void dequeue() {
		if(!this.empty()) {
			if(this.rear == this.front) { 
				//zbrisali smo edini element, ki nam je ostal v vrsti
				this.makenull();
			} else this.front = this.front.next;
		}
	}
	
	///
}
