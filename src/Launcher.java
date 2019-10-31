import java.io.File;
import java.util.ArrayList;

public class Launcher {
	
	public static void nextTurn() {}
	
	public static void generate_letter_bag() {}
	
	public static void main(String[] args) {
		ArrayList<File> dicts = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			dicts.add(new File("dict/dict"+i+".txt"));			
		}
		//PatriciaTree p = PatriciaTree.createTree(dicts);
		/*
		Auteur a1 = new Auteur();
		Auteur a2 = new Auteur();
		Auteur a3 = new Auteur();
		Auteur a4 = new Auteur();
		Politicien p1 = new Politicien();
		Politicien p2 = new Politicien();
		Politicien p3 = new Politicien();
		Politicien p4 = new Politicien();
		*/
	}

}
