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
    protected int speed = 5;
    protected int ancho = 50;
    protected  int alto = 50;


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

            int nuevaX = this.x + dx;
            int nuevaY = this.y + dy;

            Mapa mapaActual = partida.getMapaActual();

            nuevaX = Math.max(25, Math.min(nuevaX, mapaActual.getAncho() - 50));
            nuevaY = Math.max(25, Math.min(nuevaY, mapaActual.getAlto() - 50));

            boolean sup = mapaActual.noColision(nuevaX, nuevaY);
            boolean der = mapaActual.noColision(nuevaX + ancho, nuevaY);
            boolean inf = mapaActual.noColision(nuevaX, nuevaY + alto);
            boolean infD = mapaActual.noColision(nuevaX + ancho, nuevaY + alto);

            if (sup && der && inf && infD) {
                this.x = nuevaX;
                this.y = nuevaY;
            }
        }
    }

    public String getNombre() { return nombre; }

    public boolean isEstaVivo() { return estaVivo; }

    public void morir(){ estaVivo = false; }

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

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public abstract void interactuar(Tarea t);
}

