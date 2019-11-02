import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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


	public char getCharacter() {return character;}
	public ArrayList<PatriciaTree> getList_noeuds() {return list_noeuds;}
	public boolean isFin() {return fin;}
	public void setFin(boolean fin) {this.fin = fin;}

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

	public String search(char[] s) {
		if(s.length == 1) 
			for(PatriciaTree r : list_noeuds) 
				if(r.character == s[0]) return ""+s[0];
		for(PatriciaTree r : list_noeuds)
			if(r.character == s[0]) return ""+s[0]+r.search(Arrays.copyOfRange(s, 1, s.length));
		return "";
	}
	
	public static PatriciaTree createTree(ArrayList<File> dicts) {
		ArrayList<String> lines = new ArrayList<>();
		for(File f : dicts) {
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
				String line;
				while((line = br.readLine()) != null) lines.add(line);
				br.close();
			} catch (FileNotFoundException e) {e.printStackTrace();} 
			  catch (IOException e) {e.printStackTrace();}
		}
		PatriciaTree root = new PatriciaTree('.');
		for(String s : lines) root.add(s.toCharArray());
		return root;
	}
}
