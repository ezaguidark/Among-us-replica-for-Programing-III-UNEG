package logica;

/**
 * Esta clase representa las tareas a completar
 */
public class Tarea {
    private String nombre;
    private int x, y;
    private int progreso = 0;
    private boolean completada = false;


    /**
     * Constructor del objeto tarea
     * @param nombre El nombre de la tarea
     * @param x position en x
     * @param y posicion en y
     */
    public Tarea(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getProgreso() { return progreso; }
    public boolean isCompletada() { return completada; }
    public String getNombre() { return nombre; }

    /**
     * Este metodo aumenta el progreso de la tarea
     */
    public void progresar() {
        if (progreso < 300) {
            progreso++;
            if (progreso >= 300){
                completada = true;
                System.out.println("Tarea: " + nombre + " Completada.");
            }
        }
    }

    /**
     * Este metodo era para sabotear.
     */
    public void retroceder(){
        if (progreso > 0) {
            progreso--;
        }
    }


}