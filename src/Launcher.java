import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Launcher {

	private static HashMap<Auteur,Thread> auteurs = new HashMap<>();
	private static HashMap<Politicien,Thread> politiciens = new HashMap<>();

	public static void nextTurn() {
	}

	public static void generate_letter_bag() {
	}

	public static void main(String[] args) {
		// chargement des dictionnaires
		ArrayList<File> dicts = new ArrayList<>();
		for (int i = 1; i < 2; i++) {
			dicts.add(new File("src/dict/dict" + i + ".txt"));
		}
		PatriciaTree p = PatriciaTree.createTree(dicts);

		// creation des auteurs et des politiciens
		for (int i = 0; i < 3; i++) {
			Auteur a = new Auteur();
			auteurs.put(a, new Thread(a));
		}
		for (int i = 0; i < 3; i++) {
			Politicien po = new Politicien(p);
			politiciens.put(po, new Thread(po));
		}
		//		Launcher l = new Launcher(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
		Blockchain.getInstance().setNbAuteur(3);
		Blockchain.getInstance().setNbPoliticien(3);
		Blockchain.getInstance().getBlockchain().add(new Block(new Mot(new Vector<Lettre>(), "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77")));	
		System.out.println("On lance les auteurs, c'est parti !");
		for (Iterator<Politicien> iterator = politiciens.keySet().iterator(); iterator.hasNext();) {
			Politicien pol = iterator.next();
			politiciens.get(pol).start();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Iterator<Auteur> iterator = auteurs.keySet().iterator(); iterator.hasNext();) {
			Auteur aut = iterator.next();
			auteurs.get(aut).start();

		}





		//		Mot m = new Mot(bc.getLetters(), "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
		//				"0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77");
		//		bc.getBlockchain().add(new Block(m));
		//		System.out.println("full mot : \"" + m.get_full_word() + "\"");
		//		System.out.println(bc.getBlockchain().size());
		//		p1.inject_word();
		//		System.out.println("full mot m1 : \"" + m1.get_full_word() + "\"");
		//		System.out.println(bc.getWords().lastElement().getPrecedent_hashed().equals(p1.hash_word(m.get_full_word())));
		//		System.out.println(Politicien.hash_word(""));
	}
}
