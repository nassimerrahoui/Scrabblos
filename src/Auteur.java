import java.net.Socket;

public class Auteur {
	
	static int ident = 0;
	int myident;
	Socket socket;
	 
	
	public Auteur() {
		myident = ident;
		ident++;
	}
	
//	ident = 0
//    def __init__(self,lettres,auteurs, politiciens, mots, blockchain,socket):
//        self.lettres = list()
//        self.auteurs = auteurs
//        self.politiciens = politiciens
//        self.mots = mots
//        self.blockchain = blockchain
}
