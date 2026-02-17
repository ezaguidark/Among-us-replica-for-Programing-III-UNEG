package logica;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Mapa {
    private String nombre;
    private BufferedImage background;
    private BufferedImage colMap;

    public Mapa(String nombre, String rutaVisual, String rutaColision) {
        this.nombre = nombre;
        try {

            this.background = ImageIO.read(Objects.requireNonNull(getClass().getResource(rutaVisual)));
            this.colMap = ImageIO.read(Objects.requireNonNull(getClass().getResource(rutaColision)));

        } catch (IOException e) {
            System.out.println("Error cargando las imágenes del mapa: " + e.getMessage());
        }
    }


    public boolean noColision(int x, int y) {
        // 1. Validar que no estemos preguntando fuera de la imagen (evita errores)
        if (x < 0 || y < 0 || x >= getAncho() || y >= getAlto()) {
            return false;
        }

        // 2. Obtener el color del píxel en el mapa de colisiones
        int rgb = colMap.getRGB(x, y);
        Color color = new Color(rgb);

        // 3. Si el color es Blanco (o muy cercano al blanco), se puede caminar.
        // Usamos un umbral de 200 por si el JPG tiene algo de "ruido"
        return color.getRed() > 200 && color.getGreen() > 200 && color.getBlue() > 200;
    }

    public int getAncho() {
        return background.getWidth();
    }

    public int getAlto() {
        return background.getHeight();
    }

    public Image getBackground() {
        return background;
    }

    public String getNombre() {
        return nombre;
    }
}