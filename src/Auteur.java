import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Condition;

public class Auteur implements Runnable {

	protected static int ident = 0;
	protected int myident;
	protected Vector<Auteur> auteurs;
	protected Vector<Politicien> politiciens;
	protected Vector<Character> lettres;
	protected Vector<Character> mots;
	protected static Blockchain bc;
	protected int score;
	protected boolean standby;
    public static Condition auteurCondition;

	public Auteur() {
		myident = ident;
		ident++;
		auteurs = new Vector<>();
		politiciens = new Vector<>();
		lettres = new Vector<>();
		mots = new Vector<>();
		bc = Blockchain.getInstance();
		generer_lettres();
		standby = false;
		auteurCondition = bc.getLock().newCondition();
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
		byte[] input = (""+id).getBytes();
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

			/** TODO injection lettre aleatoire **/
			Lettre l = new Lettre(lettres.remove(0), "head", hash_id(myident), "signature");
			bc.getLetters().add(l);
			standby = true;

			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
