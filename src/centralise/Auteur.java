package centralise;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Auteur implements Runnable {

	//statique
	protected static int ident = 0;
	protected int myident;
	public String hashed_id;
	protected int score;
	protected Blockchain bc;

	//dynamique 
	protected Vector<Character> lettres;
	protected static int cptInjection = 0;

	public Auteur() {
		myident = ident++;
		hashed_id = new String(hash_id(myident));
		lettres = new Vector<>();
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

	private String bytesToHex(byte[] hashInBytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : hashInBytes) sb.append(String.format("%02x", b));
		return sb.toString();
	}

	public String hash_id(int id) {
		byte[] hash = new byte[256];
		byte[] input = (String.valueOf(id)).getBytes(StandardCharsets.UTF_8);
		try { hash = MessageDigest.getInstance("SHA-256").digest(input);} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		return bytesToHex(hash);
	}

	public String hash_word(String word) {
		byte[] hash = new byte[256];
		byte[] input = word.getBytes(StandardCharsets.UTF_8);
		try {hash = MessageDigest.getInstance("SHA-256").digest(input);} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		return bytesToHex(hash);
	}

	public void addLettre() {
		Lettre l = new Lettre(lettres.remove(0), 
							  hash_word(bc.getBlockchain().lastElement().getMot().get_full_word()),
							  hashed_id);
		bc.getLetters().add(l);
		cptInjection++;
	}

	@Override
	public void run() {
		int nbTours = bc.getNbTours();
		while (bc.getBlockchain().size() < nbTours+1) {
			Collections.shuffle(lettres);
			bc.getLock().lock();
			if (cptInjection == 0) {
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				System.out.println("-------------TOUR "+bc.getBlockchain().size()+"--------------");
			}
			addLettre();
			if (bc.getNbAuteur() == cptInjection) {
				cptInjection = 0;
				bc.getPoliticienCondition().signalAll();
			}
			try {
				if(bc.getBlockchain().size() == nbTours) break;
				bc.getAuteurCondition().await();
			} catch (InterruptedException e) {e.printStackTrace();
			} finally {bc.getLock().unlock();}
		}
		System.out.println("Fin Auteur "+myident);
	}
}