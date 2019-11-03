package decentralise;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Launcher {

	private static HashMap<Auteur,Thread> auteurs = new HashMap<>();
	private static HashMap<Politicien,Thread> politiciens = new HashMap<>();

	public static void main(String[] args) {
		// chargement des dictionnaires
		ArrayList<File> dicts = new ArrayList<>();
		for (int i = 1; i < 2; i++) dicts.add(new File("src/dict/dict" + i + ".txt"));
		PatriciaTree p = PatriciaTree.createTree(dicts);

		Blockchain bc = new Blockchain();
		//initialisation du nombre d'auteurs/politicien/tours et de la blockchain
		bc.setNbAuteur(10);
		bc.setNbPoliticien(10);
		bc.setNbTours(5);
		
		// creation des auteurs et des politiciens
		for (int i = 0; i < bc.getNbAuteur(); i++) {
			Auteur a = new Auteur();
			auteurs.put(a, new Thread(a));
		}
		for (int i = 0; i < bc.getNbPoliticien(); i++) {
			Politicien po = new Politicien(p);
			politiciens.put(po, new Thread(po));
		}
		System.out.println("On lance les auteurs, c'est parti !");
		
		//lancement des threads
		for (Iterator<Politicien> iterator = politiciens.keySet().iterator(); iterator.hasNext();) 
			politiciens.get(iterator.next()).start();
		
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		
		for (Iterator<Auteur> iterator = auteurs.keySet().iterator(); iterator.hasNext();) 
			auteurs.get(iterator.next()).start();
		
		//attente de fin des auteurs/politiciens
		for (Thread po : politiciens.values())
			try {po.join();} catch (InterruptedException e) {e.printStackTrace();}
		for (Thread po : auteurs.values())
			try {po.join();} catch (InterruptedException e) {e.printStackTrace();}
		
		//attribution du score aux auteurs/politiciens
		Politicien pol = politiciens.keySet().iterator().next();
		for (Block b : pol.bc.getBlockchain()) {
			Mot m = b.getMot();
			for (Lettre l : m.getMot()) {
				for (Auteur a : auteurs.keySet())
					if(a.hashed_id == l.getAuteur()) a.score += bc.getScore(l);
			}
			for (Politicien po : politiciens.keySet())
				if(po.hashed_id == m.getPoliticien_public_key()) po.score += bc.getScore(m);
		}
		//affichage des scores
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
