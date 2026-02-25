package logica;

import java.awt.Color;

public class Tripulante extends Jugador{

    private int tareasCompletadas = 0;

    public Tripulante (String nombre, Color color, int x, int y, Partida p){
        super(nombre, color, x, y, p);
        this.rol = "Tripulante";
    }



    @Override
    public void interactuar(Tarea t) {
        t.progresar();
        if(t.getProgreso() >= 500){
            tareasCompletadas++;
        }
    }
}
