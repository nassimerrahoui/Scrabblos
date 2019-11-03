package decentralise;
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
	protected static int cptUpdate = 0;

	public Auteur() {
		myident = ident++;
		hashed_id = new String(hash_id(myident));
		lettres = new Vector<>();
		bc = new Blockchain();
		bc.getBlockchain().add(new Block(new Mot(new Vector<Lettre>(), 
				   "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 
				   "0b418daae4ca18c026d7f1d55237130cbdb9e874d98f7480f85f912c6470ab77")));
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
		if(bc.getWords().size() != 0) bc.Consensus();
		Lettre l = new Lettre(lettres.remove(0), 
							  hash_word(bc.getBlockchain().lastElement().getMot().get_full_word()),
							  hashed_id);
		bc.getLetters().add(l);
		cptInjection++;
	}

	@Override
	public void run() {
		int nbTours = bc.getNbTours();
		while (bc.getBlockchain().size() < nbTours) {
			Collections.shuffle(lettres);
			//block d'éxécution
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
			bc.getLock().unlock();
			
			//block d'update
			bc.getLock().lock();
			try {
				bc.getAuteurCondition().await();
				if(bc.getWords().size() != 0) {
					bc.Consensus();
				}
				cptUpdate++;
				if (bc.getNbAuteur() == cptUpdate) {
					cptUpdate = 0;
					bc.getPoliticienCondition().signalAll();
				}
				bc.getAuteurCondition().await();
			} catch (InterruptedException e) {e.printStackTrace();} finally {
				bc.getLock().unlock();
			} 
		}
		System.out.println("Fin Auteur "+myident);
	}
}