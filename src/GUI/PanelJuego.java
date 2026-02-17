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
    private Timer time;

    // Constructor para el panel
    public PanelJuego(Partida p) {
        this.partida = p;
        this.mapaActual = partida.getMapaActual();

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new Teclado(p, this));

        // CREAR EL TIMER
        // 16ms es aprox 60 FPS (cuadros por segundo)
        Timer timer = new Timer(16, e -> {

            repaint();
        });
        timer.start();
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
                g.fillOval(j.getX(), j.getY(), j.getAncho(), j.getAlto());


                g.setFont(fuente);
                g.setColor(Color.BLACK);
                g.drawString(j.getNombre(), j.getX() + 10, j.getY() - 10);
            }
            else {
                g.setColor(Color.RED);
                g.drawOval(j.getX(), j.getY(), j.getAncho(), j.getAlto());
                g.drawLine(j.getX(), j.getY(), j.getX() + j.getAncho(), j.getY() + j.getAlto());
                g.drawLine(j.getX() + j.getAncho(), j.getY(), j.getX(), j.getY() + j.getAlto());

                g.drawString("DEAD: " + j.getNombre(), j.getX() + 10, j.getY() - 10);
            }
        }

        // 4. (Opcional) Resetear el translate si quieres dibujar un HUD/Botones fijos
        g2d.translate(camX, camY);
        g.setColor(Color.WHITE);
        g.drawString("Misiones: 0/5", 20, 20);
    }
}
