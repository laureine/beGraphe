package core;


public class Label implements Comparable<Label> {
	
	/**
	 * composition d'un Label :
	 * 
	 * marquage : un booléen (valant vrai si le sommet est définitivement fixé par l'algorithme)
	 * sommetCourant: le sommet associé à ce label (sommet ou numéro de sommet)
	 * sommetPere : le sommet précédent sur le chemin correspondant au plus court chemin courant
	 * coutCourant : la valeur courante du plus court chemin depuis l'origine vers le sommet courant
	 * coutEstimation: la valeur du cout entre le sommet courant et la destination
	 * coutTotal: la somme des coutCourant et coutEstimation (valeur d'un cout total entre le sommet initial et la destination)
	 */
	
	protected int id_sommetCourant;
	protected int id_sommetPere;
	protected double coutCourant;
	protected boolean marquage;
	protected boolean isAstar;
	//Variables à utiliser pour A*
	protected double coutEstimation = 0;
    protected double coutTotal;
    
	
	/**
	 * constructeurs
	 * un label par défault : pas de père, coût infini, non marqué
	 */
	public Label(int id_sommetCourant, boolean isAstar){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = -1;
		this.coutCourant = Double.MAX_VALUE;
		this.coutEstimation = this.getCoutEstimation();
		this.coutTotal = Double.MAX_VALUE;
		this.marquage = false;
		this.isAstar = isAstar;
	}


	public Label(int id_sommetCourant, int id_sommetPere, double coutCourant, boolean marquage, boolean isAstar){
		this.id_sommetCourant = id_sommetCourant;
		this.id_sommetPere = id_sommetPere;
		this.coutCourant = coutCourant;
		this.coutEstimation = this.getCoutEstimation();
		this.coutTotal = this.getCoutTotal();
		this.marquage = marquage;
		this.isAstar = isAstar;
	}
	
	
	/**
	 * Compares this object with the specified object for order. 
	 * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Label lb1) 
	{
		if (!isAstar)
		{
			if (this.coutCourant == lb1.getCoutCourant())
				return 0 ;
			else if (this.coutCourant < lb1.getCoutCourant())
				return -1 ;
			else
				return 1 ;
		}
		else
		{
			if (this.coutTotal < lb1.getCoutTotal() || ((this.coutTotal== lb1.getCoutTotal()) && this.coutEstimation < lb1.getCoutEstimation()))
				return -1 ;
			
			else if (coutTotal == lb1.getCoutTotal() && this.coutEstimation == lb1.getCoutEstimation()) 
				return 0 ;
			
			else 
				return 1 ;
		}
	}
	
	/**
	 * getters & setters
	 */
	
	public double getCoutCourant() {
		return coutCourant;
	}

	public int getId_sommetCourant() {
		return id_sommetCourant;
	}
	
	public int getId_sommetPere() {
		return id_sommetPere;
	}
	
	public double getCoutEstimation() {
		return coutEstimation;
	}
	
	public double getCoutTotal() {
		return coutTotal;
	}
	
	public boolean isMarquage() {
		return marquage;
	}
	
	public void setCoutCourant(double coutCourant) {
		this.coutCourant = coutCourant;
	}

	public boolean isMarque() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

	public void setId_sommetCourant(int id_sommetCourant) {
		this.id_sommetCourant = id_sommetCourant;
	}

	public void setId_sommetPere(int id_sommetPere) {
		this.id_sommetPere = id_sommetPere;
	}


	public void setCoutEstimation(double coutEstimation) {
		this.coutEstimation = coutEstimation;
	}


	public void setCoutTotal(double CoutCourant,double CoutEstimation, double coutArete) {
		coutTotal = CoutCourant + CoutEstimation + coutArete ;
	}


}

