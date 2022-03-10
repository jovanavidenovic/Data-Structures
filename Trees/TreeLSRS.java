import java.util.*;
import java.lang.*;
/*
 	DREVO (TREE) je zbirka vozlisc, hirearhicno urejena z relacijo parent - son. 
 	Vsako vozlisce ima element (label), nic ali vec sinov in enega oceta.
 	
 	Pomemben je vrstni red sinov, stejemo jih od leve proti desni. 
 	Vozlisce s enim samim sinom, ima levega! sina.
 	
 	List (LEAF) - vozlisce, brez sinov
 	Notranje vozlisce (INTERNAL NODE) - vozlisce, ki ima 1 ali vec sinov
 	Koren (ROOT) - vozlisce, brez oceta - edino taksno!
 	
 	Vozlisce drevesa vsebuje oznako in kazalaca na oceta, 
 	najbolj levega sina in desnega brata.
 */

class TreeLSRSNode {
	Object element;
	public TreeLSRSNode parent;
	public TreeLSRSNode leftSon;
	public TreeLSRSNode rightSibling;

	public TreeLSRSNode() {
		this(null, null, null, null);
	}
	
	public TreeLSRSNode(Object element) {
		this(element, null, null, null);
	}
	
	public TreeLSRSNode(Object element, TreeLSRSNode par, 
			TreeLSRSNode son, TreeLSRSNode sibl) {
		this.element = element;
		this.parent = par;
		this.leftSon = son;
		this.rightSibling = sibl;
	}
	
	public TreeLSRSNode(TreeLSRSNode x) {
		this.copy(x);
	}
	
	public void copy (TreeLSRSNode x) {
		this.element = x.element;
		this.parent = x.parent;
		this.leftSon = x.leftSon;
		this.rightSibling = x.rightSibling;
	}
}

/*
 	Drevo je definirano s kazalcem na koren drevesa.
 */

public class TreeLSRS {	
	
	public TreeLSRSNode root;
	
	public TreeLSRS() {
		makenull();
	}
	
	public TreeLSRS(Object root_val) {
		this.root = new TreeLSRSNode();
		this.root.element = root_val;
	}
	
	public void makenull() {
		this.root = null;
	}
	
	public TreeLSRSNode root() {
		return this.root;
	}
	
	public TreeLSRSNode parent(TreeLSRSNode node) {
		if(node != null)
			return node.parent;
		else return null;
	}
	
	public TreeLSRSNode leftmost_child(TreeLSRSNode node) {
		if(node != null)
			return node.leftSon;
		else return null;
	}
	
	public TreeLSRSNode right_sibling(TreeLSRSNode node) {
		if(node != null)
			return node.rightSibling;
		else return null;
	}
	
	public Object label(TreeLSRSNode node) {
		if(node != null)
			return node.element;
		else return null;
	}
	
	/// HEIGHT ALGORITAM /// /// ///
	
	public int height() {
		return this.height(this.root);
	}
	
	public int height(Object val) {
		TreeLSRSNode node = this.locate(this.root, val);
		if(node == null)
			return 0;
		else return this.height(node);
	}
	
	private int height(TreeLSRSNode node) {
		int maxH = 0;
		if(node == null)
			return 0;
		else maxH ++;
	
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			int h = 1 + this.height(child);
			if(h  > maxH) {
				maxH = h;
			}	
		}
		return maxH;
	}
	

	/// /// /// ///	/// /// /// /// /// ///
	
	/*
	CREATE generira drevo s korenom r y oynako v ter stopnjo i ter s poddrevesi T1, ..., Ti.
	Ce je i = 0, potem je r hkrati koren in list drevesa.
	
	Casovna kompleksnost funkcije je O(n).
	*/
	
	public void create(Object el, List subTrees) {	
		this.makenull();
		this.root = new TreeLSRSNode(el, null, null, null);
		
		boolean leftSon = true;
		TreeLSRSNode prev_son = null;
		
		for(ListElement iter = subTrees.first(); !subTrees.overEnd(iter); iter = subTrees.next(iter)) {
			TreeLSRS cur_subTree = (TreeLSRS) subTrees.retrieve(iter);
			
			if(leftSon) {
				this.root.leftSon = cur_subTree.root;
				cur_subTree.root = this.root;
				cur_subTree.root.rightSibling = null;
				leftSon = false;
			} else {
				prev_son.rightSibling = cur_subTree.root;
				cur_subTree.root = this.root;
				cur_subTree.root.rightSibling = null;
			}
			
			prev_son = new TreeLSRSNode(cur_subTree.root);
		}		
	}
	
	///
	
	protected TreeLSRSNode locate (TreeLSRSNode root, Object val) {
		if(root.element.equals(val)) {
			return root;
		} else {
			for(TreeLSRSNode child = this.leftmost_child(root); child != null; child = this.right_sibling(child)) {
				TreeLSRSNode found = locate(child, val);
				
				if(found != null)
					return found;
			}
		}
		return null;
	}
	
	///
	
	/*
	Novega sina dodamo kot najbolj levega, kompleksnost je O(1),
	ce ne racunamo kompleksnost od locate.
	 */
	public boolean addSon(Object parent_val, Object son_val) {
		TreeLSRSNode parent = this.locate(this.root, parent_val);
		if(parent != null) {
			TreeLSRSNode newLS = new TreeLSRSNode(son_val, parent, null, parent.leftSon);
			parent.leftSon = newLS;
			
			return true;
		} else return false;
	}
	
	public void addSon(TreeLSRSNode parent, TreeLSRSNode son) {
		son.parent = parent;
		son.rightSibling = this.leftmost_child(parent);
		parent.leftSon = son;
	}
	
	///
	
	public void delete (Object val) {
		TreeLSRSNode node = this.locate(this.root, val);
		if(node != null && node.parent != null) {
			this.delete(node);
		}
	}
	
	private void delete (TreeLSRSNode node) {
		TreeLSRSNode parent = this.parent(node);
		if(node.equals(this.leftmost_child(parent))) {
			parent.leftSon = node.rightSibling;
		} else {
			for(TreeLSRSNode child = this.leftmost_child(parent); child != null; child = this.right_sibling(child)) {
				if(child.rightSibling != null && child.rightSibling.equals(node)) {
					child.rightSibling = this.right_sibling(node);
				} 
			}
		}
		
		List listChild= new List();
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			/*
			ne sme tako, ker potem spreminjamo right sibling od otrokov node-a
			this.addSon(parent, child);
			 */
			listChild.addLast(child);
		}
	
		for(ListElement iter = listChild.first(); !listChild.overEnd(iter); iter = listChild.next(iter)) {
			TreeLSRSNode temp = (TreeLSRSNode) listChild.retrieve(iter);
			if(temp != null)
				this.addSon(parent, temp);
		}
	}
	
	///	PRINT FUNCTIONS ///	///	///	///	///	///	///	///	/// ///	///	///	///	///	///	///	///	///	/// 

	//PRINT Z ZAMIKOM /// /// /// /// 
	
	public void printTree() {
		this.printTree(this.root);
	}
	
	private void printTree(TreeLSRSNode node) {
		this.printTree(node, 0);
	}
	
	//izpise celotno drevo, z korenom
	private void printTree(TreeLSRSNode node, int zamik) {
		for(int i = 0; i < zamik; i++)
			System.out.print(" ");
		
		System.out.println(this.label(node));
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			this.printTree(child, zamik + 1);			
		}	
	}
	
	
	//izpise drevo, brez korena, do danega nivoja
	private void printTreeTillLvl(TreeLSRSNode node, int zamik, int cur_lvl, int max_lvl) {
		if(cur_lvl > max_lvl)
			return;
		
		for(int i = 0; i < zamik; i++)
			System.out.print(" ");
		
		if(cur_lvl != 0)
			System.out.println(this.label(node));
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			this.printTreeTillLvl(child, zamik + 1, cur_lvl + 1, max_lvl);			
		}	
	}
	
	///	///	///	///	
	
	//izpise drevo, brez korena na danem nivoju
	private void printTreeOnLvl(TreeLSRSNode node, int cur_lvl, int wanted_lvl) {
		if(cur_lvl == wanted_lvl) {
			System.out.print(this.label(node) + " ");
		} else {
			for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
				this.printTreeOnLvl(child, cur_lvl + 1, wanted_lvl);			
			}	
		}	
	}
	
	///	///	///	///	///	///	///	///	///	///
	
	public void printDescendants (Object root_val) {
		TreeLSRSNode node = this.locate(this.root, root_val);
		if(node != null) {
			this.printTree(node);
		}		
	}
	
	public void printDescendantsToLvl (Object root_val, int lvl) {
		TreeLSRSNode node = this.locate(this.root, root_val);
		if(node != null)
			this.printTreeTillLvl(node, 0, 0, lvl);	
	}
	
	public void printDescendantsOnLvl (Object root_val, int lvl) {
		TreeLSRSNode node = this.locate(this.root, root_val);
		if(node != null)
			this.printTreeOnLvl(node,  0, lvl);	
	}
	
	///	///	///	///	///	///	///	///	///	///
	
	private void printAncestors(TreeLSRSNode node) {
		for(TreeLSRSNode anc = this.parent(node); anc != null; anc = this.parent(anc)) {
			System.out.print(this.label(anc) + " ");
		}
	}
	
	private void printAncestors(TreeLSRSNode node, int max_lvl) {
		int i = 0;
		for(TreeLSRSNode anc = this.parent(node); anc != null; anc = this.parent(anc)) {
			if(i == max_lvl)
				return;
			i++;
			
			System.out.print(this.label(anc) + " ");
		}
		
	}
	
	public void printAncestors (Object d_val) {
		TreeLSRSNode node = this.locate(this.root, d_val);
		if(node != null) {
			this.printAncestors(node);
		}
	}
	
	public void printAncestors (Object d_val, int lvl) {
		TreeLSRSNode node = this.locate(this.root, d_val);
		if(node != null) {
			this.printAncestors(node, lvl);
		}
	}
	
	///	///	///	///	///	///	///	///	///	///
	
	public void printSons(Object parent_val) {
		this.printDescendantsOnLvl(parent_val, 1);
	/*	TreeLSRSNode parent = this.locate(this.root, parent_val);
		if(parent != null) {
			for(TreeLSRSNode child = this.leftmost_child(parent); child != null; child = this.right_sibling(child)) {
				System.out.print(child.element + " ");
			}
		}
	*/
	}
	
	///
	
	public void printGrandSons(Object gParent_val) {
		this.printDescendantsOnLvl(gParent_val, 2);
	/*	TreeLSRSNode gParent = this.locate(this.root, gParent_val);
		if(gParent != null) {
			for(TreeLSRSNode child = this.leftmost_child(gParent); child != null; child = this.right_sibling(child)) {
				this.printSons(this.label(child));
			}
		} 
	*/
	}
	
	///
	
	public void printGrandGrandSons(Object ggParent_val) {
		this.printDescendantsOnLvl(ggParent_val, 3);
	/*	TreeLSRSNode ggParent = this.locate(this.root, ggParent_val);
		if(ggParent != null) {
			for(TreeLSRSNode child = this.leftmost_child(ggParent); child != null; child = this.right_sibling(child)) {
				this.printGrandSons(this.label(child));
			}
		}
	*/
	}
	
	/// /// ///
	
	public void printUncles (Object val) {
		TreeLSRSNode node = this.locate(this.root, val);
		if(node != null) {
			TreeLSRSNode parent = this.parent(node);
			TreeLSRSNode gParent = this.parent(parent);
			if(gParent != null) {
				for(TreeLSRSNode uncle = this.leftmost_child(gParent); uncle != null; uncle = this.right_sibling(uncle)) {
					if(!uncle.equals(parent)) {
						System.out.print(this.label(uncle) + " ");
					}
				}	
			}			
		}	
	}
	
	///
	
	public void printCousins (Object val) {
		TreeLSRSNode node = this.locate(this.root, val);
		if(node != null) {
			TreeLSRSNode parent = this.parent(node);
			TreeLSRSNode gParent = this.parent(parent);
			
			if(gParent != null) {
				for(TreeLSRSNode uncle = this.leftmost_child(gParent); uncle != null; uncle = this.right_sibling(uncle)) {
					if(!uncle.equals(parent)) {
						this.printSons(this.label(uncle));
					}
				}	
			}			
		}	
	}
	
	/// ALGORITMI ZA OBHOD DREVESA /// ///	///	/// ///	///	///	///	///	///	///	///	///	///
	
	// izpisemo oznako korena pred oznakami poddreves
	public void printPreorder() {	
		this.printPreorder(this.root);
	}
	
	private void printPreorder(TreeLSRSNode node) {
		if(node == null)
			return; 
		
		System.out.print(this.label(node) + " ");
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			this.printPreorder(child);			
		}			
	}
	
	// izpisemo najprej oznake vozlisc vseh poddreves in potem oznako korena
	public void printPostorder() {	
		this.printPostorder(this.root);
	}
		
	private void printPostorder(TreeLSRSNode node) {
		if(node == null)
			return; 
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			this.printPostorder(child);			
		}	
		
		System.out.print(this.label(node) + " ");
	}
	
	// izpisemo najprej oznake vozlisc najbolj levega poddrevesa
	// potem oznako korena in potem oznake vozlisc vseh ostalih poddreves korena
	public void printInorder() {	
		this.printInorder(this.root);
	}
			
	private void printInorder(TreeLSRSNode node) {
		if(node == null)
			return; 
			
		this.printInorder(this.leftmost_child(node));
		
		System.out.print(this.label(node) + " ");
		
		for(TreeLSRSNode child = this.right_sibling(this.leftmost_child(node)); child != null; child = this.right_sibling(child)) {
			this.printInorder(child);			
		}	
	}
	
	// izpisemo najprej vsa vozlisca na 1. nivoju, zatim na 2. nivoju itd
	public void printLvlLike() {	
		this.printLvlLike(this.root);
		System.out.println();
		System.out.println("Z uporabom vrste: ");
		Queue q = new Queue();
		q.enqueue(this.label(this.root));

		//napolnimo vrsto 
		this.fillQueue(this.root, q);
		while(!q.empty()) {
			System.out.print(q.front()+ " ");
			q.dequeue();
		}
	}
				
	private void printLvlLike(TreeLSRSNode node) {
		for(int i = 0; i < this.height(node); i++) {
			this.printTreeOnLvl(node, 0, i);		
		}
	}
	
	//nivojski zapis, ki uporablja vrsto
	private void fillQueue(TreeLSRSNode node, Queue q) {		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {	
			q.enqueue(this.label(child));
		}
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {	
			this.fillQueue(child, q);
		}
	}
	
	///	///	///	///	///	///	///	///	///	/// ///	///	///	///	///	///	///	///	///	///
	///	///	///	///	///	///	///	///	///	/// ///	///	///	///	///	///	///	///	///	///
	
	public int num_elems () {
		return this.num_elems(this.root);
	}
	
	public int num_elems (TreeLSRSNode node) {
		int num = 1;
		
		for(TreeLSRSNode child = this.leftmost_child(node); child != null; child = this.right_sibling(child)) {
			num += num_elems(child);
		}
		
		return num;
	}
	
}
