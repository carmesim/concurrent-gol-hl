package game_of_life_pkg;

import java.util.ArrayList;

public class GameOfLife {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // main function
    public static void main(String[] args) {

        // square grid dimension (N x N)
        int n = 10, n_threads = 4;


        /*int i_s = n/2 , j_s = n/2;
    
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
        }*/

        // Inicializa tabela

        int [][] table = new int [n][n];

        WorkerThread_R[] th_obj;
        Thread[] threads;

        th_obj = new WorkerThread_R[n_threads];
        threads = new Thread[n_threads];

        int step_c = (int) Math.floor(n*n / n_threads);

        ArrayList<Integer> [] lin_th = new ArrayList [n_threads];
        ArrayList<Integer> [] col_th = new ArrayList [n_threads];
        int n_cells = 0, th_num = 0;

        for (int i = 0; i < n_threads; i++) {
			lin_th[i] = new ArrayList<Integer>();
            col_th[i] = new ArrayList<Integer>();
		}

        System.out.format("\nstep_c = %d\n", step_c);

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
        
                if(th_num != n_threads - 1){
                    if(n_cells < step_c){
                        lin_th[th_num].add(i);
                        col_th[th_num].add(j);
                    }else{
                        th_num++;
                        lin_th[th_num].add(i);
                        col_th[th_num].add(j);
                        n_cells = 0;
                    }
                }else{
                    lin_th[th_num].add(i);
                    col_th[th_num].add(j);
                }

                n_cells ++;
                table[i][j] = 0;
            }
        }

        for (int i = 0; i < n_threads; i++) {
            System.out.println("Th "+i+"\nlinhas: "+lin_th[i].toString()+"\ncols: "+col_th[i].toString());
        }

        try
        {
            char rv = (char) System.in.read();
            
        }catch(Exception e)
        {}



        //GLIDER
        int lin = 1, col = 1;
        table[lin  ][col+1] = 1;
        table[lin+1][col+2] = 1;
        table[lin+2][col  ] = 1;
        table[lin+2][col+1] = 1;
        table[lin+2][col+2] = 1;
/*
        //R-pentomino
        lin =10; col = 30;
        table[lin  ][col+1] = 1;
        table[lin  ][col+2] = 1;
        table[lin+1][col  ] = 1;
        table[lin+1][col+1] = 1;
        table[lin+2][col+1] = 1;
*/   
        
        long startTime = System.currentTimeMillis();
        
        int cont = 0, n_alive = 0;
        while(cont < 100){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            System.out.println("Geração " + (cont + 1));

            // ===============
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){

                    
                    boolean cell_is_alive = false; 
                    if(table[i][j] == 1){
                        cell_is_alive = true;
                    }

                    
                    if(cell_is_alive){
                        System.out.format(ANSI_RED + "%d "+ ANSI_RESET, table[i][j]);
                    }else{
                        System.out.format("%d ", table[i][j]);
                    }
                }
                System.out.println("");
            }
            // ===============



            // Cria as threads trabalhadoras
            for(int i = 0; i < n_threads; i++){
                th_obj[i] = new WorkerThread_R(table, n, i, lin_th[i], col_th[i]);
                threads[i] = new Thread(th_obj[i]);
                threads[i].start();
            }

            n_alive = 0;
            try{
                for(int i = 0; i < n_threads; i++) {
                    threads[i].join();
                    n_alive += th_obj[i].n_alive;
                    for(int j = 0; j < lin_th[i].size(); j++) {
                        int r = lin_th[i].get(j);
                        int c = col_th[i].get(j);
                        table[r][c] = th_obj[i].new_table[r][c];
                    }
                }
            }catch(InterruptedException ie){}

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
            }catch(Exception e)
            {}
            
        }

        long calcTime = System.currentTimeMillis() - startTime;

        System.out.println("Simulação Finalizada em "+calcTime+" ms!\n");
        System.out.format("%d células sobreviveram\n", n_alive);

        
        /*
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                //table[i][j] = 0;

                // Glider
                //if((i == 1 && j == 2) || (i == 2 && j == 3) || (i == 3 && j >= 1 && j <= 3 )){
                //    table[i][j] = 1;
                //}

                if(i == i_s && j == j_s){
                    System.out.print(ANSI_RED + table[i][j] + ANSI_RESET + " ");
                }else{
                    System.out.format("%d ", table[i][j]);
                }
            }
            System.out.println("");
        }
        System.out.println("");
        */

        //int val  = getNeighbors(table, i_s, j_s);
        //System.out.format("Número de vizinhos vivos de (%d, %d): %d \n\n", i_s, j_s, val);
        
        //Aqui ficava o while com o laço principal.
    }
    
}
