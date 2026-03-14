package controles;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import GUI.PanelJuego;
import logica.Impostor;
import logica.Partida;
import logica.Jugador;
import logica.Tripulante;

import javax.swing.JPanel;

/**
 * Clase funcional para los controles
 */
public class Teclado extends KeyAdapter{

    private Partida partida;
    private PanelJuego panel;
    public boolean up, down, left, right, space, shift, pressK;

    /**
     * Constructor
     * @param panel es necesario para obtener variables, etc
     */
    public Teclado(PanelJuego panel) {

        this.panel = panel;
    }

    /**
     * Se controlan todos los comandos del juego
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        partida = panel.getPartida();
        Jugador j1 = null;
        if(partida != null){
            j1 = partida.getjActual();
        }

        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A ->{
                left = true;
                if (j1 != null) j1.setDx(-1);
            }
            case KeyEvent.VK_D ->{
                if (j1 != null) j1.setDx(1);
                right = true;
            }
            case KeyEvent.VK_SPACE -> space = true;

            // Las acciones de un solo clic (como matar) se quedan aquí
            case KeyEvent.VK_K -> {
                if(j1 instanceof Impostor) {
                    ((Impostor) j1).asesinar();
                    if (!pressK){
                        ((Impostor) j1).entrarConducto();
                        pressK = true;
                    }
                }
            }
            // R para reportar un cuerpo
            case KeyEvent.VK_R -> {
                // Nota: poner que el impostor no pueda reportar xd
                if (j1 != null && j1.reportar()){
                    up = down = left = right = false;
                    partida.iniciarVotacion();
                }
            }
            // En tu clase Teclado
            case KeyEvent.VK_ENTER -> {
                if (panel.estadoActual == PanelJuego.Estado.GAME_OVER) {
                    panel.estadoActual = PanelJuego.Estado.MENU;
                } else if (panel.estadoActual == PanelJuego.Estado.CONTROLES || panel.estadoActual == PanelJuego.Estado.INFO) {
                    panel.estadoActual = PanelJuego.Estado.MENU;
                }
            }

            case KeyEvent.VK_1 -> {
                if (panel.estadoActual == PanelJuego.Estado.MENU) {
                    panel.iniciarPartida(1);
                }
            }
            case KeyEvent.VK_2 -> {
                if (panel.estadoActual == PanelJuego.Estado.MENU) {
                    panel.iniciarPartida(2);
                }
            }
            case KeyEvent.VK_3 -> {
                if (panel.estadoActual == PanelJuego.Estado.MENU) {
                    panel.estadoActual = PanelJuego.Estado.CONTROLES;
                }
            }
            case KeyEvent.VK_4 -> {
                if (panel.estadoActual == PanelJuego.Estado.MENU) {
                    panel.estadoActual = PanelJuego.Estado.INFO;
                }
            }

            case KeyEvent.VK_SHIFT -> {
                if(partida != null){
                    if (!shift){
                        partida.switchJugador();
                        shift = true;
                    }
                }
            }


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_SPACE -> space = false;
            case KeyEvent.VK_SHIFT -> shift = false;
            case KeyEvent.VK_K -> pressK = false;
        }
    }

}
