import java.util.Vector;

public class Blockchain {
	
	//chaque tour
	private Vector<Lettre> letters;
	private Vector<Mot> words;
	//blockchain courante
	private Vector<Block> blockchain;
	
    private Blockchain(){
    	letters = new Vector<Lettre>();
    	words = new Vector<Mot>();
    	blockchain = new Vector<Block>();
    }
 
    //------------------------MUTEX-------------------------
    private static Blockchain INSTANCE = new Blockchain();
     
    public static Blockchain getInstance(){
    	return INSTANCE;
    }
    
    public Vector<Lettre> getLetters() {
		return letters;
	}
    
    public Vector<Mot> getWords() {
		return words;
	}
    
    
    public Vector<Block> getBlockchain() {
		return blockchain;
	}
        
    /** choisi le meilleur mot du tour actuel, 
     * 	vide la liste de mots et de lettres du tour actuel
     * 	creer un block et l'ajoute à la liste*/
    public void Consensus() {
    	
    }
    
}
