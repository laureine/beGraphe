#include <string.h>
#include <stdlib.h>
#include <stdio.h>

/**************************** nombre total de sommets de la carte midip****************************/
//ON FERA PAREIL POUR LES AUTRES CARTES!!!

#define MAX_NOEUDS_MIDIP 150827 

int main(int argc,char **argv){
	char chaine_commande[50];
	int i;
	//pour mettre à jour le seed de la fonction rand()
	srand(time(NULL)); 
	//execute l'algorithme de Djistra ou A* pour 50 couples de points aléatoires
	for(i=0;i<50;i++){

		//origine ne doit pas depasser le nb de sommet!
		int org=rand()% MAX_NOEUDS_MIDIP; 

		//pareil pour destination
		int dest=rand()% MAX_NOEUDS_MIDIP; 

		//on forme une chaine de caractère que la commande shell va exécuter
		sprintf(chaine_commande,"java -jar beGraphe.jar %s %s %s %d %d",argv[1],argv[2],argv[3],org,dest);
		system(chaine_commande);
	}

	return 0;
}
