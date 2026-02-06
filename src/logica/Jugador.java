package logica;

import java.awt.Color;

public abstract class Jugador {
    protected String nombre;
    protected String rol;
    protected Color color;
    protected int x, y;
    protected boolean estaVivo;
    // recibe un objeto partida, porque se quiere conocer el mapa actual.
    protected Partida partida;
    protected int speed = 15;


    public Jugador(String nombre, Color color, int x, int y, Partida p) {
        this.nombre = nombre;
        this.color = color;
        this.x = x;
        this.y = y;
        this.estaVivo = true;
        this.partida = p;
    }

    public void moverse(int dx, int dy) {
        if (estaVivo) {
            this.x += dx;
            this.y += dy;
            Mapa mapaActual = partida.getMapaActual();

            // Limitar los límites del mapa
            this.x = Math.max(50, Math.min(this.x , mapaActual.getAncho() - 150));
            this.y = Math.max(50, Math.min(this.y , mapaActual.getAlto() - 150));


            // System.out.println("Moviéndose: " + x +" -" + y);
        }
    }

    public String getNombre() { return nombre; }

    public boolean isEstaVivo() { return estaVivo; }

    public Color getColor() { return  color;}

    public int getX() { return x; }

    public int getY() { return y; }

    public Partida getPartida(){
        return this.partida;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

