import java.util.Vector;

public class Mot {

	private Vector<Lettre> mot;
	private String precedent_hashed,politicien_public_key;
	
	public Mot(Vector<Lettre> mot, String precedent_hashed, String politicien_public_key) {
		this.mot = mot;
		this.precedent_hashed = precedent_hashed;
		this.politicien_public_key = politicien_public_key;
	}
	
	public Vector<Lettre> getMot() {return mot;}
	public String getPoliticien_public_key() {return politicien_public_key;}
	public String getPrecedent_hashed() {return precedent_hashed;}
	
	public String get_full_word() {
		String s = "";
		for(Lettre l : mot) s+= l.getLettre();
		return s;
	}	
}
