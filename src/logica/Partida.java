package logica;

import java.util.ArrayList;
import java.util.Collections;

public class Partida {
    private int numeroJugadores;
    private Mapa mapaActual;
    private ArrayList<Jugador> jugadores;
    private boolean gameOver;


    public Partida(int num, Mapa m){
        this.numeroJugadores = num;
        this.mapaActual = m;
        this.jugadores = new ArrayList<>();
        this.gameOver = true;
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
        Collections.shuffle(jugadores);

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
        System.out.println("Roles Asignados!");
    }
}
