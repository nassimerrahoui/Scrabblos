import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

public class Politicien implements Runnable{
	
	static int ident = 0;
	int myident;
	public static Blockchain bc = Blockchain.getInstance();
	public int score;
	public Vector<Lettre> letters;
	private PatriciaTree patricia;
	
	public Politicien(PatriciaTree patricia) {
		myident = ident++;
		score = 0;
		letters = null;
		this.patricia = patricia;
	}
	
	public Mot generateWord() {
		letters = bc.getLetters();
		//generate word
		Vector<Lettre> used_letter = new Vector<>();
		Collections.shuffle(letters);
		return null;
		//return new Mot(mot, head, politicien_public_key, signature)
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
}
