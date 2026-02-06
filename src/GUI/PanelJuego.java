package GUI;

import logica.Partida;
import logica.Mapa;
import logica.Jugador;
import javax.swing.*;
import java.awt.*;
import controles.Teclado;

public class PanelJuego extends JPanel{

    private Partida partida;
    private Mapa mapaActual;

    // Constructor para el panel
    public PanelJuego(Partida p) {
        this.partida = p;
        this.mapaActual = partida.getMapaActual();

        // Parámetros para el input:
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(new Teclado(p, this));

    }

    public void cambiarMapa(Mapa nmap) {
        partida.setMapaActual(nmap);
        repaint(); // funcion heredada de Jpanel
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. Calculamos cuánto hay que desplazar la "cámara"
        Jugador yo = partida.getJugadores().get(0);
        int camX = yo.getX() - (getWidth() / 2);
        int camY = yo.getY() - (getHeight() / 2);

        // 2. "Empujamos" todo el dibujo en sentido contrario
        g2d.translate(-camX, -camY);

        // 3. Dibujas TODO normal (Mapa, Jugadores, etc.)
        // Java se encarga de que parezca que la cámara se mueve
        g.drawImage(mapaActual.getBackground(), 0, 0, null);

        // Tamaño de renderizado de los personajes

        Font fuente = new Font("Arial", Font.BOLD, 24);

        for (Jugador j : partida.getJugadores()) {
            if (j.isEstaVivo()){
                g.setColor(j.getColor());
                g.fillOval(j.getX(), j.getY(), 100, 100);


                g.setFont(fuente);
                g.setColor(Color.BLACK);
                g.drawString(j.getNombre(), j.getX() + 20, j.getY() - 10);
            }
            else {
                g.setColor(Color.RED);
                g.drawOval(j.getX(), j.getY(), 100, 100);
                g.drawLine(j.getX(), j.getY(), j.getX() + 100, j.getY() + 100);
                g.drawLine(j.getX() + 100, j.getY(), j.getX(), j.getY() + 100);

                g.drawString("DEAD: " + j.getNombre(), j.getX() + 20, j.getY() - 10);
            }
        }

        // 4. (Opcional) Resetear el translate si quieres dibujar un HUD/Botones fijos
        g2d.translate(camX, camY);
        g.setColor(Color.WHITE);
        g.drawString("Misiones: 0/5", 20, 20);
    }
}
