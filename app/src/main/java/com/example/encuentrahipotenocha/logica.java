package com.example.encuentrahipotenocha;

public class logica {

    private int [][]tablero;

    public logica(int filas,int columnas){
        tablero= new int[filas][columnas];

    }

    //Coloca las pistas teniendo cuantas minas hay alrededor
    public void ponerPistas(int filas, int columnas) {
        for (int x = 0; x < filas; x++) {
            for (int y = 0; y < columnas; y++) {
                if (tablero[x][y] != -1) {
                    int contador = 0;
                    if ((x - 1 >= 0) && (y - 1 >= 0) && tablero[x - 1][y - 1] == -1) {
                        contador++;
                    }
                    if ((x - 1 >= 0) && tablero[x - 1][y] == -1){
                        contador++;
                    }
                    if ((x - 1 >= 0) && (y + 1 < filas) && tablero[x - 1][y + 1] == -1){
                        contador++;
                    }
                    if ((y - 1 >= 0) && tablero[x][y - 1] == -1){
                        contador++;
                    }
                    if ((y + 1 < 8) && tablero[x][y + 1] == -1){
                        contador++;
                    }
                    if ((x + 1 < 8) && (y - 1 >= 0) && tablero[x + 1][y - 1] == -1){
                        contador++;
                    }
                    if ((x + 1 < 8) && tablero[x + 1][y] == -1){
                        contador++;
                    }
                    if ((x + 1 < 8) && (y + 1 < filas) && tablero[x + 1][y + 1] == -1) {
                        contador++;
                    }
                    tablero[x][y] = contador;
                }
                System.out.print(tablero[x][y] == -1 ? "B" : tablero[x][y]);
            }
            System.out.println("\n");
        }
    }

    //Inicia los tableros en funcion de la dificultad pasada por parametro
    public void iniciar(String nivel){
        int hipotenochas = 0;
        if(nivel.equals("Principiante")){
            for(int i = 0;i<8;i++){
                for(int j = 0;j<8;j++){
                    tablero[i][j]=0;
                }
            }
            while (hipotenochas != 10) {
                int fila = (int)(Math.random()*7+1);
                int columna = (int)(Math.random()*7+1);
                if (tablero[fila][columna] != -1) {
                    tablero[fila][columna] = -1;
                    hipotenochas++;
                }
            }
        }else if(nivel.equals("Amateur")){
            for(int i = 0;i<12;i++){
                for(int j = 0;j<12;j++){
                    tablero[i][j]=0;
                }
            }
            while (hipotenochas != 30) {
                int fila = (int)(Math.random()*11+1);
                int columna = (int)(Math.random()*11+1);
                if (tablero[fila][columna] != -1) {
                    tablero[fila][columna] = -1;
                    hipotenochas++;
                }
            }
        }else if(nivel.equals("Avanzado")){
            for(int i = 0;i<16;i++){
                for(int j = 0;j<16;j++){
                    tablero[i][j]=0;
                }
            }
            while (hipotenochas != 60) {
                int fila = (int)(Math.random()*15+1);
                int columna = (int)(Math.random()*15+1);
                if (tablero[fila][columna] != -1) {
                    tablero[fila][columna] = -1;
                    hipotenochas++;
                }
            }
        }
    }
    //Devuelve el tablero para tenerlo en el Mainactivity
    public int[][] devolverArray(){
        return tablero;
    }
}
