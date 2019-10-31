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

	private Blockchain() {
		letters = new Vector<Lettre>();
		words = new Vector<Mot>();
		blockchain = new Vector<Block>();
		compteurPhase = 0;
		phaseAuteur = true;
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
			if (compteurPhase == nbAuteur) {
				compteurPhase = 0;
				phaseAuteur = false;
				politicienCondition.signalAll();
			}
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

	/**
	 * choisi le meilleur mot du tour actuel, vide la liste de mots et de lettres du
	 * tour actuel creer un block et l'ajoute à la liste
	 */
	public void Consensus() {
		Mot max = words.get(0);
		for (int i = 1; i < words.size(); i++) {
			Mot courant = words.get(i);
			if (max.getMot().size() < courant.getMot().size()) {
				max = courant;
			}
		}
		//Block blocAjouter = new Block(max.getMot().toString(), lettres_auteurs, precedent, politician_public_key)
	}
}
