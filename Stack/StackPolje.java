/*
 	IMPLEMENTACIJA STACK-a S POLJEM (tabelo)
 	
 	Stack je dolocen z indeksom top elementa.
 	
 	Push in Pop realiziramo z dodajanjem / brisanjem 
 	zadnjega elementa v tabeli. 
 	-- Pri push treba preveriti, ali se obstaja prostora
	
	Ce v stack-u nimamo elementov, potem je this.top = -1 
 */

public class StackPolje<T> {

    private static final int DEFAULT_LENGTH = 100;

    private T[] elements;
    // dejansko stevilo elementov (<= elementi.length)
    private int top; //indeks vrha stack-a

    public StackPolje() {
        this(DEFAULT_LENGTH);
    }
    
    public void makenull() {
    	this.top = -1;
    }
    
    public boolean empty(){
		return this.top == -1;
	}

    @SuppressWarnings("unchecked")     // utisamo prevajalnik
    public StackPolje(int num_elems) {
        // this.elements = new T[num_elems];  --> napaka pri prevajanju
        this.elements = (T[]) new Object[num_elems];   
        this.makenull();
    }
    
    ///
    
    public T top() {
    	if(!this.empty()) {
    		return this.elements[this.top];
    	}
    	return null;
    }
    
    /// 
    
    public void push(T x) {
    	this.top ++;
    	if(this.top >= this.elements.length) {
    		System.out.println("No more place on stack!");
    		return;
    	}
    	
    	this.elements[this.top] = x;
    }
    
    public void pop() {    	
    	if(!this.empty()) {
    		this.top --;
    	}
    }
    
    ///
}
