package logica;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Jugador {
    protected String nombre;
    protected String rol;
    protected Color color;
    protected int x, y, xi, yi;
    protected boolean estaVivo, reportado, expulsado;
    // recibe un objeto partida, porque se quiere conocer el mapa actual.
    protected Partida partida;
    protected int speed = 5;
    protected int ancho = 50;
    protected  int alto = 50;


    public Jugador(String nombre, Color color, int x, int y, Partida p) {
        this.nombre = nombre;
        this.color = color;
        this.x = this.xi = x;
        this.y = this.yi = y;
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

    public boolean reportar(){
        ArrayList<Jugador> jugadores = partida.getJugadores();

        for (Jugador j : jugadores){
            if (!j.isEstaVivo() && !j.isReportado() && !j.isExpulsado()){

                double dx = this.x - j.getX();
                double dy = this.y - j.getY();
                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia < 65){
                    j.setReportado(true);
                    System.out.println("Alerta! Se ha reportado el cuerpo de: " + j.getNombre());
                    return true;
                }
            }
        }
        return false;
    }

    public String getNombre() { return nombre; }

    public boolean isEstaVivo() { return estaVivo; }

    public void morir(){ estaVivo = false; }

    public boolean isReportado() {
        return reportado;
    }

    public void setReportado(boolean reportado) {
        this.reportado = reportado;
    }

    public boolean isExpulsado() {
        return expulsado;
    }

    public void expulsar(){
        expulsado = true;
    }

    public Color getColor() { return  color;}

    public int getX() { return x; }

    public int getY() { return y; }

    public int getXi() {
        return xi;
    }

    public void setXi(int xi) {
        this.xi = xi;
    }

    public int getYi() {
        return yi;
    }

    public void setYi(int yi) {
        this.yi = yi;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

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

