import java.util.Vector;

public class Mot {

	private Vector<Lettre> mot;
	private String precedent_hashed;
	private String politicien_public_key;
	
	public Mot(Vector<Lettre> mot, String precedent_hashed, String politicien_public_key) {
		this.mot = mot;
		this.precedent_hashed = precedent_hashed;
		this.politicien_public_key = politicien_public_key;
	}
	
	public Vector<Lettre> getMot() {
		return mot;
	}
	
	
	public String getHead() {
		return precedent_hashed;
	}
	
	public String getAuteur() {
		return politicien_public_key;
	}
}
