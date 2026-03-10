package logica;

import java.awt.Color;
import java.util.ArrayList;

public class Impostor extends Jugador{

    // Atributos mientras tanto:
    private int kills;
    private long cooldown = 0;

    public Impostor(String nombre, Color color, int x, int y, Partida p) {
        super(nombre, color, x, y, p);
        this.rol = "Impostor";
        this.kills = 0;
    }

    public void asesinar() {

        long now = System.currentTimeMillis();
        if (now - cooldown < 5000) { // 5 segundos de espera
            System.out.println("Habilidad en recarga...");
            return;
        }
        // 1. Obtenemos la lista de posibles víctimas desde la Partida
        ArrayList<Jugador> jugadores = partida.getJugadores();

        for (Jugador j : jugadores) {

            // 2. Filtros de seguridad:
            if (j != this && j instanceof Tripulante && j.isEstaVivo()) {

                // 3. Cálculo de distancia (Rango de ataque)
                double dx = this.x - j.getX();
                double dy = this.y - j.getY();
                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia < 65) { // Un poco más que el diámetro del círculo
                    kills++;
                    // Ejecuta la función
                    j.morir();

                    // 4. Lógica extra: Notificar a la consola o sistema
                    System.out.println(j.getNombre() + " a sido asesinado!");

                    // 5. Break para salir del ciclo.
                    break;
                }
            }
        }

        cooldown = now;
    }

    @Override
    public void interactuar(Tarea t) {
        //t.retroceder();
        // los impostores también pueden fingir completar tareas
        t.progresar();
    }

    public void entrarConducto(){
        for (Ventilacion v : partida.getMapaActual().getConductos()){
            // 3. Cálculo de distancia (Rango de ataque)
            double dx = this.x - v.getX();
            double dy = this.y - v.getY();
            double distancia = Math.sqrt(dx * dx + dy * dy);

            if (distancia < 65){
                this.x = v.getDestX();
                this.y = v.getDestY();
                break;
            }

        }
    }
}
