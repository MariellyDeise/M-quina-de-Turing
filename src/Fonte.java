import java.util.Scanner;

public class Fonte {
   static Scanner scanf = new Scanner(System.in);

   static String negrito = "\033[1m";
   static String reset = "\033[0m";
   static String palavra_String;
   
   static int quant_estados;
   static int quant_alfabeto;
   static int quant_alfabeto_auxiliar;
   static int quant_estados_finais;
   static int indice_palavra;
   static int estado_atual; 
   static int indice_result;
   static int[] vet_estados_finais;
   static int[][] transicoes;

   static char[] palavra_vetChar;
   static char[] result_alfabeto;
   static char[] alfabeto_char;
   static char[] alfabeto_auxiliar_char;
   static char[][] direcao;
   static char[][] alfabeto_futuro;

   static boolean aceita;
   static boolean palavra_vazia;

   static void Introduzir_palavra(){
    scanf.nextLine();

        System.out.print("Insira a palavra a ser processada: ");
        palavra_String = scanf.nextLine();

        indice_palavra = palavra_String.length();
        palavra_vetChar = palavra_String.toCharArray();
   }

    static void inicial(){
        System.out.println("-----------------------------Linguagem Formais, Automatos e Computabilidade-----------------------------");
        System.out.println("* Marielly Deise Rodrigues Tiago.*");
        System.out.println("--------Maquina de Turing--------\n\n");
        System.out.println(negrito + "ORIENTAÇÕES DE USO:" + reset);
        System.out.println(" - O '-1' representa a inexistencia de transicoes.");
        System.out.println(" - O estado inicial sempre sera o de numero 1, os demais estados seguirao a sequencia de acordo com a quantidade de estado defido.");
        System.out.println(" - O marcador de inicio sera <.");
        System.out.println(" - O branco sera >.\n\n");

        boolean confirma = true;
        System.out.println("Informe a quantidade de estados:");
        do{
            quant_estados = scanf.nextInt();
            if(quant_estados < 1){
                System.out.println("Erro: informe um numero maior ou igual a '1'.\n");
                System.out.println("Digite novamente: ");
                confirma = false;
            }else{
                confirma = true;
            }
        }while(!confirma);
        
        confirma = true;
        System.out.println("Informe a quantidade de caracteres que compoem o alfabeto:");
        do{
            quant_alfabeto = scanf.nextInt();
            if(quant_alfabeto < 1){
                System.out.println("Erro: informe um numero maior ou igual a '1'.\n");
                System.out.println("Digite novamente: ");
                confirma = false;
            }else{
                confirma = true;
            }
        }while(!confirma);

        alfabeto_char= new char[quant_alfabeto];
        for(int i = 0; i < quant_alfabeto; i++){
            System.out.println("Informe o " + (i+1) + "° caracter do alfabeto:");
            alfabeto_char[i] = scanf.next().charAt(0);
        }
        
        confirma = true;
        System.out.println("Informe a quantidade de caracteres que compoem o alfabeto auxiliar: (sem incluir marcador de inicio e branca)");
        do{
            quant_alfabeto_auxiliar = scanf.nextInt();
            if(quant_alfabeto_auxiliar < 1){
                System.out.println("Erro: informe um numero maior ou igual a '1'.\n");
                System.out.println("Digite novamente: ");
                confirma = false;
            }else{
                confirma = true;
            }
        }while(!confirma);

        alfabeto_auxiliar_char = new char[quant_alfabeto_auxiliar];
        for(int i = 0; i < quant_alfabeto_auxiliar; i++){
            System.out.println("Informe o " + (i+1) + "° caracter do alfabeto:");
            alfabeto_auxiliar_char[i] = scanf.next().charAt(0);
        }

        System.out.println("Informe a quantidade de estados finais:");
        do{
            quant_estados_finais = scanf.nextInt();
            if(quant_estados_finais > quant_estados || quant_estados_finais < 1){
                System.out.println("Erro: informe um numero maior ou igual a '1' e que nao ultrapasse o numero de estado inseridos anteriormente.\n");
                System.out.println("Digite novamente: ");
                confirma = false;
            }else{
                confirma = true;
            }
        }while(!confirma);

        vet_estados_finais = new int[quant_estados_finais];
        for(int i = 0; i < quant_estados_finais; i++){
            System.out.println("Informe o " + (i+1) + "° estado final");
            vet_estados_finais[i] = scanf.nextInt();
        }

        char[] finais = {'>', '<'};

        indice_result = (quant_alfabeto + quant_alfabeto_auxiliar + 2);

        result_alfabeto = new char[indice_result];

        System.arraycopy(alfabeto_char, 0, result_alfabeto, 0, alfabeto_char.length);
        System.arraycopy(alfabeto_auxiliar_char, 0, result_alfabeto, alfabeto_char.length, alfabeto_auxiliar_char.length);
        System.arraycopy(finais, 0, result_alfabeto, alfabeto_char.length + alfabeto_auxiliar_char.length, finais.length);

        System.out.println("-----------Tabela de transicao----------");

        System.out.print(" \t");
        for(int i = 0; i < indice_result; i++){
             System.out.print(negrito + result_alfabeto[i] + "\t" + reset);
        }

        System.out.print("\n");
        
        for(int i = 0; i < quant_estados; i++){
            for(int j = 0; j < (indice_result + 1); j++){
                if(j == 0){
                    System.out.print(negrito + "q" + (i+1) + "\t" + reset);
                }else if(j == indice_result){
                    System.out.print((i+1) + "," + j + "\t\n");
                }else{
                    System.out.print((i+1) + "," + j + "\t");
                }
            }
        }

        System.out.print("\n");

        transicoes = new int[quant_estados][indice_result];
        direcao = new char[quant_estados][indice_result];
        alfabeto_futuro = new char[quant_estados][indice_result];

        System.out.println("----------TRASICOES----------");
        System.out.println("\nPreencha com (-1) quando nao haver trasicao.");

        for(int i = 0; i < quant_estados; i++){
            for(int j = 0; j < indice_result; j++){
                boolean teste1 = false;//variáveis usadas para regular os do-while a seguir.
                boolean teste2 = false;
                boolean teste3 = false;

                do{
                    System.out.print("Preencha a trasicao " + (i+1) + "," + (j+1) + " : ");
                    transicoes[i][j] = scanf.nextInt();

                    if(transicoes[i][j] > quant_estados || (transicoes[i][j] <= 0 && transicoes[i][j] != -1)){
                        System.out.println("Erro: essa transicao nao e possivel.");
                    }else{
                        teste1 = true;
                    }
                    
                }while(teste1 == false);

                if(transicoes[i][j] != -1){// se a transição existe as próximas informações são inseridas.
                    do{
                        System.out.print("Preencha o alfabeto futuro " + (i+1) + "," + (j+1) + " : ");
                        alfabeto_futuro[i][j] = scanf.next().charAt(0);
    
                        if( alfabeto_futuro[i][j] == 'D' || alfabeto_futuro[i][j] == 'd' || alfabeto_futuro[i][j] == 'e' || alfabeto_futuro[i][j] == 'E'){//trantando o alfabeto futuro.
                            System.out.println("Erro: esses simbolos ja estao sendo usados atomaticamente para representar direcao de movimento do cabecote da fita.");
                        }else{
                            teste2 = true;
                        }
                    }while(teste2 == false);

                    do{
                        System.out.print("Preencha a direcao " + (i+1) + "," + (j+1) + " : (D = direita e E = esquerda) ");
                        direcao[i][j] = scanf.next().charAt(0);
    
                        if(direcao[i][j] != 'D' && direcao[i][j] != 'd' && direcao[i][j] != 'E' && direcao[i][j] != 'e'){// tratando a direção de movimentação do cabeçote.
                            System.out.println("Erro: para as direcoes so serao aceitas as letras 'D' e 'd' para a direita e 'E' e 'e' para a esquerda.");
                        }else{
                            teste3 = true;
                        }
                    }while(teste3 == false);
    
                }

                System.out.println("---------------------------------------------------");     
            }
        }

        System.out.print(" \t");
        for(int i = 0; i < indice_result; i++){
            System.out.print("  " + negrito + result_alfabeto[i] + " \t" + reset);
        }

        System.out.println();
        for(int i = 0; i < quant_estados; i++){
            for(int j = 0; j < indice_result + 1; j++){
                if(j == 0){
                    System.out.print("q" + (i + 1) + "\t");
                }else{
                    if(transicoes[i][j-1] != -1){
                        System.out.print(transicoes[i][j-1] + ";" + alfabeto_futuro[i][j-1] + "," + direcao[i][j-1] + "\t");
                    }else{
                        System.out.print("  " + transicoes[i][j-1] + "\t");
                    }
                }
            }
            System.out.println();
        }

        System.out.println("---------------------------------------------------");  
    }

    static void processamento_palavra(){
        inicial();
        int introduzir;// variável usando para repetir varifição de palavra.
        boolean termina_final;//conferencia de estado final.

        do{
            Introduzir_palavra();
            palavra_vazia = false;
            termina_final = false;

        if(palavra_String == " " || palavra_String == "\n"){//conferir se a palavra vazia é aceita.
            palavra_vazia = true;
            boolean aceita_palavra_vazia = false;
            for(int i = 0; i < quant_estados_finais; i++){
                if(1 == vet_estados_finais[i]){
                    termina_final = true;
                    aceita_palavra_vazia = true;
                    break;
                }
            }
            if(aceita_palavra_vazia == false){
                termina_final = false;
            }
            //return;//fim do processamento.
        }
        char[] fita = new char[palavra_vetChar.length + 15];//Trabalhamos com uma fita finita, apesar da natureza da maquina de turing.
        fita[0] = '>';// indice inicial da fita passa a ser '>'.
        System.arraycopy(palavra_vetChar, 0, fita, 1, indice_palavra);//iguala a fita a palavra que sera conferida.
        //fita[fita.length - 1] = '<';// simbolo final da fita passa a ser '<'.

        for(int i = indice_palavra + 1; i < fita.length; i++){//preenche os espaço vazios da fita com branco.
            fita[i] = '<';
        }

        String fita_inicial = new String(fita);

        int posicao = 1;//como a posição 0 contem o simbolo de início, a conferencia se incia em 1.
        estado_atual = 1;//como o estado inicial é sempre o de número 1, a analize de transições começa por esse estado.
        boolean controle = false;// usada para regular a repetição do laço de repetição while.

        while(!controle && !palavra_vazia){
            char simbAtual = fita[posicao];// fita inicia da posição 1, pois a 0 é o simbolo de inicio;
            int simbolo_indice = -1;

            //System.out.println("\n Simbolo Atual: " + simbAtual);
            for(int i = 0; i < indice_result; i++){
                if(result_alfabeto[i] == simbAtual){//se o simbolo que esta sendo verificado pertencer a 'result_alfabeto', 'simbolo_indice' se ingualara a 'i', pois assim as proximas verificações serão realizadas, caso contrario já será retornado que a palavra não pertence a lingugem.
                    simbolo_indice = i;//para que continue as proximas orientações com esse simbolo.
                    break;//quebra do for.
                }
            }

            if(simbolo_indice == -1 || transicoes[estado_atual-1][simbolo_indice] == -1){//se o simbolo analizado não pertence a linguagem ou não existe transição com esse simbolo o while para de repetir. Se tira um de 'estado_atual', pois a variável assumil o valor '1' anteriormente.
                controle = true;
                break;//quebra do while.
            }

            fita[posicao] = alfabeto_futuro[estado_atual-1][simbolo_indice];//altera a fita como indicado pelo usuário. Decrementa 1 pois o estado inicial será sempre 1.
            if(direcao[estado_atual - 1][simbolo_indice] == 'D' || direcao[estado_atual - 1][simbolo_indice] == 'd'){//conferencia da direção de movimentação do cabeçote.
                posicao++;//anda com o cabeçote para a direita.
            }else{
                posicao--;//anda com o cabeçote para a esquerda.
            }

            estado_atual = transicoes[estado_atual - 1][simbolo_indice];//estado atual passa a ser o estado que a transição foi realizada.
        }

        //System.out.println("\n estado atual: " + estado_atual + "\n");

        
        for(int i = 0; i < quant_estados_finais && !palavra_vazia; i++){
            if(estado_atual == vet_estados_finais[i]){
                termina_final = true;
                break;
            }
        }

        if(termina_final == true){// determina o retorno dependendo se a fita termina ou não em um estado final.
            System.out.println("A PALAVRA PERTENCE A LINGUAGEM\n");
        }else{
            System.out.println("A PALAVRA NÃO PERTENCE A LINGUAGEM\n");
        }

        System.out.println("Fita inicial: " + fita_inicial);

        System.out.println("Fita final: " + String.valueOf(fita) + "\n");//retorna o que esta na fita.

        System.out.println("Deseja conferir uma nova palavra? (1 - sim; 0 - nao)");

        boolean teste4 = true;
        do{
            introduzir = scanf.nextInt();
            if(introduzir != 1 && introduzir != 0){
                System.out.println("Erro: opcao inexistente.\nIntrosuza a opcao correta como orientado: ");
                teste4 = false;
            }
        }while(!teste4);

    }while(introduzir == 1);

    }     
}
