package logica;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Jugador {
    protected String nombre;
    protected String rol;
    protected Image frame1, frame2, frameDead;
    protected Color color;
    protected int x, y, xi, yi, dx;
    protected boolean estaVivo, reportado, expulsado, moviendose;
    // recibe un objeto partida, porque se quiere conocer el mapa actual.
    protected Partida partida;
    protected int speed = 5;
    protected int ancho = 75;
    protected  int alto = 75;
    protected int anim = 0; // variable para controlar la animación


    public Jugador(String nombre, Color color, int x, int y, Partida p) {
        this.nombre = nombre;
        this.color = color;
        this.x = this.xi = x;
        this.y = this.yi = y;
        this.estaVivo = true;
        this.partida = p;

        this.frame1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/frame1.png"))).getImage();
        this.frame2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/frame2.png"))).getImage();
        this.frameDead = new ImageIcon(Objects.requireNonNull(getClass().getResource("/frame3.png"))).getImage();

        this.frame1 = aplicarColor(frame1, this.color);
        this.frame2 = aplicarColor(frame2, this.color);
        this.frameDead = aplicarColor(frameDead, this.color);
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

    public void caminarAnim(Graphics g) {
        Image imgActual;
        if (moviendose) {
            anim++;
            if (anim >= 20){
                anim = 0;
            }
            imgActual = (anim < 10) ? frame1 : frame2;
        } else {
            imgActual = frame1;
        }
        if (this.dx < 0){
            g.drawImage(imgActual, x + ancho, y, -ancho, alto, null);
        }
        else{
            g.drawImage(imgActual, x, y, ancho, alto, null);
        }

    }

    public void dibujarMuerto(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(frameDead, x, y, ancho, alto, null);
        g2d.dispose();
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

    private Image aplicarColor(Image original, Color color) {
        int w = original.getWidth(null);
        int h = original.getHeight(null);

        BufferedImage tintada = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tintada.createGraphics();

        g2.drawImage(original, 0, 0, null);

        // MÁGIA AQUÍ: Usamos SRC_ATOP con 60% de opacidad (0.6f)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.6f));
        g2.setColor(color);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
        return tintada;
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

    public boolean isMoviendose() {
        return moviendose;
    }

    public void setMoviendose(boolean moviendose) {
        this.moviendose = moviendose;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public abstract void interactuar(Tarea t);
}

