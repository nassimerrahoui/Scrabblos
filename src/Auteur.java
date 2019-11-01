import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Vector;

public class Auteur implements Runnable {

	protected static int ident = 0;
	protected int myident;
	protected Vector<Auteur> auteurs;
	protected Vector<Politicien> politiciens;
	protected Vector<Character> lettres;
	protected Vector<Character> mots;
	protected static Blockchain bc;
	protected int score;

	public Auteur() {
		myident = ident;
		ident++;
		auteurs = new Vector<>();
		politiciens = new Vector<>();
		lettres = new Vector<>();
		mots = new Vector<>();
		bc = Blockchain.getInstance();
		generer_lettres();
	}

	private void generer_lettres() {
		Random r = new Random();
		int low = 97;
		int high = 122;
		int lettre;
		for (int i = 0; i < 200; i++) {
			lettre = r.nextInt(high - low) + low;
			lettres.add((char) lettre);
		}
	}

	public static String hash_id(int id) {
		byte[] hash = new byte[256];
		byte[] input = ("" + id).getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(hash);
	}

	@Override
	public void run() {
		while (bc.getBlockchain().size() == 20) {
			try {
				bc.getLock().lock();
				/** TODO injection lettre aleatoire **/
				Lettre l = new Lettre(lettres.remove(0), "head", hash_id(myident));
				bc.getLetters().add(l);
			} finally {
				bc.getLock().unlock();
			}
			try {
				bc.getAuteurCondition().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}