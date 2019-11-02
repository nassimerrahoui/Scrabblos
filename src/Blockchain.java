import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Blockchain {

	// chaque tour
	private Vector<Lettre> letters;
	private Vector<Mot> words;
	// blockchain courante
	private Vector<Block> blockchain;
	private final Lock lock = new ReentrantLock();
	private final Condition auteurCondition = lock.newCondition();
	private final Condition politicienCondition = lock.newCondition();
	private int nbAuteur;
	private int nbPoliticien;
	private int compteurPhase;
	private boolean phaseAuteur;
	private HashMap<Character, Integer> scoreLettres = new HashMap<>();

	private Blockchain() {
		letters = new Vector<Lettre>();
		words = new Vector<Mot>();
		blockchain = new Vector<Block>();
		compteurPhase = 0;
		phaseAuteur = true;

		scoreLettres.put('k', 0);
		scoreLettres.put('w', 0);
		scoreLettres.put('x', 0);
		scoreLettres.put('y', 0);
		scoreLettres.put('z', 0);
		scoreLettres.put('a', 1);
		scoreLettres.put('e', 1);
		scoreLettres.put('i', 1);
		scoreLettres.put('n', 1);
		scoreLettres.put('o', 1);
		scoreLettres.put('r', 1);
		scoreLettres.put('s', 1);
		scoreLettres.put('t', 1);
		scoreLettres.put('u', 1);
		scoreLettres.put('l', 1);
		scoreLettres.put('d', 2);
		scoreLettres.put('m', 2);
		scoreLettres.put('g', 2);
		scoreLettres.put('b', 3);
		scoreLettres.put('c', 3);
		scoreLettres.put('p', 3);
		scoreLettres.put('f', 4);
		scoreLettres.put('h', 4);
		scoreLettres.put('v', 4);
		scoreLettres.put('j', 8);
		scoreLettres.put('q', 8);
	}

	// ------------------------MUTEX-------------------------
	private static Blockchain INSTANCE = new Blockchain();

	public static Blockchain getInstance() {
		return INSTANCE;
	}

	public Vector<Lettre> getLetters() {
		// On suppose que les appels ne sont faits qu'une fois par clients, peut-etre a
		// revoir
		compteurPhase++;
		if (phaseAuteur) {

		} else {
			if (compteurPhase == nbPoliticien) {
				compteurPhase = 0;
				phaseAuteur = true;
				// Faire le consensus ici avant de reveiller les auteurs
				auteurCondition.signalAll();
			}
		}
		return letters;
	}

	public Vector<Mot> getWords() {
		return words;
	}

	public Vector<Block> getBlockchain() {
		return blockchain;
	}

	public Lock getLock() {
		return lock;
	}

	public Condition getAuteurCondition() {
		return auteurCondition;
	}

	public Condition getPoliticienCondition() {
		return politicienCondition;
	}

	public void setNbAuteur(int nbAuteur) {
		this.nbAuteur = nbAuteur;
	}

	public void setNbPoliticien(int nbPoliticien) {
		this.nbPoliticien = nbPoliticien;
	}

	public int getNbAuteur() {
		return nbAuteur;
	}

	public int getNbPoliticien() {
		return nbPoliticien;
	}

	private int getScore(Mot m) {
		int res = 0;
		for (char c : m.get_full_word().toCharArray()) {
			res += scoreLettres.get(c);
		}
		return res;
	}

	/**
	 * choisi le meilleur mot du tour actuel, vide la liste de mots et de lettres du
	 * tour actuel creer un block et l'ajoute Ã  la liste
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
		System.out.println("fin du consensus, reveil des auteurs");
		letters.clear();
		words.clear();
		// Ajouter score aux politicines/auteurs et lancer auteurs
		if(blockchain.size() < 20) {
			auteurCondition.signalAll();
		}
	}
}
