import javax.swing.JPanel;
import java.util.*;

public class Jugador {
    private static final int TOTAL_CARTAS = 10;
    private List<Carta> cartas;
    private Random r;

    public Jugador() {
        cartas = new ArrayList<>();
        r = new Random();
    }

    public void repartir() {
        cartas.clear();
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas.add(new Carta(r));
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int x = 10 + (TOTAL_CARTAS - 1) * 40;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, x, 10);
            x -= 40;
        }
        pnl.revalidate();
        pnl.repaint();
    }

    public String getGruposYEscaleras() {
        StringBuilder mensaje = new StringBuilder();
        int puntajeNoGrupo = 0;
        List<Carta> cartasEnGrupo = new ArrayList<>();

        // Agrupar cartas por nombre sin importar la pinta
        Map<NombreCarta, List<Carta>> grupos = new HashMap<>();
        for (Carta carta : cartas) {
            grupos.computeIfAbsent(carta.getNombre(), k -> new ArrayList<>()).add(carta);
        }

        // Detectar ternas, cuartas y quintas
        for (Map.Entry<NombreCarta, List<Carta>> entry : grupos.entrySet()) {
            int size = entry.getValue().size();
            if (size >= 2) {
                mensaje.append(getNombreGrupo(size)).append(" de ").append(entry.getKey()).append("\n");
                cartasEnGrupo.addAll(entry.getValue());
            }
        }

        mensaje.append("Escaleras encontradas:\n");

        // Detectar escaleras por pinta
        for (Pinta pinta : Pinta.values()) {
            List<Carta> cartasPinta = new ArrayList<>();
            for (Carta carta : cartas) {
                if (carta.getPinta() == pinta) {
                    cartasPinta.add(carta);
                }
            }

            cartasPinta.sort(Comparator.comparingInt(c -> c.getNombre().ordinal()));
            List<Carta> escalera = new ArrayList<>();

            for (int i = 0; i < cartasPinta.size(); i++) {
                if (escalera.isEmpty() || cartasPinta.get(i).getNombre().ordinal() == escalera.get(escalera.size() - 1).getNombre().ordinal() + 1) {
                    escalera.add(cartasPinta.get(i));
                } else {
                    if (escalera.size() >= 2) {
                        mensaje.append(getNombreGrupo(escalera.size())).append(" de ").append(pinta).append(": ");
                        for (Carta c : escalera) {
                            mensaje.append(c.getNombre()).append(", ");
                            cartasEnGrupo.add(c);
                        }
                        mensaje.append("\n");
                    }
                    escalera.clear();
                    escalera.add(cartasPinta.get(i));
                }
            }

            if (escalera.size() >= 2) {
                mensaje.append(getNombreGrupo(escalera.size())).append(" de ").append(pinta).append(": ");
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

    private String getNombreGrupo(int size) {
        switch (size) {
            case 2: return "Par";
            case 3: return "Terna";
            case 4: return "Cuarta";
            default: return "Quinta";
        }
    }
}
