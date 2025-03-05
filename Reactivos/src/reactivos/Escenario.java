package reactivos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

public class Escenario extends JFrame {
    private JLabel[][] tablero;
    private EstadoCelda[][] matrix;
    private final int dim = 15;
    private ImageIcon robot1, robot2, obstacleIcon, sampleIcon, motherIcon, actualIcon;
    private Agente wallE, eva;
    private final JMenu settings = new JMenu("Settings");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");

    public Escenario() {
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(50, 50, dim * 50 + 35, dim * 50 + 85);
        initComponents();
    }

    private void initComponents() {
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);
        settingsOptions.add(obstacle);
        settingsOptions.add(motherShip);

        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run = new JMenuItem("Run");
        JMenuItem exit = new JMenuItem("Exit");

        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);

        robot1 = new ImageIcon("imagenes/wall-e.png");
        robot2 = new ImageIcon("imagenes/eva.png");
        obstacleIcon = new ImageIcon("imagenes/brick.png");
        sampleIcon = new ImageIcon("imagenes/sample.png");
        motherIcon = new ImageIcon("imagenes/mothership.png");

        this.setLayout(null);
        formaPlano();

        exit.addActionListener(this::gestionaSalir);
        run.addActionListener(this::gestionaRun);
        obstacle.addItemListener(this::gestionaObstacle);
        sample.addItemListener(this::gestionaSample);
        motherShip.addItemListener(this::gestionaMotherShip);

        wallE = new Agente("Wall-E", robot1, tablero, this);
        eva = new Agente("Eva", robot2, tablero, this);
    }

    private void formaPlano() {
        tablero = new JLabel[dim][dim];
        matrix = new EstadoCelda[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                matrix[i][j] = EstadoCelda.VACIO;
                tablero[i][j] = new JLabel();
                tablero[i][j].setBounds(j * 50 + 10, i * 50 + 10, 50, 50);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);
                tablero[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        insertaObjeto(e);
                    }
                });
            }
        }
    }

    private void gestionaSalir(ActionEvent eventObject) {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "¿Desea salir?", "Aviso", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) System.exit(0);
    }

    private void gestionaRun(ActionEvent eventObject) {
        if (!wallE.isAlive()) wallE.start();
        if (!eva.isAlive()) eva.start();
        settings.setEnabled(false);
    }

    private void gestionaObstacle(ItemEvent eventObject) {
        actualIcon = obstacleIcon;
    }

    private void gestionaSample(ItemEvent eventObject) {
        actualIcon = sampleIcon;
    }

    private void gestionaMotherShip(ItemEvent eventObject) {
        actualIcon = motherIcon;
    }

    public void insertaObjeto(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        if (actualIcon != null) casilla.setIcon(actualIcon);
    }

    public EstadoCelda obtenerEstadoCelda(int i, int j) {
        return matrix[i][j];
    }

    public void actualizarEstadoCelda(int i, int j, EstadoCelda estado) {
        matrix[i][j] = estado;
        tablero[i][j].setIcon(null);
    }
}

enum EstadoCelda {
    VACIO, OBSTACULO, MUESTRA, NAVE_NODRIZA
}
