/* 
   IMPLEMENTACIJA SEZNAMA Z TABELOM (poljem)

   Objekt tega razreda predstavlja vektor z elementi tipa Object.  
   Vektor je vsebovalnik, ki se obnasa kot "raztegljiva" tabela.
   
   Seznam je podan s poljem elementov danega tipa
   in z idenksom zadnjega elementa v seznamu
   Ce je seznam prazen je lastElement = -1

   Implementacija je povoljna, ko se list zelo malo spreminja, 
   ali spreminja samo na koncu --> sklad.
   
   Pomaknjljivosti:
   -- Dolzina seznama je omejena, funkcija je podaljsanja je z linearno zahtevnostjo
   -- Linerna zahtevnost insert in delete
   -- Polje zavzima ves cas konstantno kolicno pomnilnika
 */

import java.util.*;
import java.lang.*;

public class ListPolje<T> implements Iterable<T> {

    private static final int DEFAULT_LENGTH = 100;

    private T[] elements;
    // dejansko stevilo elementov (<= elementi.length)
    private int lastElement;

    public ListPolje() {
        this(DEFAULT_LENGTH);
    }
    
    public void makenull() {
    	this.lastElement = -1;
    }
 
    @SuppressWarnings("unchecked")     // utisamo prevajalnik
    public ListPolje(int num_elems) {
        // this.elements = new T[num_elems];  --> napaka pri prevajanju
        this.elements = (T[]) new Object[num_elems];   
        this.makenull();
    }
    
    ///
    
    //vrnemo POZICIJO! prvega elementa
    public Integer first() {
    	return 0;
    }
    
    public Integer last() {
    	return lastElement;
    }
    
    public Integer end() {
    	return lastElement + 1;
    }
    
    public boolean overEnd(Integer pos) {
    	if(pos >= this.end())
    		return true;
    	else return false;
    }
    
    
    //vrnemo POZICIJO! naslednjega elementa
    public Integer next(Integer pos) {
    	if(pos.intValue() > lastElement) {
    		return null;
    	} else return pos.intValue() + 1;
    }
    
    public Integer previous(Integer pos) {
    	if(pos.intValue() < 1) {
    		return null;
    	} else return pos.intValue() - 1;
    }
    
    public T retrieve(Integer pos) {
    	if(pos.intValue() < 0)
    		return null;
    	return this.elements[pos.intValue()];
    }
    
    public boolean empty() {
    	if(lastElement == -1)
    		return true;
    	else return false;
    }
    
    /// 
    
    public void insert(T x, Integer pos) {
    	this.lastElement ++;
    	int i = this.lastElement;
    	
    	this.ifNeededIncrease();
    	
    	while(i > pos.intValue()) {
    		elements[i] = elements[i - 1];
    		i--;
    	}
    	elements[i] = x;
    }
    
    ///
    
    public void delete(Integer pos) {    	
    	for(int i = pos.intValue(); i < lastElement; i++) {
    		elements[i] = elements[i + 1];
    	}
    	this.lastElement--;
    }

    //Ce je vektor <this> poln, poveca njegovo kapaciteto za faktor 2.
    @SuppressWarnings("unchecked")
    private void ifNeededIncrease() {
        if (this.lastElement >= this.elements.length) {
            // ustvari novo, vecjo tabelo in vanjo skopiraj elemente iz stare tabele
            T[] oldElements = this.elements;
            this.elements = (T[]) new Object[2 * oldElements.length];
            for (int i = 0;  i < this.lastElement;  i++) {
                this.elements[i] = oldElements[i];
            }
        }
    }

 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0;  i < this.lastElement;  i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }
	
	/*
	implementacija metode v vmesniku Iterable - metoda vrne iterator, 
	s pomocjo katerega se bomo lahko zaporedno sprehodili po vektorju this
	 */
	@Override
	public Iterator<T> iterator(){
		return new IteratorCezVektor<T>(this);
		//moramo mu dati objekt po kome ce se sprehajati
	}
	
	
	private static class IteratorCezVektor<T> implements Iterator<T>{
		//lahko uporabljamo privatne atribute od vektor jer je notranji razred
		
		private ListPolje<T> vektor; //vektor, po katerem se bomo sprehajali
		private int indeks; 
		
		public IteratorCezVektor(ListPolje <T> vektor){
			this.vektor = vektor;
			this.indeks = 0;
		}
		
		@Override
		public boolean hasNext(){
			return (this.indeks < this.vektor.last());
		}
		
		@Override
		public T next(){
			if(this.hasNext()){
				return this.vektor.retrieve(this.indeks++);
			}else throw new NoSuchElementException();
			
		}
	}	
}
