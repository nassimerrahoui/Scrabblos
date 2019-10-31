

import java.util.ArrayList;
import java.util.Arrays;


public class PatriciaTree {

	private char character;	
	private boolean fin;
	private ArrayList<PatriciaTree> list_noeuds;

	public PatriciaTree(char character) {
		this.character = character;
		this.list_noeuds = new ArrayList<PatriciaTree>();
		this.fin = false;
	}


	public char getCharacter() {
		return character;
	}

	public ArrayList<PatriciaTree> getList_noeuds() {
		return list_noeuds;
	}

	public boolean isFin() {
		return fin;
	}

	public void setFin(boolean fin) {
		this.fin = fin;
	}

	public void add(char[] s) {
		if(s.length == 1) {
			for(PatriciaTree r : list_noeuds) {
				if(r.character == s[0]) {
					r.setFin(true);
					return;
				}
			}
			PatriciaTree r = new PatriciaTree(s[0]);
			r.setFin(true);
			list_noeuds.add(r);
		}else {
			for(PatriciaTree r : list_noeuds) {
				if(r.character == s[0]) {
					r.add(Arrays.copyOfRange(s, 1, s.length));
					return;
				}
			}
			PatriciaTree r = new PatriciaTree(s[0]);
			r.add(Arrays.copyOfRange(s, 1, s.length));
			list_noeuds.add(r);
		}
	}

	public boolean search(char[] s) {
		if(s.length == 1) {
			for(PatriciaTree r : list_noeuds) {
				if(r.character == s[0]) {
					return r.isFin();
				}
			}
		}
		for(PatriciaTree r : list_noeuds) {
			if(r.character == s[0]) {
				return r.search(Arrays.copyOfRange(s, 1, s.length));
			}
		}
		return false;
	}
		

	public static PatriciaTree createTree(ArrayList<String> words) {
		PatriciaTree root = new PatriciaTree('.');
		for(String s : words) {
			root.add(s.toCharArray());
		}
		return root;
	} 

}
