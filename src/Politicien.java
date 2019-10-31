import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

public class Politicien implements Runnable{
	
	static int ident = 0;
	int myident;
	public static Blockchain bc = Blockchain.getInstance();
	public int score;
	public String hashed_id;
	public Vector<Lettre> letters;
	private PatriciaTree patricia;
	
	public Politicien(PatriciaTree patricia) {
		myident = ident++;
		score = 0;
		letters = null;
		this.patricia = patricia;
		hashed_id = new String(hash_id(myident));
	}
	
	public Mot generateWord() {
		letters = bc.getLetters();
		//generate word
		Vector<Lettre> used_letter = new Vector<>();
		Collections.copy(letters, used_letter);
		Collections.shuffle(letters);
		char[] tab = new char[letters.size()];
		for(int i = 0;i<letters.size();i++){
			tab[i] = letters.get(i).getLettre();
		}
		String s = patricia.search(tab);
		for(int i = 0; i<letters.size();i++) {
			if(letters.get(i).getLettre() == s.charAt(i)) continue;
			used_letter.remove(i);
		}
		if(s != "") {
			return null;//return new Mot(used_letter, bc.getBlockchain().lastElement().getMot(), hashed_id);
		}
		return null;
	}
	
    public void inject_word() {
    	
    }

	@Override
	public void run() {
		while(bc.getBlockchain().size() == 20) {
			synchronized (bc) {
				inject_word();
			}
		}
	}
    
	public static byte[] hash_id(int id) {
		byte[] hash = new byte[256];
		byte[] input = (""+id).getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static byte[] hash_word(String word) {
		byte[] hash = new byte[256];
		byte[] input = word.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hash = md.digest(input);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}
