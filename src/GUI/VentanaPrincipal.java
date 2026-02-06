package GUI;

import javax.swing.JFrame;
import logica.Partida;
import logica.Mapa;
import logica.Tripulante;
import java.awt.Color;

public class VentanaPrincipal extends JFrame{

    private PanelJuego panel;

    // el constructor recibe el panel.
    public VentanaPrincipal(Partida partida) {
        setTitle("Among Us - Villa Asia");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Se crea el panel pasándole la información de la partida.
        panel = new PanelJuego(partida);
        add(panel);
    }
}
