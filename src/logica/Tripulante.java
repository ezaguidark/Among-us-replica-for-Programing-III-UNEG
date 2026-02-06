package logica;

import java.awt.Color;

public class Tripulante extends Jugador{

    private int tareasCompletadas = 0;

    public Tripulante (String nombre, Color color, int x, int y, Partida p){
        super(nombre, color, x, y, p);
        this.rol = "Tripulante";
    }

    // Método provisional.
    public void hacerMision() {
        tareasCompletadas++;
    }
}
