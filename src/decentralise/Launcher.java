package decentralise;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class Launcher {

	private static HashMap<Auteur, Thread> auteurs = new HashMap<>();
	private static HashMap<Politicien, Thread> politiciens = new HashMap<>();

	public static void main(String[] args) {
		// chargement des dictionnaires
		ArrayList<File> dicts = new ArrayList<>();
		for (int i = 1; i < 5; i++)
			dicts.add(new File("src/dict/dict" + i + ".txt"));
		System.out.println("Chargement du dictionnaire...");
		PatriciaTree p = PatriciaTree.createTree(dicts);
		System.out.println("Fin du chargement...");
		Blockchain bc = new Blockchain();
		// initialisation du nombre d'auteurs/politicien/tours et de la blockchain
		bc.setNbAuteur(10);
		bc.setNbPoliticien(10);
		bc.setNbTours(10);

		// creation des auteurs et des politiciens
		for (int i = 0; i < bc.getNbAuteur(); i++) {
			Auteur a = new Auteur();
			auteurs.put(a, new Thread(a));
		}
		for (int i = 0; i < bc.getNbPoliticien(); i++) {
			Politicien po = new Politicien(p);
			politiciens.put(po, new Thread(po));
		}
		System.out.println("On lance les joueurs, c'est parti !");

		// lancement des threads
		for (Iterator<Politicien> iterator = politiciens.keySet().iterator(); iterator.hasNext();)
			politiciens.get(iterator.next()).start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		for (Iterator<Auteur> iterator = auteurs.keySet().iterator(); iterator.hasNext();)
			auteurs.get(iterator.next()).start();

		// attente de fin des auteurs/politiciens
		for (Thread po : politiciens.values())
			try {po.join();} catch (InterruptedException e) {e.printStackTrace();}
		for (Thread po : auteurs.values())
			try {po.join();} catch (InterruptedException e) {e.printStackTrace();}

		// attribution du score aux auteurs/politiciens
		Politicien pol = politiciens.keySet().iterator().next();
		for (Block b : pol.bc.getBlockchain()) {
			Mot m = b.getMot();
			for (Lettre l : m.getMot()) {
				for (Auteur a : auteurs.keySet())
					if (a.hashed_id == l.getAuteur())
						a.score += bc.getScore(l);
			}
			for (Politicien po : politiciens.keySet())
				if (po.hashed_id == m.getPoliticien_public_key())
					po.score += bc.getScore(m);
		}
		
		// tri des scores
		LinkedList<Map.Entry<Politicien, Thread>> linked_list_pol = new LinkedList<Map.Entry<Politicien, Thread>>(politiciens.entrySet());
		Collections.sort(linked_list_pol, new Comparator<Map.Entry<Politicien, Thread>>() {
			@Override
			public int compare(Entry<Politicien, Thread> o1, Entry<Politicien, Thread> o2) {return o2.getKey().score - o1.getKey().score;}
		});
		LinkedList<Map.Entry<Auteur, Thread>> linked_list_aut = new LinkedList<Map.Entry<Auteur, Thread>>(
				auteurs.entrySet());
		Collections.sort(linked_list_aut, new Comparator<Map.Entry<Auteur, Thread>>() {
			@Override
			public int compare(Entry<Auteur, Thread> o1, Entry<Auteur, Thread> o2) {return o2.getKey().score - o1.getKey().score;}
		});
		LinkedHashMap<Politicien, Thread> lpol = new LinkedHashMap<>();
		for (Map.Entry<Politicien, Thread> entry : linked_list_pol) lpol.put(entry.getKey(), entry.getValue());
		LinkedHashMap<Auteur, Thread> laut = new LinkedHashMap<>();
		for (Map.Entry<Auteur, Thread> entry : linked_list_aut) laut.put(entry.getKey(), entry.getValue());
		
		//affichage des scores
		System.out.println("---------------SCORE--------------------\n");
		for (Politicien po : lpol.keySet()) System.out.println("Politicien " + po.myident + " : " + po.score);
		System.out.println("------------------------------");
		for (Auteur aut : laut.keySet()) System.out.println("Auteur " + aut.myident + " : " + aut.score);
	}
}