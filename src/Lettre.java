
public class Lettre {

	private char lettre;
	private String head;
	private String auteur_public_key;
	private String signature;
	
	public Lettre(char lettre, String head, String auteur_public_key, String signature) {
		this.lettre = lettre;
		this.head = head;
		this.auteur_public_key = auteur_public_key;
		this.signature = signature;
	}
	
	public char getLettre() {
		return lettre;
	}
		
	public String getHead() {
		return head;
	}
	
	public String getAuteur() {
		return auteur_public_key;
	}
	
	public String getSignature() {
		return signature;
	}
}
