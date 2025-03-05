package reactivos;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Agente extends Thread {
    private final String nombre;
    private int i, j;
    private final ImageIcon icon;
    private final int[][] matrix = new int[i][j];
    private final JLabel[][] tablero;
    private final Escenario escenario;
    private boolean tieneMuestra = false;
    private boolean modoAleatorio = false;
    private int intentosFallidos = 0;
    private final Random random = new Random();
    private JLabel casillaAnterior;

    public Agente(String nombre, ImageIcon icon, JLabel[][] tablero, Escenario escenario) {
        this.nombre = nombre;
        this.icon = icon;
        this.tablero = tablero;
        this.escenario = escenario;
        do {
            this.i = new Random().nextInt(tablero.length);
            this.j = new Random().nextInt(tablero[0].length);
        } while (esObstaculo(i, j) || esMuestra(i, j));

        tablero[i][j].setIcon(icon);
    }


    @Override
    public void run() {
        while (true) {
            if (tieneMuestra) {
                moverConObjeto();
            } else {
                moverSinObjeto();
            }
            try {
                Thread.sleep(100 + random.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void moverSinObjeto() {
        String direccion = elegirDireccionAleatoria();
        mover(direccion);
        if (esMuestra(i, j)) {
            recogerObjeto();
            tieneMuestra = true;
        }
    }

    private void moverConObjeto() {
        if (modoAleatorio) {
            mover(elegirDireccionAleatoria());
            if (intentosFallidos == 0) {
                modoAleatorio = false;
            }
        } else {
            int[] naveNodriza = encontrarNaveNodriza();
            if (naveNodriza == null) return;

            int madreI = naveNodriza[0], madreJ = naveNodriza[1];
            if (i < madreI) mover("SUR");
            else if (i > madreI) mover("NORTE");
            else if (j < madreJ) mover("ESTE");
            else if (j > madreJ) mover("OESTE");

            if (i == madreI && j == madreJ) {
                entregarObjeto();
                tieneMuestra = false;
            }
        }
        if (intentosFallidos > 2) {
            modoAleatorio = true;
            intentosFallidos = 0;
        }
        actualizarPosicion();
    }

    private int[] encontrarNaveNodriza() {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                if (esMadreNodriza(x, y)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    private String elegirDireccionAleatoria() {
        String[] direcciones = {"NORTE", "ESTE", "OESTE", "SUR"};
        return direcciones[random.nextInt(direcciones.length)];
    }

    private void mover(String direccion) {
        int nuevaI = i, nuevaJ = j;
        switch (direccion) {
            case "NORTE": if (i > 0) nuevaI--; break;
            case "SUR": if (i < matrix.length - 1) nuevaI++; break;
            case "ESTE": if (j < matrix[0].length - 1) nuevaJ++; break;
            case "OESTE": if (j > 0) nuevaJ--; break;
        }
        if (!esObstaculo(nuevaI, nuevaJ)) {
            casillaAnterior = tablero[i][j];
            i = nuevaI;
            j = nuevaJ;
            actualizarPosicion();
        } else {
            intentosFallidos++;
        }
    }

    private void actualizarPosicion() {
        if (casillaAnterior != null) casillaAnterior.setIcon(null);
        tablero[i][j].setIcon(icon);
    }

    private void recogerObjeto() {
        if (esMuestra(i, j)) {
            escenario.actualizarEstadoCelda(i, j, EstadoCelda.VACIO);
            matrix[i][j] = 0;
            tablero[i][j].setIcon(null);
            System.out.println(nombre + " recogió un objeto en (" + i + ", " + j + ")");
        }
    }

    private void entregarObjeto() {
        System.out.println(nombre + " entregó un objeto en (" + i + ", " + j + ")");
    }

    private boolean esObstaculo(int fila, int columna) {
        return escenario.obtenerEstadoCelda(fila, columna) == EstadoCelda.OBSTACULO;
    }

    private boolean esMuestra(int fila, int columna) {
        return escenario.obtenerEstadoCelda(fila, columna) == EstadoCelda.MUESTRA;
    }

    private boolean esMadreNodriza(int fila, int columna) {
        return escenario.obtenerEstadoCelda(fila, columna) == EstadoCelda.NAVE_NODRIZA;
    }
}

