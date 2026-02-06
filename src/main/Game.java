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
        Mapa mapaInicial = new Mapa("Villa Asia - Mapa 1", "/mapa.png");

        Partida miPartida = new Partida(5, mapaInicial);

        // Prueba con 5 personas se unen
        miPartida.unirsePartida(new Tripulante("Alex", Color.RED, 100, 100, miPartida));
        miPartida.unirsePartida(new Tripulante("Santi", Color.BLUE, 200, 100, miPartida));
        miPartida.unirsePartida(new Tripulante("Maria", Color.GREEN, 300, 100, miPartida));
        miPartida.unirsePartida(new Tripulante("Jose", Color.YELLOW, 400, 100, miPartida));
        miPartida.unirsePartida(new Tripulante("Bello", Color.BLACK, 500, 100, miPartida));


        miPartida.prepararJugadores();

        for(Jugador j : miPartida.getJugadores()) {
            System.out.println(j.getNombre() + " es un " + j.getClass().getSimpleName());
        }



        // Objeto ventana, con partida y el mapa seleccionado.
        VentanaPrincipal ventana = new VentanaPrincipal(miPartida);
        ventana.setVisible(true);
    }


}
