/*
	STACK - IMPLEMENTACIJA S KAZALCI
	
	Stack je seznam, kjer se elementi vedno:
	-- dodajo na zacetku seznama
	-- brisejo na zacetku seznama
 	--> dela po sistemu LIFO
 	
 	Stack je dolocen z elementom na vrhu sklada.
 	Vse operacije so konstantne kompleksnosti.
 
 	this.top.next = this.(top - 1) -- povezani "na dole"
 */


class StackElement{
	
	Object element;
	StackElement next;
	
	public StackElement() {
		this.element = null;
		this.next = null;
	}
	
	public StackElement(Object element, StackElement next) {
		this.element = element;
		this.next = next;
	}
}

public class Stack {
	
	StackElement top;
	
	public Stack() {
		this.makenull();
	}
	
	public void makenull() {
		this.top = null;
	}
	
	public boolean empty() {
		return this.top == null;
	}
	
	///
	
	//vrne vrhnji element sklada
	public Object top() {
		if(!this.empty())
			return this.top.element;
		else return null;
	}
	
	//vstavi element x na vrh sklada
	public void push(Object x) {
		StackElement newEl = new StackElement(x, null);
		if(this.empty()) {
			this.top = newEl;
		} else {
			newEl.next = this.top;
			this.top = newEl;
		}
	}
	
	//zbrise vrhnji element iz sklada
	public void pop() {
		if(!this.empty()) {
			this.top = this.top.next;
		}
	}
}
