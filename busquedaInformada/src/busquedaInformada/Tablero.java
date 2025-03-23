package busquedaInformada;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//clase para crear el tablero donde se van a mover los agentes
public class Tablero extends JFrame {
    private JLabel[][] tablero;
    private int[][] matrix;
    private final int dim = 15;

    private ImageIcon agente1Img;
    private ImageIcon agente2Img;
    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherICon;
    private ImageIcon path;

    private int bandera = 0;
    private boolean naveColocada = false;
    private int naveFila = -1;
    private int navecColumna = -1;

    private Agente agente1;
    private Agente agente2;

    private final BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/plano.jpg"));

    private final JMenu generate = new JMenu("Random generate");
    private final JMenu settings = new JMenu("Settings");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem mothership = new JRadioButtonMenuItem("Mothership");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JMenuItem randomSample = new JMenuItem("Random Sample");
    private final JMenuItem randomObstacle = new JMenuItem("Random Obstacle");
    private  JMenuItem music= new JMenuItem("Stop Music");
    private Clip clip;

    public Tablero(){
        this.setContentPane(fondo);
        this.setTitle("Practica 3, busqueda informada");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50, 50, dim * 50 + 35 , dim * 50 + 35);
        initComponentes();

        try{
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("audio/cancion.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(clip.LOOP_CONTINUOUSLY);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initComponentes(){
        ButtonGroup settingOptions = new ButtonGroup();
        settingOptions.add(sample);
        settingOptions.add(obstacle);
        settingOptions.add(mothership);

        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("FIle");
        JMenuItem run = new JMenuItem("Run");
        JMenuItem exit = new JMenuItem("Exit");

        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        barraMenus.add(generate);
        file.add(run);
        file.add(exit);
        file.add(music);
        settings.add(mothership);
        settings.add(obstacle);
        settings.add(sample);
        generate.add(randomSample);
        generate.add(randomObstacle);

        agente1Img = new ImageIcon("imagenes/agente1.png");
        agente1Img = new ImageIcon(agente1Img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        agente2Img = new ImageIcon("imagenes/agente2.png");
        agente2Img = new ImageIcon(agente2Img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        obstacleIcon = new ImageIcon("imagenes/obstacle.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        sampleIcon = new ImageIcon("imagenes/sample.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        motherICon = new ImageIcon("imagenes/motherICon.png");
        motherICon= new ImageIcon(motherICon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        this.setLayout(null);
        formaPlano();

        exit.addActionListener(evt -> gestionaSalir(evt));
        run.addActionListener(evt->gestionaRun(evt));
        obstacle.addItemListener(evt-> gestionaObstacle(evt));
        sample.addItemListener(evt -> gestionaSample(evt));
        mothership.addItemListener(evt -> gestionaMotherShip(evt));
        randomSample.addActionListener(evt -> generarAleatorio(sampleIcon,3));
        randomObstacle.addActionListener(evt -> generarAleatorio(obstacleIcon,1));
        music.addActionListener(evt -> {
           if(clip != null){
               if(clip.isRunning()){
                   clip.stop();
                   music.setText("Play music");
               }else{
                   clip.start();
                   music.setText("Stop music");
               }
           }
        });

        class MyWindowAdapter extends WindowAdapter {
            @Override
            public void windowClosing(WindowEvent e) {
                goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());

         agente1 = new Agente("Agente1 ",agente1Img,matrix,tablero );
         agente2 = new Agente("Agente2", agente2Img, matrix, tablero);

        Agente.setNavePosicion(naveFila,navecColumna);

    }

        private void gestionaSalir(ActionEvent evt){
            goodBye();
        }

        private void goodBye(){
            int respuesta = JOptionPane.showConfirmDialog(rootPane, "Deseas Salir?", "Abiso", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) System.exit(0);
        }

        private void formaPlano(){
            tablero = new JLabel[dim][dim];
            matrix = new int [dim][dim];

            int i ,j ;
            for(i=0;i<dim;i++){
                for(j=0;j<dim;j++){
                    matrix[i][j] = 0;
                    tablero[i][j] = new JLabel();
                    tablero[i][j].setBounds(j*50 + 15, i * 50 + 15, 50, 50);
                    tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                    tablero[i][j].setOpaque(false);
                    this.add(tablero[i][j]);

                    tablero[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            insertaObjeto(e);
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            insertaObjeto(e);
                        }
                    });
                }
            }
        }

        private void generarAleatorio(Icon icon, int tipo){
        int x = 0 , y = 0 ;
            for (int i = 0 ; i < 7 ; i ++){
                x = (int)(Math.random()*dim);
                y = (int)(Math.random()*dim);
                if(matrix[x][y] == 0){
                    matrix[x][y] = tipo;
                    tablero[x][y].setIcon(icon);
                }
            }
        }

        private void gestionaObstacle(ItemEvent eventObject){
            JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
            if(opt.isSelected() ){
                actualIcon = obstacleIcon;
            }else{
                actualIcon = null;
            }
        }

        private void gestionaSample(ItemEvent eventObject){
            JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
            if(opt.isSelected() ){
                actualIcon = sampleIcon;

            }else{
                actualIcon = null;
            }
        }

        private void gestionaMotherShip(ItemEvent eventObject){
            JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
            if (opt.isSelected()){
                actualIcon = motherICon;
            }else{
                actualIcon = null;
            }
        }

        private void gestionaRun(ActionEvent evt){
            if(!agente1.isAlive()) agente1.start();
            if(!agente2.isAlive()) agente2.start();
            settings.setEnabled(false);
            generate.setEnabled(false);
        }

    public void insertaObjeto(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        int fila = (casilla.getY() - 15) / 50;
        int columna = (casilla.getX() - 15) / 50;

        if (actualIcon == motherICon && matrix[fila][columna] != 2) {
            if (!naveColocada) {
                casilla.setIcon(motherICon);
                naveColocada = true;
                matrix[fila][columna] = 2;
                naveFila = fila;
                navecColumna= columna;

                System.out.println("La nave nodriza está en la posición: Fila " + naveFila + " Columna " + ", Columna" + navecColumna);

                Agente.setNavePosicion(naveFila, navecColumna);
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una nave en el tablero");
            }
        } else if (actualIcon == obstacleIcon) {
            casilla.setIcon(obstacleIcon);
            matrix[fila][columna] = 1; //obstáculo
        } else if (actualIcon == sampleIcon) {
            casilla.setIcon(sampleIcon);
            matrix[fila][columna] = 3; //el premio
        } else if (actualIcon == null) {
            casilla.setIcon(null);
            matrix[fila][columna] = 0; //voidsianin
        }

    }


}

