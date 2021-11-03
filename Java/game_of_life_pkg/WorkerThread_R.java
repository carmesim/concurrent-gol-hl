package game_of_life_pkg;

import java.util.List;
import java.util.ArrayList;

class WorkerThread_R implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
	private int n;
    private boolean view_t;
	public int thread_id;
	public int [][] table;
    public int [][] new_table;
    public int n_alive;
    public ArrayList<Integer> row;
    public ArrayList<Integer> col;

    // Construtor
	public WorkerThread_R(int[][] table, int n, int thread_id, ArrayList<Integer> row, ArrayList<Integer> col, boolean view_t) {
            this.n = n;
            this.thread_id = thread_id;
            this.n_alive = 0;
            this.row = row;
            this.col = col;
            this.view_t = view_t;
            // Faz cópia local do grid para a thread
            this.table = new int[n][n];
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    this.table[i][j] = table[i][j];
                }
            }
            
	}

    // Método run, executado por cada thread criada
	public void run() {
        
        n_alive = 0;
        
        // Cria grid que receberá os valores da próxima geração
        new_table = new int[n][n];
        
        // Visualização da área de atuação da thread
        if(view_t){
            try{
                Thread.sleep(100*thread_id);
            }catch(InterruptedException ie){}

            System.out.println("\nMATRIZ A SER PROCESSADA PELA THREAD " + thread_id);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    boolean ponto_pertence = false;
                    for(int k = 0; k < row.size(); k++){
                        int r = row.get(k);
                        int c = col.get(k);
                        if(i == r && j == c){
                            ponto_pertence = true;
                        }
                    }
                    if(ponto_pertence){
                        // Pinta células de vermelho
                        System.out.print(ANSI_RED);
                        System.out.format("%d ", table[i][j]);
                        System.out.print(ANSI_RESET);
                    }else{
                        System.out.format("%d ", table[i][j]);
                    }
                }
                System.out.println("");
            }
            System.out.flush();
        }

        for(int i = 0; i < n; i++){
            new_table[i] = table[i].clone();
        }

        // Aplicação das regras do jogo
        for(int i = 0; i < row.size(); i++){
            int r = row.get(i);
            int c = col.get(i);
            int n_alive_neighbors = getNeighbors(table, r, c);

            boolean cell_is_alive = (table[r][c] == 1);
            if(cell_is_alive){
                //System.out.format(ANSI_RED + "%d "+ ANSI_RESET, table[i][j]);
                // célula viva
                if(n_alive_neighbors < 2){ 
                    // viva e com menos de 2 vizinhos vivos
                    new_table[r][c] = 0;
                }else if(n_alive_neighbors >= 4){
                    new_table[r][c] = 0;
                    // viva e com mais de 4 vizinhos vivos
                }
                // viva e com 2 ou 3 vizinhos vivos [FAZ NADA]
                n_alive++;
            }else{
                // célula morta
                if(n_alive_neighbors == 3){
                    new_table[r][c] = 1; // torna-se viva
                }
            }                
        }
    }

    // Returns the number of alive neighbors of a given (i,j) cell
    public static int getNeighbors(int[][] table, int i, int j){
        int numAliveNeighbors = 0;
        int nRows = table.length;
        int nCols = table[0].length;

        // Up
        if(i != 0){
            if(table[i - 1][j] == 1){
                numAliveNeighbors++;
            }
        }else{
            if(table[nCols - 1][j] == 1){
                numAliveNeighbors++;
            }
        }

        // Down
        if(table[(i + 1)%nRows][j] == 1){
            numAliveNeighbors++;
        }

        // Left
        if(j != 0){
            if(table[i][j - 1] == 1){
                numAliveNeighbors++;
            }
        }else{
            if(table[i][nCols - 1] == 1){
                numAliveNeighbors++;
            }
        }

        // Right
        if(table[i][(j + 1)%nCols] == 1){
            numAliveNeighbors++;
        }

        // Upper-Right Corner
        if((i == 0) && (j == nCols - 1)){
            if(table[nRows - 1][0] == 1){
                numAliveNeighbors++;
            }
        }else{
            // i!=0 || j != n-1
            if(i == 0){
                // já sabemos que j != nCols - 1
                if(table[nRows - 1][j + 1] == 1){
                    numAliveNeighbors++;
                }
            }else{// i != 0
                if(j == nCols - 1){
                    if(table[i - 1][0] == 1){
                        numAliveNeighbors++;
                    }
                }else{
                    if(table[i - 1][j + 1] == 1){
                        numAliveNeighbors++;
                    }
                }
            }
        }

        // Lower-Right Corner
        if(table[(i + 1)%nRows][(j + 1)%nCols] == 1){
            numAliveNeighbors++;
        }

        // Upper-Left Corner
        if((i == 0) && (j == 0)){
                if(table[nRows - 1][nCols - 1] == 1){
                numAliveNeighbors++;
            }
        }else{
            // i!=0 || j != 0
            if(i == 0){
                // já sabemos que j != 0
                if(table[nRows - 1][j -1] == 1){
                    numAliveNeighbors++;
                }
            }else{// i != 0
                if(j == 0){
                    if(table[i - 1][nCols - 1] == 1){
                        numAliveNeighbors++;
                    }
                }else{
                    if(table[i - 1][j - 1] == 1){
                        numAliveNeighbors++;
                    }
                }
            }
        }

        // Lower-Left Corner
        if((i == nRows - 1) && (j == 0)){
                if(table[0][nCols - 1] == 1){
                numAliveNeighbors++;
            }
        }else{
            // i!=n-1 || j != 0
            if(i == nRows - 1){
                // já sabemos que j != 0
                if(table[0][j - 1] == 1){
                    numAliveNeighbors++;
                }
            }else{// i != n-1
                if(j == 0){
                    if(table[i + 1][nCols - 1] == 1){
                        numAliveNeighbors++;
                    }
                }else{
                    if(table[i + 1][j - 1] == 1){
                        numAliveNeighbors++;
                    }
                }
            }
        }

        return numAliveNeighbors;
    }

}
