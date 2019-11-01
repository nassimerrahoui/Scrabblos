
public class Lettre {

	private char lettre;
	private String precedent_hashed;
	private String auteur_public_key;
	
	public Lettre(char lettre, String precedent_hashed, String auteur_public_key) {
		this.lettre = lettre;
		this.precedent_hashed = precedent_hashed;
		this.auteur_public_key = auteur_public_key;
	}
	
	public char getLettre() {
		return lettre;
	}
		
	public String getHead() {
		return precedent_hashed;
	}
	
	public String getAuteur() {
		return auteur_public_key;
	}
}
