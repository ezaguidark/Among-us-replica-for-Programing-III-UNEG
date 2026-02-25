package main;

import logica.*;
import GUI.*;
import logica.Tripulante;

import javax.swing.JFrame;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        System.out.println("Prueba... AmongUS UNEG.");

        // objeto mapa
        Mapa mapaInicial = new Mapa("Villa Asia - Mapa 1", "/provisional.jpg", "/col_map.png");

        Partida miPartida = new Partida(5, mapaInicial);

        // Prueba con 5 personas se unen
        miPartida.unirsePartida(new Tripulante("Alex", Color.RED, 100, 300, miPartida));
        miPartida.unirsePartida(new Tripulante("Santi", Color.BLUE, 200, 300, miPartida));
        miPartida.unirsePartida(new Tripulante("Maria", Color.GREEN, 300, 300, miPartida));
        miPartida.unirsePartida(new Tripulante("Jose", Color.YELLOW, 400, 300, miPartida));
        miPartida.unirsePartida(new Tripulante("Bello", Color.BLACK, 500, 300, miPartida));


        miPartida.prepararJugadores();

        for(Jugador j : miPartida.getJugadores()) {
            System.out.println(j.getNombre() + " es un " + j.getClass().getSimpleName());
        }

        // provisionalmente aqui.
        miPartida.setGameOver(false);

        // Objeto ventana, con partida y el mapa seleccionado.
        VentanaPrincipal ventana = new VentanaPrincipal(miPartida);
        ventana.setVisible(true);
    }


}
