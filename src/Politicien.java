import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

public class Politicien implements Runnable {

	// --------ID---------
	static int ident = 0;
	int myident;
	public String hashed_id;
	public int score;
	private PatriciaTree patricia;

	// -----------------BLOCKCHAIN-------------------
	public static Blockchain bc = Blockchain.getInstance();

	// -----------TOUR----------------
	public Vector<Lettre> letters;
	protected static int cptInjection = 0;

	public Politicien(PatriciaTree patricia) {
		myident = ident++;
		score = 0;
		letters = null;
		this.patricia = patricia;
		hashed_id = new String(hash_id(myident));
	}

	public Mot generateWord() {
		letters = bc.getLetters();
		// generate word
		System.out.println("size letter : "+letters.size());
		Vector<Lettre> used_letter = new Vector<>(letters);
		System.out.println("size used" +used_letter.size());
		Collections.shuffle(used_letter);
		char[] tab = new char[used_letter.size()];
		for (int i = 0; i < used_letter.size(); i++) {
			tab[i] = used_letter.get(i).getLettre();
		}
		String s = patricia.search(tab);
		used_letter.clear();
		for (int i = 0; i < letters.size(); i++) {
			if (letters.get(i).getLettre() == s.charAt(i))
				used_letter.add(letters.get(i));
		}
		if (s != "") {
			return new Mot(used_letter, hash_word(bc.getBlockchain().lastElement().getMot().get_full_word()),
					hashed_id);
		}
		return null;
	}

	public void inject_word() {
		Mot m = generateWord();
		bc.getWords().add(m);
		System.out.println("ajout du mot "+m.get_full_word()+" à la liste de possibilités");
		cptInjection++;
	}

	@Override
	public void run() {
		while (bc.getBlockchain().size() < 3) {
			try {
				bc.getLock().lock();
				bc.getPoliticienCondition().await();
				if(bc.getBlockchain().size() == 3) break;
				System.out.println("Tour "+bc.getBlockchain().size()+" de Politicien "+myident);
				inject_word();
				System.out.println("Fin Tour "+bc.getBlockchain().size()+" de Politicien "+myident);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}finally {	
				if (bc.getNbPoliticien() == cptInjection) {
					System.out.println("Last politicien, au tour du consensus");
					cptInjection = 0;
					bc.Consensus();
				}
				bc.getLock().unlock();
			}
		}
		System.out.println("Fin Politicien "+myident);
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
}
