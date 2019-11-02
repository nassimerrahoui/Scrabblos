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

		Blockchain.getInstance().setNbAuteur(100);
		Blockchain.getInstance().setNbPoliticien(100);
		Blockchain.getInstance().setNbTours(30);
		
		// creation des auteurs et des politiciens
		for (int i = 0; i < Blockchain.getInstance().getNbAuteur(); i++) {
			Auteur a = new Auteur();
			auteurs.put(a, new Thread(a));
		}
		for (int i = 0; i < Blockchain.getInstance().getNbPoliticien(); i++) {
			Politicien po = new Politicien(p);
			politiciens.put(po, new Thread(po));
		}
		Blockchain.getInstance().getBlockchain().add(new Block(new Mot(new Vector<Lettre>(), "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77")));	
		//		Launcher l = new Launcher(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
		System.out.println("On lance les auteurs, c'est parti !");
		for (Iterator<Politicien> iterator = politiciens.keySet().iterator(); iterator.hasNext();) {
			Politicien pol = iterator.next();
			politiciens.get(pol).start();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Iterator<Auteur> iterator = auteurs.keySet().iterator(); iterator.hasNext();) {
			Auteur aut = iterator.next();
			auteurs.get(aut).start();

		}
		for (Thread po : politiciens.values()) {
			try {
				po.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Thread po : auteurs.values()) {
			try {
				po.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//----------------------SCORE----------------------------
		for (Block b : Blockchain.getInstance().getBlockchain()) {
			Mot m = b.getMot();
			for (Lettre l : m.getMot()) {
				String hashed_id = l.getAuteur();
				for (Auteur a : auteurs.keySet()) {
					if(a.hashed_id == hashed_id) {
						a.score += Blockchain.getInstance().getScore(l);
					}
				}
			}
			for (Politicien po : politiciens.keySet()) {
				String hashed_id = m.getPoliticien_public_key();
				if(po.hashed_id == hashed_id) {
					po.score += Blockchain.getInstance().getScore(m);
				}
			}
		}
		System.out.println("---------------SCORE--------------------\n");
		for (Politicien po : politiciens.keySet()) {
			System.out.println("Politicien "+po.myident+" : "+po.score);
		}
		System.out.println("------------------------------");
		for (Auteur aut : auteurs.keySet()) {
			System.out.println("Auteur "+aut.myident+" : "+aut.score);
		}
		
	}
}
