package logica;

import javax.swing.*;
import java.awt.*;

public class Mapa {
    private String nombre;
    private  String rutaImg;
    private Image background;


    public Mapa(String n, String r){
        this.nombre = n;
        this.rutaImg = r;
        // cargar la imagen:
        this.background = new ImageIcon(getClass().getResource(rutaImg)).getImage();
    }

    public int getAncho(){
        return this.background.getWidth(null);
    }

    public int getAlto(){
        return this.background.getHeight(null);
    }


    public String getNombre() {
        return nombre;
    }
    public Image getBackground(){
        return background;
    }
}
