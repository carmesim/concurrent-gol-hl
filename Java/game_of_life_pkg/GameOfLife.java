package game_of_life_pkg;

import java.util.ArrayList;

public class GameOfLife {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // Função Principal
    public static void main(String[] args) {

        // n: dimensão do grid (n x n)
        // n_threads: número de threads para execução do código
        int n = 10, n_threads = 4;
        
        String opt = "";

        // trata de argumentos de linha de comando
        if(args.length > 0){
            n = Integer.parseInt(args[0]);
            if(args.length > 1){
                n_threads = Integer.parseInt(args[1]);
                if(args.length > 2)
                opt = args[2];                    
                
            }
        }else{
            System.out.println("Argumentos de linha de comando não recebidos...");
            System.out.format("Rodando com grid (%d x %d) e %d threads\n", n,n,n_threads);
        }

        // opções adicionais de visualização pelo usuário
        boolean visual = false;
        boolean manual_iter = false;       
        boolean view_threads = false;

        if(opt.matches("-.*v.*")) {
            visual = true;
            if(opt.matches("-.*m.*")) manual_iter = true;
            if(opt.matches("-.*t.*")) view_threads = true;
        }

        // Cria grid (n x n)
        int [][] table = new int [n][n];

        // Cria e instancia vetores de objetos refrentes às threads
        WorkerThread_R[] th_obj; // objetos runnable
        Thread[] threads; // objetos thread

        th_obj = new WorkerThread_R[n_threads]; 
        threads = new Thread[n_threads];

        // Quantidade padrão de células que cada thread terá de lidar
        //    - floor( total de células (n^2) dividido pelo número de threads )
        // obs: não se aplica à ultima thread (threads[n_threads - 1])
        int cells_per_thread = (int) Math.floor(n*n / n_threads);

        
        ArrayList<Integer> [] lin_th = new ArrayList [n_threads];
        ArrayList<Integer> [] col_th = new ArrayList [n_threads];
        int n_cells = 0, th_num = 0;

        for (int i = 0; i < n_threads; i++) {
			lin_th[i] = new ArrayList<Integer>();
            col_th[i] = new ArrayList<Integer>();
		}

        // Faz a divisão do trabalho entre as threads
        // -- cada thread recebe dois ArrayLists
        // -- linhas e colunas que cada uma deve checar
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(th_num != n_threads - 1){
                    if(n_cells < cells_per_thread){
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


        // Condições Iniciais
        int lin, col;

        if(n >=4){
            //GLIDER
            lin = 1; col = 1;
            table[lin  ][col+1] = 1;
            table[lin+1][col+2] = 1;
            table[lin+2][col  ] = 1;
            table[lin+2][col+1] = 1;
            table[lin+2][col+2] = 1;
        }

        if(n >= 33 ){
            //R-pentomino
            lin =10; col = 30;
            table[lin  ][col+1] = 1;
            table[lin  ][col+2] = 1;
            table[lin+1][col  ] = 1;
            table[lin+1][col+1] = 1;
            table[lin+2][col+1] = 1;
        }
 

        int cont = 0, n_alive = 0;

        // Começa contagem do tempo de execução
        long startTime = System.currentTimeMillis();       

        while(cont < 2000){

            // Visualização do grid
            if(visual){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Geração " + (cont + 1));

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
            }

            // Cria as threads trabalhadoras
            for(int i = 0; i < n_threads; i++){
                th_obj[i] = new WorkerThread_R(table, n, i, lin_th[i], col_th[i], view_threads);
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

            // Resumo da geração 
            if(visual){                
                System.out.format("\n[%d CÉLULAS VIVAS]\n", n_alive);
                System.out.println("\n- - - - - - - - - - - - - - - - ");
                if(manual_iter){
                    System.out.println("| "+ANSI_RED+"q"+ANSI_RESET+" - sair");
                    System.out.println("| outra tecla - continuar");
                    System.out.println("- - - - - - - - - - - - - - - - \n");
                    
                    // aguarda avanço do usuário
                    try
                    {
                        char rv = (char) System.in.read();
                        if(rv == 'q'){
                            break;
                        }
                    }catch(Exception e)
                    {}
                }else{
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException ie){}
                }
            }
            cont++;            
        }

        // Finaliza cálculo do tempo de execução
        long calcTime = (System.currentTimeMillis() - startTime)/1000;

        System.out.println("\nSimulação Finalizada em "+calcTime+" s!");
        System.out.println(n_threads+" threads foram utilizadas\n");
        System.out.format("[%d células sobreviveram]\n\n", n_alive);
    }
    
}
