package GUI;

import javax.swing.JFrame;
import logica.Partida;
import logica.Mapa;
import logica.Tripulante;
import java.awt.Color;

/**
 * Ventana principal usando Jframe
 */
public class VentanaPrincipal extends JFrame{

    private PanelJuego panel;

    public VentanaPrincipal() {
        setTitle("Among Us - Villa Asia");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Se crea el panel
        panel = new PanelJuego();
        add(panel);
    }
}
