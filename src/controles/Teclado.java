package controles;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import logica.Impostor;
import logica.Partida;
import logica.Jugador;
import logica.Tripulante;

import javax.swing.JPanel;

public class Teclado extends KeyAdapter{

    private Partida partida;
    private JPanel panel;
    public boolean up, down, left, right, space;

    // Recibe el objeto partida para poder tener la lista de jugadores
    // Recibe el panel para poder hacer repaint.
    public Teclado(Partida partida, JPanel panel) {
        this.partida = partida;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> space = true;

            // Las acciones de un solo clic (como matar) se quedan aquí
            case KeyEvent.VK_K -> {
                Jugador j1 = partida.getJugadores().get(0);
                if(j1 instanceof Impostor) {
                    ((Impostor) j1).asesinar();
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
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

}
