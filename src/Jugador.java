import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
            
            // Filtrar cartas de la misma pinta
            for (Carta carta : cartas) {
                if (carta.getPinta() == pinta) {
                    cartasPinta.add(carta);
                }
            }
            
            // Ordenar cartas por su valor
            cartasPinta.sort(Comparator.comparingInt(c -> c.getNombre().ordinal()));

            List<Carta> escalera = new ArrayList<>();
            
            for (int i = 0; i < cartasPinta.size(); i++) {
                if (escalera.isEmpty() || 
                    cartasPinta.get(i).getNombre().ordinal() == escalera.get(escalera.size() - 1).getNombre().ordinal() + 1) {
                    
                    escalera.add(cartasPinta.get(i)); // Agregar carta si es consecutiva
                } else if (cartasPinta.get(i).getNombre().ordinal() != escalera.get(escalera.size() - 1).getNombre().ordinal()) {
                    // Si se rompe la secuencia y la escalera es válida, la procesamos
                    if (escalera.size() >= 3) {
                        mensaje.append(getNombreGrupo(escalera.size())).append(" de ").append(pinta).append(": ");
                        for (Carta c : escalera) {
                            mensaje.append(c.getNombre()).append(", ");
                            cartasEnGrupo.add(c);
                        }
                        mensaje.append("\n");
                    }
                    escalera.clear(); // Reiniciar para una nueva escalera
                    escalera.add(cartasPinta.get(i)); // Agregar la nueva carta inicial
                }
            }

            // Si al final hay una escalera válida, la agregamos
            if (escalera.size() >= 3) {
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
            case 3: return "Terna";
            case 4: return "Cuarta";
            default: return "Quinta";
        }
    }
}
