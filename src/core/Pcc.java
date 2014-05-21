package core ;
import java.awt.Color;
import java.io.* ;
import java.util.HashMap;
import base.* ;

	/**
	 * La classe Pcc va implémenter l'algorithme de Dijkstra
	 */

public class Pcc extends Algo {

    protected int zoneOrigine ;
    protected int zoneDestination ;
    protected int origine ;
    protected int destination ;
    
    protected Dessin dessin ;
    
    protected long tempsExecution;
    
    // existence d'un plus court chemin
	protected boolean existencePCC = false;
	
    // booléan permettant aux utilisateurs de choisir le coût en temps ou en distance
    protected int isEnTemps;
    
    // HashMap qui met en correspondance le numéro de noeud et son label
    private HashMap<Integer, Label> mapCorrespondanceNoeudLabel = new HashMap<Integer, Label>();
    
	// on a le tas pour y mettre tous les labels (pour ordonner le coût courant)
	BinaryHeap<Label> tasLabel= new BinaryHeap<Label>();
	
	// nombre de sommets dans le graphe
	private int numNoeudGraphe = this.graphe.getListeNoeuds().size();
	
	// nombre maximal des elements présents dans le tas
	private int nbMaxTas;
	
	// le plus court chemin obtenu par algorithme de Dijkstra
	private Chemin plusCourtChemin = new Chemin();
	
	// le nombre de sommets marqués
	private int nbMarque=0;
	
	// le nombre de sommets parcourus 
	private int nbParcouru=0;
	
    // booléan qui permet utilisateurs à choisir entre l'algo Dijkstra(0) ou A*(1)
    protected int choixAlgo;
	
	/**
	 * constructeur, ici, notre fonction qui lit tous les paramètres d'entrée
	 */
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		// demander la zone et le sommet départ.
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// demander la zone et le sommet destination.
		this.zoneDestination = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		
		// demander l'utilisateur à choisir entre Dijsktra et A Star
		this.choixAlgo = readarg.lireInt ("Dijkstra(0) ou A Star(1) ?");
		
		// demander l'utilisateur à choisir entre temps et distance
		this.isEnTemps = readarg.lireInt ("Distance(0) ou Temps(1) ?");
		
		// Demander le dessin du graphe
		dessin = gr.getDessin() ;
    }

	/**
	 * les deux fonctions qui vérifient la bonne saisie du numéro de noeud départ/destination
	 */
	public boolean verifierSaisirNoeudDepart(){
    	boolean trouveOrigine = false;
		for (int i=0; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == origine)
    			trouveOrigine = true;
		}
		return trouveOrigine;
	}
	
	public boolean verifierSaisirNoeudDest(){
    	boolean trouveDest = false;
		for (int i=0; i<numNoeudGraphe ; i++){
    		if(this.graphe.getListeNoeuds().get(i).getId_noeud() == destination)
    			trouveDest = true;
		}
		return trouveDest;
	}
	
	/**
	 * initialise le tas, et met les labels des sommets dans le hashmap
	 */
	public void initialisationAlgos(){
 	    
     // on met les labels de tous les noeuds dans la map de correspondance "numéro de noeud, label"
     for (int i=0; i<numNoeudGraphe ; i++){
    		int numNoeudCourant = this.graphe.getListeNoeuds().get(i).getId_noeud();
    		
    	//Initialisation de l'algorithme de Djistra	
    	if (choixAlgo==0){
    		if(numNoeudCourant == origine){
    			// Affichage du sommet origine sur la carte
    	    	   dessin.setColor(Color.green) ;
    	    	   dessin.drawPoint(this.graphe.getListeNoeuds().get(origine).getLongitude(), this.graphe.getListeNoeuds().get(origine).getLatitude(), 2) ;
    	    	
    	    	//initialisation du label du sommet d'origine et insertion dans le tas
    	    		Label labelOrigine = new Label(origine, -1, 0, false, false) ;
    	    		mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
        			tasLabel.insert(labelOrigine);
        			nbParcouru++;
        	}
        	else 
            		mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i, false));
    	}
            		
    	 //Initialisation de l'algorithme de A* 
    	else{
    		if(numNoeudCourant == origine){
    			// Affichage du sommet origine sur la carte
    	    	   dessin.setColor(Color.green) ;
    	    	   dessin.drawPoint(this.graphe.getListeNoeuds().get(origine).getLongitude(), this.graphe.getListeNoeuds().get(origine).getLatitude(), 2) ;
    	    	
    	    	//initialisation du label du sommet d'origine et insertion dans le tas
    	    		Label labelOrigine = new Label(origine, -1, 0, false, true) ;
    	    		mapCorrespondanceNoeudLabel.put(numNoeudCourant, labelOrigine);
        			tasLabel.insert(labelOrigine);
        			nbParcouru++;
        	}
        	else 
            		mapCorrespondanceNoeudLabel.put(numNoeudCourant, new Label(i, true));
    	    		
    	}
    			
    }
    		
    	
 }
    	
    /**
     * algorithme de Dijkstra et A star avec le tas
     * à utilisateur de choisir l'algorithme à lancer
	/* complexité de l'algorithme de Djistra: o(nlogn) 
	 * complexité de l'algorithme de A Star: o(nlogn)
	 */
     
    public void algoPCC(){
    	
    	// commencer le calcul temps d'exécution de l'algorithme
    	long debut = System.nanoTime();
    	// initialiser l'algo
    	this.initialisationAlgos();
    	// tourner l'algo jusqu'à le noeud destination soit marqué
		do{
	    	// si le tas n'est pas vide
	    	if(!(tasLabel.isEmpty())) {
	        	// on choisie le label ayant le plus petit coût dans le tas
	        	Label labelCourant = tasLabel.deleteMin();
	        	// on le marque
	        	labelCourant.setMarquage(true);
	        	// on décrémente le nombre d'élément dans le tas, et incrémente le nombre d'éléments marqués
	        	nbMaxTas--;
	        	nbMarque++;
	        	
	        	// pour ce label, on obtient le noeud correspondant
	        	Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetCourant());
		        	// pour tous les successeurs (tous les arcs sortants) de ce noeud
		        	for (Successeur succ : noeudCourant.getListeSuccesseur()){
		        		// on choisie un noeud successeur courant
		        		Noeud noeudSuccCourant = succ.getNoeudDestination();
		        		// on obtient son label à partir de son numéro
		        		Label labelNoeudSuccCourant = mapCorrespondanceNoeudLabel.get(noeudSuccCourant.getId_noeud());
		        		// si ce noeud successeur n'est pas encore marqué par algo (ie si son marquage est encore à false)
			    		if(!labelNoeudSuccCourant.isMarque()){

			    			// si l'utilisateur a choisi l'algo de Dijkstra, on met estimation à 0
			    			if(choixAlgo == 0){
			    				// si l'utilisateur a choisi pcc Dijkstra en distance
				    			if(isEnTemps == 0) {
				    				labelCourant.setCoutEstimation(0);
				    				labelNoeudSuccCourant.setCoutTotal(labelCourant.getCoutCourant(), labelCourant.getCoutEstimation(), succ.getLongueurArrete());
				    			}
				    			// si l'utilisateur a choisi pcc Dijkstra en temps
				    			if(isEnTemps == 1)
				    				labelCourant.setCoutEstimation(0);
				    				labelNoeudSuccCourant.setCoutTotal(labelCourant.getCoutCourant(),labelCourant.getCoutEstimation(),succ.getTempsArrete());
				    		}
			    			
			    			// si l'utilisateur a choisi l'algo de A Star, on règle l'estimation
			    			if(choixAlgo == 1){
			    				// on récupère le noeud destination
			    				Noeud noeudDestination = this.graphe.getListeNoeuds().get(destination);
			    				// si l'utilisateur a choisi PCC A Star en distance
				    			if(isEnTemps == 0) {
				    				labelCourant.setCoutEstimation(calculerEstimationDistance(noeudSuccCourant,noeudDestination));
				    				labelNoeudSuccCourant.setCoutTotal(labelCourant.getCoutCourant(),labelCourant.getCoutEstimation(),succ.getLongueurArrete()); 
				    			}
				    			
				    			// si l'utilisateur a choisi PCC A Star en temps
				    			if(isEnTemps == 1){
				    				labelCourant.setCoutEstimation(calculerEstimationTemps(noeudSuccCourant,noeudDestination));
				    				labelNoeudSuccCourant.setCoutTotal( labelCourant.getCoutCourant(),succ.getTempsArrete(),succ.getLongueurArrete());
				    			}
				    			
			    			}
			    			
			    		
			    			// si cette fois, le coût total obtenu est inférieur à son coût total d'avant
			    			if (labelNoeudSuccCourant.getCoutTotal() < labelNoeudSuccCourant.getCoutCourant()) {
			    				// on remplace l'ancien coût avec ce nouveau coût total
			    				labelNoeudSuccCourant.setCoutCourant(labelNoeudSuccCourant.getCoutTotal());
			    				// on change son père à ce noeud courant
			    				labelNoeudSuccCourant.setId_sommetPere(noeudCourant.getId_noeud());
			    			}
			    			
			    			// si ce noeud est déjà présent dans le tas
			    			if(tasLabel.isInHeap(labelNoeudSuccCourant)){
		    				// on met à jour le tas pour l'éventuel changement de coût de ce noeud courant
					    		tasLabel.update(labelNoeudSuccCourant);
				    		}
			    			// sinon
			    			else{
			    				// on le met dans le tas
			    				tasLabel.insert(labelNoeudSuccCourant);
			    				nbParcouru++;
			    				nbMaxTas++;
			    				//on met à jour le tas
			    				updateNbElementMaxTas();
			    				// on dessine un segment entre noeudCourant et son succCourant
								this.graphe.getDessin().setColor(Color.BLUE);
								this.graphe.getDessin().setWidth(2);
								this.graphe.getDessin().drawLine(noeudCourant.getLongitude(), noeudCourant.getLatitude(), noeudSuccCourant.getLongitude(), noeudSuccCourant.getLatitude());
			    			}
			    		}
		        	} 
	    	}
	    	else
	    		// s'il n'existe pas de plus court chemin entre ces deux noeuds, on sort de cette fonction avec "return"
		    	return;
    	} while(!mapCorrespondanceNoeudLabel.get(destination).isMarque());
		// on termine le calcul de temps d'exécution de l'algo
		tempsExecution = System.nanoTime() - debut;
		// si l'algo est arrivé jusqu'ici, le Pcc existe
		existencePCC = true;	
	
    }
    
    /**
     * fonction qui met à jour le nombre maximal des elements dans le tas
     */
    public void updateNbElementMaxTas(){
    	if(tasLabel.size() > nbMaxTas)
    		nbMaxTas = tasLabel.size();
    }
    
    /**
     * fonction qui construit le plus court chemin avec une recherche dans le sens inverse
     */
    public void construirePlusCourtChemin(){
    	Noeud noeudDepart = this.graphe.getListeNoeuds().get(origine);
    	Noeud noeudDestination = this.graphe.getListeNoeuds().get(destination);
    	// on place le noeud de destination dans le chemin
    	plusCourtChemin.addNoeud(noeudDestination);	
    	Label labelCourant = mapCorrespondanceNoeudLabel.get(destination);
    	// on cherche parmi tous les noeuds qui ont été marqués
    	
    	for(int i = 0; i < nbMarque;i++){
    		// si le noeud courant n'est pas le noeud départ
    		while(labelCourant.getId_sommetPere() != -1){
    			// on cherche son père, puis ajoute le père dans le chemin
    			Noeud noeudCourant = this.graphe.getListeNoeuds().get(labelCourant.getId_sommetPere());
    			plusCourtChemin.addNoeud(noeudCourant);
    			labelCourant = mapCorrespondanceNoeudLabel.get(labelCourant.getId_sommetPere());
    		}
    	}
    	// on met le chemin dans la bonne ordre
		plusCourtChemin.renverserChemin();
    	// compléter les information de PCC
    	plusCourtChemin.setNoeudDepart(noeudDepart);
    	plusCourtChemin.setNoeudDestination(noeudDestination);
    	plusCourtChemin.setNbNoeud(plusCourtChemin.getListeNoeudChemin().size());
    }
    
	// calculer l'estimation entre noeud courant et noeud destination
	public double calculerEstimationDistance(Noeud noeudSuccCourant, Noeud noeudDestination){
		float latitude_noeudSuccCourant = noeudSuccCourant.getLatitude();
		float longitude_noeudSuccCourant = noeudSuccCourant.getLongitude();
		float latitude_noeudDestination = noeudDestination.getLatitude();
		float longitude_noeudDestination = noeudDestination.getLongitude();
		return (double)Graphe.distance(longitude_noeudSuccCourant, latitude_noeudSuccCourant, 
				longitude_noeudDestination, latitude_noeudDestination);
	}
	
	// calculer l'estimation en temps 
	public double calculerEstimationTemps(Noeud noeudSuccCourant, Noeud noeudDestination){
		double coutEstimationDistance = calculerEstimationDistance(noeudSuccCourant,noeudDestination);
		// on prend ici la vitesse maximale 130 km/h, retourner le temps en seconde
		return 3600*(coutEstimationDistance / (130*1000));
	}
    
    /**
     * afficher des informations de plus court chemin et le dessiner
     */
    public void afficherPCC(){    	
    	
    	// on construit le chemin
    	construirePlusCourtChemin();
    	
    	// affichage le nombre noeuds dans Pcc
		System.out.println("Le nombre de noeud dans le plus court chemin est: " + plusCourtChemin.getListeNoeudChemin().size());
		
    	// affichage de tous les noeuds dans le Pcc
		/*for (int i=0;i < plusCourtChemin.getNbNoeud();i++)
		{
			System.out.println("Le sommet " + (i+1) + " est: " + plusCourtChemin.getListeNoeudChemin().get(i).getId_noeud());
		}*/
		
    	// affichage nombre de noeuds parcourus
		System.out.println("Le nombre de sommets parcourus est : " + nbParcouru);
		
    	// affichage nombre max de noeuds dans le tas
		System.out.println("Le nombre maximal des sommets dans le tas est : " + nbMaxTas);
		
    	// affichage nombre de noeuds marqué
		System.out.println("Le nombre des sommets marqués : " + nbMarque);
		
    	// affichage en distance de PCC
		if(isEnTemps == 0)
			plusCourtChemin.affichageCoutEnDistance();
    	// affichage en temps de PCC
		if(isEnTemps == 1)
			plusCourtChemin.affichageCoutEnTemps();
		
		// dessiner le chemin sur la carte
		plusCourtChemin.dessinerChemin(this.graphe.getDessin());
    }
    
    /**
     * fonction principale qui lance l'algo, affiche les résultats et donne le temps d'exécution de l'algo
     */
    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		
		// si la saisie est erronée ou c'est le même noeud, inutile de lancer Dijkstra
		if (!verifierSaisirNoeudDepart())
			System.out.println("Le sommet d'origine " + origine + " n'existe pas dans cette carte.");
		else if (!verifierSaisirNoeudDest())
			System.out.println("Le sommet destination " + destination + " n'existe pas dans cette carte.");
		else if (origine == destination)
			System.out.println("Le plus court chemin est 0.");
		// si la saisie est bonne, on lance Dijkstra
		else {
			algoPCC();
			// s'il existe le Pcc, on affiche des informations et le temps exécution
			if(existencePCC == true){
				afficherPCC();
				System.out.println("Le temps d'exécution de algo Dijkstra est "+ tempsExecution + " ns.");
			}
			// sinon
			else {
	    		System.out.println("Il n'existe pas de chemin entre noeud "+ origine + " et noeud " +destination);
	    		System.out.println("Algo Dijkstra est arrêté.");
			}
		}
    }
}

