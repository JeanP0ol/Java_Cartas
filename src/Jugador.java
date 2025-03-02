import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jugador {
    private static final int TOTAL_CARTAS = 10;
    private List<Carta> cartas;
    private Random random;

    public Jugador() {
        cartas = new ArrayList<>();
        random = new Random();
    }

    public void repartir() {
        cartas.clear();
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas.add(new Carta(random));
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int x = 10;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, x, 10);
            x += 40;
        }
        pnl.revalidate();
        pnl.repaint();
    }

    public String getGruposYEscaleras() {
        StringBuilder mensaje = new StringBuilder("Los grupos encontrados:\n");
        int puntajeNoGrupo = 0;
        List<Carta> cartasEnGrupo = new ArrayList<>();

        // Contador de cartas por nombre
        int[] contador = new int[13];
        for (Carta carta : cartas) {
            contador[carta.getNombre().ordinal()]++;
        }

        // Detectar grupos
        for (int i = 0; i < contador.length; i++) {
            if (contador[i] >= 2) {
                mensaje.append("Par de ").append(NombreCarta.values()[i]).append("\n");
                for (Carta c : cartas) {
                    if (c.getNombre().ordinal() == i) {
                        cartasEnGrupo.add(c);
                    }
                }
            }
        }

        // Detectar escaleras por pinta
        for (Pinta pinta : Pinta.values()) {
            List<Carta> cartasPinta = new ArrayList<>();
            for (Carta carta : cartas) {
                if (carta.getPinta() == pinta) {
                    cartasPinta.add(carta);
                }
            }
            cartasPinta.sort((a, b) -> a.getNombre().ordinal() - b.getNombre().ordinal());

            List<Carta> escalera = new ArrayList<>();
            for (int i = 0; i < cartasPinta.size() - 1; i++) {
                if (cartasPinta.get(i + 1).getNombre().ordinal() == cartasPinta.get(i).getNombre().ordinal() + 1) {
                    escalera.add(cartasPinta.get(i));
                    escalera.add(cartasPinta.get(i + 1));
                } else if (!escalera.isEmpty()) {
                    break;
                }
            }

            if (escalera.size() >= 3) {
                mensaje.append("Escalera de ").append(pinta).append(": ");
                for (Carta c : escalera) {
                    mensaje.append(c.getNombre()).append(", ");
                    cartasEnGrupo.add(c);
                }
                mensaje.append("\n");
            }
        }

        // Calcular puntaje de cartas no agrupadas
        for (Carta c : cartas) {
            if (!cartasEnGrupo.contains(c)) {
                puntajeNoGrupo += c.getValor();
            }
        }

        mensaje.append("Puntaje de cartas que no están en grupos: ").append(puntajeNoGrupo);
        return mensaje.toString();
    }
}
