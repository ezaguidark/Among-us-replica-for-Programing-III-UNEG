package logica;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Mapa {
    private String nombre;
    private BufferedImage background;
    private BufferedImage colMap;
    private ArrayList<Tarea> tareas = new ArrayList<>();
    private ArrayList<Ventilacion> conductos = new ArrayList<>();


    public Mapa(String nombre, String rutaVisual, String rutaColision) {
        this.nombre = nombre;
        inicializarTareas();
        colocarVentilaciones();
        try {

            this.background = ImageIO.read(Objects.requireNonNull(getClass().getResource(rutaVisual)));
            this.colMap = ImageIO.read(Objects.requireNonNull(getClass().getResource(rutaColision)));

        } catch (IOException e) {
            System.out.println("Error cargando las imágenes del mapa: " + e.getMessage());
        }
    }

    // Cada mapa tendrá su lista de tareas en ubicaciones específicas.
    private void inicializarTareas() {

        // Provisional:
        if (this.nombre.equals("Villa Asia")) {
            tareas.add(new Tarea("Aprobar Programación III", 1250, 1430));
            tareas.add(new Tarea("Ver Matemáticas", 300, 1180));
            tareas.add(new Tarea("Ir al Baño", 330, 800));
            tareas.add(new Tarea("Ir a Biblioteca", 1000, 760));
            tareas.add(new Tarea("Jugar Futbol", 2400, 1400));
            tareas.add(new Tarea("Coordinación", 1070, 490));
            tareas.add(new Tarea("Antiguo Comedor", 3000, 1200));
            tareas.add(new Tarea("Sacar Copias", 2900, 200));

        } else if (this.nombre.equals("Atlantico")) {
            tareas.add(new Tarea("Baños", 1850, 1300));
            tareas.add(new Tarea("Escaleras 1", 3000, 700));
            tareas.add(new Tarea("Escaleras 2", 250, 1300));
            tareas.add(new Tarea("Cafetín", 680, 600));
            tareas.add(new Tarea("Salon UsosMultiples", 900, 1500));
            tareas.add(new Tarea("Aula 14", 2650, 1350));
        }

    }


    private void colocarVentilaciones(){
        // Provisional:
        if (this.nombre.equals("Villa Asia")) {
            Ventilacion v1 = new Ventilacion(330, 500);
            Ventilacion v2 = new Ventilacion(2550, 200);
            Ventilacion v3 = new Ventilacion(2500, 1600);
            Ventilacion v4 = new Ventilacion(1050, 1700);

            v1.setDestino(v2);
            v2.setDestino(v3);
            v3.setDestino(v4);
            v4.setDestino(v1);

            conductos.add(v1); conductos.add(v2); conductos.add(v3); conductos.add(v4);

        } else if (this.nombre.equals("Atlantico")) {
            Ventilacion v1 = new Ventilacion(260, 400);
            Ventilacion v2 = new Ventilacion(600, 1500);
            Ventilacion v3 = new Ventilacion(1850, 1600);
            Ventilacion v4 = new Ventilacion(3050, 1500);

            v1.setDestino(v2);
            v2.setDestino(v3);
            v3.setDestino(v4);
            v4.setDestino(v1);

            conductos.add(v1); conductos.add(v2); conductos.add(v3); conductos.add(v4);
        }
    }

    public ArrayList<Ventilacion> getConductos(){
        return conductos;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
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