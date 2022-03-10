/*
	Binarno iskalno drevo za katerega velja:
	-- vsako vozlisce je bodisi rdece bodisi crne barve
	-- rdece vozlisce ima lahko samo crna sinova
	-- za vsako vozlisce velja, da vsaka pot od vozlisca do praznega poddrevesa (nil) 
	   vsebuje enako stevilo crnih vozlisc (crna visina je konstantna)
	
	--> VEDNO DELNO PORAVNANO - za vsako vozlisce velja da je razlika med visinami
								poddreves maksimalno 1
		Potrebuje maksimalno 3 rotacije da se drevo balansira.
								
	Visina RB drevesa z n vozlisci je maksimalno 2log2(n+1)
	
	Zahtevnost pri iskanju, dodajanju in brisanju je maksimalno O(log n)
 */

/*
	K obicajnemu BST vozliscu dodamo:
	-- kazalec na oceta
	-- podatek o barvi vozlisca
 */
class RBTreeNode extends BSTreeNode{
	RBTreeNode parent;
	int color;
	
	public RBTreeNode() {
		super(null, null, null);
		this.parent = null;
		this.color = 0;
	}
	
	public RBTreeNode(Comparable key) {
		super(key);
		this.parent = null;
		this.color = 0;
	}
	
	public RBTreeNode(Comparable key, RBTreeNode left, RBTreeNode right, RBTreeNode parent, int color) {
		super(key, left, right);
		this.parent = parent;
		this.color = color;
	}
}


public class RBTree extends BSTree{
	static final int RED = 1;
	static final int BLACK = 2;
	// this.root prevzema iz BSTree 
	private boolean leftDeleted;
	private RBTreeNode parDeleted;
		
	public RBTree() {
		this.makenull();
	}
	
	/*
 	Pri ustavljanju, dodamo novo vozlisce KOT LIST in ga povarbamo RDECE.

 --> Problematican slucaj je ce je OCE novega vozlisca RDECE.
 V tem primeru je deda novega vozlisca zagotovo CRN, ker drugace ne bi bil 
 izpolnjen pogoj da mora vsako rdece vozlisce imati crne sinove.
 
 	3 razlicne resitve:
 	-- ce dede ni - oce je koren drevesa, OCETA pofarbamo s CRNO
 	-- ce je brat 
 	  	
	*/
	
	/*
	    Pomocna funkcija za prvobitno vstavljanje r-a. 
		Vstavimo ga kot rdec list.
		Funkcija vrne tisti LIST, ki smo takoj vstavili.
		-- Razlika u odnosu na funkciju kod BST, tamo vraca svakako KOREN drevesa.
	*/
	
	public RBTreeNode rbInsertLeaf(Comparable x, RBTreeNode r) {
		if(r == null) { //prvo vstavljanje v drevo
			this.root = new RBTreeNode(x);
			return (RBTreeNode) this.root;
		} else if(x.compareTo(r.key) < 0) {
			//gledamo po levem poddrevesu, ker je x manjsi od korena
			
			if(r.left == null) { //prisli smo do mesta, kjer lahko vstavimo novo vozlisce
				r.left = new RBTreeNode(x);
				((RBTreeNode)r.left).parent = r;
				((RBTreeNode)r.left).color = RED;
				return (RBTreeNode) r.left;
			} else {
				return rbInsertLeaf(x, (RBTreeNode) r.left);	
			}	
		} else if (x.compareTo(r.key) > 0) {
			//gledamo po desnem poddrevesu, ker je x vecji od korena
			
			if(r.right == null) { //prisli smo do mesta, kjer lahko vstavimo novo vozlisce
				r.right = new RBTreeNode(x);
				((RBTreeNode)r.right).parent = r;
				((RBTreeNode)r.right).color = RED;
				return (RBTreeNode) r.right;	
			} else {
				return rbInsertLeaf(x, (RBTreeNode) r.right);
			}	
		} else return r; //element ze obstaja v drevesu
	}
	
	/*
		Najprej doda vozlisce tako da uporabi zgornjo funkcijo.
		Potem, po pravilih popravlja drevo.
	*/
	
	public void insert(Comparable x) {
		RBTreeNode newNode, parent, gParent, uncle;
		
		// spremenljivke, ki dolocata ali sta oce in stric rdeci
		boolean redParent = false;
		boolean redUncle = false;
		
		newNode = this.rbInsertLeaf(x, (RBTreeNode) this.root);
		parent = new RBTreeNode();
		gParent = new RBTreeNode();
		uncle = new RBTreeNode();
		
		do {
		// doloci oceta, dedu in strica ter ali sta oce ali stric rdeca
			parent = newNode.parent;
			if(parent != null) {
				gParent = parent.parent;
				redParent = (parent.color == RED);
				
				if(gParent != null) {
					if(gParent.left.equals(parent)) 
						uncle = (RBTreeNode) gParent.right;
					else uncle = (RBTreeNode) gParent.left;
					
					if(uncle != null) {
						redUncle = (uncle.color == RED);
					} else redUncle = false;		
				
				} else {
					uncle = null;
					redUncle = false;
				}
			} else {
				gParent = null;
				redParent = false;
			}
			
			if(redParent && redUncle) {
				//2. slucaj
				// vsi zagotovo obstajajo, ker ce ne potem ne bi bil izpoljnjen pogoj redParent && redUncle
				parent.color = BLACK;
				uncle.color = BLACK;
				gParent.color = RED;
				
				newNode = gParent;
				//celoten postopek se ponovi s dedom
			}
		} while(redParent && redUncle); //2. slucaj
		
		
		if(redParent) { //1. ali 3. slucaj
			if(gParent == null) { //1. slucaj
				parent.color = BLACK; 
				this.root = parent;
			} else { //3.slucaj
				if(parent.right == newNode && gParent.left == parent) {
					parent = (RBTreeNode) this.leftRotation(parent);
					gParent = (RBTreeNode) this.rightRotation(gParent);
				} else 
				if (parent.left == newNode && gParent.left == parent) {
					gParent = (RBTreeNode) this.rightRotation(gParent);
				} else 
				if (parent.left == newNode && gParent.right == parent) {
					parent = (RBTreeNode) this.rightRotation(parent);
					gParent = (RBTreeNode) this.leftRotation(gParent);
				} else 
				if (parent.right == newNode && gParent.right == parent) {
					gParent = (RBTreeNode) this.leftRotation(gParent);
				}
				//gParent je nov koren, povarbamo ga crno, sinova pa rdece
				gParent.color = BLACK;
				((RBTreeNode)gParent.left).color = RED;
				((RBTreeNode)gParent.right).color = RED;
				
				if(gParent.parent == null)
					this.root = gParent;
			}
		}
	}
	
	///	///	///	///	///	///	///	///	///	///	///	///	///	///	///	///	///	
	// BRISANJE ELEMENTOV
	
	//zbrisi in vrni minimalni element v danem poddrevesu
	private RBTreeNode rbDeleteMin(RBTreeNode r) {
		if(r.left != null) {
			return rbDeleteMin((RBTreeNode) r.left);
		} else {
			//ne obstaja levo poddrevo
			parDeleted = r.parent;
			leftDeleted = (r.parent.left == r);
			//leftDeleted = false, kada u originalnom desnom poddrevu nema elemenata sem korena
			
			RBTreeNode temp = r;
			r = (RBTreeNode) r.right;
			if(r != null) {
				r.parent = temp.parent;
			}
			if(parDeleted != null) {
				if(leftDeleted) 
					parDeleted.left = r;
				else parDeleted.right = r;
			}
			return temp;			
		}
	}
	
	/*
	 * funkcija za brisanje vozlisca z kjucem x
	 */
	private RBTreeNode rbDelete(Comparable x, RBTreeNode par, RBTreeNode r) {
		/*
		 * par - oce trenutnega vozlisca r
		 * parDeleted - oce zbrisanega vozlisca
		 * leftDeleted == true parDeleted.left je smer zbrisanega vozlisca
		 * leftDeleted == false parDeleted.right je smer zbrisanega vozlisca
		 */
		
		RBTreeNode temp;
		if(r == null) //brisanje iz praznega drevesa
			return null;
		
		if(r.key.compareTo(x) == 0) { //zbrisi r
			if(r.left == null || r.right == null) { //prevezemo r na neprazno poddrevo
				parDeleted = par;
				if(par != null) {
					leftDeleted = (par.left == r);
				} else leftDeleted = false;
				
				
				temp = r;
				if(r.left == null) {
					r = (RBTreeNode) r.right;
				} else r = (RBTreeNode) r.left;
			
				if(r != null)
					r.parent = par;
			
				if(par != null) {
					if(leftDeleted)
						par.left = r;
					else par.right = r;
				}
				
				return temp;
			} else { //nadomestimo z minimalnim elementom iz desnega poddrevesa
				temp = this.rbDeleteMin((RBTreeNode) r.right);
				r.key = temp.key;
				return temp;		
			}	
		} else if (r.key.compareTo(x) > 0){
			return this.rbDelete(x, r, (RBTreeNode) r.left);
		} else return this.rbDelete(x, r, (RBTreeNode) r.right);	
	
	}
	
	
	public void delete(Comparable x) {
		//definiramo brat, notranji in zunanji necak obrisanega vozlisca
		RBTreeNode sibling, nephewIn, nephewOut;
		RBTreeNode del; //zbrisano vozlisce
		
		boolean stop; //ali se je popravljanje zakljucilo po 3. tocki
		boolean bothBlack; //ali sta oba necaka crna
		
		parDeleted = null;
		del = this.rbDelete(x, null, (RBTreeNode)this.root);
		if(del == this.root) { //2. slucaj --> ni oceta
			this.root = null;
		}
		if(del == null || del.color == RED) {
			stop = true;
		} else stop = false; //popravljanje je potrebno le v slucaju da je zbrisano vozlisce CRNO.
		
		// 1. slucaj
		if(leftDeleted && parDeleted != null && parDeleted.left != null && ((RBTreeNode) parDeleted.left).color == RED) {
			((RBTreeNode) parDeleted.left).color = BLACK;
			stop = true;
		}
		
		if(!leftDeleted && parDeleted != null && parDeleted.right != null && ((RBTreeNode) parDeleted.right).color == RED) {
			((RBTreeNode) parDeleted.right).color = BLACK;
			stop = true;	
		}
		
		//3. slucaj
		while (parDeleted != null && !stop) {
			if(leftDeleted) { //sibling zagotovo obstaja
				sibling = (RBTreeNode) parDeleted.right;
				nephewIn = (RBTreeNode) sibling.left;
				nephewOut = (RBTreeNode) sibling.right;	
			} else { //sibling zagotovo obstaja
				sibling = (RBTreeNode) parDeleted.left;
				nephewIn = (RBTreeNode) sibling.right;
				nephewOut = (RBTreeNode) sibling.left;	
			}
			
			if(sibling.color == RED) {
				//3.1. slucaj
				parDeleted.color = RED;
				sibling.color = BLACK;
				sibling = nephewIn;
				if(leftDeleted) {
					parDeleted = (RBTreeNode) this.leftRotation(parDeleted);
					parDeleted = (RBTreeNode) parDeleted.left;
					nephewIn = (RBTreeNode) sibling.left;
					nephewOut = (RBTreeNode) sibling.right;
				} else {
					parDeleted = (RBTreeNode) this.rightRotation(parDeleted);
					parDeleted = (RBTreeNode) parDeleted.right;
					nephewIn = (RBTreeNode) sibling.right;
					nephewOut = (RBTreeNode) sibling.left;
				}		
			} 
			//zdaj je brat zagotovo crn
			
			if((nephewIn == null || nephewIn.color == BLACK) && (nephewOut == null || nephewOut.color == BLACK)) {
				bothBlack = true;
			} else bothBlack = false;
			
			if(bothBlack) {
				//3.2. slucaj
				sibling.color = RED;
				if(parDeleted.parent == null) {
					this.root = parDeleted; //novi koren		
				} else {
					leftDeleted = (parDeleted.parent.left == parDeleted);
				}
				
				if(parDeleted.color == RED) {
					//1.slucaj
					parDeleted.color = BLACK;
					stop = true;
				}
				parDeleted = parDeleted.parent;
			} else {
				//3.3. slucaj
				if(nephewOut == null || nephewOut.color == BLACK) {
					//to pomeni da notranji necak ni null in da je pobarvan rdece
					nephewIn.color = BLACK;
					sibling.color = RED;
					if(leftDeleted) {
						sibling = (RBTreeNode) this.rightRotation(sibling);
						nephewOut = (RBTreeNode) sibling.right;
					} else {
						sibling = (RBTreeNode) this.leftRotation(sibling);
						nephewOut = (RBTreeNode) sibling.left;
					}
				}
				//zdaj je zunanji necak rdec
				nephewOut.color = BLACK;
				sibling.color = parDeleted.color;
				parDeleted.color = BLACK;
				
				if(leftDeleted) {
					parDeleted = (RBTreeNode) this.leftRotation(parDeleted);
				} else parDeleted = (RBTreeNode) this.rightRotation(parDeleted);
				
				if(parDeleted.parent == null) {
					this.root = parDeleted; //novi koren
				} 
				stop = true;
			}	
		}
	}
	
}
