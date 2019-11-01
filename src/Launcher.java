import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class Launcher {
	
	public static void nextTurn() {}
	
	public static void generate_letter_bag() {}
	
	public static void main(String[] args) {
		ArrayList<File> dicts = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			dicts.add(new File("src/dict/dict"+i+".txt"));			
		}
		PatriciaTree p = PatriciaTree.createTree(dicts);
//		System.out.println("salut");
//		Auteur a1 = new Auteur();
//		Auteur a2 = new Auteur();
//		Auteur a3 = new Auteur();
//		Auteur a4 = new Auteur();
		Politicien p1 = new Politicien(p);
//		Politicien p2 = new Politicien(p);
//		Politicien p3 = new Politicien(p);
//		Politicien p4 = new Politicien(p);
		Lettre l = new Lettre('a', "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77");
		Vector<Lettre> v = new Vector<Lettre>();
		v.add(l);
		Blockchain bc = Blockchain.getInstance();
		Mot m = new Mot(v, "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77");
		bc.getBlockchain().add(new Block(m));
		System.out.println("full mot : \""+m.get_full_word()+"\"");
		Mot m1 = p1.generateWord(v);
		System.out.println("full mot m1 : \""+m1.get_full_word()+"\"");
		System.out.println(m1.getPrecedent_hashed().equals(p1.hash_word(m.get_full_word())));
		System.out.println(Politicien.hash_word(""));
	}
}
