package controles;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import logica.Impostor;
import logica.Partida;
import logica.Jugador;
import javax.swing.JPanel;

public class Teclado extends KeyAdapter{

    private Partida partida;
    private JPanel panel;

    // Recibe el objeto partida para poder tener la lista de jugadores
    // Recibe el panel para poder hacer repaint.
    public Teclado(Partida partida, JPanel panel) {
        this.partida = partida;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (partida.getJugadores().isEmpty()) return;

        // En este caso solo controlas a tu jugador.
        Jugador j1 = partida.getJugadores().get(0);
        int velocidad = j1.getSpeed();

        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> j1.moverse(0, -velocidad);
            case KeyEvent.VK_S -> j1.moverse(0, velocidad);
            case KeyEvent.VK_A -> j1.moverse(-velocidad, 0);
            case KeyEvent.VK_D -> j1.moverse(velocidad, 0);
            case KeyEvent.VK_K -> {
                if(j1 instanceof Impostor){
                    ((Impostor) j1).asesinar();
                    panel.repaint();
                }
            }
        }
        panel.repaint();
    }

}
