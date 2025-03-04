import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmJuego extends JFrame {
    private JPanel pnlJugador1, pnlJugador2;
    private JTabbedPane tpJugadores;
    private Jugador jugador1, jugador2;

    public FrmJuego() {
        setSize(700, 250);
        setTitle("Juguemos al apuntado!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnRepartir = new JButton("Repartir");
        btnRepartir.setBounds(10, 10, 100, 25);
        getContentPane().add(btnRepartir);

        JButton btnVerificar = new JButton("Verificar");
        btnVerificar.setBounds(120, 10, 100, 25);
        getContentPane().add(btnVerificar);

        tpJugadores = new JTabbedPane();
        tpJugadores.setBounds(10, 40, 650, 150);
        getContentPane().add(tpJugadores);

        pnlJugador1 = new JPanel();
        pnlJugador1.setBackground(new Color(255, 165, 0));
        pnlJugador1.setLayout(null);

        pnlJugador2 = new JPanel();
        pnlJugador2.setBackground(new Color(0, 0, 255));
        pnlJugador2.setLayout(null);

        tpJugadores.addTab("Goku", pnlJugador1);
        tpJugadores.addTab("Vegueta", pnlJugador2);

        jugador1 = new Jugador();
        jugador2 = new Jugador();

        btnRepartir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repartirCartas();
            }
        });

        btnVerificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarJugador();
            }
        });
    }

    private void repartirCartas() {
        jugador1.repartir();
        jugador1.mostrar(pnlJugador1);
        jugador2.repartir();
        jugador2.mostrar(pnlJugador2);
    }

    private void verificarJugador() {
        int pestañaSeleccionada = tpJugadores.getSelectedIndex();
        switch (pestañaSeleccionada) {
            case 0:
                JOptionPane.showMessageDialog(null, jugador1.getGruposYEscaleras());
                break;
            case 1:
                JOptionPane.showMessageDialog(null, jugador2.getGruposYEscaleras());
                break;
        }
    }

    public static void main(String[] args) {
        new FrmJuego().setVisible(true);
    }
}
