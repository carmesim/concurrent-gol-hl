package game_of_life_pkg;

public class GameOfLife {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // main function
    public static void main(String[] args) {

        // square grid dimension (N x N)
        int n = 20;

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

        // Inicializa tabela

        int [][] table = new int [n][n];


        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                table[i][j] = 0;
            }
        }
        //GLIDER
        int lin = i_s, col = j_s;
        table[lin  ][col+1] = 1;
        table[lin+1][col+2] = 1;
        table[lin+2][col  ] = 1;
        table[lin+2][col+1] = 1;
        table[lin+2][col+2] = 1;

        //GLIDER 2
        
        /*
        lin = 3;
        col = 8;
        table[lin  ][col-1] = 1;
        table[lin-1][col-2] = 1;
        table[lin-2][col  ] = 1;
        table[lin-2][col-1] = 1;
        table[lin-2][col-2] = 1;
        */


        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                //table[i][j] = 0;

                // Glider
                /*if((i == 1 && j == 2) || (i == 2 && j == 3) || (i == 3 && j >= 1 && j <= 3 )){
                    table[i][j] = 1;
                }*/

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
        
        int cont = 0, n_alive;
        while(cont < 1e6){
            n_alive = 0;
            System.out.print("\033[H\033[2J");
            System.out.flush();

            int [][] new_table = new int[n][];
            for(int i = 0; i < n; i++){
                new_table[i] = table[i].clone();
            }
            System.out.println("========= Conway's Game of Life =========\n");
            System.out.format("GERAÇÃO %d:\n", cont+1);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){

                    int n_alive_neighbors = getNeighbors(table, i, j);
                    boolean cell_is_alive = (table[i][j] == 1);

                    
                    if(cell_is_alive){
                        System.out.format(ANSI_RED + "%d "+ ANSI_RESET, table[i][j]);
                        // célula viva
                        if(n_alive_neighbors < 2){ 
                            // viva e com menos de 2 vizinhos vivos
                            new_table[i][j] = 0;
                        }else if(n_alive_neighbors >= 4){
                            new_table[i][j] = 0;
                            // viva e com mais de 4 vizinhos vivos
                        }
                        // viva e com 2 ou 3 vizinhos vivos [FAZ NADA]
                        n_alive++;
                    }else{
                        System.out.format("%d ", table[i][j]);
                        // célula morta
                        if(n_alive_neighbors == 3){
                            new_table[i][j] = 1; // torna-se viva
                        }
                    }

                }
                System.out.println("");
            }


            for(int i = 0; i < n; i++){
                table[i] = new_table[i].clone();
            }

            cont++;

            System.out.format("\n[%d CÉLULAS VIVAS]\n", n_alive);
            System.out.println("\n- - - - - - - - - - - - - - - - ");
            System.out.println("| "+ANSI_RED+"q"+ANSI_RESET+" - sair");
            System.out.println("| outra tecla - continuar");
            System.out.println("- - - - - - - - - - - - - - - - \n");

            try
            {
                char rv = (char) System.in.read();
                if(rv == 'q'){
                    break;
                }


            }
            catch(Exception e)
            {}
        }
        System.out.println("\nSimulação Terminada !\n");
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
