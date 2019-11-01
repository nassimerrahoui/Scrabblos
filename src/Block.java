public class Block {
	private Mot mot;
	
	public Block(Mot mot) {
		this.mot = mot;
	}
	
	public Mot getMot() {
		return mot;
	}
}

//private Vector<Lettre> lettres_auteurs;
//private Block precedent;
//private String politician_public_key;
//
//public Block(Mot mot, Vector<Lettre> lettres_auteurs, Block precedent, String politician_public_key) {
//	this.mot = mot;
//	this.lettres_auteurs = lettres_auteurs;
//	this.precedent = precedent;
//	this.politician_public_key = politician_public_key;
//}
//
//public Mot getMot() {
//	return mot;
//}
//
//public Vector<Lettre> getLettres_auteurs() {
//	return lettres_auteurs;
//}
//
//public Block getPrecedent() {
//	return precedent;
//}
//
//public String getPolitician_public_key() {
//	return politician_public_key;
//}