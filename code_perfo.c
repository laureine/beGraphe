#include <string.h>
#include <stdlib.h>
#include <stdio.h>

/**************************** nombre total de sommets de la carte midip ********************************/
//ON FERA PAREIL POUR LES AUTRES CARTES!!!

#define MAX_NOEUDS_MIDIP 150827 

int main(int argc,char **argv){

	char cmd[200];
	int i;

	char* fich ="test";
	int s=0, k=0, l=0, m=0; j=2;
		

	//pour mettre à jour le seed de la fonction rand()
	srand(time(NULL)); 

	//execute l'algorithme de Djistra ou A* pour 50 couples de points aléatoires
	for(i=0;i<50;i++){

		//origine ne doit pas depasser le nombre de sommets!
		int org=rand()% MAX_NOEUDS_MIDIP; 

		//pareil pour destination
		int dest=rand()% MAX_NOEUDS_MIDIP; 	

		//on forme une chaine de caractère que la commande shell va exécuter

		sprintf(cmd,"%s %s %s %s %d %d %s %d %d %d %d %d",argv[1],argv[2],argv[3],argv[4],s,j,fich,org,dest,k,l,m);
		//cmd correspond à "java -jar beGraphe.jar midip 0 2 test 3555 3535 0 0 0"
		system(cmd);	
		
	}

	return 0;
}
