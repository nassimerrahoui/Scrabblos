import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Auteur implements Runnable {

	protected static int ident = 0;
	protected int myident;
	public String hashed_id;
	protected Vector<Auteur> auteurs;
	protected Vector<Politicien> politiciens;
	protected Vector<Character> lettres;
	protected Vector<Character> mots;
	protected static Blockchain bc;
	protected int score;
	protected static int cptInjection = 0;

	public Auteur() {
		myident = ident;
		ident++;
		auteurs = new Vector<>();
		politiciens = new Vector<>();
		lettres = new Vector<>();
		mots = new Vector<>();
		bc = Blockchain.getInstance();
		generer_lettres();
		hashed_id = new String(hash_id(myident));
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
		byte[] input = ("" + id).getBytes(StandardCharsets.UTF_8);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return bytesToHex(hash);
	}

	private static String bytesToHex(byte[] hashInBytes) {

		StringBuilder sb = new StringBuilder();
		for (byte b : hashInBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();

	}

	public static String hash_word(String word) {
		byte[] hash = new byte[256];
		byte[] input = word.getBytes(StandardCharsets.UTF_8);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return bytesToHex(hash);
	}

	public void addLettre() {
		Lettre l = new Lettre(lettres.remove(0), hash_word(bc.getBlockchain().lastElement().getMot().get_full_word()),
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
				System.out.println("-------------TOUR "+bc.getBlockchain().size()+"--------------");
			}
//			System.out.println("Tour "+bc.getBlockchain().size()+" de Auteur "+myident);
			addLettre();
//			System.out.println("Fin Tour "+bc.getBlockchain().size()+" de Auteur "+myident);	
			if (bc.getNbAuteur() == cptInjection) {
				cptInjection = 0;
				bc.getPoliticienCondition().signalAll();
//				System.out.println("Last auteur, au tour des politiciens");
			}

			try {
				if(bc.getBlockchain().size() == nbTours) break;
				bc.getAuteurCondition().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				bc.getLock().unlock();
			}
		}
		System.out.println("Fin Auteur "+myident);
	}
}