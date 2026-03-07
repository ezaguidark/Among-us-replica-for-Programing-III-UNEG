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
    public enum Estado { MENU, JUGANDO, GAME_OVER }

    public Estado estadoActual = Estado.MENU;

    // Constructor para el panel
    // Constructor del panel (Ahora no recibe nada, empieza en el Menú)
    public PanelJuego() {
        // 1. Estado inicial
        this.estadoActual = Estado.MENU;
        this.partida = null; // No hay partida todavía

        // 2. Configuración de pantalla
        this.setFocusable(true);
        this.requestFocusInWindow();

        // 3. El teclado ahora solo necesita saber que existe este panel
        this.addKeyListener(new Teclado(this));

        // 4. CREAR EL TIMER
        // El Timer sigue corriendo siempre para dibujar el menú o el juego
        Timer timer = new Timer(16, e -> {
            // Solo intentamos actualizar lógica si estamos jugando y la partida existe
            if (estadoActual == Estado.JUGANDO && partida != null) {

                // Verificamos si la partida sigue activa
                if (!partida.getGameOver()) {
                    actualizarMovimiento();
                    actualizarTareas();
                    partida.verificarEstado();
                } else {
                    // Si la lógica de la partida dice Game Over, lo detectamos aquí
                    this.estadoActual = Estado.GAME_OVER;
                }
            }
            repaint(); // El repaint SIEMPRE debe ocurrir para actualizar el menú o la pantalla final
        });
        timer.start();
    }

    public void cambiarMapa(Mapa nmap) {
        partida.setMapaActual(nmap);
        repaint(); // función heredada de Jpanel
    }

    // Función para actualizar el movimiento
    // aumenta las coordenadas en función a la velocidad del jugador.
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

    // Función que se ejecuta en el timer para actualizar el estado de las tareas
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

    // Recordatorio: todo lo que sucede en pantalla se actualiza aquí.
    // por ejemplo, si un jugador cambia a muerto, aquí se verifica y se dibuja.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(estadoActual == Estado.MENU){
            dibujarMenu(g);
        } else if (estadoActual == Estado.JUGANDO) {
            Graphics2D g2d = (Graphics2D) g;

            // --- CAPA DEL MUNDO (Con Cámara) ---
            int camX = yo.getX() - (getWidth() / 2);
            int camY = yo.getY() - (getHeight() / 2);

            g2d.translate(-camX, -camY); // Entramos al mundo

            dibujarMundo(g);
            dibujarJugadores(g);

            g2d.translate(camX, camY); // Salimos del mundo (Reset cámara)

            // --- CAPA DE INTERFAZ (Fija) ---
            dibujarHUD(g);
        }

        // --- CAPA DE ESTADO ---
        else if (estadoActual == Estado.GAME_OVER) {
            dibujarPantallaFinal(g);
        }
    }

    private void dibujarMundo(Graphics g) {
        // Dibujamos el fondo del mapa
        g.drawImage(mapaActual.getBackground(), 0, 0, null);

        // Dibujamos las tareas
        for (Tarea t : partida.getTareas()) {
            if (!t.isCompletada()) {
                g.setColor(Color.YELLOW);
                g.fillOval(t.getX(), t.getY(), 40, 40);

                g.setColor(Color.WHITE);
                g.drawString(t.getNombre(), t.getX(), t.getY() - 10);

                if (t.getProgreso() > 0) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(t.getX() - 30, t.getY() - 25, 100, 10);
                    g.setColor(Color.GREEN);
                    g.fillRect(t.getX() - 30, t.getY() - 25, t.getProgreso() / 5, 10);
                }
            }
        }
    }

    private void dibujarJugadores(Graphics g) {
        Font fuente = new Font("Arial", Font.BOLD, 24);
        g.setFont(fuente);

        for (Jugador j : partida.getJugadores()) {
            if (j.isEstaVivo()) {
                g.setColor(j.getColor());
                g.fillOval(j.getX(), j.getY(), j.getAncho(), j.getAlto());

                g.setColor(Color.BLACK);
                g.drawString(j.getNombre(), j.getX() - 10, j.getY() - 10);
            } else {
                // Lógica de cadáveres (X roja y estados)
                g.setColor(Color.RED);
                g.drawOval(j.getX(), j.getY(), j.getAncho(), j.getAlto());
                g.drawLine(j.getX(), j.getY(), j.getX() + j.getAncho(), j.getY() + j.getAlto());
                g.drawLine(j.getX() + j.getAncho(), j.getY(), j.getX(), j.getY() + j.getAlto());

                String estadoStr = "";
                if (j.isReportado()) estadoStr = "REPORTADO: ";
                else if (j.isExpulsado()) estadoStr = "EXPULSADO: ";
                else estadoStr = "DEAD: ";

                g.drawString(estadoStr + j.getNombre(), j.getX() - 30, j.getY() - 10);
            }
        }
    }

    private void dibujarHUD(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Misiones
        g.setColor(Color.WHITE);
        g.drawString("Misiones: " + partida.getTareasCompletadas() + "/" + partida.getTotalTareas(), 21, 21);
        g.setColor(Color.BLACK);
        g.drawString("Misiones: " + partida.getTareasCompletadas() + "/" + partida.getTotalTareas(), 20, 20);

        // Jugadores
        g.setColor(Color.WHITE);
        g.drawString("Jugadores: " + partida.getJugadoresVivos() + "/" + partida.getNumeroJugadores(), 201, 21);
        g.setColor(Color.BLACK);
        g.drawString("Jugadores: " + partida.getJugadoresVivos() + "/" + partida.getNumeroJugadores(), 200, 20);
    }

    private void dibujarPantallaFinal(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String mensaje = "VICTORIA: " + partida.getGanador();
        g.drawString(mensaje, getWidth() / 2 - 150, getHeight() / 2);
        g.drawString("Presione ''Enter'' para continuar", getWidth()/2 - 100, getHeight()/2 + 60);
    }

    private void dibujarMenu(Graphics g) {
        // Fondo oscuro
        g.setColor(new Color(20, 20, 40));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Título principal
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String titulo = "AMONG US UNEG";
        // Centrar el texto (truco rápido)
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (getWidth() - anchoTitulo) / 2, getHeight() / 2 - 50);

        // Instrucciones
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        g.drawString("1. Jugar en Villa Asia", getWidth()/2 - 100, getHeight()/2 + 20);
        g.drawString("2. Jugar en Laboratorio UNEG", getWidth()/2 - 100, getHeight()/2 + 60);
    }

    public void iniciarPartida(int numeroMapa) {
        Mapa mapaSeleccionado;
        if (numeroMapa == 1) {
            mapaSeleccionado = new Mapa("Villa Asia", "/provisional.jpg", "/col_map.png");
        } else {
            // Un segundo mapa (puedes usar la misma imagen por ahora si no tienes otra)
            mapaSeleccionado = new Mapa("Atlantico", "/provisional.jpg", "/col_map.png");
        }

        // Creamos la partida fresca
        this.partida = new Partida(5, mapaSeleccionado);

        // Agregamos a los "bots" de prueba
        partida.unirsePartida(new Tripulante("Alex", Color.RED, 100, 300, partida));
        partida.unirsePartida(new Tripulante("Santi", Color.BLUE, 200, 300, partida));
        partida.unirsePartida(new Tripulante("Maria", Color.GREEN, 300, 300, partida));
        partida.unirsePartida(new Tripulante("Jose", Color.YELLOW, 400, 300, partida));
        partida.unirsePartida(new Tripulante("Bello", Color.BLACK, 500, 300, partida)); // ¡Puse un impostor!

        partida.prepararJugadores();

        // Actualizamos nuestras variables de atajo
        this.mapaActual = partida.getMapaActual();
        this.yo = partida.getJugadores().get(0); // Tú controlas a Alex


        // ¡Cambiamos la pantalla al juego!
        this.estadoActual = Estado.JUGANDO;
    }

    public Partida getPartida(){
        return partida;
    }



}
