package GUI;

import logica.*;

import javax.swing.*;
import java.awt.*;
import controles.Teclado;

public class PanelJuego extends JPanel{

    private Partida partida;
    private Mapa mapaActual;
    private Timer time;
    private Jugador yo;

    // Constructor para el panel
    public PanelJuego(Partida p) {
        this.partida = p;
        this.mapaActual = partida.getMapaActual();
        this.yo = partida.getJugadores().get(0);

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new Teclado(p, this));
        Teclado t = (Teclado) this.getKeyListeners()[0]; // Obtenemos el teclado

        // CREAR EL TIMER
        // 16ms es aprox 60 FPS (cuadros por segundo)
        Timer timer = new Timer(16, e -> {
            if (!partida.getGameOver()){
                actualizarMovimiento();
                actualizarTareas();
                partida.verificarEstado();
            }
            repaint();

        });
        timer.start();
    }

    public void cambiarMapa(Mapa nmap) {
        partida.setMapaActual(nmap);
        repaint(); // funcion heredada de Jpanel
    }

    private void actualizarMovimiento() {
        if (partida.getJugadores().isEmpty()) return;

        Teclado t = (Teclado) this.getKeyListeners()[0]; // Obtenemos el teclado

        int dx = 0;
        int dy = 0;
        int v = yo.getSpeed();

        if (t.up) dy -= v;
        if (t.down) dy += v;
        if (t.left) dx -= v;
        if (t.right) dx += v;

        // Si hay movimiento en algún eje, movemos al jugador
        if (dx != 0 || dy != 0) {
            yo.moverse(dx, dy);
        }
    }

    private void actualizarTareas() {

        Teclado tec = (Teclado) this.getKeyListeners()[0]; // Obtenemos el teclado

        // Solo si el tripulante está vivo y presiona la tecla de acción (ej. Espacio)
        if (yo.isEstaVivo() && tec.space) {
            for (Tarea t : partida.getTareas()) {
                // Calculamos distancia entre jugador y tarea
                double dist = Math.sqrt(Math.pow(t.getX() - yo.getX(), 2) + 1.2 * Math.pow(t.getY() - yo.getY(), 2));

                if (dist < 80) { // Si está cerca
                    yo.interactuar(t);
                    break; // Solo puede hacer una tarea a la vez
                }
            }
        }

        // Verificar victoria
        if (partida.tareasCompletadas()) {
            System.out.println("¡VICTORIA DE TRIPULANTES!");
            // Aquí podrías mostrar un mensaje en pantalla más adelante
        }
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;



        // 1. Calculamos cuánto hay que desplazar la "cámara"

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

        // Dibujar las tareas en el mapa.
        for (Tarea t : partida.getTareas()) {
            if (!t.isCompletada()) {
                g.setColor(Color.YELLOW);
                g.fillOval(t.getX(), t.getY(), 40, 40);

                // Dibujar el nombre de la tarea
                g.setColor(Color.WHITE);
                g.drawString(t.getNombre(), t.getX(), t.getY() - 10);

                // Si el progreso es mayor a 0, dibujar la barra
                if (t.getProgreso() > 0) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(t.getX() - 30, t.getY() - 25, 100, 10);
                    g.setColor(Color.GREEN);
                    g.fillRect(t.getX() - 30, t.getY() - 25, t.getProgreso()/5, 10);
                }
            }
        }

        // 4. (Opcional) Resetear el translate si quieres dibujar un HUD/Botones fijos
        g2d.translate(camX, camY);
        // Sombra (desplazada 1 píxel)
        g.setColor(Color.WHITE);
        g.drawString("Misiones: " + partida.getTareasCompletadas() + "/" + partida.getTotalTareas(), 21, 21);

        // Texto principal
        g.setColor(Color.BLACK);
        g.drawString("Misiones: " + partida.getTareasCompletadas() + "/" + partida.getTotalTareas(), 20, 20);;

        g.setColor(Color.WHITE);
        g.drawString("Jugadores: " + partida.getJugadoresVivos() + "/" + partida.getNumeroJugadores(), 201, 21);

        // Texto principal
        g.setColor(Color.BLACK);
        g.drawString("Jugadores: " + partida.getJugadoresVivos() + "/" + partida.getNumeroJugadores(), 200, 20);;

        if (partida.getGameOver()) {
            g.setColor(new Color(0, 0, 0, 180)); // Oscurecer pantalla
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String mensaje = "VICTORIA: " + partida.getGanador();
            g.drawString(mensaje, getWidth()/2 - 150, getHeight()/2);
        }

    }
}
