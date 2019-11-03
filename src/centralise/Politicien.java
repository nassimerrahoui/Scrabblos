package centralise;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

import centralise.Mot;

public class Politicien implements Runnable {

	//statique
	protected static int ident = 0;
	protected int myident;
	protected String hashed_id;
	protected int score;
	protected PatriciaTree patricia;
	public static Blockchain bc;

	//dynamique
	public Vector<Lettre> letters;
	protected static int cptInjection = 0;

	public Politicien(PatriciaTree patricia) {
		myident = ident++;
		hashed_id = new String(hash_id(myident));
		score = 0;
		bc = Blockchain.getInstance();
		letters = bc.getLetters();
		this.patricia = patricia;
	}

	public Mot generateWord() {
		letters = bc.getLetters();
		Vector<Lettre> used_letter = new Vector<>(letters);
		Collections.shuffle(used_letter);
		char[] tab = new char[used_letter.size()];
		for (int i = 0; i < used_letter.size(); i++) tab[i] = used_letter.get(i).getLettre();
		String s = patricia.search(tab);
		for(int i = used_letter.size()-1;i >= s.length();i--) used_letter.remove(i);
		return new Mot(used_letter, hash_word(bc.getBlockchain().lastElement().getMot().get_full_word()),hashed_id);
	}

	public void inject_word() {
		Mot m = generateWord();
		if(m.get_full_word().length() >= bc.getDifficulte()) {
			bc.getWords().add(m);
			System.out.println("Politicien "+myident+"		ajout du mot \""+m.get_full_word()+"\" aux possibilités");
		}
		cptInjection++;
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
	
	@Override
	public void run() {
		int nbTours = bc.getNbTours();
		while (bc.getBlockchain().size() < nbTours) {
			try {
				bc.getLock().lock();
				bc.getPoliticienCondition().await();
				if(bc.getBlockchain().size() == nbTours) break;
				inject_word();
			} catch(InterruptedException e) {e.printStackTrace();
			} finally {	
				if (bc.getNbPoliticien() == cptInjection) {
					cptInjection = 0;
					if(bc.getWords().size() > (int)bc.getNbPoliticien()*0.1) {
						bc.setDifficulte(bc.getDifficulte()+2);
						System.out.println("On augmente la difficulté à "+bc.getDifficulte());
					}else {
						bc.setDifficulte(bc.getDifficulte()-1);
						System.out.println("On baisse la difficulté à "+bc.getDifficulte());
					}
					if(bc.getWords().size() != 0) bc.Consensus();
					if(bc.getBlockchain().size() <= nbTours+1) bc.getAuteurCondition().signalAll();
				}
				bc.getLock().unlock();
			}
		}
		System.out.println("Fin Politicien "+myident);
	}
}
