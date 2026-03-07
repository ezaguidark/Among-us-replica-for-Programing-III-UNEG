package logica;

import GUI.PanelJuego;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Partida {
    private int numeroJugadores;
    private int jugadoresVivos;
    private Mapa mapaActual;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Tarea> tareas;
    private boolean gameOver;
    private String ganador;
    private int tareasCompletadas;


    public Partida(int num, Mapa m){
        this.numeroJugadores = num;
        this.mapaActual = m;
        this.jugadores = new ArrayList<>();
        this.gameOver = false;
        this.tareas = new ArrayList<>();
        inicializarTareas();
    }

    public int getNumeroJugadores() { return  numeroJugadores;}

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    // Getter y setter para el Mapa Actual.
    public Mapa getMapaActual(){
        return this.mapaActual;
    }

    public void setMapaActual(Mapa m){
        this.mapaActual = m;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public void setGameOver(boolean b){
        gameOver = b;
    }

    public int getJugadoresVivos(){
        return jugadoresVivos;
    }

    // Método para unirse a la partida, a la lista de jugadores.
    public void unirsePartida(Jugador nuevoJugador){
        if (jugadores.size() < numeroJugadores){
            jugadores.add(nuevoJugador);
            System.out.println("Se unio: " + nuevoJugador.getNombre() + " a la partida.");
        }
        else{
            System.out.println("La partida ya esta llena.");
        }
    }

    // Reemplaza un tripulante con un impostor
    private Impostor swapImpostor(Jugador j){
        return new Impostor(j.getNombre(), j.getColor(), j.getX(), j.getY(), j.getPartida());
    }

    // Sortea a los jugadores y define a los impostores.
    public void prepararJugadores(){
        if (jugadores.size() < 5) {
            System.out.println("Faltan jugadores para iniciar.");
            return;
        }

        // Aunque el enunciado dice que son 2 impostores, si hay solo 5 jugadores, habrá un impostor.
        if (jugadores.size() > 5){
            for (int i = 0; i < 2; i++){
                Jugador j = jugadores.get(i);
                jugadores.set(i, swapImpostor(j));
            }
        }else{
            Jugador j = jugadores.get(0);
            jugadores.set(0, swapImpostor(j));
        }

        //Collections.shuffle(jugadores);

        System.out.println("Roles Asignados!");
    }

    public void inicializarTareas() {
        tareas.clear();
        tareasCompletadas = 0;
        tareas = mapaActual.getTareas();
    }

    public void iniciarVotacion(){
        System.out.println("¡EMERGENCIA: Cuerpo Encontrado!");

        // 1. Recopilar los nombres de los jugadores VIVOS para las opciones
        ArrayList<String> sospechosos = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (j.isEstaVivo()) {
                sospechosos.add(j.getNombre());
            }
        }
        sospechosos.add("Omitir Voto");

        // Convertimos la lista a un arreglo de Strings (requisito de JOptionPane)
        String[] opciones = sospechosos.toArray(new String[0]);

        // 2. Mostrar la ventana de votación
        // Esto detendrá la ejecución del juego hasta que el jugador haga clic
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "¡Cuerpo encontrado! Elige a un sospechoso: ",
                "Tiempo de Votar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[opciones.length - 1] // Opción por defecto (Omitir)
        );

        // 3. Procesar el voto
        if (seleccion >= 0 && seleccion < opciones.length - 1) {
            // Votaron por un jugador (no eligieron "Omitir" ni cerraron la ventana)
            String sospechoso = opciones[seleccion];

            for (Jugador j : jugadores) {
                if (j.getNombre().equals(sospechoso)) {
                    j.morir(); // ¡Lo expulsamos!
                    j.expulsar();
                    System.out.println(j.getNombre() + " fue Expulsado.");
                    break;
                }
            }
        } else {
            System.out.println("Nadie fue Expulsado (Voto omitido).");
        }

        // 4. Limpieza post-reunión
        reiniciarPosiciones();

    }

    void reiniciarPosiciones(){
        for(Jugador j : jugadores){
            if(j.isEstaVivo()){
                j.setX(j.xi);
                j.setY(j.yi); // provisional.
            }
        }
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

    public int getTotalTareas(){
        return tareas.size();
    }

    public int getTareasCompletadas(){
        return tareasCompletadas;
    }

    // Saber si ganaron los Jugadores
    public boolean tareasCompletadas() {
        int c = 0;

        for (Tarea t : tareas) {
            if (t.isCompletada()){
                c++;
            }
        }
        tareasCompletadas = c;
        for (Tarea t : tareas){
            if(!t.isCompletada()){
                return false;
            }
        }
        return true;
    }

    // Verifica el estado de la partida contando cuantos tripulantes y cuantos impostores hay
    public void verificarEstado() {
        int tripulantesVivos = 0;
        int impostoresVivos = 0;

        // 1. Contamos la realidad actual del mapa
        for (Jugador j : jugadores) {
            if (j.isEstaVivo()) {
                if (j instanceof Impostor) impostoresVivos++;
                else tripulantesVivos++;
            }
        }

        // 2. Lógica de victoria del Impostor
        if (impostoresVivos >= tripulantesVivos) {
            this.gameOver = true;
            this.ganador = "IMPOSTORES";
            return;
        }

        // 3. Lógica de victoria de Tripulantes (por muerte de impostores)
        if (impostoresVivos == 0) {
            this.gameOver = true;
            this.ganador = "TRIPULANTES";
            return;
        }

        // 4. Lógica de victoria de Tripulantes (por tareas)
        if (tareasCompletadas()) { // Tu función que recorre la lista de tareas
            this.gameOver = true;
            this.ganador = "TRIPULANTES";
        }

        jugadoresVivos = tripulantesVivos + impostoresVivos;
    }

    public String getGanador(){
        return ganador;
    }
}
