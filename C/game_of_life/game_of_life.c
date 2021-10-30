#include <stdio.h>
#include <stdlib.h>

#define N 20
#define itera_max 3

int grid [N][N];
int new_grid[N][N];

void inicia_grids_zero(){
	int i, j;
	//iniciando com zero
	for (i = 0; i < N; i++){
		for (j = 0; j < N; j++){
			grid[i][j] = 0;
			new_grid[i][j] = 0;
		}
	}
}

void geracao_inicial(){
	//GLIDER
	int lin = 1, col = 1;
	grid[lin  ][col+1] = 1;
	grid[lin+1][col+2] = 1;
	grid[lin+2][col  ] = 1;
	grid[lin+2][col+1] = 1;
	grid[lin+2][col+2] = 1;
	 
	//R-pentomino
	lin =10; col = 30;
	grid[lin  ][col+1] = 1;
	grid[lin  ][col+2] = 1;
	grid[lin+1][col  ] = 1;
	grid[lin+1][col+1] = 1;
	grid[lin+2][col+1] = 1;
}

int getNeighbors(int i, int j){

	int cont;
	cont = 0;

	if (grid[i-1][j]==1){cont++;}
	if (grid[i+1][j]==1){cont++;}
	if (grid[i][j-1]==1){cont++;}
	if (grid[i][j+1]==1){cont++;}
	if (grid[i-1][j-1]==1){cont++;}
	if (grid[i-1][j+1]==1){cont++;}
	if (grid[i+1][j+1]==1){cont++;}
	if (grid[i+1][j-1]==1){cont++;}

	return cont;
}


void game_of_life(){

	int i;
	int j;




}

int main (){

	int i, j;
	int var;
	int enter;

	inicia_grids_zero();

	geracao_inicial();

	var = getNeighbors(2,2); 

	//preenche o novo grid
	//dps passa o novo grid pro corrente
	for (i = 0; i < itera_max; i++){
		game_of_life();
		printf("ESPERANDO...\n");
		enter = getchar();
	}

	
	



	for (i = 0; i < N; i++){
		for (j = 0; j < N; j++){
			printf("%d", grid[i][j]);
		}
		printf("\n");
	}






	return 0;
}