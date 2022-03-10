/*
	Binarno iskalno drevo (Binary search tree) 
	je najbolj preprosta drevesna implementacija slovarja.
	
	
	SLOVAR - DICTIONARY - poseben primer ADT Set, ki omogoca le:
	-- vstavljanje elementa
	-- brisanje elementa
	-- iskanje elementa -- za ucinkovitost, potrebujemo UREJENOST
	Urejenost je definirana bodisi na elementih samih, bodisi na 
	delih elemntov - kljucih (keys)
	
	Omogoca ucinkovitost iskanja min, max, 
	predhodnika, naslednika in izpisa na intervalu - O(log n) 

	Drevo z oznako korena x in levim in desnim poddrevesom L in R
	JE BST, ce velja:
	-- za VSAKO y iz L, y < x
	-- za VSAKO y iz R, y > x
	
	BST je definirano z referenco na koren BST.
 */
import java.util.*;
class BSTreeNode {
	Comparable key;
	BSTreeNode left, right;
	int diff;
	
	public void makenull() {
		this.key = null;
		this.left = null;
		this.right = null;
	}
	
	public BSTreeNode() {
		this.makenull();
	}
	
	public BSTreeNode(Comparable key) {
		this(key, null, null);
	}
	
	public BSTreeNode(Comparable key, BSTreeNode left, BSTreeNode right) {
		this.key = key;
		this.left = left;
		this.right = right;
	}
}
class StackElement {
	public BSTreeNode node;
	public int visina, address;
	public StackElement left, right;
	public int sum;
	
	public StackElement(StackElement e){
		this.node = e.node;
		this.visina = e.visina;
		this.address = e.address;
		this.sum = e.sum;
	}
	
	public StackElement() {
		
	}
}

public class BSTree {
	
	BSTreeNode root;
	
	public BSTree() {
		this.makenull();
	}
	
	public BSTree(Comparable key) {
		this.root = new BSTreeNode(key, null, null);
	}
	
	public void makenull() {
		this.root = null;
	}
	
	/// HEIGHT /// /// /// /// 
	
	public int height() {
		return this.height(this.root);
	}
	
	private int height(BSTreeNode node) {
		if(node == null)
			return 0;
		else return 1 + Math.max(this.height(node.right), this.height(node.left));
	}
	
	/// NUMBER OF ELEMS /// /// /// ///
	
	public int num_elems() {
		return this.num_elems(this.root);
	}
	
	private int num_elems(BSTreeNode node) {
		if(node == null)
			return 0;
		
		return this.num_elems(node.left) + this.num_elems(node.right);
	}
	
	/// BALANCED /// /// /// /// /// ///
	/*
	Binarno iskalno drevo je (delno) uravnotezeno, ce ZA VSAKO vozlisce velja, 
	da se visini obeh poddreves razlikujeta najvec za 1
	 */
	public boolean isBalanced() {
		return this.isBalanced(this.root);
	}
	
	public boolean isBalanced(BSTreeNode node) {
		if(node == null)
			return true;
		
		if(Math.abs(this.height(node.left) - this.height(node.right)) <= 1) {
			return this.isBalanced(node.left) && this.isBalanced(node.right); 
		} else return false;
	}
	
	/// MEMBER / LOCATE ///	///	///	////
	/*
		Funkcija, ki preverja, ali element z kljucem x 
		obstaja v drevesu. 
		Maksimalno stevilo korakov je enako visini drevesa.
	 */
	public boolean member(Object x) {
		return this.member((Comparable) x, this.root);
	}
	
	private boolean member(Comparable x, BSTreeNode node) {
		if(node == null)
			return false;
		
		if(x.compareTo(node.key) == 0)
			return true;
		else if(x.compareTo(node.key) < 0)
			return this.member(x, node.left);
		else return this.member(x, node.right);
	}
	
	/// /// ///
	
	/*
	Funkcija, ki vrne vozlisce, z datim kljucem.
	Maksimalno stevilo korakov je enako visini drevesa.
	 */
	public BSTreeNode locate(Object x) {
		return this.locate((Comparable) x, this.root);
	}

	private BSTreeNode locate(Comparable x, BSTreeNode node) {
		if(node == null)
			return null;
	
		if(x.compareTo(node.key) == 0)
			return node;
		else if(x.compareTo(node.key) < 0)
			return this.locate(x, node.left);
		else return this.locate(x, node.right);
	}
	
	/// INSERT ///	///	///	////
	
	// INSERT LEAF - vedno lahko dodamo element, kot list
	
	public void insertLeaf(Object x) {
		this.root = this.insertLeaf((Comparable) x, this.root);
	}
	
	
	// Ohranjamo vse pozicije pri vstavljanju in vrnemo koren. 
	private BSTreeNode insertLeaf(Comparable x, BSTreeNode node) {
		if(node == null) //prazno BST
			node = new BSTreeNode(x, null, null);
		else {
			if(x.compareTo(node.key) < 0) {
				node.left = this.insertLeaf(x, node.left);
			} else if (x.compareTo(node.key) > 0){
				node.right = this.insertLeaf(x, node.right);
			}
			
			//ce je duplikat, ne naredimo nicesar
		}
		
		return node;
	}
	
	// INSERT ROOT - dodamo element kot koren drevesa
	
	public void insertRoot(Object x) {
		this.root = this.insertRoot((Comparable) x, this.root);
	}
	
	public BSTreeNode insertRoot(Comparable x, BSTreeNode node) {
		if(node == null) {
			node = new BSTreeNode(x, null, null);
		} else {
			if(x.compareTo(node.key) < 0) {
				node.left = this.insertRoot(x, node.left);
				node = this.rightRotation(node);
			} else if (x.compareTo(node.key) > 0){
				node.right = this.insertRoot(x, node.right);
				node = this.leftRotation(node);
			}
		}
		return node;
	}
		
	// ROTACIJE /// /// /// /// 

	public BSTreeNode rightRotation(BSTreeNode d) {
		BSTreeNode b = d.left;
		if(b != null && d != null) {
			d.left = b.right;
			b.right = d;
		}	
		return b; //novi koren
	}
	
	public BSTreeNode leftRotation(BSTreeNode b) {
		BSTreeNode d = b.right;
		if(b != null && d != null) {
			b.right = d.left;
			d.left = b;
		}	
		return d; //novi koren
	}
	
	/// /// /// /// /// /// 
	
	// DELETE /// /// /// ///
	/*
	Pri brisanju elementa iz BST je potrebno zbrisano vozlisce nadomestiti 
	bodisi z maksimalnim elementom levega poddrevesa bodisi z minimalnim
	elementom desnega poddrevesa
	*/
	
	public void delete(Comparable key) {
		this.root = this.delete(key, this.root);
	}
	
	/*
	Funkcija mora da vraca root, da bi se promena odrazila na pravo drevo.
	Drugacije ne bi moglo da se poveze drvo nazad, jer nije povezano u oba smera.
	 */
	public BSTreeNode delete(Comparable key, BSTreeNode root) {
		if(root == null)
			return null;
		
		if(root.key.compareTo(key) == 0) {
			if(root.left == null) {
				root = root.right;
			} else if (root.right == null) {
				root = root.left;
			} else {
				/* 
				NE RADI - ne otkazujemo povezavu do minNode
				BSTreeNode minNode = this.min(root.right);
				root.key = minNode.key;		
				minNode = null;
				*/
				root.key = this.min(root.right).key;
				/*
				Brisemo podvojen element v drevesu, to je element
				ki ima enak key kot root in se nahaja v desnem drevesu.
				*/
				root.right = this.delete(root.key, root.right);
			}
			
		} else if (key.compareTo(root.key) < 0) {
			root.left = this.delete(key, root.left);
		} else if (key.compareTo(root.key) > 0) {
			root.right = this.delete(key, root.right); 
		}
		
		
		return root;
	}
	
	
	/// PRUNE /// /// /// /// /// ///
	// ukloni sve listove drveta
	public void prune() {
		if(this.root != null) {
			if(isLeaf(this.root)) {
				this.root = null;
			} else {
				this.root.left = this.prune(this.root.left);
				this.root.right = this.prune(this.root.right);
				/*
			 	Moramo ovako, da bi se izbrisale povezave sa listovima.
			 	Ne korisititi i private void funkciju!
				 */
			}
		}
	}
	
	private boolean isLeaf(BSTreeNode node) {
		return node.left == null && node.right == null;
	}
	
	private BSTreeNode prune(BSTreeNode node) {
		//preverjamo ali je levi otrok list
		if(node.left != null) {
			if(isLeaf(node.left)) {
				node.left = null;
			} else {
				node.left = this.prune(node.left);
			}
		}
		
		//preverjamo ali je desni otrok list
		if(node.right != null) {
			if(isLeaf(node.right)) {
				node.right = null;
			} else {
				node.right = this.prune(node.right);
			}
		}
		return node;
	}

	
	/// MIN / MAX /// /// /// /// /// 
	/*
	Najdemo minimalni element v desnem poddrevesu - to je najleviji list.
	Node ovde je zapravo prvi desni od orignalnog.
	 */
	public BSTreeNode min(BSTreeNode node) {
		BSTreeNode iter = node.left;
		
		if(iter == null)
			return node;
		
		while(iter.left != null) {
			iter = iter.left;
		}
				
		return iter;
	}
	
	public BSTreeNode max(BSTreeNode node) {
		BSTreeNode iter = node.right;
		
		if(iter == null)
			return node;
		
		while(iter.right != null) {
			iter = iter.right;
		}
				
		return iter;
	}
	
	// PRETHODNIK / NASLEDNIK /// /// /// ///
	
	/*
	Vrne kazalec na element drevesa s prvo 
	manjso vrednostjo od podanega kljuca
	 */
	public BSTreeNode predecessor(Comparable key) {
		BSTreeNode resNode = null;
		BSTreeNode curNode = this.root;
		
		while(curNode != null) {
			if (key.compareTo(curNode.key) > 0) {
				/*
				Key je vecji od curNode.key, vzamemo kot moznost
				resNode in gremo skozi desno drevo, kjer so elementi manji od curNode.
				 */
				resNode = curNode;
				curNode = curNode.right;
			}
			else if (key.compareTo(curNode.key) <= 0) {
				/*
				Key je manjsi ALI ENAK od curNode.key, NE vzamemo kot moznost,
				gremo skozi levo drevo.
				 */
				curNode = curNode.left;
			}	
		}
		
		return resNode;
	}
	
	/*
	Vrne kazalec na element drevesa s prvo 
	vecjo vrednostjo od podanega kljuca
	*/
	
	public BSTreeNode successor (Comparable key) {
		BSTreeNode resNode = null;
		BSTreeNode curNode = this.root;
		
		while(curNode != null) {
			if(key.compareTo(curNode.key) >= 0) {
				/*
				Key je vecji ALI ENAK kot curNode.key, NE vzamemo curNode kot moznost.
				Gremo pa skozi desno poddrevo, kjer so elementi vecji od curNode.key.
				 */
				curNode = curNode.right;
			}else if(key.compareTo(curNode.key) < 0) {
				/*
				Key je manjsi ALI ENAK kot curNode.key, vzamemo curNode kot moznost za resNode.
				Gremo skozi levo poddrevo, kjer so elementi manjsi od curNode.key
				 */
				resNode = curNode;
				curNode = curNode.left;
			}	
		}
		return resNode;
	}
	
	
	/// /// /// /// /// /// 
	
	// Funkcija za izpis drevesa
	public void printTree() {
		this.printTree(root);
		System.out.println();
	}
	

	// Rekurzivna funkcija za izpis poddrevesa s podanim korenom
	private void printTree(BSTreeNode node) {
		if (node != null) {
			System.out.print("(");
			printTree(node.left);
			System.out.print(", " + node.key + ", ");
			printTree(node.right);
			System.out.print(")");
		}
		else
			System.out.print("null");
	}
	
	/// /// ///
	
	public void printDesc() {
		this.printDesc(this.root);
		Queue q = new Queue();
		this.printDesc(this.root, q);
		while(!q.empty()) {
			System.out.print(q.front() + " ");
			q.dequeue();
		}
		System.out.println();
	}
	
	//Izpisujemo pocev od maksimalnog vozlisca, prethodnike jedan za drugim
	private void printDesc(BSTreeNode node) {
		for(BSTreeNode iter = this.max(node); iter != null; iter = this.predecessor(iter.key)) {
			System.out.print(iter.key + " ");
		}
		System.out.println();
	}
	
	public void printDesc(BSTreeNode node, Queue q) {
		if(node == null)
			return;
		
		printDesc(node.right, q);	
		q.enqueue(node.key);
		printDesc(node.left, q);
	}
	
	public int visina_iter (){
		Stack<StackElement> s = new Stack<>();
		StackElement e = new StackElement();
			
		e.visina = 0;
		e.node = this.root;
		e.address = 0;
	
		s.push(new StackElement(e));
		int result = 0;
		do {
			e = s.peek();
			s.pop();
				
			switch(e.address) {
			case 0:
				if(e.node == null){
					e.visina = 0;
					result = 0;
				} else {
					// za povratak iz rekurzija sa n - 1
					e.address = 1;
					s.push(new StackElement(e));
					
					// rekurzija sa node.left
					e.visina = 0; e.node = e.node.left; e.address = 0; 
					s.push(new StackElement(e));
				}
					
			break;
	
			case 1: 
				e.visina = result;
				
				e.address = 2;
				s.push(new StackElement(e));
				
				e.visina = 0; e.node = e.node.right; e.address = 0;
				s.push(new StackElement(e));

				break;
				
			case 2:
				//povratak iz rekurzije sa node.left i node.right
				e.visina = 1 + Math.max(result, e.visina);
				result = e.visina;
				break;
			}
				
		} while(!s.empty());
				
		return e.visina;
	}

public int sum_iter (){
	Stack<StackElement> s = new Stack<>();
	StackElement e = new StackElement();
			
	e.sum = 0;
	e.node = this.root;
	e.address = 0;
	
	s.push(new StackElement(e));
	int result = 0;
	
	do {
		e = s.peek();
		s.pop();
				
		switch(e.address) {
		case 0:
			if(e.node == null){
				e.sum = 0;
				result = 0;
			} else {
				// za povratak iz rekurzija sa n - 1
				e.address = 1;
				s.push(new StackElement(e));
					
				// rekurzija sa node.left
				e.sum = 0; e.node = e.node.left; e.address = 0; 
				s.push(new StackElement(e));
			}
					
			break;
	
		case 1: 
			e.sum = result;	e.address = 2;
			s.push(new StackElement(e));
				
			e.sum = 0; e.node = e.node.right; e.address = 0;
			s.push(new StackElement(e));

			break;
				
		case 2:
			//povratak iz rekurzije sa node.left i node.right
			e.node.diff = result - e.sum;
			
			e.sum = (int) e.node.key + result + e.sum;
			result = e.sum;
			break;
		}
		
		
	} while(!s.empty());
	
	return e.sum;
 }
}