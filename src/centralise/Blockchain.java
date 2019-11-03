package centralise;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Blockchain {

	// statique
	private final Lock lock;
	private final Condition auteurCondition,politicienCondition;
	private int nbAuteur,nbPoliticien,nbTours,difficulte;
	
	//dynamique
	private Vector<Lettre> letters;
	private Vector<Mot> words;
	private Vector<Block> blockchain;
	private HashMap<Character, Integer> scoreLettres;

	private Blockchain() {
		lock = new ReentrantLock();
		auteurCondition = lock.newCondition();
		politicienCondition = lock.newCondition();
		letters = new Vector<Lettre>();
		words = new Vector<Mot>();
		blockchain = new Vector<Block>();
		scoreLettres = new HashMap<>();
		difficulte = 0;

		scoreLettres.put('a', 1);scoreLettres.put('e', 1);scoreLettres.put('i', 1);scoreLettres.put('n', 1);scoreLettres.put('o', 1);scoreLettres.put('r', 1);
		scoreLettres.put('s', 1);scoreLettres.put('t', 1);scoreLettres.put('u', 1);scoreLettres.put('l', 1);
		scoreLettres.put('d', 2);scoreLettres.put('m', 2);scoreLettres.put('g', 2);
		scoreLettres.put('b', 3);scoreLettres.put('c', 3);scoreLettres.put('p', 3);
		scoreLettres.put('f', 4);scoreLettres.put('h', 4);scoreLettres.put('v', 4);
		scoreLettres.put('j', 8);scoreLettres.put('q', 8);
		scoreLettres.put('k', 10);scoreLettres.put('w', 10);scoreLettres.put('x', 10);scoreLettres.put('y', 10);scoreLettres.put('z', 10);
	}

	// ------------------------GETTERS-------------------------
	private static Blockchain INSTANCE = new Blockchain();
	public static Blockchain getInstance() {return INSTANCE;}
	public Vector<Lettre> getLetters() {return letters;}
	public Vector<Mot> getWords() {return words;}
	public int getDifficulte() {return difficulte;}
	public Vector<Block> getBlockchain() {return blockchain;}
	public Lock getLock() {return lock;}
	public Condition getAuteurCondition() {return auteurCondition;}
	public Condition getPoliticienCondition() {return politicienCondition;}
	public int getNbTours() {return nbTours;}
	public int getNbAuteur() {return nbAuteur;}
	public int getNbPoliticien() {return nbPoliticien;}
	public int getScore(Mot m) {
		int res = 0;
		for (char c : m.get_full_word().toCharArray()) {
			res += scoreLettres.get(c);
		}
		return res;
	}
	public int getScore(Lettre l) {
		int res = 0;
		res = scoreLettres.get(l.getLettre());
		return res;
	}

	// ------------------------SETTERS-------------------------
	public void setNbAuteur(int nbAuteur) {this.nbAuteur = nbAuteur;}
	public void setNbPoliticien(int nbPoliticien) {this.nbPoliticien = nbPoliticien;}
	public void setNbTours(int nbTours) {this.nbTours = nbTours;}
	public void setDifficulte(int difficulte) {this.difficulte = difficulte;}

	/**
	 * choisi le meilleur mot du tour actuel, 
	 * vide la liste de mots et de lettres 
	 * du tour actuel creer un block et l'ajoute Ã  la liste
	 */
	public void Consensus() {
		Mot max = words.get(0);
		int score = getScore(max);
		for (int i = 1; i < words.size(); i++) {
			Mot courant = words.get(i);
			int scoreCourant = getScore(courant);
			if (score < scoreCourant) {
				max = courant;
				score = scoreCourant;
			}
		}
		blockchain.add(new Block(max));
		System.out.println("le mot " + max.get_full_word() + " à été choisi");
		letters.clear();
		words.clear();
	}
}
