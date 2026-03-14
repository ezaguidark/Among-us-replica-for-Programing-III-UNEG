package logica;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Clase que representa la ventilacion o el conducto del impostor
 */
public class Ventilacion {
    private int x, y;
    private int alto, ancho;
    private Image icono;
    /** debe tener una ventilacion destino */
    private Ventilacion destino;

    /**
     * Constructor que recibe las coordenadas en el mapa
     * @param x x pos
     * @param y y pos
     */
    public Ventilacion(int x, int y){
        this.x = x;
        this.y = y;
        icono = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Vent.png"))).getImage();
        alto = ancho = 100;
    }

    public void setDestino(Ventilacion destino) {
        this.destino = destino;
    }

    public int getDestX(){
        return destino.x;
    }

    public int getDestY(){
        return destino.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void dibujarIcon(Graphics g){
        g.drawImage(this.icono, x, y, alto, alto, null);
    }
}
