package logica;

public class Tarea {
    private String nombre;
    private int x, y;
    private int progreso = 0;
    private boolean completada = false;

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

    public void progresar() {
        if (progreso < 300) {
            progreso++;
            if (progreso >= 300){
                completada = true;
                System.out.println("Tarea: " + nombre + " Completada.");
            }
        }
    }

    public void retroceder(){
        if (progreso > 0) {
            progreso--;
        }
    }


}