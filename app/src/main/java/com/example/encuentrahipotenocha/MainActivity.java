package com.example.encuentrahipotenocha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    //ATRIBUTOS
    private RecyclerView recyclerView;
    private Adaptador adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Item> exampleList = new ArrayList<>();
    private int [][] tablero = new int [8][8];
    private boolean [][] visible = new boolean [8][8];
    private int filas, columnas;
    private logica logica;
    private String nivel;
    private int QuedanBombas;
    private boolean ganador;
    private Vibrator vibrator;
    private int imagen;
    private boolean suenaMusica;
    private MediaPlayer SonidoBomba;
    private MediaPlayer SonidoClick;
    private MediaPlayer SonidoPerder;
    private MediaPlayer SonidoVida;
    private MediaPlayer SonidoWin;
    private MediaPlayer SonidoFondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //INICIALIZO TODOS LOS ATRIBUTOS
        filas = 8;
        columnas = 8;
        IniciarTableros(filas, columnas);
        nivel = "Principiante";
        QuedanBombas = 10;
        ganador=false;
        suenaMusica=true;
        logica = new logica(filas, columnas);
        logica.iniciar(nivel);
        logica.ponerPistas(filas, columnas);
        tablero=logica.devolverArray();
        vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
        imagen = (R.drawable.banderamorada);
        SonidoBomba = MediaPlayer.create(this, R.raw.bomb);
        SonidoClick = MediaPlayer.create(this, R.raw.click);
        SonidoPerder = MediaPlayer.create(this, R.raw.perder);
        SonidoVida = MediaPlayer.create(this, R.raw.vida);
        SonidoWin = MediaPlayer.create(this, R.raw.win);
        SonidoFondo = MediaPlayer.create(this, R.raw.fondo);
        SonidoFondo.start();
        SonidoFondo.setLooping(true);

        //Creo el tablero de inicio
        for (int i = 0; i < 64; i++) {
            exampleList.add(new Item(R.drawable.cuadrado));
        }

        //INSTANCIAMOS Y ASOCIAMOS ELEMENTOS NECESARIOS PARA EL CORRECTO FUNCIONAMIENTO DEL RECYCLERVIEW
        recyclerView = findViewById(R.id.recyclerViewXML); //Vinculamos el recyclerview del xml con el de la clase main
        recyclerView.setHasFixedSize(false);// RecyclerView sabe de antemano que su tamaño no depende del contenido del adaptador, entonces omitirá la comprobación de si su tamaño debería cambiar cada vez que se agregue o elimine un elemento del adaptador.(mejora el rendimiento)
        layoutManager = new GridLayoutManager(this, 8);//Creamos el layoutManager de tipo GridLayaout que vamos a utilizar
        recyclerView.setLayoutManager(layoutManager);//Asociamos al recyclerView el layoutManager que creamos en el paso anterior
        adapter = new Adaptador(exampleList);//Instanciamos un objeto de tipo Example_Adapter
        recyclerView.setAdapter(adapter);//Vinculamos el adapter al recyclerView


        adapter.setOnClickListener(new Adaptador.OnItemClickListener () {

            @Override
            public void OnItemClick(int position) {
                if(ganador==false) { //Para que el juego continue
                    if(visible[x(position)][y(position)] == true){
                        Toast.makeText(MainActivity.this, "Meteorito esquivado", Toast.LENGTH_SHORT).show();
                        SonidoVida.start();
                    }else{
                        if (comprobarResultado(position) == -1) { //Si en la casilla donde pulsa hay un mina
                            Toast.makeText(MainActivity.this, "METEORITO", Toast.LENGTH_SHORT).show();
                            exampleList.get(position).setmImageResource(R.drawable.mina);
                            adapter.notifyItemChanged(position);
                            ganador = true;
                            vibrator.vibrate(1000);
                            SonidoBomba.start();
                        } else {
                            //Si no hay una mina
                            vibrator.vibrate(25);
                            SonidoClick.start();
                        }
                    }

                }
            }
        });
        //Para el evento Long Click
        adapter.setOnLongClickListener(new Adaptador.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if(ganador==false){ //Para que continue la partida
                    if(comprobarResultado(position) == -1){ //Cuando descubres y hay una mina entra aqui
                        QuedanBombas -= 1;
                        exampleList.get(position).setmImageResource(imagen);
                        adapter.notifyItemChanged(position);
                        if(QuedanBombas==0){ //Si bombas es igual a 0 ganas
                            Toast.makeText(MainActivity.this,"Has llegado a marte", Toast.LENGTH_SHORT).show();
                            ganador=true;
                            SonidoWin.start();
                            long tiempo = 1000; //en milisegundos
                            vibrator.vibrate(1000);
                        }else { // Si no es igual a cero solo salta el aviso con las que te quedan
                            visible[x(position)][y(position)] = true;
                            vibrator.vibrate(25);
                            Toast.makeText(MainActivity.this, "Has esquivado un meteorito: " + QuedanBombas, Toast.LENGTH_SHORT).show();
                            SonidoVida.start();
                        }
                    }else{//Si desactivas una y no es igual a -1 pierdes
                        Toast.makeText(MainActivity.this,"Te has entrellado", Toast.LENGTH_SHORT).show();
                        long tiempo = 250; //en milisegundos
                        vibrator.vibrate(250);
                        ganador=true;
                        SonidoPerder.start();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Cuando pulsas el boton de intrucciones
        if (id == R.id.intrucciones) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Instrucciones");
            builder1.setMessage("El juego es de tipo buscaminas: Cuando pulsas en una casilla, sale un número que identifica cuántas hipotenochas hay alrededor: Ten cuidado porque sin pulsas en una casilla que tenga una hipotenocha escondida, perderás. Si crees o tienes la certeza de que hay una hipotenocha, haz un click largo sobre la casilla para señalarla. No hagas un click largo en una casilla donde no hay un hipoteocha porque perderás. Ganas una vezque hayas encontrado todas las hipotenochas.");
            builder1.setCancelable(true);
            builder1.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
            //cuando pulsas el boton de comenzar juego
        }else  if (id == R.id.comenzarJuego) {
            //Se reinician las variables
            ganador=false;

            //En funcion del modo de juego pone mas minas o menos
            if(nivel.equals("Principiante")){
                QuedanBombas = 10;
            }else if(nivel.equals("Amateur")){
                QuedanBombas = 30;
            }else if(nivel.equals("Amateur")){
                QuedanBombas = 60;
            }
            logica = new logica(filas, columnas);
            logica.iniciar(nivel);
            logica.ponerPistas(filas, columnas);
            IniciarTableros(filas, columnas);
            tablero=logica.devolverArray();
            exampleList.clear();
            //El tablero se reinicia
            for (int i = 0; i < filas*columnas; i++) {
                exampleList.add(new Item(R.drawable.cuadrado));
            }
            //El ArrayList se reinicia
            for (int i = 0; i < exampleList.size(); i++) {
                exampleList.get(i).setmImageResource(R.drawable.cuadrado);
                adapter.notifyItemChanged(i);
            }
            layoutManager = new GridLayoutManager(this, filas);
            recyclerView.setLayoutManager(layoutManager);

            return true;
            //cuando pulsas el boton de comenzar juego

            //Opcion Principiante
        }else  if (id == R.id.principiante) {

            //Se reinician las variables
            ganador=false;
            QuedanBombas = 10;
            filas = 8;
            columnas = 8;
            nivel = "Principiante";
            logica = new logica(filas, columnas);
            logica.iniciar(nivel);
            logica.ponerPistas(filas, columnas);
            IniciarTableros(filas, columnas);
            tablero=logica.devolverArray();
            exampleList.clear();

            //El tablero se reinicia
            for (int i = 0; i < filas*columnas; i++) {
                exampleList.add(new Item(R.drawable.cuadrado));
            }

            //El ArrayList se reinicia
            for (int i = 0; i < exampleList.size(); i++) {
                exampleList.get(i).setmImageResource(R.drawable.cuadrado);
                adapter.notifyItemChanged(i);
            }
            layoutManager = new GridLayoutManager(this, filas);
            recyclerView.setLayoutManager(layoutManager);

            return true;

            //Opcion amateur
        }else  if (id == R.id.amateur) {

            //Se reinician las variables
            ganador=false;
            QuedanBombas = 30;
            filas = 12;
            columnas = 12;
            nivel = "Amateur";
            logica = new logica(filas, columnas);
            logica.iniciar(nivel);
            logica.ponerPistas(filas, columnas);
            IniciarTableros(filas, columnas);
            tablero=logica.devolverArray();
            exampleList.clear();

            //El tablero se reinicia
            for (int i = 0; i < filas*columnas; i++) {
                exampleList.add(new Item(R.drawable.cuadrado));
            }

            //El ArrayList se reinicia
            for (int i = 0; i < exampleList.size(); i++) {
                exampleList.get(i).setmImageResource(R.drawable.cuadrado);
                adapter.notifyItemChanged(i);
            }
            layoutManager = new GridLayoutManager(this, filas);
            recyclerView.setLayoutManager(layoutManager);

            return true;
        }else  if (id == R.id.avanzado) {

            //Se reinician las variables
            ganador=false;
            QuedanBombas = 60;
            filas = 16;
            columnas = 16;
            nivel = "Avanzado";
            logica = new logica(filas, columnas);
            logica.iniciar(nivel);
            logica.ponerPistas(filas, columnas);
            IniciarTableros(filas, columnas);
            tablero=logica.devolverArray();
            exampleList.clear();

            //El tablero se reinicia
            for (int i = 0; i < filas*columnas; i++) {
                exampleList.add(new Item(R.drawable.cuadrado));
            }

            //El ArrayList se reinicia
            for (int i = 0; i < exampleList.size(); i++) {
                exampleList.get(i).setmImageResource(R.drawable.cuadrado);
                adapter.notifyItemChanged(i);
            }
            layoutManager = new GridLayoutManager(this, filas);
            recyclerView.setLayoutManager(layoutManager);
            return true;
        }else  if (id == R.id.menu_opcion) {
            return true;

            //Menu para cambiar el color de la bandera
        }else  if (id == R.id.azul) {
            imagen = (R.drawable.banderaazul);
            Toast.makeText(MainActivity.this, "Azul", Toast.LENGTH_SHORT).show();
        }else  if (id == R.id.verde) {
            imagen = (R.drawable.banderaverde);
            Toast.makeText(MainActivity.this, "Verde", Toast.LENGTH_SHORT).show();
        }else  if (id == R.id.morada) {
            imagen = (R.drawable.banderamorada);
            Toast.makeText(MainActivity.this, "Morada", Toast.LENGTH_SHORT).show();
        }else  if (id == R.id.naranja) {
            imagen = (R.drawable.banderanaranja);
            Toast.makeText(MainActivity.this, "Naranja", Toast.LENGTH_SHORT).show();
        }else  if (id == R.id.nomusica) {
            SonidoFondo.stop();
        }else  if (id == R.id.musica) {
            SonidoFondo = MediaPlayer.create(this, R.raw.fondo);
            SonidoFondo.start();
            SonidoFondo.setLooping(true);
        }

        return super.onOptionsItemSelected(item);
    }
    //Primero comprueba que la casilla existe si es asi la funcion compruebaCelda devuleve el valor de la casilla en funcion
    //de su hay mina cerca si esta vacia etc
    //Si lo que pulsamos es igual a 0 es decir una casilla sin ninguna mina cerca, entrará en el else
    // y comprobará todas las casillas alrededor de esta hacia arriba abajo izquierda y derecha, en busca de minas
    public void DestaparCeldas(int tablero[][], boolean visible[][], int x, int y, int pos){
        if (compruebaCelda(x,y) != 0) {
            revelarTablero(x ,y );
        }else{
            for (int fila = -1; fila <= 1; fila++) {
                for (int columna = -1; columna <= 1; columna++) {
                    if (fila != columna) {
                        if (compruebaCelda(x + fila, y + columna) != -2 && visible[x + fila][y + columna] == false) {
                            visible[x + fila][y + columna] = true;
                            DestaparCeldas(tablero, visible, x + fila, y + columna, pos);
                        }
                    }
                }
            }
            revelarTablero(x, y);
        }
    }

    //Revela el tablero
    public void revelarTablero(int x, int y ){
        if(tablero[x][y]==0){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.cuadradogana);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==1){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.uno);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==2){
            exampleList.get(pos(x, y)).setmImageResource(R.drawable.dos);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==3){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.tres);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==4){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.cuatro);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==5){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.cinco);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==6){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.seis);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==7){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.siete);
            adapter.notifyItemChanged(pos(x,y));
        }else if(tablero[x][y]==8){
            exampleList.get(pos(x,y)).setmImageResource(R.drawable.ocho);
            adapter.notifyItemChanged(pos(x,y));
        }
    }

    //Comprueba si hay una mina en la posicion que se le pasa por parametro
    public int comprobarResultado(int pos){
        int resultado;
        if(tablero[x(pos)][y(pos)] == -1){
            visible[x(pos)][y(pos)]=true;
            resultado=-1;
        }else{
            DestaparCeldas(tablero,visible,x(pos),y(pos), pos);
            resultado=contarCeldasDestapadas(visible);
        }
        return resultado;
    }

    //Deveulve el valor de la casilla
    private int contarCeldasDestapadas(boolean[][] visible) {
        int num=0;
        for (int x = 0; x < filas; x++) {
            for (int y = 0; y < columnas; y++) {
                if(visible[x][y]==true){
                    num++;
                }
            }
        }
        return num;
    }

    //Transforma la posicion en un numero este sera la fila en el array bidimensional
    public int x(int pos){
        int num;
        num=(int) Math.floor ( pos / filas );
        return num;
    }

    //Transforma la posicion en un numero este sera la columna en el array bidimensional
    public int y(int pos){
        int num;
        num=(int) Math.floor( pos % filas);
        return num;
    }

    //Tranforma la fila y la columna en una posicion se utiliza para el array de exampleList
    public int pos(int x, int y){
        int pos;
        pos=(x*filas)+y;
        return pos;
    }

    //Comprueba si la celda existe
    public int compruebaCelda(int x, int y) {
        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
            return tablero[x][y];
        } else {
            return -2;
        }
    }

    //Reinicia los tableros
    public void IniciarTableros(int filas, int columnas){
        visible = new boolean[filas][columnas];
        tablero = new int[filas][columnas];
    }

    //Cuando el juego vuelve a ser visualizado
    public void onRestart () {
        super.onRestart ();
        SonidoFondo = MediaPlayer.create(this, R.raw.fondo);
        SonidoFondo.start();
        SonidoFondo.setLooping(true);
    }

    //Cuando desaparece de la pantalla
    public  void onPause () {
        super.onPause();
        SonidoFondo.stop();
    }

}
