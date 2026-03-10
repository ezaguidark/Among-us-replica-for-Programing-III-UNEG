package GUI;

import logica.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import controles.Teclado;

public class PanelJuego extends JPanel{

    private Partida partida;
    private Mapa mapaActual;
    private Timer time;
    private Jugador yo;
    public enum Estado { MENU, JUGANDO, GAME_OVER, CONTROLES }
    private Image fondoMenu, victoria, derrota;

    public Estado estadoActual = Estado.MENU;

    // Constructor para el panel
    // Constructor del panel (Ahora no recibe nada, empieza en el Menú)
    public PanelJuego() {
        // 1. Estado inicial
        this.estadoActual = Estado.MENU;
        this.partida = null; // No hay partida todavía
        this.fondoMenu = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ImagenMenu.jpg"))).getImage();
        this.victoria = new ImageIcon(Objects.requireNonNull(getClass().getResource("/victoria.jpg"))).getImage();
        this.derrota = new ImageIcon(Objects.requireNonNull(getClass().getResource("/derrota.jpg"))).getImage();

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
            yo.setMoviendose(true);  // se agregó esto para cambiar la animación
        }
        else {
            yo.setMoviendose(false);
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

    // Recordatorio: lo que sucede en pantalla se actualiza aquí.
    // por ejemplo, si un jugador cambia a muerto, aquí se verifica y se dibuja.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(estadoActual == Estado.MENU){
            dibujarMenu(g);

        } else if (estadoActual == Estado.CONTROLES) {
            dibujarControles(g);

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
                g.setColor(Color.BLUE);
                g.fillOval(t.getX(), t.getY(), 40, 40);

                Font fuente = new Font("Arial", Font.BOLD, 24);
                g.setFont(fuente);
                g.setColor(Color.BLACK);
                g.drawString(t.getNombre(), t.getX() - 30, t.getY() - 20);

                if (t.getProgreso() > 0) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(t.getX() - 30, t.getY() - 25, 100, 10);
                    g.setColor(Color.GREEN);
                    g.fillRect(t.getX() - 30, t.getY() - 25, t.getProgreso() / 3, 10);  // nota: se divide entre 3 porque el total es 300
                }
            }
        }
    }

    private void dibujarJugadores(Graphics g) {
        Font fuente = new Font("Arial", Font.BOLD, 24);
        g.setFont(fuente);

        for (Jugador j : partida.getJugadores()) {
            if (j.isEstaVivo()) {
                j.caminarAnim(g);

                g.setColor(Color.BLACK);
                g.drawString(j.getNombre(), j.getX() - 10, j.getY() - 10);
            } else {
                // Lógica de cadáveres (X roja y estados)
                j.dibujarMuerto(g);

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
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (partida.getGanador().equals("IMPOSTORES")){
            g.setColor(Color.RED);
            if (derrota != null) {
                g.drawImage(derrota, 0, 0, getWidth(), getHeight(), null);
            }
        }
        else{
            g.setColor(Color.CYAN);
            if (victoria != null) {
                g.drawImage(victoria, 0, 0, getWidth(), getHeight(), null);
            }
        }
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String mensaje = "VICTORIA: " + partida.getGanador();
        g.drawString(mensaje, getWidth() / 2 - 250, getHeight() / 2);
        g.drawString("Presione ''Enter'' para continuar", getWidth()/2 - 250, getHeight()/2 + 60);
    }

    private void dibujarMenu(Graphics g) {
        // 1. Dibujar imagen de fondo
        if (fondoMenu != null) {
            g.drawImage(fondoMenu, 0, 0, getWidth(), getHeight(), null);
            // Oscurecemos un poco el fondo para que el texto se lea mejor
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }


        // 3. Opciones
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        int centroX = getWidth() / 2 - 300;
        int centroY = getHeight() / 2;

        g.drawString("1. Jugar en Villa Asia", centroX, centroY);
        g.drawString("2. Jugar en Atlántico", centroX, centroY + 60);
        g.drawString("3. Ver Controles", centroX, centroY + 120); // Nueva opción
    }

    public void iniciarPartida(int numeroMapa) {
        Mapa mapaSeleccionado;
        if (numeroMapa == 1) {
            mapaSeleccionado = new Mapa("Villa Asia", "/Villa_Asia.jpg", "/col_map.png");
        } else {
            // Un segundo mapa (puedes usar la misma imagen por ahora si no tienes otra)
            mapaSeleccionado = new Mapa("Atlantico", "/Atlantico.jpg", "/col_map_2.png");
        }

        // Creamos la partida fresca
        this.partida = new Partida(8, mapaSeleccionado);

        // Prueba con 6 personas se unen

        partida.unirsePartida(new Tripulante("David", Color.RED, 1300, 240, partida));
        partida.unirsePartida(new Tripulante("Carlos", Color.BLUE, 1300, 340, partida));
        partida.unirsePartida(new Tripulante("Maria", Color.GREEN, 1300, 440, partida));
        partida.unirsePartida(new Tripulante("Rayz", Color.ORANGE, 1300, 540, partida));
        partida.unirsePartida(new Tripulante("Jose", Color.YELLOW, 1500, 240, partida));
        partida.unirsePartida(new Tripulante("Pedro", Color.BLACK, 1500, 340, partida));
        partida.unirsePartida(new Tripulante("Alex", Color.MAGENTA,1500, 440, partida));
        partida.unirsePartida(new Tripulante("Daniel", Color.CYAN, 1500, 540, partida));


        partida.prepararJugadores();

        for(Jugador j : partida.getJugadores()) {
            System.out.println(j.getNombre() + " es un " + j.getClass().getSimpleName());
        }

        // Actualizamos nuestras variables de atajo
        this.mapaActual = partida.getMapaActual();
        this.yo = partida.getJugadores().get(0); // Tú controlas a Alex


        // ¡Cambiamos la pantalla al juego!
        this.estadoActual = Estado.JUGANDO;
    }

    public Partida getPartida(){
        return partida;
    }

    private void dibujarControles(Graphics g) {
        // 1. Dibujar imagen de fondo
        if (fondoMenu != null) {
            g.drawImage(fondoMenu, 0, 0, getWidth(), getHeight(), null);
            // Oscurecemos un poco el fondo para que el texto se lea mejor
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        g.setColor(Color.WHITE);

        int xIn = getWidth() / 2 - 200;
        int yIn = getHeight() / 2; // la mitad en y

        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("CONTROLES:", getWidth()/2 - 100, yIn);

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("- WASD: Moverse", xIn, yIn + 60);
        g.drawString("- Mantener Espacio: Realizar Tarea", xIn, yIn + 100);
        g.drawString("- K: Eliminar Jugador", xIn, yIn + 140);
        g.drawString("- R: Reportar Cuerpo", xIn, yIn + 180);

        g.setColor(Color.YELLOW);
        g.drawString("Presiona Enter para volver al menú", getWidth()/2 - 250, getHeight() - 100);
    }



}
