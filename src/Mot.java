import java.util.Vector;

public class Mot {

	private Vector<Lettre> mot;
	private String head;
	private String politicien_public_key;
	
	public Mot(Vector<Lettre> mot, String head, String politicien_public_key) {
		this.mot = mot;
		this.head = head;
		this.politicien_public_key = politicien_public_key;
	}
	
	public Vector<Lettre> getMot() {
		return mot;
	}
	
	
	public String getHead() {
		return head;
	}
	
	public String getAuteur() {
		return politicien_public_key;
	}
}
