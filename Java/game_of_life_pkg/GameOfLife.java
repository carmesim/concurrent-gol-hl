package game_of_life_pkg;

public class GameOfLife {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
        System.out.println("Game of Life !\n");

        // teste
        Cell c1 = new Cell(2);

        // square grid dimension (N x N)
        int n = 4;

        int i_s = n/2 , j_s = n/2;
        if(args.length > 0){
            i_s = Integer.parseInt(args[0]);
            j_s = Integer.parseInt(args[1]);

            // Invalid line / column
            if(i_s < 0 || i_s > n || j_s < 0 || j_s > n){
                System.out.println("Argumentos Inválidos! Digite números de 0 a " + n);
                System.exit(1);
            }
        }else{
            System.out.println("Argumentos de linha de comando não recebidos...");
            System.out.println("Ponto analisado: ("+ i_s +", "+ j_s +")");
        }


        int [][] table = new int [n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                table[i][j] = 0;
                // Glider
                if((i == 1 && j == 2) || (i == 2 && j == 3) || (i == 3 && j >= 1 && j <= 3 )){
                    table[i][j] = 1;
                }
                if(i == i_s && j == j_s){
                    System.out.print(ANSI_RED + table[i][j] + ANSI_RESET + " ");
                }else{
                    System.out.format("%d ", table[i][j]);
                }
            }
            System.out.println("");
        }
        System.out.println("");

        int val  = getNeighbors(table, i_s, j_s);
        System.out.format("Número de vizinhos vivos de (%d, %d): %d \n\n", i_s, j_s, val);

    }

    // Returns the number of alive neighbors of a given (i,j) cell
    public static int getNeighbors(int[][] table, int i, int j){
        int numAliveNeighbors = 0;
        int nRows = table.length;
        int nCols = table[0].length;

        // Up
        if(i != 0 && table[i - 1][j] == 1){
            numAliveNeighbors++;
        }
        // Down
        if((i != nRows - 1) && table[i + 1][j] == 1){
            numAliveNeighbors++;
        }

        // Left
        if(j != 0 && table[i][j - 1] == 1){
            numAliveNeighbors++;
        }
        // Right
        if((j != nCols - 1) && table[i][j + 1] == 1){
            numAliveNeighbors++;
        }
        // Upper-Right Corner
        if((i != 0) && (j != nCols - 1) && table[i - 1][j + 1] == 1){
            numAliveNeighbors++;
        }
        // Lower-Right Corner
        if((i != nRows - 1) && (j != nCols - 1) && table[i + 1][j + 1] == 1){
            numAliveNeighbors++;
        }
        // Upper-Left Corner
        if((i != 0) && (j != 0) && table[i - 1][j - 1] == 1){
            numAliveNeighbors++;
        }
        // Lower-Left Corner
        if((i != nRows - 1) && (j != 0) && table[i + 1][j - 1] == 1){
            numAliveNeighbors++;
        }

        return numAliveNeighbors;
    }
    
}
